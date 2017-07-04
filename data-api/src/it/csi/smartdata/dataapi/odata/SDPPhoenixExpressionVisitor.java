package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmType;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.ExpressionVisitor;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.LiteralExpression;
import org.apache.olingo.odata2.api.uri.expression.MemberExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodExpression;
import org.apache.olingo.odata2.api.uri.expression.MethodOperator;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.PropertyExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;
import org.apache.olingo.odata2.api.uri.expression.UnaryExpression;
import org.apache.olingo.odata2.api.uri.expression.UnaryOperator;
import org.apache.olingo.odata2.core.edm.Uint7;
import org.apache.olingo.odata2.core.edm.provider.EdmSimplePropertyImplProv;
import org.apache.olingo.odata2.core.uri.expression.OrderByExpressionImpl;
import org.apache.olingo.odata2.core.uri.expression.OrderExpressionImpl;
import org.apache.olingo.odata2.core.uri.expression.PropertyExpressionImpl;

public class SDPPhoenixExpressionVisitor implements ExpressionVisitor {
	static Logger log = Logger.getLogger(SDPExpressionVisitor.class.getPackage().getName());

	
	
	public static final int MODE_BASIC=0; 
	public static final int MODE_STATS_HAVINGCLAUSE=1; 
	
	private int visitorMOde=MODE_BASIC;
	
	private String entitySetName=null;

	private HashMap<String, String> mappaCampi=null;
	
	public HashMap<String, String> getMappaCampi() {
		return mappaCampi;
	}
	public void setMappaCampi(HashMap<String, String> mappaCampi) {
		this.mappaCampi = mappaCampi;
	}
	public String getEntitySetName() {
		return entitySetName;
	}
	public void setEntitySetName(String entitySetName) {
		this.entitySetName = entitySetName;
	}
	
	public void setvisitorMOde(int visitorMOde) {
		this.visitorMOde = visitorMOde;
	}
	
	
	@Override
	public Object visitBinary(final BinaryExpression binaryExpression, final BinaryOperator operator, final Object leftSide, final Object rightSide) {
		//Transform the OData filter operator into an equivalent sql operator
		String sqlOperator = "";
		switch (operator) {
		case EQ:
			sqlOperator = "=";
			break;
		case NE:
			sqlOperator = "<>";
			break;
		case OR:
			sqlOperator = "OR";
			break;
		case AND:
			sqlOperator = "AND";
			break;
		case GE:
			sqlOperator = ">=";
			break;
		case GT:
			sqlOperator = ">";
			break;
		case LE:
			sqlOperator = "<=";
			break;
		case LT:
			sqlOperator = "<";
			break;
		default:
			//Other operators are not supported for SQL Statements
			throw new UnsupportedOperationException("Unsupported operator: " + operator.toUriLiteral());
		}
		//The idea is to check if the left side is of type property. If this is the case we append the property name and the operator to the where clause
		if (leftSide instanceof EdmTyped && !(rightSide instanceof SDPPhoenixExpression)) {
			//if (leftSide instanceof EdmTyped && !(rightSide instanceof String)) {
			SDPPhoenixExpression expression = new SDPPhoenixExpression(operator);
			try {
			if (rightSide instanceof Date) {
				SimpleDateFormat dateFormatB = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSS");
				dateFormatB.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				String qq = dateFormatB.format((Date)rightSide);
				expression.setPrepeparedWhere(      getFullFielName(((EdmTyped)   leftSide).getName(),((EdmTyped)   leftSide).getType()) + " " + sqlOperator + " TO_DATE('"+qq+"','yyyy-MM-dd HH:mm:ss.SSS') ");
				
			} else {
					expression.setPrepeparedWhere(      getFullFielName(((EdmTyped)   leftSide).getName(),((EdmTyped)   leftSide).getType()) + " " + sqlOperator + " ?");
				expression.addParameter(rightSide);
			}
			} catch (EdmException e) {
				throw new RuntimeException("EdmException occured");
			}
			return expression;
		} else if (leftSide instanceof SDPPhoenixExpression && rightSide instanceof SDPPhoenixExpression) {
			SDPPhoenixExpression returnExpression = new SDPPhoenixExpression(operator);
			SDPPhoenixExpression leftSideExpression = (SDPPhoenixExpression) leftSide;
			if (BinaryOperator.AND.equals    (leftSideExpression.getOperator()) || BinaryOperator.OR.equals(leftSideExpression.getOperator())) {
				leftSideExpression.setPrepeparedWhere("(" + leftSideExpression.toString() + ")");
			}
			SDPPhoenixExpression rightSideExpression = (SDPPhoenixExpression) rightSide;
			if (BinaryOperator.AND.equals(rightSideExpression.getOperator()) || BinaryOperator.OR.equals(rightSideExpression.getOperator())) {
				rightSideExpression.setPrepeparedWhere("(" + rightSideExpression.toString() + ")");
			}
			returnExpression.setPrepeparedWhere(leftSideExpression.toString() + " " + sqlOperator + " " + rightSideExpression.toString());

			for (Object parameter : leftSideExpression.getParameters()) {
				returnExpression.addParameter(parameter);
			}
			for (Object parameter : rightSideExpression.getParameters()) {
				returnExpression.addParameter(parameter);
			}
			return returnExpression;
		} else {
			throw new RuntimeException("Not right format");
		}
	}	
	@Override
	public Object visitProperty(final PropertyExpression propertyExpression, final String uriLiteral, final EdmTyped edmProperty) {
		if (edmProperty == null) {
			//If a property is not found it wont be represented in the database thus we have to throw an exception

			throw new UnsupportedOperationException("Could not find Property: " + uriLiteral);
		} else {
			
			
		
			
			//To distinguish between literals and properties we give back the whole edmProperty in this case
//			if (this.visitorMOde==this.MODE_STATS_HAVINGCLAUSE) {
//				try {
//				String ret=this.fieldAppendMap.get(this.entitySetName+"."+edmProperty.getName());
//				if (ret!=null) return ret; 				
//				} catch (Exception e) {
//					
//				}
//			}
			return edmProperty;
			
		}

//		try {
//			
//			return getFullFielName (uriLiteral,edmProperty.getType());
//
//		} catch (EdmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;

		
		
	}


	@Override
	public Object visitLiteral(final LiteralExpression literal, final EdmLiteral edmLiteral) {
		//Sql Injection is not possible anymore since we are using prepared statements. Thus we can just give back the edmLiteral content
		//return edmLiteral.getLiteral();
		return visitLiteral_n( literal,edmLiteral);

	}
	
	
	public Object visitLiteral_n(LiteralExpression paramLiteralExpression,
			EdmLiteral paramEdmLiteral) {
		if(EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Boolean(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			//float??
			Object ret = new Double(paramEdmLiteral.getLiteral());
			//Object ret = new Float(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Int32.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Int64.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			//Object ret = new Integer(paramEdmLiteral.getLiteral());
			Object ret = new Long(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
//			String retStr =StringUtils.replace(new String(paramEdmLiteral.getLiteral()), " ", "\\ ");
//			retStr =StringUtils.replace(retStr, "+", "\\+");
//			retStr =StringUtils.replace(retStr, "-", "\\-");
//			retStr =StringUtils.replace(retStr, "&&", "\\&&");
//			retStr =StringUtils.replace(retStr, "||", "\\||");
//			retStr =StringUtils.replace(retStr, "!", "\\!");
//			retStr =StringUtils.replace(retStr, "(", "\\(");
//			retStr =StringUtils.replace(retStr, ")", "\\)");
//			retStr =StringUtils.replace(retStr, "{", "\\{");
//			retStr =StringUtils.replace(retStr, "}", "\\}");
//			retStr =StringUtils.replace(retStr, "[", "\\[");
//			retStr =StringUtils.replace(retStr, "]", "\\]");
//			retStr =StringUtils.replace(retStr, "^", "\\^");
//			retStr =StringUtils.replace(retStr, "\"", "\\\"");
//			retStr =StringUtils.replace(retStr, "~", "\\~");
//			retStr =StringUtils.replace(retStr, "*", "\\*");
//			retStr =StringUtils.replace(retStr, "?", "\\?");
//			retStr =StringUtils.replace(retStr, ":", "\\:");
//			retStr =StringUtils.replace(retStr, "\\", "\\\\");
			Object ret=StringUtils.replaceEach(new String(paramEdmLiteral.getLiteral()), 
						new String [] { "\\"   ," "  , "+"  ,"-"  ,":"  , "?"  ,"*"   ,"~"  ,"^"  , "]"  , "["  ,"{"  , "}"  , "("   , ")"  ,"!"  , "||"  , "&&"  , "\""} , 
						new String [] { "\\\\" ,"\\ ", "\\+","\\-","\\:", "\\?","\\*" ,"\\~","\\^", "\\]", "\\[","\\{", "\\}", "\\(" , "\\)","\\!", "\\||", "\\&&", "\\\""} );
			 
			
			
			//TODO levare apici inizio e fine???
			return ret;
		} else if(EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Double(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			try {

				Date data =null;
				SimpleDateFormat dateFormatA = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				SimpleDateFormat dateFormatB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				SimpleDateFormat dateFormatC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				SimpleDateFormat dateFormatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");


				try  {
					data = dateFormatA.parse(paramEdmLiteral.getLiteral());
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatB.parse(paramEdmLiteral.getLiteral());
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatC.parse(paramEdmLiteral.getLiteral());
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatD.parse(paramEdmLiteral.getLiteral());
				} catch (Exception e) {}




				data.setTime(data.getTime()-data.getTimezoneOffset()*60*1000);

				// per deprecation da sostituire con (Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET))

				return data;

			} catch (Exception e) {
				log.error("[SDPExpressionVisitor::visitLiteral] exception handling "+e);
			}
		} else if(EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			try {
				String dataStrIn=paramEdmLiteral.getLiteral();

				String dataStr=dataStrIn;
				if (dataStrIn.length()==29 || dataStrIn.length()==25) {
					String dataPrima=dataStrIn.substring(0,dataStrIn.length()-5);
					String timeZ=dataStrIn.substring(dataStrIn.length()-5);
					timeZ=timeZ.replace(":", "");
					dataStr=dataPrima+timeZ;
				}


				Date data =null;
				java.sql.Date dataSql=null;
				
				
				SimpleDateFormat dateFormatA = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
				SimpleDateFormat dateFormatB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				SimpleDateFormat dateFormatC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				SimpleDateFormat dateFormatD = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

				SimpleDateFormat dateFormatE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				SimpleDateFormat dateFormatF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
				SimpleDateFormat dateFormatG = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz");
				SimpleDateFormat dateFormatH = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");

				
				dateFormatA.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatB.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatC.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatD.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatE.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatF.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatG.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
				dateFormatH.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));


				//				try  {
				//					data = dateFormatE.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatF.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatG.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatH.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data =  dateFormatA.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatB.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatC.parse(dataStr);
				//				} catch (Exception e) {}
				//				try  {
				//					if (data==null) data = dateFormatD.parse(dataStr);
				//				} catch (Exception e) {}


				try  {
					data = dateFormatE.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatF.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatG.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatH.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatB.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data =  dateFormatA.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatD.parse(dataStr);
				} catch (Exception e) {}
				try  {
					if (data==null) data = dateFormatC.parse(dataStr);
				} catch (Exception e) {}











				//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				//				System.out.println("---------------      "+data.getTimezoneOffset());
				//				System.out.println("---------------      |"+dateFormat.format(data)+"|");
				//				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				//				System.out.println("---------------      |"+dateFormat2.format(data)+"|");



				//data.setTime(data.getTime()-data.getTimezoneOffset()*60*1000);

				// per deprecation da sostituire con (Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET))

				//return data;
				
				SimpleDateFormat dateFormatBB = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				SimpleDateFormat dateFormatEE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				String a = dateFormatBB.format(data);
				String b = dateFormatEE.format(data);
				String c = dateFormatB.format(data);
				String d = dateFormatE.format(data);
				
				//return c;
				return data;
				//dataSql=new java.sql.Date(data.getTime()); 

				
				

			} catch (Exception e) {
				log.error("[SDPExpressionVisitor::visitLiteral] exception handling "+e);
			}
		} else if(Uint7.getInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(org.apache.olingo.odata2.core.edm.Bit.getInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Byte.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.SByte.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		}




		//out.append("visitLiteral ").append(paramLiteralExpression.getUriLiteral()).append("--"+paramEdmLiteral.getType()+"\n");
		return null;
	}	
	
	@Override
	public Object visitFilterExpression(FilterExpression filterExpression,
			String expressionString, Object expression) {
		// TODO Auto-generated method stub
		return expression;
	}
	@Override
	public Object visitOrderByExpression(OrderByExpression orderByExpression,
			String expressionString, List<Object> orders) {
		// TODO Auto-generated method stub
		
		
		try {
		OrderByExpressionImpl orb=(OrderByExpressionImpl ) orderByExpression;
		List<OrderExpression> lista= orb.getOrders();
		String ret= null;
		for (int i =0 ; i<lista.size();i++) {
			OrderExpressionImpl cur= (OrderExpressionImpl) lista.get(i);
			
			
			
			String nome = (fieldAppendMap.get(this.entitySetName+"."+cur.getExpression().getUriLiteral()) == null ?cur.getExpression().getUriLiteral() : fieldAppendMap.get(this.entitySetName+"."+cur.getExpression().getUriLiteral()));
			nome="\""+nome+"\"";
			if (ret==null) ret = nome+ " " + cur.getSortOrder();
			else ret+=", "+nome+ " " + cur.getSortOrder();
			
		}
		
		return ret;
		} catch (Exception e ) {
			
		}
		
		return expressionString;
	}
	@Override
	public Object visitOrder(OrderExpression orderExpression,
			Object filterResult, SortOrder sortOrder) {
		
		
		
		return null;
	}
	@Override
	public Object visitMethod(MethodExpression methodExpression,
			MethodOperator method, List<Object> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object visitMember(MemberExpression memberExpression, Object path,
			Object property) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object visitUnary(UnaryExpression unaryExpression,
			UnaryOperator operator, Object operand) {
		// TODO Auto-generated method stub
		return null;
	}	

	
	
	
	
	
	
	
	
	
	
	
	private String getFullFielName(String fieldNameInput,EdmType type) {
		return "\""+getFullFielName( fieldNameInput, type,false)+"\"";
	}
	
	
	
	private String getFullFielName(String fieldNameInput,EdmType type,boolean dummyboolean ) {
		String ret=this.fieldAppendMap.get(this.entitySetName+"."+fieldNameInput);
		if (ret==null) ret = fieldNameInput;
		
		if ("id".equals(ret)) return ret;
		if (this.visitorMOde==this.MODE_BASIC) {

		String tipo=this.mappaCampi.get(ret);
		ret += (SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo)!=null ? SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo) : "");
		}
		if (ret!=null) return ret;
		else return fieldNameInput;
	}


	private static Map<String,String> fieldAppendMap;
	static {
		fieldAppendMap = new HashMap<String,String>();
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".internalId" ,"id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".time" ,"time_dt");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".idDataset" ,"idDataset_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".datasetVersion" ,"datasetVersion_l");
		
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".internalId" ,"id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".idDataset" ,"idDataset_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".time" ,"time_dt");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".datasetVersion" ,"datasetVersion_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".count" ,"totale");

		
		
		
		
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".count" ,"totale");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".count" ,"totale");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".internalId" ,"id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".time" ,"time_dt");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".idDataset" ,"idDataset_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".datasetVersion" ,"datasetVersion_l");
		
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".internalId" ,"id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".idDataset" ,"idDataset_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".time" ,"time_dt");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".datasetVersion" ,"datasetVersion_l");
		
		
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".internalId" ,"id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".idDataset" ,"idDataset_l");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".idBinary" ,"idBinary_s");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".filenameBinary" ,"filenameBinary_s");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".aliasNameBinary" ,"aliasNameBinary_s");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".contentTypeBinary" ,"contentTypeBinary_s");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".urlDownloadBinary" ,"urlDownloadBinary_s");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_BINARY+".metadataBinary" ,"metadataBinary_s");
		
		
		
		
		
		
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".codiceStream" ,"streams.stream.codiceStream");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".codiceTenant" ,"streams.stream.codiceTenant");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".nomeStream" ,"streams.stream.nomeStream");
//
//		//MISURE - non serve ma per tenere traccia ...
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".streamCode" ,"streamCode");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".sensor" ,"sensor");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".internalId" ,"_id");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".time" ,"time");
//
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL+".internalId" ,"_id");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA+".internalId" ,"_id");
//
//
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".year" ,"_id.year");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".month" ,"_id.month");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".dayofmonth" ,"_id.dayofmonth");
//		//YUCCA-388
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".dayofweek" ,"_id.dayofweek");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".hour" ,"_id.hour");
//		//YUCCA-346
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".minute" ,"_id.minute");
//
//		//YUCCA-388
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".minute" ,"_id.retweetparentid");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES_STATS+".minute" ,"_id.iduser");
//
//		
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".year" ,"_id.year");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".month" ,"_id.month");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".dayofmonth" ,"_id.dayofmonth");
//		//YUCCA-388
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".dayofweek" ,"_id.dayofweek");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".hour" ,"_id.hour");
//		//YUCCA-346
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".minute" ,"_id.minute");
//
//		//YUCCA-388
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".minute" ,"_id.retweetparentid");
//		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_SOCIAL_STATS+".minute" ,"_id.iduser");
		
		

	}	
	
	
	
	
	
	
	
	
	
	
}
