package it.csi.smartdata.dataapi.mongo;


import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.dto.SDPDataResult;
import it.csi.smartdata.dataapi.mongo.exception.SDPOrderBySizeException;
import it.csi.smartdata.dataapi.mongo.exception.SDPPageSizeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTargetPath;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.ComplexProperty;
import org.apache.olingo.odata2.api.edm.provider.ComplexType;
import org.apache.olingo.odata2.api.edm.provider.CustomizableFeedMappings;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SDPMongoOdataCast {

	static Logger log = Logger.getLogger(SDPMongoOdataCast.class.getPackage().getName());

	private SDPDataApiMongoAccess mongoDataAccess=null;
	private String codiceApi=null;
	private ArrayList<DBObject> configObject=null;

	public SDPMongoOdataCast () {
		try {
			mongoDataAccess = new SDPDataApiMongoAccess();
		} catch (Exception e) {
			//TODO log
			e.printStackTrace();
		}
	}
	private void initDbObject(String codiceApi) {
		if (null==configObject || !codiceApi.equals(this.codiceApi)) {
			this.codiceApi=codiceApi;
			this.configObject=mongoDataAccess.initConfDbObject(this.codiceApi);
		}
	}

	public EntityType getEntityType(final FullQualifiedName edmFQName,String codiceApi) throws ODataException {

		try {
			log.debug("[SDPMongoOdataCast::getEntityType] BEGIN");
			log.debug("[SDPMongoOdataCast::getEntityType] FullQualifiedName="+edmFQName);
			log.debug("[SDPMongoOdataCast::getEntityType] codiceApi="+codiceApi);

			EntityType ret=null;

			initDbObject(codiceApi);
			for (int i=0;i<this.configObject.size();i++) {
				DBObject obj=this.configObject.get(i);
				String type=((DBObject)obj.get("configData")).get("type").toString();
				String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
				String subType=((DBObject)obj.get("configData")).get("subtype").toString();


				String binaryIdDataset=null;
				try {
					binaryIdDataset=takeNvlValues(    obj.get("binaryIdDataset"));
					//binaryIdDataset=takeNvlValues(    ((DBObject)(((DBObject)obj.get("configData")).get("info"))).get("binaryIdDataset"));
				} catch (Exception e) {
					binaryIdDataset=null;
				}



				if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
					//TODO eliminato
				} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {

					if (SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT.equals(edmFQName.getName())) {
						ret=getSmartObjectType();
					} else if (SDPDataApiConstants.ENTITY_NAME_STREAMS.equals(edmFQName.getName())) {
						ret= getStreamType(nameSpace);
					} else if (SDPDataApiConstants.ENTITY_NAME_MEASURES.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret= getMeasureType (nameSpace,eleCapmpi);
						//				} else if (SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES.equals(edmFQName.getName())) {
						//					//ret= getMeasureValueType(nameSpace);
						//				} else if (SDPDataApiConstants.ENTITY_NAME_MEASURECOMPONENTS.equals(edmFQName.getName())) {
						//					Object eleCapmpi=obj.get("mergedComponents");
						//					//ret= getMeasureComponentType (nameSpace,eleCapmpi);
					} else if (SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");

						//START STATSOLR

						ret= getMeasureStatsType(nameSpace,eleCapmpi,(String)obj.get("groupFields"));
					}


				} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
					if (SDPDataApiConstants.ENTITY_NAME_SOCIAL.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret= getSocialType(nameSpace,eleCapmpi);
					} else if (SDPDataApiConstants.ENTITY_NAME_SOCIAL_STATS.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret= getSocialStatsType(nameSpace,eleCapmpi,(String)obj.get("groupFields"));
					}


				} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
					if (SDPDataApiConstants.ENTITY_NAME_UPLOADDATA.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret=getUploadDataType(nameSpace,eleCapmpi,false,binaryIdDataset);			
					} else if (SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret=getUploadDataType(nameSpace,eleCapmpi,true,binaryIdDataset);			
					}  

					//1.2 binary
					else if (SDPDataApiConstants.ENTITY_NAME_BINARY.equals(edmFQName.getName())) {
						Object eleCapmpi=obj.get("mergedComponents");
						ret=getBinaryDataType(nameSpace,eleCapmpi,true);			
					} 					
				}
			}
			return ret;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getEntityType] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getEntityType] END");
		}
	}

	public ComplexType getComplexType(final FullQualifiedName edmFQName,String codiceApi) throws ODataException {

		initDbObject(codiceApi);
		for (int i=0;i<this.configObject.size();i++) {
			DBObject obj=this.configObject.get(i);
			String type=((DBObject)obj.get("configData")).get("type").toString();
			String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
			String subType=((DBObject)obj.get("configData")).get("subtype").toString();
			if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
				//TODO eliminato

				//			} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
				//				if (SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES.equals(edmFQName.getName())) {
				//					//return new ComplexType().setName(SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES).setProperties( (getMeasureValueType (nameSpace)).getProperties()  );
				//				} else if (SDPDataApiConstants.ENTITY_NAME_MEASURECOMPONENTS.equals(edmFQName.getName())) {
				//					Object eleCapmpi=obj.get("mergedComponents");
				//					//return new ComplexType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURECOMPONENTS).setProperties( (getMeasureComponentType (nameSpace,eleCapmpi)).getProperties()  );
				//
				//				} 
				//
			} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType) && nameSpace.equals(edmFQName.getNamespace())) {
				List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String));
				return new ComplexType().setName(SDPDataApiConstants.COMPLEX_TYPE_BINARYREF).setProperties(properties);
			}
		}
		return null;
	}

	public Association getAssociation(final FullQualifiedName edmFQName, String codiceApi) throws ODataException {

		initDbObject(codiceApi);
		for (int i=0;i<this.configObject.size();i++) {
			DBObject obj=this.configObject.get(i);
			String type=((DBObject)obj.get("configData")).get("type").toString();
			String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
			String subType=((DBObject)obj.get("configData")).get("subtype").toString();
			if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
				if (SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM.equals(edmFQName.getName())) {
					return new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)
							.setEnd1(
									new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES)).setRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setMultiplicity(EdmMultiplicity.MANY))
									.setEnd2(
											new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_STREAMS)).setRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setMultiplicity(EdmMultiplicity.MANY));						
				}
			} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
				if (SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM.equals(edmFQName.getName())) {
					return new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)
							.setEnd1(
									new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES)).setRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setMultiplicity(EdmMultiplicity.MANY))
									.setEnd2(
											new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_STREAMS)).setRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setMultiplicity(EdmMultiplicity.MANY));						
				}
			}


			//1.2 binary
			else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(edmFQName.getNamespace())) {
				if (SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY.equals(edmFQName.getName())) {
					return new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)
							.setEnd1(
									new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)).setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setMultiplicity(EdmMultiplicity.MANY))
									.setEnd2(
											new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY)).setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setMultiplicity(EdmMultiplicity.MANY));						
					//					return new Association().setName(SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)
					//							.setEnd1(
					//									new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)).setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setMultiplicity(EdmMultiplicity.MANY))
					//									.setEnd2(
					//											new AssociationEnd().setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY)).setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setMultiplicity(EdmMultiplicity.ONE))
					//											;						
				}
			}			
		}		  

		return null;
	}

	public EntitySet getEntitySet(final String entityContainer, final String name,String codiceApi) throws ODataException {

		try {
			log.debug("[SDPMongoOdataCast::getEntitySet] BEGIN");
			log.debug("[SDPMongoOdataCast::getEntitySet] entityContainer="+entityContainer);
			log.debug("[SDPMongoOdataCast::getEntitySet] name="+name);
			log.debug("[SDPMongoOdataCast::getEntitySet] codiceApi="+codiceApi);

			initDbObject(codiceApi);
			for (int i=0;i<this.configObject.size();i++) {
				DBObject obj=this.configObject.get(i);
				String type=((DBObject)obj.get("configData")).get("type").toString();
				String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
				String subType=((DBObject)obj.get("configData")).get("subtype").toString();
				String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');
				//entContainerDB=SMART_ENTITY_CONTAINER;

				if (entContainerDB.equals(entityContainer)) {
					if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type)) {
						if (SDPDataApiConstants.ENTITY_SET_NAME_STREAMS.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_STREAMS));						
						} else if (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES));						

						} if (SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT));						

						}  

					} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) ) {
						if (SDPDataApiConstants.ENTITY_SET_NAME_STREAMS.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_STREAMS));						
						} else if (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES));						
						} else if (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS));						
						} if (SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT));						
						}
					} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) ) {

						if (SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL));						
						} else if (SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL_STATS));						
						} 						

					} else if (SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL.equals(name)) {
						return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL));						
					} else if (SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS.equals(name)) {
						return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL_STATS));						



					} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) ) {
						if (SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA));
						}

						//1.2 binary
						else if (SDPDataApiConstants.ENTITY_SET_NAME_BINARY.equals(name)) {
							return new EntitySet().setName(name).setEntityType( new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY));
						} 
					}
				}
			}		  
			return null;

		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getEntityType] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getEntityType] END");
		}
	}

	private ArrayList<String> getEntitysetsNamesStats() {
		//		Object eleCapmpi=obj.get("mergedComponents");
		//		BasicDBList lista=null;
		//		if (eleCapmpi instanceof BasicDBList) {
		//			lista=(BasicDBList)eleCapmpi;
		//		} else {
		//			lista=new BasicDBList();
		//			lista.add(eleCapmpi);
		//		}
		//		for (int j=0;j<lista.size();j++) {
		//			
		//		}	
		return null;
	}

	public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association,
			final String sourceEntitySetName, final String sourceEntitySetRole,String codiceApi) throws ODataException {

		initDbObject(codiceApi);
		for (int i=0;i<this.configObject.size();i++) {
			DBObject obj=this.configObject.get(i);
			String type=((DBObject)obj.get("configData")).get("type").toString();
			String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
			String subType=((DBObject)obj.get("configData")).get("subtype").toString();
			String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');
			//entContainerDB=SMART_ENTITY_CONTAINER;

			if (entContainerDB.equals(entityContainer)) {
				if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type) && nameSpace.equals(association.getNamespace())) {
					return  new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_MEASURE_STREAM)
							.setAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM))
							.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES))
							.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS));						

				} else if (("apiSingleStream".equals(subType) || SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) ) &&  SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && nameSpace.equals(association.getNamespace())) {
					return  new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_MEASURE_STREAM)
							.setAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM))
							.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES))
							.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS));						
				}  

				//1.2 binary
				else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType) && nameSpace.equals(association.getNamespace())) {
					return  new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_DATASETUPLOAD_BINARY)
							.setAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY))
							.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA))
							.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY));						
					//					return  new AssociationSet().setName(SDPDataApiConstants.ASSOCIATION_SET_DATASETUPLOAD_BINARY)
					//							.setAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY))
					//							.setEnd1(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_BINARY))
					//							.setEnd2(new AssociationSetEnd().setRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA));						
				}
			}
		}		  
		return null;
	}	

	public EntityContainerInfo getEntityContainerInfo(final String name,String codiceApi) throws ODataException {
		try {
			log.debug("[SDPMongoOdataCast::getEntityContainerInfo] BEGIN");
			log.debug("[SDPMongoOdataCast::getEntityContainerInfo] name="+name);
			log.debug("[SDPMongoOdataCast::getEntityContainerInfo] codiceApi="+codiceApi);

			EntityContainerInfo eci = null;

			initDbObject(codiceApi);
			for (int i=0;i<this.configObject.size();i++) {
				DBObject obj=this.configObject.get(i);
				String type=((DBObject)obj.get("configData")).get("type").toString();
				String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
				String subType=((DBObject)obj.get("configData")).get("subtype").toString();
				String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');
				//entContainerDB=SMART_ENTITY_CONTAINER;

				if (name == null || entContainerDB.equals(name)) {
					eci = new EntityContainerInfo().setName(entContainerDB).setDefaultEntityContainer(true);
				}
			}
			return eci;

		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getEntityContainerInfo] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.debug("[SDPMongoOdataCast::getEntityContainerInfo] END");
		}		
	}	

	private EntityType getSmartObjectType()throws Exception {
		try {
			log.debug("[SDPMongoOdataCast::getSmartObjectType] BEGIN");
			List<Property> propertiesSmartObject = new ArrayList<Property>();

			CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
			cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);
			propertiesSmartObject.add(new SimpleProperty().setName("idSmartObject").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));

			propertiesSmartObject.add(new SimpleProperty().setName("codiceSmartObject").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			List<PropertyRef> keyPropertiesSmartObject = new ArrayList<PropertyRef>();
			keyPropertiesSmartObject.add(new PropertyRef().setName("idSmartObject"));
			Key keySmartObject = new Key().setKeys(keyPropertiesSmartObject);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT)
					.setProperties(propertiesSmartObject)
					.setKey(keySmartObject);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSmartObjectType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSmartObjectType] END");
		}		
	}

	private EntityType getStreamType(String nameSpace)throws Exception {
		try {
			log.debug("[SDPMongoOdataCast::getStreamType] BEGIN");
			List<Property> propertiesStream = new ArrayList<Property>();

			CustomizableFeedMappings cfeed = new CustomizableFeedMappings();
			cfeed.setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE);

			propertiesStream.add(new SimpleProperty().setName("idStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)).setCustomizableFeedMappings(cfeed));
			propertiesStream.add(new SimpleProperty().setName("codiceStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			propertiesStream.add(new SimpleProperty().setName("codiceTenant").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			propertiesStream.add(new SimpleProperty().setName("nomeStream").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			List<PropertyRef> keyPropertiesStream = new ArrayList<PropertyRef>();
			keyPropertiesStream.add(new PropertyRef().setName("idStream"));
			Key key = new Key().setKeys(keyPropertiesStream);
			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)).setFromRole(SDPDataApiConstants.ROLE_STREAM_MEASURE).setToRole(SDPDataApiConstants.ROLE_MEASURE_STREAM));
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_STREAMS)
					.setProperties(propertiesStream)
					.setKey(key);					
			//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_STREAMS)
			//				.setProperties(propertiesStream)
			//				.setKey(key)
			//				.setNavigationProperties(navigationProperties);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getStreamType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getStreamType] END");
		}			
	}

	//1.2 binary
	private EntityType getBinaryDataType (String nameSpace,Object eleCapmpi,boolean historical) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getBinaryDataType] BEGIN");
			log.debug("[SDPMongoOdataCast::getBinaryDataType] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getBinaryDataType] historical="+historical);
			List<Property> dataAttributes=new ArrayList<Property>();

			dataAttributes.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			dataAttributes.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			//			dataAttributes.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("idBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			dataAttributes.add(new SimpleProperty().setName("filenameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("aliasNameBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("sizeBinary").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			//			dataAttributes.add(new SimpleProperty().setName("insertDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
			//			dataAttributes.add(new SimpleProperty().setName("lastUpdateDateBinary").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("contentTypeBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			//dataAttributes.add(new SimpleProperty().setName("pathHdfsBinary ").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("urlDownloadBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("metadataBinary").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(true)));

			//			if(historical) {
			//				dataAttributes.add(new SimpleProperty().setName("startdate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			//				dataAttributes.add(new SimpleProperty().setName("enddate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			//				dataAttributes.add(new SimpleProperty().setName("parentObjId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			//
			//			}
			//			List<Property> componentProp= getDatasetField(eleCapmpi);
			//			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
			//				dataAttributes.add(componentProp.get(i));
			//			}
			List<PropertyRef> keyPropertiesDataAttributes = new ArrayList<PropertyRef>();

			keyPropertiesDataAttributes.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesDataAttributes);

			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties = new ArrayList<NavigationProperty>();
			//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
			//				.setProperties(measureProps).setKey(keyMeasure).setNavigationProperties(navigationProperties);	

			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD).setToRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY));

			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_BINARY)
					.setProperties(dataAttributes).setKey(keyMeasure);

		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getBinaryDataType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getBinaryDataType] END");
		}			
	}		

	private EntityType getUploadDataType (String nameSpace,Object eleCapmpi,boolean historical, String binaryIdDataset) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getUploadDataType] BEGIN");
			log.debug("[SDPMongoOdataCast::getUploadDataType] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getUploadDataType] historical="+historical);
			List<Property> dataAttributes=new ArrayList<Property>();

			dataAttributes.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			dataAttributes.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			//			dataAttributes.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			dataAttributes.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			if(historical) {
				dataAttributes.add(new SimpleProperty().setName("startdate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("enddate").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
				dataAttributes.add(new SimpleProperty().setName("parentObjId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));

			}
			List<Property> componentProp= getDatasetField(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				dataAttributes.add(componentProp.get(i));
			}
			List<PropertyRef> keyPropertiesDataAttributes = new ArrayList<PropertyRef>();

			keyPropertiesDataAttributes.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesDataAttributes);

			//1.2 binary

			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties = new ArrayList<NavigationProperty>();
			//			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_BINARY)
			//					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setToRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD)
			//					);
			if (null!=binaryIdDataset) navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_BINARY)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY)).setFromRole(SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY).setToRole(SDPDataApiConstants.ROLE_BINARY_DATASETUPLOAD));


			if(historical) {
				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY)
						.setProperties(dataAttributes).setKey(keyMeasure).setNavigationProperties(navigationProperties);

			} else {
				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)
						.setProperties(dataAttributes).setKey(keyMeasure).setNavigationProperties(navigationProperties);
			}

			//			if(historical) {
			//				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY)
			//						.setProperties(dataAttributes).setKey(keyMeasure);
			//			} else {
			//				return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_UPLOADDATA)
			//						.setProperties(dataAttributes).setKey(keyMeasure);
			//			}
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getUploadDataType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getUploadDataType] END");
		}			
	}	


	private EntityType getSocialType (String nameSpace,Object eleCapmpi) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getSocialType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();

			// SPOSTATI IN CFGd
			measureProps.add(new SimpleProperty().setName("streamCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("sensor").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("time").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(false)));

			measureProps.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));


			List<Property> componentProp= getDatasetField(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				measureProps.add(componentProp.get(i));
			}
			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();

			keyPropertiesMeasure.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_SOCIAL)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSocialType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSocialType] END");
		}			
	}

	private EntityType getSocialStatsType (String nameSpace,Object eleCapmpi,String groupFields) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getSocialStatsType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();

			measureProps.add(new SimpleProperty().setName("year").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("month").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("dayofmonth").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("hour").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			//YUCCA-346
			measureProps.add(new SimpleProperty().setName("minute").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));


			//YUCCA-388
			measureProps.add(new SimpleProperty().setName("dayofweek").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("retweetparentid").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("iduser").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();


			List<Property> componentProp= getDatasetField(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				if ( ((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Decimal) || 
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Int32) || 
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Int64) ||
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
//					SimpleProperty curProp=new SimpleProperty()
//					.setName( ((SimpleProperty)componentProp.get(i)).getName()+"_sts")
//					.setType(((SimpleProperty)componentProp.get(i)).getType())
//					.setFacets(new Facets().setNullable(true));
					
					SimpleProperty curProp=new SimpleProperty()
					.setName( ((SimpleProperty)componentProp.get(i)).getName()+"_sts")
					.setType(EdmSimpleTypeKind.Double)
					.setFacets(new Facets().setNullable(true));					
					measureProps.add(curProp);
				}
				
				if (null!=groupFields && groupFields.indexOf("|"+((SimpleProperty)componentProp.get(i)).getName()+"|")!=-1) {
					measureProps.add(new SimpleProperty().setName(((SimpleProperty)componentProp.get(i)).getName()).setType(((SimpleProperty)componentProp.get(i)).getType()).setFacets(new Facets().setNullable(true)));
					keyPropertiesMeasure.add(new PropertyRef().setName(((SimpleProperty)componentProp.get(i)).getName()));
				}
				
			}

			measureProps.add(new SimpleProperty().setName("count").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			keyPropertiesMeasure.add(new PropertyRef().setName("year"));
			keyPropertiesMeasure.add(new PropertyRef().setName("month"));
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofmonth"));
			keyPropertiesMeasure.add(new PropertyRef().setName("hour"));
			//YUCCA-346
			keyPropertiesMeasure.add(new PropertyRef().setName("minute"));
			//YUCCA-388
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofweek"));
			keyPropertiesMeasure.add(new PropertyRef().setName("retweetparentid"));
			keyPropertiesMeasure.add(new PropertyRef().setName("iduser"));

			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_SOCIAL_STATS)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSocialStatsType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSocialStatsType] END");
		}			
	}



	private EntityType getMeasureType (String nameSpace,Object eleCapmpi) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getMeasureType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();

			// SPOSTATI IN CFGd
			measureProps.add(new SimpleProperty().setName("streamCode").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("sensor").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("time").setType(EdmSimpleTypeKind.DateTimeOffset).setFacets(new Facets().setNullable(false)));

			measureProps.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			measureProps.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			/* VECCHI eliminati */
			//measureProps.add(new ComplexProperty().setName("values").setType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES)));
			//measureProps.add(new SimpleProperty().setName("current").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));

			List<Property> componentProp= getDatasetField(eleCapmpi,nameSpace);
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				measureProps.add(componentProp.get(i));
			}
			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();

			keyPropertiesMeasure.add(new PropertyRef().setName("internalId"));
			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties = new ArrayList<NavigationProperty>();
			navigationProperties.add(new NavigationProperty().setName(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS)
					.setRelationship(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM)).setFromRole(SDPDataApiConstants.ROLE_MEASURE_STREAM).setToRole(SDPDataApiConstants.ROLE_STREAM_MEASURE));
			//		return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
			//				.setProperties(measureProps).setKey(keyMeasure).setNavigationProperties(navigationProperties);	
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getMeasureType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getMeasureType] END");
		}			
	}

	private EntityType getMeasureStatsType (String nameSpace,Object eleCapmpi,String groupFields) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getMeasureStatsType] BEGIN");
			List<Property> measureProps=new ArrayList<Property>();

			//			measureProps.add(new SimpleProperty().setName("internalId").setType(EdmSimpleTypeKind.String).setFacets(new Facets().setNullable(false)));
			//			measureProps.add(new SimpleProperty().setName("datasetVersion").setType(EdmSimpleTypeKind.Int32).setFacets(new Facets().setNullable(true)));
			//			measureProps.add(new SimpleProperty().setName("idDataset").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("year").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("month").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("dayofmonth").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			measureProps.add(new SimpleProperty().setName("hour").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));
			//YUCCA-346
			measureProps.add(new SimpleProperty().setName("minute").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			//YUCCA-388
			measureProps.add(new SimpleProperty().setName("dayofweek").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));


			List<Property> componentProp= getDatasetField(eleCapmpi,nameSpace);
			List<PropertyRef> keyPropertiesMeasure = new ArrayList<PropertyRef>();
			for (int i=0;componentProp!=null && i<componentProp.size();i++) {
				if ( ((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Decimal) || 
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Int32) || 
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Int64) ||
						((SimpleProperty)componentProp.get(i)).getType().equals(EdmSimpleTypeKind.Double)) {
					//				SimpleProperty curProp=new SimpleProperty()
					//				.setName( ((SimpleProperty)componentProp.get(i)).getName()+"_sts")
					//				.setType(((SimpleProperty)componentProp.get(i)).getType())
					//				.setFacets(new Facets().setNullable(true));

					SimpleProperty curProp=new SimpleProperty()
					.setName( ((SimpleProperty)componentProp.get(i)).getName()+"_sts")
					.setType(EdmSimpleTypeKind.Double)
					.setFacets(new Facets().setNullable(true));


					measureProps.add(curProp);
				}

				if (null!=groupFields && groupFields.indexOf("|"+((SimpleProperty)componentProp.get(i)).getName()+"|")!=-1) {
					measureProps.add(new SimpleProperty().setName(((SimpleProperty)componentProp.get(i)).getName()).setType(((SimpleProperty)componentProp.get(i)).getType()).setFacets(new Facets().setNullable(true)));
					keyPropertiesMeasure.add(new PropertyRef().setName(((SimpleProperty)componentProp.get(i)).getName()));
				}

			}

			measureProps.add(new SimpleProperty().setName("count").setType(EdmSimpleTypeKind.Int64).setFacets(new Facets().setNullable(true)));

			keyPropertiesMeasure.add(new PropertyRef().setName("year"));
			keyPropertiesMeasure.add(new PropertyRef().setName("month"));
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofmonth"));
			keyPropertiesMeasure.add(new PropertyRef().setName("hour"));
			//YUCCA-346
			keyPropertiesMeasure.add(new PropertyRef().setName("minute"));

			//YUCCA-388
			keyPropertiesMeasure.add(new PropertyRef().setName("dayofweek"));


			Key keyMeasure = new Key().setKeys(keyPropertiesMeasure);
			return new EntityType().setName(SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS)
					.setProperties(measureProps).setKey(keyMeasure);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getMeasureStatsType] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getMeasureStatsType] END");
		}			
	}

	public List<Schema> getSchemasInternal(String codiceApi) throws ODataException,Exception {
		try {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] BEGIN");
			log.info("[SDPMongoOdataCast::getSchemasInternal] codiceApi="+codiceApi);

			List<Schema> schemas = new ArrayList<Schema>();
			initDbObject(codiceApi);
			for (int i=0;i<this.configObject.size();i++) {
				DBObject obj=this.configObject.get(i);
				String type=((DBObject)obj.get("configData")).get("type").toString();
				String nameSpace=((DBObject)obj.get("configData")).get("entityNameSpace").toString();
				String subType=((DBObject)obj.get("configData")).get("subtype").toString();
				String entContainerDB=SDPDataApiConstants.SMART_ENTITY_CONTAINER+"_"+nameSpace.replace('.', '_');

				//1.2 binary
				String binaryIdDataset=null;
				try {
					binaryIdDataset=takeNvlValues(    obj.get("binaryIdDataset"));
					//binaryIdDataset=takeNvlValues(    ((DBObject)(((DBObject)obj.get("configData")).get("info"))).get("binaryIdDataset"));
				} catch (Exception e) {
					binaryIdDataset=null;
				}

				//entContainerDB=SMART_ENTITY_CONTAINER;

				Schema schema = new Schema();
				schema.setNamespace(nameSpace);

				if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_STREAM.equals(type) || (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISTREAM.equals(subType) )) {

					List<EntityType> entityTypes = new ArrayList<EntityType>();
					//				entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SMARTOBJECT),codiceApi));
					//				entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_STREAMS),codiceApi));
					entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES),codiceApi));
					entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES_STATS),codiceApi));

					//			    entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES),codiceApi));
					//			    entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURECOMPONENTS),codiceApi));
					schema.setEntityTypes(entityTypes);

					//			    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
					//			    complexTypes.add(getComplexType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURECOMPONENTS),codiceApi));
					//			    complexTypes.add(getComplexType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASUREVALUES),codiceApi));
					//			    complexTypes.add(getComplexType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_MEASURES),codiceApi));
					//			    schema.setComplexTypes(complexTypes);

					//				List<Association> associations = new ArrayList<Association>();
					//				associations.add(getAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM),codiceApi));
					//				schema.setAssociations(associations);

					List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
					EntityContainer entityContainer = new EntityContainer();
					entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

					List<EntitySet> entitySets = new ArrayList<EntitySet>();
					entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_MEASURES,codiceApi));

					entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS,codiceApi));

					//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT,codiceApi));
					//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_STREAMS,codiceApi));
					entityContainer.setEntitySets(entitySets);

					//				List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
					//				associationSets.add(getAssociationSet(entContainerDB, new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM),
					//						SDPDataApiConstants.ENTITY_SET_NAME_STREAMS, SDPDataApiConstants.ROLE_STREAM_MEASURE,codiceApi));
					//				entityContainer.setAssociationSets(associationSets);

					//			    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
					//			    functionImports.add(getFunctionImport(entContainerDB, "funzioneProva"),codiceApi);
					//			    entityContainer.setFunctionImports(functionImports);

					entityContainers.add(entityContainer);
					schema.setEntityContainers(entityContainers);

					schemas.add(schema);
				} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTISOCIAL.equals(subType)) {

					List<EntityType> entityTypes = new ArrayList<EntityType>();
					entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL),codiceApi));
					entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_SOCIAL_STATS),codiceApi));

					schema.setEntityTypes(entityTypes);


					List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
					EntityContainer entityContainer = new EntityContainer();
					entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

					List<EntitySet> entitySets = new ArrayList<EntitySet>();
					entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL,codiceApi));

					entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS,codiceApi));

					//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT,codiceApi));
					//entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_STREAMS,codiceApi));
					entityContainer.setEntitySets(entitySets);

					//				List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
					//				associationSets.add(getAssociationSet(entContainerDB, new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_MEASURE_STREAM),
					//						SDPDataApiConstants.ENTITY_SET_NAME_STREAMS, SDPDataApiConstants.ROLE_STREAM_MEASURE,codiceApi));
					//				entityContainer.setAssociationSets(associationSets);

					//			    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
					//			    functionImports.add(getFunctionImport(entContainerDB, "funzioneProva"),codiceApi);
					//			    entityContainer.setFunctionImports(functionImports);

					entityContainers.add(entityContainer);
					schema.setEntityContainers(entityContainers);					
					schemas.add(schema);

				} else if (SDPDataApiConstants.SDPCONFIG_CONSTANTS_TYPE_API.equals(type) && SDPDataApiConstants.SDPCONFIG_CONSTANTS_SUBTYPE_APIMULTIBULK.equals(subType)) {
					//TODO bulk or 
					List<EntityType> entityTypes = new ArrayList<EntityType>();
					entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA),codiceApi));

					//1.2 binary
					if (null!=binaryIdDataset) entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_BINARY),codiceApi));

					//entityTypes.add(getEntityType(new FullQualifiedName(nameSpace, SDPDataApiConstants.ENTITY_NAME_UPLOADDATA_HISTORY),codiceApi));
					schema.setEntityTypes(entityTypes);

					List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
					EntityContainer entityContainer = new EntityContainer();
					entityContainer.setName(entContainerDB).setDefaultEntityContainer(true);

					List<EntitySet> entitySets = new ArrayList<EntitySet>();
					entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA,codiceApi));

					//1.2 binary
					if (null!=binaryIdDataset)   entitySets.add(getEntitySet(entContainerDB, SDPDataApiConstants.ENTITY_SET_NAME_BINARY,codiceApi));


					entityContainer.setEntitySets(entitySets);
					entityContainers.add(entityContainer); 

					//1.2 binary
					List<Association> associations = new ArrayList<Association>();
					if (null!=binaryIdDataset)   associations.add(getAssociation(new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_NAME_DATASETUPLOAD_BINARY),codiceApi));
					schema.setAssociations(associations);

					if (null!=binaryIdDataset)  {
						List<ComplexType> complexTypes = new ArrayList<ComplexType>();
						complexTypes.add(getComplexType(new FullQualifiedName(nameSpace, SDPDataApiConstants.COMPLEX_TYPE_BINARYREF),codiceApi));
						schema.setComplexTypes(complexTypes);
					}

					//1.2 binary
					List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
					if (null!=binaryIdDataset)   associationSets.add(getAssociationSet(entContainerDB, new FullQualifiedName(nameSpace, SDPDataApiConstants.ASSOCIATION_SET_DATASETUPLOAD_BINARY),
							SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA, SDPDataApiConstants.ROLE_DATASETUPLOAD_BINARY,codiceApi));
					entityContainer.setAssociationSets(associationSets);					

					schema.setEntityContainers(entityContainers);
					schemas.add(schema);
				}
			}
			return schemas;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getSchemasInternal] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getSchemasInternal] END");
		}		
	}	

	private String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}

	private List<Property> getDatasetField(Object eleCapmpi,String nameSpace) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getDatasetField] BEGIN");

			List<Property> propOut=new ArrayList<Property>();

			BasicDBList lista=null;
			if (eleCapmpi instanceof BasicDBList) {
				lista=(BasicDBList)eleCapmpi;
			} else {
				lista=new BasicDBList();
				lista.add(eleCapmpi);
			}

			for (int i=0;i<lista.size();i++) {
				DBObject elemento=(DBObject)lista.get(i);
				Set<String> chivi= elemento.keySet();
				log.debug("[SDPMongoOdataCast::getDatasetField] elencoCampi["+i+"]="+elemento);


				String propName=null;
				String porpType=null;
				Iterator<String> itcomp=chivi.iterator();
				while (itcomp.hasNext()) {
					String chiaveCur=itcomp.next();
					String valor=takeNvlValues(elemento.get(chiaveCur));

					log.debug("[SDPMongoOdataCast::getDatasetField] elencoCampi["+i+"] --> chiave,valore = ("+chiaveCur+","+valor+")");

					if (chiaveCur.equals("fieldName")) propName=valor;
					if (chiaveCur.equals("dataType")) porpType=valor;
				}
				if (SDPDataApiConstants.SDP_DATATYPE_MAP.get(porpType).equals(EdmSimpleTypeKind.Binary)) {
					propOut.add(new ComplexProperty().setName(propName).setType(new FullQualifiedName(nameSpace,SDPDataApiConstants.COMPLEX_TYPE_BINARYREF)).setFacets(new Facets().setNullable(true)));
				} else {
					propOut.add(new SimpleProperty().setName(propName).setType(SDPDataApiConstants.SDP_DATATYPE_MAP.get(porpType)).setFacets(new Facets().setNullable(true)));
				}
			}
			return propOut;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getDatasetField] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getDatasetField] END");
		}		
	}	



	public HashMap<String, String> getDatasetMetadata (String codiceApi) throws Exception{
		initDbObject(codiceApi);
		List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));

		BasicDBList elencoCampi= mongoDataAccess.getMetadataComponents(elencoDataset.get(0));
		HashMap<String, String> mappaCampi=new HashMap<String, String>();

		for (int i=0; i<elencoCampi.size();i++) {
			BasicDBObject cur= (BasicDBObject)elencoCampi.get(i);
			String nome=cur.getString("fieldName");
			String tipo=cur.getString("dataType");
			mappaCampi.put(nome, tipo);

		}


		return mappaCampi;
	}


	/**
	 * DATI
	 * @param codiceApi
	 * @param nameSpace
	 * @param entityContainer
	 * @param internalId
	 * @return
	 */
	public SDPDataResult getMeasuresPerApi(String codiceApi, String nameSpace, EdmEntityContainer entityContainer, String internalId, Object userQuery,Object userOrderBy,
			int skip,
			int limit,
			String dataType) throws Exception{

		// TODO YUCCA-74 odata evoluzione
		try {
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] BEGIN");
			log.info("[SDPMongoOdataCast::getMeasuresPerApi] codiceApi = " + codiceApi);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] nameSpace = " + nameSpace);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] entityContainer = " + entityContainer);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] internalId = " + internalId);
			log.info("[SDPMongoOdataCast::getMeasuresPerApi] userQuery = " + userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
			int totCnt = 0;
			//List<DBObject> elencoDS = mongoDataAccess.getDatasetPerApi(codiceApi);

			//List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));

			List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 *  elencodataset potrebbe contenere pi� elementi dello stesso dataset in versione differente ad es:
			 *  idDataset= 1, datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 *  idDataset= 1, datasetVersion=2, [campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1, [campo1:log]
			 *  
			 *  deve diventare 
			 *  idDataset= 1, datasetVersion=1,2, [campo1:int,camp2:string,campo3:date,campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1 [campo1:log]
			 *  
			 *  
			 *  si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti di questo tipo:
			 *  
			 *  idDataset
			 *  array di datasetVersion
			 *  array dei campi ottenuto come join dei campi delle varie versioni di quel dataset
			 *  parte di config (presa da una versione a caso)
			 *  info presa da una versione a caso
			 *  
			 */

			String dsCodes="|";
			String tenantsCodes="|";

			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] Dataset.size = " + elencoDataset.size());
			for (int i=0;elencoDataset!=null && i<elencoDataset.size(); i++) {

				log.debug("[SDPMongoOdataCast::getMeasuresPerApi] Dataset = " + ((DBObject)elencoDataset.get(i)));

				String nameSpaceStrean=((DBObject)elencoDataset.get(i).get("configData")).get("entityNameSpace").toString();
				String tenantStrean=((DBObject)elencoDataset.get(i).get("configData")).get("tenantCode").toString();

				String datasetCode=((DBObject)elencoDataset.get(i)).get("datasetCode").toString();
				dsCodes+=datasetCode+"|";
				tenantsCodes+=tenantStrean+"|";


				//				SDPDataResult cur=mongoDataAccess.getMeasuresPerStream(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_MEASURE, userQuery
				//						,userOrderBy,skip,limit);
				SDPDataResult cur=mongoDataAccess.getMeasuresPerStreamNewLimitSolr(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,dataType, userQuery
						,userOrderBy,skip,limit);



				List<Map<String, Object>> misureCur = cur.getDati();

				for (int k=0;misureCur!=null && k<misureCur.size(); k++) {
					ret.add(misureCur.get(k));
				}
				totCnt+=cur.getTotalCount();

			}

			return new SDPDataResult(ret,totCnt,tenantsCodes,dsCodes);

		} catch (SDPOrderBySizeException e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerApi] SDPOrderBySizeException" +e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerDataset] SDPPageSizeException" +e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] END");

		}			
	}	

	private List<DBObject> orderNestDS(List<DBObject> listDS) {
		log.debug("[SDPMongoOdataCast::orderNestDS] START");
		log.debug("[SDPMongoOdataCast::orderNestDS] listDS = " + listDS);
		List<DBObject> elenco = new ArrayList();
		List<String> dsCodeList = new ArrayList();
		Iterator<DBObject> iteretorDS = listDS.iterator();
		while (iteretorDS.hasNext()) {
			DBObject itemDS = iteretorDS.next();
			String datasetCode = (String) itemDS.get("datasetCode");

			Integer posizioneItemDS = 0;

			//Condizione in cui nella lista elenco non c'�, ancora, nessun dataset con questo determinato datasetCode 
			if (dsCodeList.indexOf(datasetCode) == -1) {

				List<DBObject> tmpArrList = new ArrayList();
				Integer obj = (Integer) itemDS.get("datasetVersion");
				DBObject tmpDSVersion = new BasicDBObject("datasetVersion"+datasetCode, obj);
				tmpArrList.add(tmpDSVersion);
				itemDS.put("listDatasetVersion", tmpArrList);
				elenco.add(itemDS);
				dsCodeList.add(datasetCode);
				posizioneItemDS = dsCodeList.size();
			} else {

				//Ho giˆ inserito nella list elenco il dataset con questo determinato datasetCode, quindi vado ad integrare
				Integer pos = 0;
				Iterator<DBObject> elIterator = elenco.iterator();
				while (elIterator.hasNext()) {
					DBObject el = elIterator.next();
					if (el.get("datasetCode").equals(itemDS.get("datasetCode"))){
						DBObject tmpInfoElDS = (DBObject) el.get("info");
						List<DBObject> tmpFieldsInfoElDS = (List<DBObject>) tmpInfoElDS.get("fields");

						DBObject tmpInfoItemDS = (DBObject) itemDS.get("info");
						List<DBObject> tmpFieldsInfoItemDS = (List<DBObject>) tmpInfoItemDS.get("fields");

						// TODO: aggiungo i fields della nuova versione e aggiorno il campo versione
						Iterator<DBObject> tmpFieldsIterator = tmpFieldsInfoItemDS.iterator();
						while (tmpFieldsIterator.hasNext()) {
							DBObject fd = tmpFieldsIterator.next();
							tmpFieldsInfoElDS.add(fd);
						}
						List<DBObject> tmpDSVerElDS = (List<DBObject>) el.get("listDatasetVersion");
						//tmpDSVerElDS.add((DBObject) el.get("DatasetVersion"));


						Integer itemDSVers = (Integer) itemDS.get("datasetVersion");
						DBObject tmpDSVersion = new BasicDBObject("datasetVersion"+itemDS.get("datasetCode"), itemDSVers);
						tmpDSVerElDS.add(tmpDSVersion);

						break;
					}
				}
			}
		}

		log.debug("[SDPMongoOdataCast::orderNestDS] elenco = " + elenco);
		log.debug("[SDPMongoOdataCast::orderNestDS] END");
		return elenco;
	}

	public SDPDataResult getMeasuresStatsPerApi(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,String internalId, Object userQuery,Object userOrderBy,
			int skip,
			int limit,
			String timeGroupByParam,
			String timeGroupOperatorsParam,
			Object groupOutQuery,String dataType) throws Exception{

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] BEGIN");
			log.info("[SDPMongoOdataCast::getMeasuresPerApi] codiceApi="+codiceApi);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] entityContainer="+entityContainer);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] internalId="+internalId);
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] userQuery="+userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();
			int totCnt=0;
			//List<DBObject> elencoDataset=mongoDataAccess.getDatasetPerApi(codiceApi);

			//List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));


			List<DBObject> elencoDatasetFull = mongoDataAccess.getDatasetPerApi(codiceApi);
			String elencoCampiGroup="";
			for (int i=0;elencoDatasetFull!=null && i<elencoDatasetFull.size(); i++) {
				try {
					if ("1".equals(((DBObject)elencoDatasetFull.get(i).get("configData")).get("current").toString())) {
						elencoCampiGroup=((DBObject)elencoDatasetFull.get(i).get("info")).get("groupFields").toString();
					}
				} catch (Exception e) {}
			}			



			List<DBObject> elencoDataset = orderNestDS(elencoDatasetFull);
			//List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 *  elencodataset potrebbe contenere più elementi dello stesso dataset in versione differente ad es:
			 *  idDataset= 1, datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 *  idDataset= 1, datasetVersion=2, [campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1, [campo1:log]
			 *  
			 *  deve diventare 
			 *  idDataset= 1, datasetVersion=1,2, [campo1:int,camp2:string,campo3:date,campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1 [campo1:log]
			 *  
			 *  
			 *  si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti di questo tipo:
			 *  
			 *  idDataset
			 *  array di datasetVersion
			 *  array dei campi ottenuto come join dei campi delle varie versioni di quel dataset
			 *  parte di config (presa da una versione a caso)
			 *  info presa da una versione a caso
			 *  
			 */	

			String dsCodes="|";
			String tenantsCodes="|";

			for (int i=0;elencoDataset!=null && i<elencoDataset.size(); i++) {
				String nameSpaceStrean=((DBObject)elencoDataset.get(i).get("configData")).get("entityNameSpace").toString();
				String tenantStrean=((DBObject)elencoDataset.get(i).get("configData")).get("tenantCode").toString();

				String datasetCode=((DBObject)elencoDataset.get(i)).get("datasetCode").toString();


				dsCodes+=datasetCode+"|";
				tenantsCodes+=tenantStrean+"|";
				//				SDPDataResult cur=mongoDataAccess.getMeasuresStatsPerStreamPhoenix(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,dataType, userQuery
				//						,userOrderBy,skip,limit,timeGroupByParam,timeGroupOperatorsParam,groupOutQuery);


				SDPDataResult cur=mongoDataAccess.getMeasuresStatsPerStreamSolr(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,dataType, userQuery
						,userOrderBy,skip,limit,timeGroupByParam,timeGroupOperatorsParam,groupOutQuery,elencoCampiGroup);


				List<Map<String, Object>> misureCur = cur.getDati();

				for (int k=0;misureCur!=null && k<misureCur.size(); k++) {
					ret.add(misureCur.get(k));
				}
				totCnt+=cur.getTotalCount();

			}

			return new SDPDataResult(ret,totCnt,tenantsCodes,dsCodes);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerApi] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] END");

		}			
	}		

	public SDPDataResult getMeasuresPerDataset(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,String internalId, Object userQuery,Object userOrderBy,
			int skip,
			int limit) throws Exception{

		// TODO YUCCA-74 odata evoluzione
		try {
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] BEGIN");
			log.info("[SDPMongoOdataCast::getMeasuresPerDataset] codiceApi="+codiceApi);
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] entityContainer="+entityContainer);
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] internalId="+internalId);
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] userQuery="+userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();

			//List<DBObject> elencoDS = mongoDataAccess.getDatasetPerApi(codiceApi);

			List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));
			//elencoDataset = mongoDataAccess.getDatasetPerApi(codiceApi);
			int totCnt=0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 *  elencodataset potrebbe contenere più elementi dello stesso dataset in versione differente ad es:
			 *  idDataset= 1, datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 *  idDataset= 1, datasetVersion=2, [campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1, [campo1:log]
			 *  
			 *  deve diventare 
			 *  idDataset= 1, datasetVersion=1,2, [campo1:int,camp2:string,campo3:date,campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1 [campo1:log]
			 *  
			 *  
			 *  si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti di questo tipo:
			 *  
			 *  idDataset
			 *  array di datasetVersion
			 *  array dei campi ottenuto come join dei campi delle varie versioni di quel dataset
			 *  parte di config (presa da una versione a caso)
			 *  info presa da una versione a caso
			 *  
			 *  
			 */

			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] Dataset.size = " + elencoDataset.size());
			log.debug("[SDPMongoOdataCast::getMeasuresPerApi] elencoDataset = " + elencoDataset);

			String dsCodes="|";
			String tenantsCodes="|";

			for (int i=0;elencoDataset!=null && i<elencoDataset.size(); i++) {

				log.debug("[SDPMongoOdataCast::getMeasuresPerApi] Dataset = " + ((DBObject)elencoDataset.get(i)));
				//TODO log a debug
				String nameSpaceStrean=((DBObject)elencoDataset.get(i).get("configData")).get("entityNameSpace").toString();
				String tenantStrean=((DBObject)elencoDataset.get(i).get("configData")).get("tenantCode").toString();

				String datasetCode=((DBObject)elencoDataset.get(i)).get("datasetCode").toString();

				dsCodes+=datasetCode+"|";
				tenantsCodes+=tenantStrean+"|";


				//				SDPDataResult cur=mongoDataAccess.getMeasuresPerStream(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_DATA, userQuery
				//						,userOrderBy,skip,limit);
				//				SDPDataResult cur=mongoDataAccess.getMeasuresPerStreamNewLimit(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_DATA, userQuery
				//						,userOrderBy,skip,limit);

				SDPDataResult cur=mongoDataAccess.getMeasuresPerStreamNewLimitSolr(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_DATA, userQuery
						,userOrderBy,skip,limit);


				List<Map<String, Object>> misureCur = cur.getDati();
				for (int k=0;misureCur!=null && k<misureCur.size(); k++) {
					ret.add(misureCur.get(k));
				}
				totCnt+=cur.getTotalCount();
			}

			return new SDPDataResult(ret,totCnt,tenantsCodes,dsCodes);
		} catch (SDPOrderBySizeException e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerDataset] SDPOrderBySizeException" +e);
			throw (SDPOrderBySizeException)e;
		} catch (SDPPageSizeException e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerDataset] SDPPageSizeException" +e);
			throw (SDPPageSizeException)e;
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getMeasuresPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getMeasuresPerDataset] END");

		}		
	}	

	public SDPDataResult getBynaryPerDataset(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,String internalId, Object userQuery,Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			int skip,
			int limit) throws Exception{

		// TODO YUCCA-74 odata evoluzione

		try {
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] BEGIN");
			log.info("[SDPMongoOdataCast::getBynaryPerDataset] codiceApi="+codiceApi);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] entityContainer="+entityContainer);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] internalId="+internalId);
			log.info("[SDPMongoOdataCast::getBynaryPerDataset] userQuery="+userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();

			//List<DBObject> elencoDataset=mongoDataAccess.getDatasetPerApi(codiceApi);

			List<DBObject> elencoDataset = orderNestDS(mongoDataAccess.getDatasetPerApi(codiceApi));
			int totCnt=0;

			// TODO YUCCA-74 odata evoluzione - dettaglio
			/*
			 *  elencodataset potrebbe contenere più elementi dello stesso dataset in versione differente ad es:
			 *  idDataset= 1, datasetVersion=1, [campo1:int,camp2:string,campo3:date]
			 *  idDataset= 1, datasetVersion=2, [campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1, [campo1:log]
			 *  
			 *  deve diventare 
			 *  idDataset= 1, datasetVersion=1,2, [campo1:int,camp2:string,campo3:date,campo1:int,camp2:string,campo3:date,campo11:long]
			 *  idDataset= 3, datasetVersion=1 [campo1:log]
			 *  
			 *  
			 *  si dovrebbe trasformare List<DBObject> elencoDataset in un array di oggetti di questo tipo:
			 *  
			 *  idDataset
			 *  array di datasetVersion
			 *  array dei campi ottenuto come join dei campi delle varie versioni di quel dataset
			 *  parte di config (presa da una versione a caso)
			 *  info presa da una versione a caso
			 *  
			 */			
			String dsCodes="|";
			String tenantsCodes="|";

			for (int i=0;elencoDataset!=null && i<elencoDataset.size(); i++) {
				//TODO log a debug
				String nameSpaceStrean=((DBObject)elencoDataset.get(i).get("configData")).get("entityNameSpace").toString();
				String tenantStrean=((DBObject)elencoDataset.get(i).get("configData")).get("tenantCode").toString();

				String datasetCode=((DBObject)elencoDataset.get(i)).get("datasetCode").toString();

				dsCodes+=datasetCode+"|";
				tenantsCodes+=tenantStrean+"|";


				SDPDataResult cur=mongoDataAccess.getBinary(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_DATA, userQuery
						,userOrderBy,elencoIdBinary,codiceApi,skip,limit);
				List<Map<String, Object>> misureCur = cur.getDati();
				for (int k=0;misureCur!=null && k<misureCur.size(); k++) {
					ret.add(misureCur.get(k));
				}
				totCnt+=cur.getTotalCount();
			}

			return new SDPDataResult(ret,totCnt,tenantsCodes,dsCodes);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getBynaryPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] END");

		}		
	}	

	public SDPDataResult getDatasetPerBinary(String codiceApi, String nameSpace, EdmEntityContainer entityContainer,String internalId, Object userQuery,Object userOrderBy,
			ArrayList<String> elencoIdBinary,
			int skip,
			int limit) throws Exception{
		try {
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] BEGIN");
			log.info("[SDPMongoOdataCast::getBynaryPerDataset] codiceApi="+codiceApi);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] nameSpace="+nameSpace);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] entityContainer="+entityContainer);
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] internalId="+internalId);
			log.info("[SDPMongoOdataCast::getBynaryPerDataset] userQuery="+userQuery);

			initDbObject(codiceApi);
			List<Map<String, Object>> ret= new ArrayList<Map<String, Object>>();

			List<DBObject> elencoDataset=mongoDataAccess.getDatasetPerApi(codiceApi);
			int totCnt=0;

			String dsCodes="|";
			String tenantsCodes="|";


			for (int i=0;elencoDataset!=null && i<elencoDataset.size(); i++) {
				//TODO log a debug
				String nameSpaceStrean=((DBObject)elencoDataset.get(i).get("configData")).get("entityNameSpace").toString();
				String tenantStrean=((DBObject)elencoDataset.get(i).get("configData")).get("tenantCode").toString();


				String datasetCode=((DBObject)elencoDataset.get(i)).get("datasetCode").toString();

				dsCodes+=datasetCode+"|";
				tenantsCodes+=tenantStrean+"|";


				SDPDataResult cur=mongoDataAccess.getBinary(tenantStrean,nameSpaceStrean,entityContainer,(DBObject)elencoDataset.get(i),internalId,SDPDataApiMongoAccess.DATA_TYPE_DATA, userQuery
						,userOrderBy,elencoIdBinary,codiceApi,skip,limit);
				List<Map<String, Object>> misureCur = cur.getDati();
				for (int k=0;misureCur!=null && k<misureCur.size(); k++) {
					ret.add(misureCur.get(k));
				}
				totCnt+=cur.getTotalCount();
			}

			return new SDPDataResult(ret,totCnt,tenantsCodes,dsCodes);
		} catch (Exception e) {
			log.error("[SDPMongoOdataCast::getBynaryPerDataset] " + e);
			throw e;
		} finally {
			log.debug("[SDPMongoOdataCast::getBynaryPerDataset] END");
		}		
	}	
}