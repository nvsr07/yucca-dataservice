package it.csi.smartdata.odata.datadiscovery;

import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_SET_NAME_DATASETS;
import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_NAME_DATASET;
import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_SET_NAME_FIELDS;
import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_SET_NAME_STREAMS;
import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_NAME_STREAM;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

import com.mongodb.BasicDBObject;
public class SmartDataDiscoverySingleProcessor extends ODataSingleProcessor {
	static Logger log = Logger.getLogger(SmartDataDiscoverySingleProcessor.class);

	@Override
	public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) 
			throws ODataException {

		EdmEntitySet entitySet;

		if (uriInfo.getNavigationSegments().size() == 0) {
			entitySet = uriInfo.getStartEntitySet();

			if (ENTITY_SET_NAME_DATASETS.equals(entitySet.getName())) {

				Object userQuery = new BasicDBObject();
				FilterExpression fe = uriInfo.getFilter();
				if (fe != null) {
					SDPExpressionVisitor ev = new SDPExpressionVisitor();
					ev.setEntitySetName(entitySet.getName());
					userQuery = fe.accept(ev);
					log.info("expression:\n" + ev.getOut());
				}

				MongoDbStore mongoAccess=new MongoDbStore();

				//Map<String,String> param = uriInfo.getCustomQueryOptions();
				
				
				//TODO
				Integer skip=(uriInfo.getSkip()!=null ? uriInfo.getSkip() : new Integer(-1));
				Integer top= (uriInfo.getTop()!= null ? uriInfo.getTop()  : new Integer(-1));

				List<Map<String,Object>> allDatasets=mongoAccess.getAllFilteredDatasets(userQuery,skip.intValue(),top.intValue());
				if (allDatasets != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder =EntityProviderWriteProperties.serviceRoot(serviceRoot);

					// create and register callback
					Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
					callbacks.put(ENTITY_SET_NAME_FIELDS, new MyCallback(mongoAccess, serviceRoot));
					callbacks.put(ENTITY_NAME_STREAM, new MyCallback(mongoAccess, serviceRoot));
					ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
					//
					
//					ExpandSelectTreeNode expandSensorTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
					//
					propertiesBuilder.expandSelectTree(expandSelectTreeNode).callbacks(callbacks);

					return EntityProvider.writeFeed(contentType, entitySet, allDatasets, propertiesBuilder.build());
				}
			} else if (ENTITY_SET_NAME_STREAMS.equals(entitySet.getName())) {

				Object userQuery = new BasicDBObject();
				FilterExpression fe = uriInfo.getFilter();
				if (fe != null) {
					SDPExpressionVisitor ev = new SDPExpressionVisitor();
					ev.setEntitySetName(entitySet.getName());
					userQuery = fe.accept(ev);
					log.info("expression:\n" + ev.getOut());
				}

				MongoDbStore mongoAccess=new MongoDbStore();

				//Map<String,String> param = uriInfo.getCustomQueryOptions();

				List<Map<String,Object>> allStreams=mongoAccess.getAllFilteredStreams(userQuery);
				if (allStreams != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder =EntityProviderWriteProperties.serviceRoot(serviceRoot);

					// create and register callback
					Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
					callbacks.put(ENTITY_NAME_DATASET, new MyCallback(mongoAccess, serviceRoot));
					ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
//					//
//					ExpandSelectTreeNode expandSensorTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
					//
					propertiesBuilder.expandSelectTree(expandSelectTreeNode).callbacks(callbacks);
					return EntityProvider.writeFeed(contentType, entitySet, allStreams, propertiesBuilder.build());
				}
			} 

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			// navigation first level, simplified example for illustration purposes only
			entitySet = uriInfo.getTargetEntitySet();

			if (ENTITY_SET_NAME_FIELDS.equals(entitySet.getName())) {

				Long datasetKey = getLongKeyValue(uriInfo.getKeyPredicates().get(0));

				MongoDbStore mongoAccess=new MongoDbStore();
				List<Map<String,Object>> allDatasets=mongoAccess.getDatasetFields(datasetKey);

				return EntityProvider.writeFeed(contentType, entitySet, allDatasets, EntityProviderWriteProperties.serviceRoot(
						getContext().getPathInfo().getServiceRoot()).build());
			}
			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
		}
		throw new ODataNotImplementedException();
	}

	@Override
	public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException {
		EdmEntitySet entitySet;
		if (uriInfo.getNavigationSegments().size() == 0) {
			 entitySet = uriInfo.getStartEntitySet();

			if (ENTITY_SET_NAME_DATASETS.equals(entitySet.getName())) {

				MongoDbStore mongoAccess=new MongoDbStore();
				Long idDataset = getLongKeyValue(uriInfo.getKeyPredicates().get(0));
				Map<String,Object> dataset=mongoAccess.getDataset(idDataset);

				if (dataset != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder =EntityProviderWriteProperties.serviceRoot(serviceRoot);

					// create and register callback
					Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
					callbacks.put(ENTITY_SET_NAME_FIELDS, new MyCallback(mongoAccess, serviceRoot));
					callbacks.put(ENTITY_NAME_STREAM, new MyCallback(mongoAccess, serviceRoot));
					
					ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
					//
					propertiesBuilder.expandSelectTree(expandSelectTreeNode).callbacks(callbacks);


					return EntityProvider.writeEntry(contentType, entitySet, dataset, propertiesBuilder.build());
				}
			}else if (ENTITY_SET_NAME_STREAMS.equals(entitySet.getName())) {

				MongoDbStore mongoAccess=new MongoDbStore();
				Long idStream = getLongKeyValue(uriInfo.getKeyPredicates().get(0));
				Map<String,Object> stream=mongoAccess.getStream(idStream);

				if (stream != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder =EntityProviderWriteProperties.serviceRoot(serviceRoot);

					// create and register callback
					Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
					callbacks.put(ENTITY_NAME_DATASET, new MyCallback(mongoAccess, serviceRoot));
					
					ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
					
					propertiesBuilder.expandSelectTree(expandSelectTreeNode).callbacks(callbacks);

					return EntityProvider.writeEntry(contentType, entitySet, stream, propertiesBuilder.build());
				}
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		}else if (uriInfo.getNavigationSegments().size() == 1) {
			// navigation first level, simplified example for illustration purposes only
			entitySet = uriInfo.getTargetEntitySet();

			if (ENTITY_SET_NAME_FIELDS.equals(entitySet.getName()) && ENTITY_SET_NAME_DATASETS.equals(uriInfo.getStartEntitySet().getName())) {

				Long datasetKey = getLongKeyValue(uriInfo.getKeyPredicates().get(0));

				MongoDbStore mongoAccess=new MongoDbStore();
				List<Map<String,Object>> allDatasets=mongoAccess.getDatasetFields(datasetKey);

				return EntityProvider.writeFeed(contentType, entitySet, allDatasets, EntityProviderWriteProperties.serviceRoot(
						getContext().getPathInfo().getServiceRoot()).build());
			}else if (ENTITY_NAME_STREAM.equals(entitySet.getName())  && ENTITY_SET_NAME_DATASETS.equals(uriInfo.getStartEntitySet().getName())) {

				Long datasetKey = getLongKeyValue(uriInfo.getKeyPredicates().get(0));

				MongoDbStore mongoAccess=new MongoDbStore();
				Map<String,Object>Sensor=mongoAccess.getDatasetSensor(datasetKey);

				return EntityProvider.writeEntry(contentType, entitySet, Sensor, EntityProviderWriteProperties.serviceRoot(
						getContext().getPathInfo().getServiceRoot()).build());
			}else if (ENTITY_SET_NAME_DATASETS.equals(entitySet.getName())  && ENTITY_SET_NAME_STREAMS.equals(uriInfo.getStartEntitySet().getName())) {

				Long streamKey = getLongKeyValue(uriInfo.getKeyPredicates().get(0));

				MongoDbStore mongoAccess=new MongoDbStore();
				Map<String,Object>Sensor=mongoAccess.getDatasetFromStream(streamKey.intValue());

				return EntityProvider.writeEntry(contentType, entitySet, Sensor, EntityProviderWriteProperties.serviceRoot(
						getContext().getPathInfo().getServiceRoot()).build());
			}
		}
		throw new ODataNotImplementedException();
	}

//	private String getStringKeyValue(final KeyPredicate key) throws ODataException {
//		EdmProperty property = key.getProperty();
//		EdmSimpleType type = (EdmSimpleType) property.getType();
//		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), String.class);
//	}
	private Long getLongKeyValue(final KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), Long.class);
	}
}
