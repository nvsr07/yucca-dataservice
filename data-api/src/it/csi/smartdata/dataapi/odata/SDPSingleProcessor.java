package it.csi.smartdata.dataapi.odata;

import it.csi.smartdata.dataapi.constants.SDPDataApiConfig;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;
import it.csi.smartdata.dataapi.mongo.SDPMongoOdataCast;
import it.csi.smartdata.dataapi.mongo.dto.SDPDataResult;
import it.csi.smartdata.dataapi.mongo.exception.SDPPageSizeException;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.InlineCount;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.MessageReference;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

public class SDPSingleProcessor extends ODataSingleProcessor {
	static Logger log = Logger.getLogger(SDPSingleProcessor.class.getPackage().getName());



	private String codiceApi=null;
	private String baseUrl=null;
	public String getBaseUrl() {
		return baseUrl;
	}


	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}


	public String getCodiceApi() {
		return codiceApi;
	}


	public void setCodiceApi(String codiceApi) {
		this.codiceApi = codiceApi;
	}



	private int [] checkPagesData_old(Integer skip,Integer top, int resultSize) throws Exception{
		int startindex=0;
		int endindex=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


		log.info("[SDPSingleProcessor::checkPagesData] skipParameter="+skip);
		log.info("[SDPSingleProcessor::checkPagesData] topParameter="+top);


		//controlli ... sollevo eccezione quando:
		// top valorizzato e > di maxsize
		// top non valorizzato e size > max
		
		
		
		
		if(top!=null && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+endindex,Locale.UK);
		if(top==null && resultSize>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())  throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+endindex,Locale.UK);


		//se skip è valorizzato
		if(skip!=null) {
			startindex=startindex+skip.intValue();
		}
		
		
		// a questo punto i parametri sono buoni ... valorizzo endindex in base al top se valorizzato (sempre con start index >0
		if(top!=null) endindex=top.intValue();

		endindex=startindex+endindex;
		
		// riporto endinx a resultsize nel caso in cui sia maggiore
		if (endindex>resultSize) endindex=resultSize;


		



		log.info("[SDPSingleProcessor::checkPagesData] checkPagesData="+startindex);
		log.info("[SDPSingleProcessor::checkPagesData] checkPagesData="+endindex);	



		int [] ret = new int[] {startindex,endindex}; 
		return ret; 

	}
	
	
	private int [] checkPagesData(Integer skip,Integer top, int resultSize) throws Exception{
		int startindex=0;
		int endindex=SDPDataApiConfig.getInstance().getMaxDocumentPerPage();


		log.info("[SDPSingleProcessor::checkPagesData] skipParameter="+skip);
		log.info("[SDPSingleProcessor::checkPagesData] topParameter="+top);

		//se skip è valorizzato
		if(skip!=null) {
			startindex=startindex+skip.intValue();
		}
		
		if(skip!=null && skip.intValue()>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max page = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
		

		//controlli ... sollevo eccezione quando:
		// top valorizzato e > di maxsize
		// top non valorizzato e size - start > max
		if(top!=null && top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+endindex,Locale.UK);
		if(top==null && (resultSize-startindex)>SDPDataApiConfig.getInstance().getMaxDocumentPerPage())  throw new SDPPageSizeException("too many documents; use top parameter: max document per page = "+endindex,Locale.UK);
		if(skip!=null && skip.intValue()>resultSize) throw new SDPPageSizeException("skip value out of range: max document in query result = "+resultSize,Locale.UK);


		
		
		// a questo punto i parametri sono buoni ... valorizzo endindex in base al top se valorizzato (sempre con start index >0
		if(top!=null) endindex=top.intValue();

		endindex=startindex+endindex;
		
		// riporto endinx a resultsize nel caso in cui sia maggiore
		if (endindex>resultSize) endindex=resultSize;


		



		log.info("[SDPSingleProcessor::checkPagesData] checkPagesData="+startindex);
		log.info("[SDPSingleProcessor::checkPagesData] checkPagesData="+endindex);	



		int [] ret = new int[] {startindex,endindex, ((top!=null) ? top.intValue() : -1 ) , ((skip!=null) ? skip.intValue() : -1 ) }; 
		return ret; 

	}	

	
	private int [] checkSkipTop(Integer skip,Integer top) throws Exception{
		
		if (skip==null) skip=new Integer(-1);
		if (top==null) top= new Integer(-1);
		if(skip.intValue()>SDPDataApiConfig.getInstance().getMaxSkipPages()) throw new SDPPageSizeException("invalid skip value: max skip = "+SDPDataApiConfig.getInstance().getMaxSkipPages(),Locale.UK);
		if(top.intValue()>SDPDataApiConfig.getInstance().getMaxDocumentPerPage()) throw new SDPPageSizeException("invalid top value: max document per page = "+SDPDataApiConfig.getInstance().getMaxDocumentPerPage(),Locale.UK);
		


		int [] ret = new int[] { skip.intValue() ,top.intValue() }; 
		return ret; 

	}		
	

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo,final String contentType) throws ODataException {

		try {
			log.info("[SDPSingleProcessor::readEntitySet] BEGIN");
			log.info("[SDPSingleProcessor::readEntitySet] uriInfo="+uriInfo);
			log.info("[SDPSingleProcessor::readEntitySet] contentType="+contentType);
			log.info("[SDPSingleProcessor::readEntitySet] codiceApi="+codiceApi);
			log.debug("[SDPSingleProcessor::readEntitySet] uriInfoDetail="+dump("uriInfo",uriInfo));
			URI newUri=getContext().getPathInfo().getServiceRoot();
			try {
				newUri=new URI(this.baseUrl);
			} catch (Exception e) {}
			log.info("[SDPSingleProcessor::readEntitySet] newUri="+newUri);
			EdmEntitySet entitySet;

			if (uriInfo.getNavigationSegments().size() == 0) {
				entitySet = uriInfo.getStartEntitySet();

				Object userQuery=null;

				FilterExpression fe = uriInfo.getFilter();
				//OrderByExpression oe=uriInfo.getOrderBy();
				if (fe != null) {
					SDPExpressionVisitor ev = new SDPExpressionVisitor();
					ev.setEntitySetName(entitySet.getName());
					userQuery = fe.accept(ev);
					log.info("[SDPSingleProcessor::readEntitySet] userQuery="+userQuery);
				}
//				if (oe != null) {
//					SDPExpressionVisitor ev = new SDPExpressionVisitor();
//					ev.setEntitySetName(entitySet.getName());
//					Object orderQuery=oe.accept(ev);
//					log.info("[SDPSingleProcessor::readEntitySet] orderQuery="+orderQuery);
//				}
				log.info("[SDPSingleProcessor::readEntitySet] entitySet="+entitySet.getName());


				if ((SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).equals(entitySet.getName())) {

					
					
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).getEntityType().getNamespace();
					
					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop());
					int skip=skiptop[0];
					int top=skiptop[1];
					SDPDataResult dataRes= new SDPMongoOdataCast().getMeasuresPerApi(this.codiceApi, nameSpace,uriInfo.getEntityContainer(),null,userQuery,skip,top);
					
//					int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(), misure.size());
//					int startindex=limiti[0];
//					int endindex=limiti[1];
//
//
//					List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
//					for (int i=startindex;i<endindex;i++) {
//						misureNew.add(misure.get(i));
//					}



					ODataResponse ret= EntityProvider.writeFeed (
							contentType,
							entitySet,
							dataRes.getDati(),
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(dataRes.getTotalCount())
									.build());

					return ret;

				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();

					
					int [] skiptop = checkSkipTop(uriInfo.getSkip(), uriInfo.getTop());
					
					
					SDPDataResult dataRes=  new SDPMongoOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),null,userQuery,
							skiptop[0],
							skiptop[1]);

//					int [] limiti=checkPagesData(uriInfo.getSkip(), uriInfo.getTop(), misure.size());
//					int startindex=limiti[0];
//					int endindex=limiti[1];
//
//					List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
//					for (int i=startindex;i<endindex;i++) {
//						misureNew.add(misure.get(i));
//					}




					ODataResponse ret= EntityProvider.writeFeed(
							contentType,
							entitySet,
							dataRes.getDati(),
							EntityProviderWriteProperties.serviceRoot(
									newUri)
									.inlineCountType(InlineCount.ALLPAGES)
									.inlineCount(dataRes.getTotalCount())
									.build());

					return ret;


				}				
				throw new ODataNotFoundException(ODataNotFoundException.ENTITY);



			} else if (uriInfo.getNavigationSegments().size() == 1) {
				// navigation first level, simplified example for illustration
				// purposes only
				entitySet = uriInfo.getTargetEntitySet();
				/*
				 * if (ENTITY_SET_NAME_CARS.equals(entitySet.getName())) { int
				 * manufacturerKey = getKeyValue(uriInfo.getKeyPredicates().get(0));
				 * 
				 * List<Map<String, Object>> cars = new ArrayList<Map<String,
				 * Object>>(); cars.addAll(dataStore.getCarsFor(manufacturerKey));
				 * 
				 * return EntityProvider.writeFeed(contentType, entitySet, cars,
				 * EntityProviderWriteProperties.serviceRoot(
				 * getContext().getPathInfo().getServiceRoot()).build()); }
				 */

				throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
			}
			throw new ODataNotImplementedException();


		} catch (Exception e) {
			log.error("[SDPSingleProcessor::readEntitySet] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.info("[SDPSingleProcessor::readEntitySet] END");

		}







		//		
		//		
		//		EdmEntitySet entitySet;
		//		if (uriInfo.getNavigationSegments().size() == 0) {
		//			entitySet = uriInfo.getStartEntitySet();
		//
		//			Object userQuery=null;
		//
		//			FilterExpression fe = uriInfo.getFilter();
		//			if (fe != null) {
		//				SDPExpressionVisitor ev = new SDPExpressionVisitor();
		//				ev.setEntitySetName(entitySet.getName());
		//				userQuery = fe.accept(ev);
		//				log.info("expression:\n" + ev.getOut());
		//
		//			}
		//
		//
		//			if ((SDPDataApiConstants.ENTITY_SET_NAME_SMARTOBJECT).equals(entitySet.getName())) {
		//				return EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						getSmartObj(),
		//						EntityProviderWriteProperties.serviceRoot(
		//								getContext().getPathInfo().getServiceRoot())
		//								.build());
		//			} else 			if ((SDPDataApiConstants.ENTITY_SET_NAME_STREAMS).equals(entitySet.getName())) {
		//				String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).getEntityType().getNamespace();
		//
		//				List<Map<String, Object>> streams= new MongoDataAccess().getStreamsPerApi(this.codiceApi, nameSpace,
		//						uriInfo.getEntityContainer());
		//
		//
		//				ODataResponse ret= EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						streams,
		//						EntityProviderWriteProperties.serviceRoot(
		//								newUri)
		//								.build());
		//
		//				return ret;
		//
		//			} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
		//				String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();
		//
		//				List<Map<String, Object>> misure= new SDPMongoOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
		//						uriInfo.getEntityContainer(),null,userQuery);
		//
		//				int startindex=0;
		//				int endindex=misure.size();
		//
		//				if(uriInfo.getSkip()!=null) startindex=startindex+uriInfo.getSkip().intValue();
		//				if(uriInfo.getTop()!=null) endindex=startindex+uriInfo.getTop().intValue();
		//
		//				List<Map<String, Object>> misureNew=new ArrayList<Map<String,Object>>();
		//				for (int i=startindex;i<endindex;i++) {
		//					misureNew.add(misure.get(i));
		//				}
		//
		//
		//
		//
		//				ODataResponse ret= EntityProvider.writeFeed(
		//						contentType,
		//						entitySet,
		//						misureNew,
		//						EntityProviderWriteProperties.serviceRoot(
		//								newUri)
		//								.build());
		//
		//				return ret;
		//
		//
		//			}
		//
		//			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		//
		//		} else if (uriInfo.getNavigationSegments().size() == 1) {
		//
		//			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		//		}
		//
		//		throw new ODataNotImplementedException();

	}

	@Override
	public ODataResponse readEntity(final GetEntityUriInfo uriInfo,final String contentType) throws ODataException {


		try {
			log.info("[SDPSingleProcessor::readEntity] BEGIN");
			log.info("[SDPSingleProcessor::readEntity] uriInfo="+uriInfo);
			log.info("[SDPSingleProcessor::readEntity] contentType="+contentType);
			log.info("[SDPSingleProcessor::readEntity] codiceApi="+codiceApi);
			log.debug("[SDPSingleProcessor::readEntity] uriInfoDetail="+dump("uriInfo",uriInfo));
			URI newUri=getContext().getPathInfo().getServiceRoot();
			try {
				newUri=new URI(this.baseUrl);
			} catch (Exception e) {}
			log.info("[SDPSingleProcessor::readEntitySet] newUri="+newUri);

			if (uriInfo.getNavigationSegments().size() == 0) {
				EdmEntitySet entitySet = uriInfo.getStartEntitySet();

				if  (SDPDataApiConstants.ENTITY_SET_NAME_MEASURES.equals(entitySet.getName())) {

					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_MEASURES).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));

					SDPDataResult dataRes=  new SDPMongoOdataCast().getMeasuresPerApi(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,-1,-1);


					//Map<String, Object> data = dataRes.getDati().get(0);
					Map<String, Object> data = ( dataRes.getDati()!= null && dataRes.getDati().size()>0 ) ? dataRes.getDati().get(0) :null ;
					

					if (data != null) {
						URI serviceRoot = getContext().getPathInfo()
								.getServiceRoot();
						ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
								.serviceRoot(newUri);

						return EntityProvider.writeEntry(contentType, entitySet,
								data, propertiesBuilder.build());
					} else {
						throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
					}
				} else if ((SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).equals(entitySet.getName())) {
					String nameSpace=uriInfo.getEntityContainer().getEntitySet(SDPDataApiConstants.ENTITY_SET_NAME_UPLOADDATA).getEntityType().getNamespace();

					String id = getKeyValue(uriInfo.getKeyPredicates().get(0));

					SDPDataResult dataRes  = new SDPMongoOdataCast().getMeasuresPerDataset(this.codiceApi, nameSpace,
							uriInfo.getEntityContainer(),id,null,-1,-1);


					Map<String, Object> data = ( dataRes.getDati()!= null && dataRes.getDati().size()>0 ) ? dataRes.getDati().get(0) : null ;

					if (data != null) {
						URI serviceRoot = getContext().getPathInfo()
								.getServiceRoot();
						ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties
								.serviceRoot(newUri);

						return EntityProvider.writeEntry(contentType, entitySet,
								data, propertiesBuilder.build());
					} else {
						throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
					}
				} 
				/**
				 * else if
				 * (ENTITY_SET_NAME_MANUFACTURERS.equals(entitySet.getName())) { int
				 * id = getKeyValue(uriInfo.getKeyPredicates().get(0)); Map<String,
				 * Object> data = dataStore.getManufacturer(id);
				 * 
				 * if (data != null) { URI serviceRoot =
				 * getContext().getPathInfo().getServiceRoot();
				 * ODataEntityProviderPropertiesBuilder propertiesBuilder =
				 * EntityProviderWriteProperties.serviceRoot(serviceRoot);
				 * 
				 * return EntityProvider.writeEntry(contentType, entitySet, data,
				 * propertiesBuilder.build()); } }
				 **/
			}
			throw new ODataNotImplementedException();			
			
			
			
		} catch (Exception e) {
			log.error("[SDPSingleProcessor::readEntity] " + e);
			if (e instanceof ODataException) throw (ODataException)e;
			throw new ODataException(e);
		} finally {
			log.info("[SDPSingleProcessor::readEntity] END");

		}
		
		




	}


	private String getKeyValue(final KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT,
				property.getFacets(), String.class);
	}

	private String dump(String prefix,Object o) {
		StringBuilder sb = new StringBuilder();
		if (o == null)
			sb.append("null");
		else {
			Class<?> cl = o.getClass();

			for (Method m : cl.getMethods()) {
				String methodName = m.getName();
				if (methodName.startsWith("get") && m.getParameterTypes().length == 0 &&
						!methodName.equals("getClass")) {

					sb.append("\n");
					if (prefix != null)
						sb.append(prefix).append(".");
					sb.append(methodName.substring(3)).append(" ");
					int l = methodName.length();
					for (int i = 3; i < 25 - l ; i++)
						sb.append(".");
					sb.append(": ");
					try {
						Object val = m.invoke(o, (Object[]) null);
						sb.append(val);
						if (val != null)
							sb.append(" (").append(sb.getClass().getName()).append(")");
					} catch (Exception e) {
						sb.append("got exception ").append(e);
					}
				}
			}
		}

		return sb.toString();
	}


}
