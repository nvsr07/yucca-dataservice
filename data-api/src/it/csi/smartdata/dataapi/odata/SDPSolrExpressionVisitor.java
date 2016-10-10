package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.regex.Pattern;

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
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;

import com.mongodb.BasicDBList;

public class SDPSolrExpressionVisitor implements ExpressionVisitor {
	static Logger log = Logger.getLogger(SDPExpressionVisitor.class.getPackage().getName());

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

	private StringBuilder out = new StringBuilder();

	public String getOut() {
		return out.toString();
	}
	@Override
	public Object visitFilterExpression(FilterExpression paramFilterExpression,
			String paramString, Object paramObject) {
		out.append("visitFilterExpression\n");
		return paramObject;
	}

	@Override
	public Object visitBinary(BinaryExpression binaryExpression, BinaryOperator operator, Object leftSide, Object rightSide) {
		
		
	  String actualLeftSide = leftSide.toString();
	  String actualRightSide = rightSide.toString();
//	  if (leftSide instanceof Expression) {
//	    //If something is lower in the tree and is of the type AND or OR it needs brackets to show the higher priority
//	    if (BinaryOperator.AND.equals(((Expression) leftSide).getOperator()) || BinaryOperator.OR.equals(((Expression) leftSide).getOperator())) {
//	    actualLeftSide = "(" + leftSide + ")";
//	    }
//	  }
//	  if (rightSide instanceof Expression) {
//	    //If something is lower in the tree and is of the type AND or OR it needs brackets to show the higher priority
//	    if (BinaryOperator.AND.equals(((Expression) rightSide).getOperator()) || BinaryOperator.OR.equals(((Expression) rightSide).getOperator())) {
//	    actualRightSide = "(" + rightSide + ")";
//	    }
//	  }
	  //Transform the OData filter operator into an equivalent sql operator
	  String sqlOperator = "";
	  String pre="";
	  
	  String ret="";
	  
	  switch (operator) {
	  case EQ:
	    sqlOperator = "=";
	    ret = actualLeftSide + " : " + actualRightSide;
	    break;
	  case NE:
	    sqlOperator = "<>";
	    ret = "!("+actualLeftSide + " : " + actualRightSide +")";
	    break;
	  case OR:
	    sqlOperator = "OR";
	    ret = actualLeftSide + " OR " + actualRightSide;
	    break;
	  case AND:
	    sqlOperator = "AND";
	    ret = actualLeftSide + " AND " + actualRightSide;
	    break;
	  case GE:
	    sqlOperator = ">=";
	    ret = actualLeftSide + " : [ " + actualRightSide +" TO * ]";
	    break;
	  case GT:
	    sqlOperator = ">";
	    ret = actualLeftSide + " : { " + actualRightSide +" TO * }";
	    break;
	  case LE:
	    sqlOperator = "<=";
	    ret = actualLeftSide + " : [ * TO " + actualRightSide +"]";
	    break;
	  case LT:
	    sqlOperator = "<";
	    ret = actualLeftSide + " : { * TO " + actualRightSide +"}";
	    break;
	  default:
	    //Other operators are not supported for SQL  Statements
		throw new UnsupportedOperationException("Unsupported operator: " + operator.toUriLiteral());
	  }

	  //return the binary statement
	  return"("+ret+")";
	}

	@Override
	public Object visitOrderByExpression(
			OrderByExpression paramOrderByExpression, String paramString,
			List<Object> paramList) {
		out.append("visitOrderByExpression\n");

		ArrayList<SortClause> ret=new ArrayList<SortClause>();
		for (int i =0;paramList!=null && i<paramList.size() ; i++) {
			if (paramList.get(i) instanceof SortClause) {
				ret.add((SortClause)paramList.get(i));
			}
		}
		if (ret.size()>0) return ret;
		return null;
	}

	@Override
	public Object visitOrder(OrderExpression paramOrderExpression,
			Object paramObject, SortOrder paramSortOrder) {


		if (paramObject instanceof String ) {
			//String val= getFullFielName ((String)paramObject);
			
			//TODO 
			String val= (String)paramObject;
			
			if (paramSortOrder.compareTo(SortOrder.asc)==0)  return new SortClause(val, ORDER.asc);
			if (paramSortOrder.compareTo(SortOrder.desc)==0)  return  new SortClause(val, ORDER.desc);

			return null;
		}  

		out.append("visitOrder\n");
		return null;
	}

	@Override
	public Object visitLiteral(LiteralExpression paramLiteralExpression,
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
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new String(paramEdmLiteral.getLiteral());
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

				return data;

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




		out.append("visitLiteral ").append(paramLiteralExpression.getUriLiteral()).append("--"+paramEdmLiteral.getType()+"\n");
		return null;
	}




	private Object visitMethod(MethodExpression paramMethodExpression,
			MethodOperator paramMethodOperator, List<Object> paramList, boolean forceToFalse) {
		out.append("visitMethod\n");

		String clause=null;


		//		int left=-1;
		//		int right=-1;
		//		String [] parametri= new String[paramMethodExpression.getParameters().size()];
		//		for (int i = 0 ; i<paramMethodExpression.getParameters().size();i++) {
		//			String cur=paramMethodExpression.getParameters().get(i).getUriLiteral();
		//			if (cur.startsWith("'")) cur=cur.substring(1);
		//			if (cur.endsWith("'")) cur=cur.substring(0, cur.length()-1);
		//			cur=getFullFielName(cur);
		//			parametri[i]=cur;
		//			
		//			
		//			if (paramMethodExpression.getParameters().get(i) instanceof PropertyExpression)  left=i;
		//			else if (paramMethodExpression.getParameters().get(i) instanceof LiteralExpression)  right=i;
		//			
		//
		//			
		//		}


		Pattern regex=null;

		switch (paramMethodOperator) {
		case STARTSWITH:
			if (paramList.size()!=2) throw new java.lang.UnsupportedOperationException("Unsupported parematers for: " + paramMethodOperator.toUriLiteral());
			regex = Pattern.compile("^"+(String)paramList.get(1)+".*");
			clause = paramList.get(0).toString() + " :  ";
			if (forceToFalse) clause=" !("+clause +""+regex+")"; 
			else clause=clause+regex;
			break;
		case ENDSWITH:
			if (paramList.size()!=2) throw new java.lang.UnsupportedOperationException("Unsupported parematers for: " + paramMethodOperator.toUriLiteral());
			regex = Pattern.compile((String)paramList.get(1)+"$");
			if (forceToFalse) clause=" !("+clause +""+regex+")"; 
			else clause=clause+regex;
			break;
		case SUBSTRINGOF:
			if (paramList.size()!=2) throw new java.lang.UnsupportedOperationException("Unsupported parematers for: " + paramMethodOperator.toUriLiteral());
			regex = Pattern.compile((String)paramList.get(0));
			if (forceToFalse) clause=" !("+clause +""+regex+")"; 
			else clause=clause+regex;
			break;
		default:
			throw new UnsupportedOperationException("Unsupported operator: " + paramMethodOperator.toUriLiteral());
		}

		return "("+clause+")";
	}	

	@Override
	public Object visitMethod(MethodExpression paramMethodExpression,
			MethodOperator paramMethodOperator, List<Object> paramList) {
		out.append("visitMethod\n");

		String clause=null;





		switch (paramMethodOperator) {
		case ENDSWITH:
			clause=(String)visitMethod(paramMethodExpression,paramMethodOperator,paramList,false);
			break;
		case STARTSWITH:
			clause=(String)visitMethod(paramMethodExpression,paramMethodOperator,paramList,false);
			break;
		case SUBSTRINGOF:
			clause=(String)visitMethod(paramMethodExpression,paramMethodOperator,paramList,false);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported operator: " + paramMethodOperator.toUriLiteral());
		}

		return clause;
	}

	@Override
	public Object visitMember(MemberExpression paramMemberExpression,
			Object paramObject1, Object paramObject2) {
		out.append("visitMember\n");
		try {
		if (			paramMemberExpression.getPath().getEdmType().getName().equals("BinaryRef") ) {
			return paramObject1;
		}
		} catch (Exception e ) {
			
		}
		return null;
	}

	@Override
	public Object visitProperty(PropertyExpression paramPropertyExpression,
			String paramString, EdmTyped paramEdmTyped) {

		try {
			out.append("visitProperty ").append(paramString)
			.append(" type ").append(paramEdmTyped.getType()).append("\n");

			return getFullFielName (paramString,paramEdmTyped.getType());

		} catch (EdmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object visitUnary(UnaryExpression paramUnaryExpression,
			UnaryOperator paramUnaryOperator, Object paramObject) {
		out.append("visitUnary\n");
		return null;
	}

	private String getFullFielName_old(String fieldNameInput) {
		String ret=this.fieldAppendMap.get(this.entitySetName+"."+fieldNameInput);
		if (ret!=null) return ret;
		else return fieldNameInput;
	}
	private String getFullFielName(String fieldNameInput,EdmType type) {
		String ret=this.fieldAppendMap.get(this.entitySetName+"."+fieldNameInput);
		if (ret==null) ret = fieldNameInput;
		
		if ("id".equals(ret)) return ret;
		
		String tipo=this.mappaCampi.get(ret);
		ret += SDPDataApiConstants.SDP_DATATYPE_SOLRSUFFIX.get(tipo);

		if (ret!=null) return ret;
		else return fieldNameInput;
	}


	private static Map<String,String> fieldAppendMap;
	static {
		fieldAppendMap = new HashMap<String,String>();
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".internalId" ,"id");
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
