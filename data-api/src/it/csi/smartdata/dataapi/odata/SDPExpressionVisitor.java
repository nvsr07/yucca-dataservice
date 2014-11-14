package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmLiteral;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTyped;
import org.apache.olingo.odata2.api.uri.expression.BinaryExpression;
import org.apache.olingo.odata2.api.uri.expression.BinaryOperator;
import org.apache.olingo.odata2.api.uri.expression.CommonExpression;
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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class SDPExpressionVisitor implements ExpressionVisitor {
	static Logger log = Logger.getLogger(SDPExpressionVisitor.class.getPackage().getName());

	private String entitySetName=null;

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
	public Object visitBinary(BinaryExpression paramBinaryExpression,
			BinaryOperator paramBinaryOperator, Object paramObject1,
			Object paramObject2) {
		//		out.append("visitBinary oper ").append(paramBinaryOperator).append("\n");
		//		return null;
		BasicDBObject query = null;
		String sqlOperator = "";
		//BasicDBObject clause=null;


		DBObject clause=null;
		//		String left=null;
		//		String right=null;
		//		if (paramObject1 == null) {
		//			left=paramBinaryExpression.getLeftOperand().getUriLiteral();
		//			if (left.startsWith("'")) left=left.substring(1);
		//			if (left.endsWith("'")) left=left.substring(0, left.length()-1);
		//			left=getFullFielName(left);
		//		}
		//		if (paramObject2 == null) {
		//			right=paramBinaryExpression.getRightOperand().getUriLiteral();
		//			if (right.startsWith("'")) right=right.substring(1);
		//			if (right.endsWith("'")) right=right.substring(0, right.length()-1);
		//			right=getFullFielName(right);
		//		}


		BasicDBList lista=new BasicDBList();
		switch (paramBinaryOperator) {
		case EQ:


			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),paramObject2);
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),paramObject1);
			} else if (paramObject1 instanceof BasicDBObject && paramBinaryExpression.getRightOperand() instanceof LiteralExpression ) {
				//A sx ho una query mongo, a dx un literal ... se la query mongo deriva da una function allora 
				//prima verifica: se il literal e' un boolean ed e' un true, dovrebbe andare bene cosi'
				//                se il literal e' un boolean ed e' false, richiamo la parte che genera la query passando il false

				if (paramBinaryExpression.getRightOperand().getEdmType().toString().equals("Edm.Boolean") &&
						paramBinaryExpression.getLeftOperand() instanceof MethodExpression) {
					if (((Boolean)paramObject2).booleanValue()==false) {
						paramObject1=revertMethodAndBoolean(paramObject1,
								paramObject2,
								paramBinaryExpression.getLeftOperand(),
								paramBinaryExpression.getRightOperand(),
								true);

						//paramObject1=visitMethod(paramMethodExpression, paramMethodOperator, paramList)
					}					
					clause=(BasicDBObject )paramObject1;
				}
			} else if (paramObject2 instanceof BasicDBObject && paramBinaryExpression.getLeftOperand() instanceof LiteralExpression ) {
				//A sx ho una query mongo, a dx un literal ... se la query mongo deriva da una function allora 
				//prima verifica: se il literal e' un boolean ed e' un true, dovrebbe andare bene cosi'
				//                se il literal e' un boolean ed e' false, richiamo la parte che genera la query passando il false

				if (paramBinaryExpression.getLeftOperand().getEdmType().toString().equals("Edm.Boolean") &&
						paramBinaryExpression.getRightOperand() instanceof MethodExpression) {
					if (((Boolean)paramObject1).booleanValue()==false) {
						paramObject2=revertMethodAndBoolean(paramObject2,
								paramObject1,
								paramBinaryExpression.getRightOperand(),
								paramBinaryExpression.getLeftOperand(),
								true);

						//paramObject1=visitMethod(paramMethodExpression, paramMethodOperator, paramList)
					}					
					clause=(BasicDBObject )paramObject2;
				}

			}  

			/*

			sqlOperator = "=";
			if (left!=null && right!=null) 
			if (paramObject1 != null && paramObject2== null &&  paramBinaryExpression.getRightOperand().getEdmType().toString().equals("Edm.Boolean")) {
				if (right.equalsIgnoreCase("false")) {
					clause = new BasicDBObject("$not",paramObject1);
				} else {
					clause=(BasicDBObject)paramObject1;
				}
			}
			if (paramObject2 != null && paramObject1== null &&  paramBinaryExpression.getLeftOperand().getEdmType().toString().equals("Edm.Boolean")) {
				if (left.equalsIgnoreCase("false")) {
					clause = new BasicDBObject("$not",paramObject2);
				} else {
					clause=(BasicDBObject)paramObject2;
				}
			}
			 */

			break;
		case NE:
			sqlOperator = "<>";
			//			if (left!=null && right!=null) clause = new BasicDBObject(left,right);
			//			if (paramObject1 != null && paramObject2== null &&  paramBinaryExpression.getRightOperand().getEdmType().toString().equals("Edm.Boolean")) {
			//				if (right.equalsIgnoreCase("false")) {
			//					clause = new BasicDBObject("$not",paramObject1);
			//				} else {
			//					clause=(BasicDBObject)paramObject1;
			//				}
			//			}
			//			if (paramObject2 != null && paramObject1== null &&  paramBinaryExpression.getLeftOperand().getEdmType().toString().equals("Edm.Boolean")) {
			//				if (left.equalsIgnoreCase("false")) {
			//					clause = new BasicDBObject("$not",paramObject2);
			//				} else {
			//					clause=(BasicDBObject)paramObject2;
			//				}
			//			}
			//
			//			BasicDBObject clauseTmp=new BasicDBObject("$not",clause);
			//			clause=clauseTmp;

			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),new BasicDBObject("$ne", paramObject2));
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),new BasicDBObject("$ne" ,paramObject1));
			} else if (paramObject1 instanceof BasicDBObject && paramBinaryExpression.getRightOperand() instanceof LiteralExpression ) {
				//A sx ho una query mongo, a dx un literal ... se la query mongo deriva da una function allora 
				//prima verifica: se il literal e' un boolean ed e' un true, dovrebbe andare bene cosi'
				//                se il literal e' un boolean ed e' false, richiamo la parte che genera la query passando il false

				if (paramBinaryExpression.getRightOperand().getEdmType().toString().equals("Edm.Boolean") &&
						paramBinaryExpression.getLeftOperand() instanceof MethodExpression) {
					if (((Boolean)paramObject2).booleanValue()==true) {
						paramObject1=revertMethodAndBoolean(paramObject1,
								paramObject2,
								paramBinaryExpression.getLeftOperand(),
								paramBinaryExpression.getRightOperand(),
								true);

						//paramObject1=visitMethod(paramMethodExpression, paramMethodOperator, paramList)
					}					
					clause=(BasicDBObject )paramObject1;
				}
			} else if (paramObject2 instanceof BasicDBObject && paramBinaryExpression.getLeftOperand() instanceof LiteralExpression ) {
				//A sx ho una query mongo, a dx un literal ... se la query mongo deriva da una function allora 
				//prima verifica: se il literal e' un boolean ed e' un true, dovrebbe andare bene cosi'
				//                se il literal e' un boolean ed e' false, richiamo la parte che genera la query passando il false

				if (paramBinaryExpression.getLeftOperand().getEdmType().toString().equals("Edm.Boolean") &&
						paramBinaryExpression.getRightOperand() instanceof MethodExpression) {
					if (((Boolean)paramObject1).booleanValue()==true) {
						paramObject2=revertMethodAndBoolean(paramObject2,
								paramObject1,
								paramBinaryExpression.getRightOperand(),
								paramBinaryExpression.getLeftOperand(),
								true);

						//paramObject1=visitMethod(paramMethodExpression, paramMethodOperator, paramList)
					}					
					clause=(BasicDBObject )paramObject2;
				}
			}


			break;
		case OR:
			sqlOperator = "OR";

			lista.add(paramObject1);
			lista.add(paramObject2);
			clause = new BasicDBObject("$or",lista);
			//return clause;

			break;
		case AND:
			sqlOperator = "AND";

			lista.add(paramObject1);
			lista.add(paramObject2);
			clause = new BasicDBObject("$and",lista);
			//return clause;

			break;
		case GE:
			sqlOperator = ">=";


			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),new BasicDBObject("$gte",paramObject2));
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),new BasicDBObject("$lte",paramObject1));
			}  

			break;
		case GT:
			sqlOperator = ">";
			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),new BasicDBObject("$gt",paramObject2));
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),new BasicDBObject("$le",paramObject1));
			}  
			break;
		case LE:
			sqlOperator = "<=";
			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),new BasicDBObject("$lte",paramObject2));
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),new BasicDBObject("$gte",paramObject1));
			}  
			break;
		case LT:
			sqlOperator = "<";
			if (paramBinaryExpression.getLeftOperand() instanceof PropertyExpression && paramBinaryExpression.getRightOperand() instanceof LiteralExpression) {
				clause = new BasicDBObject(paramObject1.toString(),new BasicDBObject("$lt",paramObject2));
			} else if (paramBinaryExpression.getLeftOperand() instanceof  LiteralExpression && paramBinaryExpression.getRightOperand() instanceof PropertyExpression) {
				clause = new BasicDBObject(paramObject2.toString(),new BasicDBObject("$gt",paramObject1));
			}  
			break;
		default:
			//Other operators are not supported for SQL Statements
			throw new UnsupportedOperationException("Unsupported operator: " + paramBinaryOperator.toUriLiteral());
		}  
		//return the binary statement

		//	    if (paramObject1 != null && paramObject2!=null) return paramObject1 + " " + sqlOperator + " " + paramObject2; 
		//	    else return paramBinaryExpression.getLeftOperand().getUriLiteral() + " " + sqlOperator + " " + paramBinaryExpression.getRightOperand().getUriLiteral();

		return clause;

	}

	@Override
	public Object visitOrderByExpression(
			OrderByExpression paramOrderByExpression, String paramString,
			List<Object> paramList) {
		out.append("visitOrderByExpression\n");
		return null;
	}

	@Override
	public Object visitOrder(OrderExpression paramOrderExpression,
			Object paramObject, SortOrder paramSortOrder) {
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
		} else if(Uint7.getInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		} else if(org.apache.olingo.odata2.core.edm.Bit.getInstance().equals(paramEdmLiteral.getType())) {
			Object ret = new Integer(paramEdmLiteral.getLiteral());
			return ret;
		}




		out.append("visitLiteral ").append(paramLiteralExpression.getUriLiteral()).append("--"+paramEdmLiteral.getType()+"\n");
		return null;
	}


	private Object revertMethodAndBoolean(Object paramObjectLeft,Object paramObjectRight, CommonExpression leftExpression, CommonExpression rightExpression,boolean forceToFalse) {
		List<CommonExpression> actualParameters=  ((MethodExpression)leftExpression).getParameters();
		ArrayList<Object> retParameters = new ArrayList<Object>();
		try {
			for (CommonExpression parameter : actualParameters) {

				Object retParameter = parameter.accept(this);
				retParameters.add(retParameter);
			}	
			paramObjectLeft=(BasicDBObject)visitMethod((MethodExpression)leftExpression,
					((MethodExpression)leftExpression).getMethod(),
					retParameters,
					forceToFalse);

		} catch (Exception e ) {
			e.printStackTrace();
		}
		return paramObjectLeft;

	}

	private Object visitMethod(MethodExpression paramMethodExpression,
			MethodOperator paramMethodOperator, List<Object> paramList, boolean forceToFalse) {
		out.append("visitMethod\n");

		BasicDBObject clause=null;


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




		switch (paramMethodOperator) {
		case SUBSTRINGOF:
			if (paramList.size()!=2) throw new java.lang.UnsupportedOperationException("Unsupported parematers for: " + paramMethodOperator.toUriLiteral());
			Pattern regex = Pattern.compile((String)paramList.get(0));
			clause = new BasicDBObject();
			if (forceToFalse) clause.put(paramList.get(1).toString(), new BasicDBObject("$not",regex));
			else clause.put(paramList.get(1).toString(), regex);
			break;
		default:
			throw new UnsupportedOperationException("Unsupported operator: " + paramMethodOperator.toUriLiteral());
		}

		return clause;
	}	

	@Override
	public Object visitMethod(MethodExpression paramMethodExpression,
			MethodOperator paramMethodOperator, List<Object> paramList) {
		out.append("visitMethod\n");

		BasicDBObject clause=null;





		switch (paramMethodOperator) {
		case SUBSTRINGOF:
			clause=(BasicDBObject)visitMethod(paramMethodExpression,paramMethodOperator,paramList,false);
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
		return null;
	}

	@Override
	public Object visitProperty(PropertyExpression paramPropertyExpression,
			String paramString, EdmTyped paramEdmTyped) {

		try {
			out.append("visitProperty ").append(paramString)
			.append(" type ").append(paramEdmTyped.getType()).append("\n");

			return getFullFielName (paramString);

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

	private String getFullFielName(String fieldNameInput) {
		String ret=this.fieldAppendMap.get(this.entitySetName+"."+fieldNameInput);
		if (ret!=null) return ret;
		else return fieldNameInput;
	}


	private static Map<String,String> fieldAppendMap;
	static {
		fieldAppendMap = new HashMap<String,String>();
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".codiceStream" ,"streams.stream.codiceStream");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".codiceTenant" ,"streams.stream.codiceTenant");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_STREAMS+".nomeStream" ,"streams.stream.nomeStream");

		//MISURE - non serve ma per tenere traccia ...
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".streamCode" ,"streamCode");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".sensor" ,"sensor");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".internalId" ,"_id");
		fieldAppendMap.put(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES+".time" ,"time");



	}



}
