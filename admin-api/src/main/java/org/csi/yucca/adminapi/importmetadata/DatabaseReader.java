package org.csi.yucca.adminapi.importmetadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csi.yucca.adminapi.model.Component;
import org.csi.yucca.adminapi.model.ComponentJson;
import org.csi.yucca.adminapi.model.DataType;
import org.csi.yucca.adminapi.model.Dataset;
import org.csi.yucca.adminapi.model.DettaglioDataset;
import org.csi.yucca.adminapi.response.DettaglioStreamDatasetResponse;
import org.csi.yucca.adminapi.util.Constants;
import org.csi.yucca.adminapi.util.Util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabaseReader {

	private String organizationCode;
	private String tenantCode;
	private String dbType;
	private String dbUrl;
	private String dbName;
	private String username;
	private String password;
	private String dbSchema;

	private List<DettaglioDataset> existingMedatataList;
	private DatabaseConfiguration databaseConfiguation;

	List<Dataset> dataset = new LinkedList<Dataset>();
	Map<String, List<String>> columnWarnings = new HashMap<String, List<String>>();

	Map<String, String> fkMap;
	static Logger log = Logger.getLogger(DatabaseReader.class);

	ObjectMapper mapper = new ObjectMapper();

	public DatabaseReader(String organizationCode, String tenantCode, String dbType, String dbUrl, String dbName,
			String username, String password, List<DettaglioDataset> existingMedatataList, String hiveUser,
			String hivePassword, String hiveUrl) throws ImportDatabaseException {
		super();
		log.debug("[DatabaseReader::DatabaseReader] START - dbType:  " + dbType + ", dbUrl: " + dbUrl + ", dbName: "
				+ dbName + ", username: " + username);
		this.existingMedatataList = existingMedatataList;
		this.organizationCode = organizationCode;
		this.tenantCode = tenantCode;
		this.dbType = dbType;
		this.dbUrl = dbUrl;
		this.dbName = dbName;

		if (username != null && username.contains(":")) {
			String[] usernameSchemaDB = username.split(":");
			this.username = usernameSchemaDB[0];
			this.dbSchema = usernameSchemaDB[1];
		} else
			this.username = username;

		this.password = password;

		if (DatabaseConfiguration.DB_TYPE_HIVE.equals(dbType)) {
			this.dbUrl = "yucca_datalake";
			this.dbName = ("stg_" + organizationCode + "_" + tenantCode.replaceAll("-", "_")).toLowerCase(); // stage
			// area
			this.username = hiveUser;
			this.password = hivePassword;
			this.dbUrl = hiveUrl;
		}

		databaseConfiguation = DatabaseConfiguration.getDatabaseConfiguration(dbType);
		if (databaseConfiguation == null)
			throw new ImportDatabaseException(ImportDatabaseException.INVALID_DB_TYPE,
					"Database type used: " + dbType + " - Database type supported: MYSQL, ORACLE, POSTGRESQL, HIVE");

	}

	private Connection getConnection() throws ClassNotFoundException, ImportDatabaseException {
		log.debug("[DatabaseReader::getConnection] START");
		Class.forName(databaseConfiguation.getDbDriver());
		log.debug("[DatabaseReader::getConnection] Driver Loaded.");
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(databaseConfiguation.getConnectionUrl(dbUrl, dbName), username,
					password);
			if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE)) {
				((oracle.jdbc.driver.OracleConnection) connection).setIncludeSynonyms(true);
				((oracle.jdbc.driver.OracleConnection) connection).setRemarksReporting(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ImportDatabaseException(ImportDatabaseException.CONNECTION_FAILED, e.getMessage());
		}
		return connection;
	}

	public String loadSchema() throws ClassNotFoundException, ImportDatabaseException, SQLException, IOException {
		
		log.debug("[DatabaseReader::loadSchema] START");
		Connection conn = getConnection();
		log.debug("[DatabaseReader::loadSchema]  Got Connection.");

		Map<String, DettaglioDataset> existingMetadataMap = mapExistingMetadata();
		log.debug("[DatabaseReader::loadSchema]  existing metadata loaded.");

		DatabaseMetaData meta = conn.getMetaData();
		String[] types = { "TABLE", "VIEW", "SYNONYM" };
		ResultSet tablesResultSet = meta.getTables(null, null, "%", types);

		List<DatabaseTableDataset> tables = new LinkedList<DatabaseTableDataset>();

		if (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE)) {
			loadFk(meta, null);
			loadComponentsMetadata(conn);

		}
		// Map<String, String> pkMap = loadPk(meta);

		//
		// TABLE_CAT String => table catalog (may be null)
		// TABLE_SCHEM String => table schema (may be null)
		// TABLE_NAME String => table name
		// TABLE_TYPE String => table type. Typical types are "TABLE", "VIEW",
		// "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS",
		// "SYNONYM".
		// REMARKS String => explanatory comment on the table
		// TYPE_CAT String => the types catalog (may be null)
		// TYPE_SCHEM String => the types schema (may be null)
		// TYPE_NAME String => type name (may be null)
		// SELF_REFERENCING_COL_NAME String => name of the designated
		// "identifier" column of a typed table (may be null)
		// REF_GENERATION String => specifies how values in
		// SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER",
		// "DERIVED". (may be null)
		String hiveStageArea = ("stg_" + organizationCode + "_" + tenantCode.replaceAll("-", "_")).toLowerCase();

		while (tablesResultSet.next()) {

			String tableName = tablesResultSet.getString("TABLE_NAME");
			String tableSchema = tablesResultSet.getString("TABLE_SCHEM");
			String tableCat = tablesResultSet.getString("TABLE_CAT");
			String tableType = tablesResultSet.getString("TABLE_TYPE");
			String tableComment = tablesResultSet.getString("REMARKS");

			log.debug("[DatabaseReader::loadSchema] tableName " + tableName);

			DatabaseTableDataset table = new DatabaseTableDataset();
			table.setTableName(tableName);

			boolean checkColumOracle = !dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE)
					|| (dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE)
							&& componentsMetadata.get(tableName) != null);

			// System.out.println("tableType:" + tableType + ", tableSchema: " +
			// tableSchema + ", tableName: " + tableName + " tableCat: " +
			// tableCat);
			if (!tableName.equals("TOAD_PLAN_TABLE") && !tableName.equals("PLAN_TABLE") && checkColumOracle
					&& ((dbType.equals(DatabaseConfiguration.DB_TYPE_HIVE)
							&& tableSchema.toLowerCase().startsWith(hiveStageArea))
							|| ((tableSchema == null || username.toUpperCase().equalsIgnoreCase(tableSchema))
									&& (tableCat == null || dbName.toUpperCase().equalsIgnoreCase(tableCat))))) {
				// printResultSetColumns(tablesResultSet);
				// System.out.println("tableType:" + tableType +
				// ", tableSchema: " + tableSchema + ", tableName: " + tableName
				// + " tableCat: " + tableCat);
				ComponentJson[] components = new ComponentJson[0];
				
				// ****************************************
				// DB DIVERSO DA ORACLE
				// ****************************************
				if (!dbType.equals(DatabaseConfiguration.DB_TYPE_ORACLE)) {
					
					try {
						loadFk(meta, tableName);
					} 
					catch (Exception e) {
						log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
						e.printStackTrace();
						table.addWarning("Error loading foreign keys: " + e.getMessage());
					}
					
					try {
						components = loadColumns(meta, tableName, tableSchema);
					} 
					catch (Exception e) {
						log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
						e.printStackTrace();
						table.addWarning("Error loading foreign keys: " + e.getMessage());
					}
				
				} 
				
				// ****************************************
				// DB ORACLE
				// ****************************************				
				else {
					try {
						components = loadColumnsOracle(conn, tableName, tableSchema);
					} 
					catch (Exception e) {
						log.error("[DatabaseReader::loadSchema] error while loading fk  of table " + tableName + " - message: " + e.getMessage());
						e.printStackTrace();
						table.addWarning("Error loading foreign keys: " + e.getMessage());
					}
				}
				
				for (ComponentJson component : components) {
					if (fkMap.containsKey(tableName + "." + component.getSourcecolumnname())) {
						component.setForeignkey(fkMap.get(tableName + "." + component.getSourcecolumnname()));
					}
				}

				DettaglioDataset metadata = existingMetadataMap.get(tableName);
				
				if (metadata == null) {
					table.setStatus(DatabaseTableDataset.DATABASE_TABLE_DATASET_STATUS_NEW);
					table.setTableType(tableType);
					metadata = new DettaglioDataset();
					metadata.setDatasetname(tableName);
					String description = "Imported from " + dbName + " " + tableName
							+ (tableComment != null ? " - " + tableComment : "");
					metadata.setDescription(description);

					// String componentsJson =
					// mapper.writeValueAsString(components);
					metadata.setComponents(components);

					metadata.getComponents();

					metadata.setJdbcdbname(dbName);
					metadata.setJdbcdburl(dbUrl);
					metadata.setJdbcdbtype(dbType);
					metadata.setJdbctablename(tableName);
					if (dbSchema != null)
						metadata.setJdbcdbschema(dbSchema);

				} else {
					table.setStatus(DatabaseTableDataset.DATABASE_TABLE_DATASET_STATUS_EXISTING);
					table.setTableType(tableType);

					List<ComponentJson> newComponents = new LinkedList<ComponentJson>();
					ComponentJson[] existingComponents = metadata.getComponents();
					for (int i = 0; i < components.length; i++) {
						ComponentJson existingComponent = findExistingComponentFromSourceColumnName(
								components[i].getSourcecolumnname(), existingComponents);
						if (existingComponent == null) {
							newComponents.add(components[i]);
						} else {
							if (existingComponent.getSourcecolumn() == null)
								existingComponent.setSourcecolumn(i + 1);
							components[i] = existingComponent;
						}
					}

					if (newComponents.size() > 0) {
						// Component[] newComponentsArray = new
						// Component[newComponents.size() +
						// metadata.getInfo().getComponents().length];
						// int counter = 0;
						// for (Component component :
						// metadata.getInfo().getComponents()) {
						// newComponentsArray[counter] = component;
						// counter++;
						// }
						// for (Component component : newComponents) {
						// newComponentsArray[counter] = component;
						// counter++;
						// }
						// String componentsJson =
						// mapper.writeValueAsString(components);

						metadata.setComponents(components);
						table.setNewComponents(newComponents);
					}
				}

				loadPk(meta, tableName, components);

				if (columnWarnings.containsKey(table.getTableName())) {
					for (String warning : columnWarnings.get(table.getTableName())) {
						table.addWarning(warning);
					}
				}
				try {
					table.setDataset(new DettaglioStreamDatasetResponse(metadata));
					tables.add(table);
				} catch (Exception e) {
					e.printStackTrace();
					log.error("[DatabaseReader::loadSchema] error while adding  of table " + tableName + " - message: "
							+ e.getMessage());
				}
			}

		}

		String json = mapper.writeValueAsString(tables);

		conn.close();

		return json;
	}

	private ComponentJson findExistingComponentFromSourceColumnName(String sourcecolumnname,
			ComponentJson[] existingComponents) {
		ComponentJson foundedComponent = null;
		if (existingComponents != null) {
			for (ComponentJson component : existingComponents) {
				if (component.getSourcecolumnname() != null && sourcecolumnname != null
						&& component.getSourcecolumnname().equals(sourcecolumnname)) {
					foundedComponent = component;
					break;
				}

			}
		}
		return foundedComponent;
	}

	private Map<String, DettaglioDataset> mapExistingMetadata() {
		log.debug("[DatabaseReader::loadExistingMetadata] START");

		Map<String, DettaglioDataset> existingMedatata = new HashMap<String, DettaglioDataset>();
		if (existingMedatataList != null) {
			if (existingMedatataList != null && existingMedatataList.size() > 0) {

				for (DettaglioDataset dettaglioDataset : existingMedatataList) {
					existingMedatata.put(dettaglioDataset.getJdbctablename(), dettaglioDataset);
				}
			}
		}

		return existingMedatata;

	}

	private ComponentJson[] loadColumns(DatabaseMetaData metaData, String tableName, String tableSchema)
			throws SQLException {
		List<ComponentJson> components = new LinkedList<ComponentJson>();

		ResultSet columnsResultSet = null;
		try {
			columnsResultSet = metaData.getColumns(null, tableSchema, tableName, null);
			int columnCounter = 1;
			while (columnsResultSet.next()) {
				ComponentJson component = new ComponentJson();
				String columnName = columnsResultSet.getString("COLUMN_NAME");
				component.setName(Util.camelcasefy(columnName, "_"));
				if (columnsResultSet.getString("REMARKS") != null)
					component.setAlias(columnsResultSet.getString("REMARKS"));
				else
					component.setAlias(columnName.replace("_", " "));

				String columnType = columnsResultSet.getString("TYPE_NAME");

				String hiveType = databaseConfiguation.getHiveType(columnType);
				component.setHiveType(hiveType);

				DataType dataType = null;
				
				if (columnType != null){
					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get(columnType));
				}
				else {
					
					if (!columnWarnings.containsKey(tableName)){
						columnWarnings.put(tableName, new LinkedList<String>());
					}

					columnWarnings.get(tableName).add("Unkonwn data type for column " + columnName + ": " + columnType);
					
					log.warn("[DatabaseReader::loadColumns] unkonwn data type  " + columnType + " for table " + tableName + " column " + columnName);
					
					dataType = org.csi.yucca.adminapi.util.DataType.getFromId(Constants.ADMINAPI_DATA_TYPE_STRING);
				}

				component.setJdbcNativeType(columnType);
				component.setDataType(dataType);

				component.setInorder(columnCounter);
				component.setSourcecolumn(columnCounter);
				component.setSourcecolumnname(columnName);

				components.add(component);
				columnCounter++;
			}

			ComponentJson[] componentsArr = new ComponentJson[components.size()];
			componentsArr = components.toArray(componentsArr);

			return componentsArr;
		} finally {
			if (columnsResultSet != null)
				columnsResultSet.close();
		}

	}

	private Map<String, List<String>> componentsMetadata = new HashMap<String, List<String>>();

	
	
	private PreparedStatement getStatement(Connection conn) throws SQLException {
		String query = " select a.table_name, "
				             + "a.column_name, "
				             + "a.data_type, "

				             + "a.data_precision, "
				             + "a.data_scale, "
				             
				             + "b.comments  "
				     + " from all_tab_columns a, all_col_comments b "
				     + " WHERE a.table_name  = b.table_name AND "
				           + " a.column_name = b.column_name";
		
		if (dbSchema != null){
			query += " AND  a.owner=?";
		}
		
		PreparedStatement statement = conn.prepareStatement(query);
		
		if (dbSchema != null){
			statement.setString(1, dbSchema.toUpperCase());
		}
		
		return statement;
	}
	
	
	private void loadComponentsMetadata(Connection conn) throws SQLException {
			
		PreparedStatement statement = getStatement(conn);

		ResultSet rs = null;
		
		try {
			
			rs = statement.executeQuery();
			
			while (rs.next()) {
				String tableName = rs.getString("table_name");
				String columnName = rs.getString("column_name");
				String columnComment = rs.getString("comments");
				String columnType = rs.getString("data_type");
				Integer precision = rs.getInt("data_precision");
				Integer scale     = rs.getInt("data_scale");
				
				if (columnComment == null || columnComment.trim().equals("")){
					columnComment = columnName.replace("_", " ");
				}
			    else if (columnComment.length() > 500){
			    	columnComment = columnComment.substring(0, 500);
			    }

				Integer idDataType = Constants.ADMINAPI_DATA_TYPE_STRING;
				if (columnType != null && databaseConfiguation.getTypesMap().get(columnType) != null)
					idDataType = databaseConfiguation.getTypesMap().get(columnType);
				else {

					if (!columnWarnings.containsKey(tableName)){
						columnWarnings.put(tableName, new LinkedList<String>());
					}
					
					columnWarnings.get(tableName).add("Unkonwn data type for column " + columnName + ": " + columnType);
					
					log.warn("[DatabaseReader::loadComponentsMetadata] unkonwn data type  " + columnType + " for table " + tableName + " column " + columnName);
				}

				if (!componentsMetadata.containsKey(tableName)) {
					componentsMetadata.put(tableName, new LinkedList<String>());
				}
				
				componentsMetadata.get(tableName).add(columnName + "|" + columnComment + "|" + columnType + "|" + precision + "|" + scale);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	/**
	 * 
	 * @param columnType
	 * @param scale
	 * @param precision
	 * @return
	 */
	private String getHiveToOracleType(String columnType, Integer scale, Integer precision){
		if("NUMBER".equals(columnType) && scale == 0 && precision < 10 ){
			//	INT
			return databaseConfiguation.getHiveType("NUMBER_INT");
		}

		if("NUMBER".equals(columnType) && scale == 0 && precision >= 10 && precision < 19){
			//	BIGINT
			return databaseConfiguation.getHiveType("NUMBER_BIGINT");
		}

		if("NUMBER".equals(columnType) && scale > 0 && precision >= 19){
			//	DECIMAL
			return databaseConfiguation.getHiveType("NUMBER_DECIMAL");
		}
		
		return databaseConfiguation.getHiveType(columnType);
	}
	
	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param tableSchema
	 * @return
	 * @throws SQLException
	 */
	private ComponentJson[] loadColumnsOracle(Connection conn, String tableName, String tableSchema) throws SQLException {
		
		List<ComponentJson> components = new LinkedList<ComponentJson>();
		
		try {
		
			List<String> columns = componentsMetadata.get(tableName);
			
			int counter = 0;
			
			// add(columnName + "|" + columnComment + "|" + columnType + "|" + precision + "|" + scale);
			for (String columnData : columns) {
				
				String[] columnDataSplitted = columnData.split("[|]");

				String columnName    = columnDataSplitted[0];
				String columnComment = columnDataSplitted[1];
				String columnType    = columnDataSplitted[2];
				Integer precision    = Integer.parseInt(columnDataSplitted[3]);
				Integer scale        = Integer.parseInt(columnDataSplitted[4]);
				
				ComponentJson component = new ComponentJson();
				
				component.setName(Util.camelcasefy(columnDataSplitted[0], "_"));
				component.setAlias(columnDataSplitted[1]);
				
				DataType dataType = org.csi.yucca.adminapi.util.DataType.getFromId(databaseConfiguation.getTypesMap().get(columnType));
				
				component.setDataType(dataType);

				component.setJdbcNativeType(columnType);
				component.setHiveType(getHiveToOracleType(columnType, scale, precision));
				component.setSourcecolumn(counter);
				component.setInorder(counter);
				component.setSourcecolumnname(columnDataSplitted[0]);
				components.add(component);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		ComponentJson[] componentsArr = new ComponentJson[components.size()];
		componentsArr = components.toArray(componentsArr);
		
		return componentsArr;

	}

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param tableSchema
	 * @return
	 * @throws SQLException
	 */
	private Component[] loadColumnsFromMetadata(Connection conn, String tableName, String tableSchema)
			throws SQLException {
		List<Component> components = new LinkedList<Component>();
		PreparedStatement statement = null;
		try {
			statement = conn.prepareStatement("SELECT * from " + tableName + " WHERE 1 = 0");
			ResultSetMetaData statementMetaData = statement.getMetaData();
			for (int i = 1; i < statementMetaData.getColumnCount() + 1; i++) {
				Component component = new Component();
				String columnName = Util.camelcasefy(statementMetaData.getColumnName(i), "_");
				component.setName(columnName);

				if (statementMetaData.getColumnLabel(i) != null)
					component.setAlias(statementMetaData.getColumnLabel(i));
				else
					component.setAlias(columnName.replace("_", " "));

				String columnType = statementMetaData.getColumnTypeName(i);
				if (columnType != null && databaseConfiguation.getTypesMap().get(columnType) != null)
					component.setIdDataType(databaseConfiguation.getTypesMap().get(columnType));
				else {
					log.warn("[DatabaseReader::loadColumns] unkonwn data type  " + columnType);
					component.setIdDataType(Constants.ADMINAPI_DATA_TYPE_STRING);
				}
				component.setSourcecolumn(i - 1);
				component.setInorder(i - 1);

				component.setSourcecolumnname(columnName);

				components.add(component);

			}

			Component[] componentsArr = new Component[components.size()];
			componentsArr = components.toArray(componentsArr);

			return componentsArr;
		} finally {
			if (statement != null)
				statement.close();
		}

	}

	private void loadFk(DatabaseMetaData metaData, String table) throws SQLException {
		ResultSet foreignKeys = null;
		try {
			fkMap = new HashMap<String, String>();

			foreignKeys = metaData.getImportedKeys(null, null, table);
			while (foreignKeys.next()) {
				String fkTableName = foreignKeys.getString("FKTABLE_NAME");
				String fkColumnName = foreignKeys.getString("FKCOLUMN_NAME");
				String pkTableName = foreignKeys.getString("PKTABLE_NAME");
				String pkColumnName = foreignKeys.getString("PKCOLUMN_NAME");
				// System.out.println("--" + fkTableName + "." + fkColumnName +
				// " -> " + pkTableName + "." + pkColumnName);
				fkMap.put(fkTableName + "." + fkColumnName, pkTableName + "." + pkColumnName);
			}
		} finally {
			if (foreignKeys != null)
				foreignKeys.close();
		}

	}

	private void loadPk(DatabaseMetaData metaData, String tableName, ComponentJson[] components) throws SQLException {
		ResultSet primaryKeys = null;
		try {
			primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
			while (primaryKeys.next()) {
				String pkColumnName = primaryKeys.getString("COLUMN_NAME");
				for (ComponentJson component : components) {
					if (component.getSourcecolumnname().equals(pkColumnName))
						component.setIskey(Util.booleanToInt(true));
				}
			}
		} finally {
			if (primaryKeys != null)
				primaryKeys.close();
		}
	}

	public List<String> printResultSetColumns(ResultSet rs) {
		List<String> columnsName = new LinkedList<String>();
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();

			// The column count starts from 1
			for (int i = 1; i <= columnCount; i++) {
				String name = rsmd.getColumnName(i);
				// System.out.println("" + name + ": " + rs.getObject(name));
				columnsName.add(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return columnsName;
	}

	public static void main(String[] args) {

		String organizationCode = "SANDBOX";  
		String tenantCode = "SANDBOX";  
		String dbType  = DatabaseConfiguration.DB_TYPE_ORACLE;  
		String dbUrl = "sitprod.csi.it:1569";  
		String dbName = "SITPROD";  
		String username  ="TOPON_SCA:TOPON_COTO";  
		String password = "topon$sca";  
		  
		try {   
			DatabaseReader databaseReader = new DatabaseReader(organizationCode, tenantCode, dbType, 
					dbUrl, dbName, username, password, null, null, null, null);

			String schema = databaseReader.loadSchema();  
			
			System.out.println("schema " + schema);  
			} 
		catch (ImportDatabaseException e) {   
			// TODO Auto-generated catch block   e.printStackTrace();  
			} 
		catch (ClassNotFoundException e) {   
				// TODO Auto-generated catch block   e.printStackTrace();  
				} 
		catch (SQLException e) {   
			// TODO Auto-generated catch block   e.printStackTrace();  
			} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
