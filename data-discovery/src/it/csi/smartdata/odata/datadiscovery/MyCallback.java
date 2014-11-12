package it.csi.smartdata.odata.datadiscovery;

import java.net.URI;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.callback.OnWriteFeedContent;
import org.apache.olingo.odata2.api.ep.callback.WriteCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackContext;
import org.apache.olingo.odata2.api.ep.callback.WriteFeedCallbackResult;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_SET_NAME_DATASETS;
import static it.csi.smartdata.odata.datadiscovery.SmartDataDiscoveryEdmProvider.ENTITY_SET_NAME_FIELDS;

/**
 * 
 */
public class MyCallback implements /*OnWriteEntryContent,*/ OnWriteFeedContent {
  private static final Logger LOG = LoggerFactory.getLogger(MyCallback.class);
  
   private final MongoDbStore dataStore;
   private final URI serviceRoot;
  
  public MyCallback(MongoDbStore store, URI serviceRoot) {
    this.dataStore = store;
    this.serviceRoot = serviceRoot;
  }

//  @Override
//  public WriteEntryCallbackResult retrieveEntryResult(WriteEntryCallbackContext context) throws ODataApplicationException {
//    WriteEntryCallbackResult result = new WriteEntryCallbackResult();
//    
//    try {
//      if (isNavigationFromTo(context, ENTITY_SET_NAME_CARS, ENTITY_NAME_MANUFACTURER)) {
//        EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
//            .expandSelectTree(context.getCurrentExpandSelectTreeNode())
//            .build();
//
//        Map<String, Object> keys = context.extractKeyFromEntryData();
//        Integer carId = (Integer) keys.get("Id");
//        result.setEntryData(dataStore.getManufacturerFor(carId));
//        result.setInlineProperties(inlineProperties);
//      }
//    } catch (EdmException e) {
//      // TODO: should be handled and not only logged
//      LOG.error("Error in $expand handling.", e);
//    } catch (EntityProviderException e) {
//      // TODO: should be handled and not only logged
//      LOG.error("Error in $expand handling.", e);
//    }
//    
//    return result;
//  }

  @Override
  public WriteFeedCallbackResult retrieveFeedResult(WriteFeedCallbackContext context) throws ODataApplicationException {
    WriteFeedCallbackResult result = new WriteFeedCallbackResult();
    try {
      if(isNavigationFromTo(context, ENTITY_SET_NAME_DATASETS, ENTITY_SET_NAME_FIELDS)) {
        EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
            .expandSelectTree(context.getCurrentExpandSelectTreeNode())
            .selfLink(context.getSelfLink())
            .build();

        Map<String, Object> keys = context.extractKeyFromEntryData();
        Integer datasetId =  (Integer) keys.get("idDataset");
        result.setFeedData(dataStore.getDatasetFields(datasetId));
        result.setInlineProperties(inlineProperties);
      }
    } catch (EdmException e) {
      // TODO: should be handled and not only logged
      LOG.error("Error in $expand handling.", e);
    } catch (EntityProviderException e) {
      // TODO: should be handled and not only logged
      LOG.error("Error in $expand handling.", e);
    }
    return result;
  }
  

  private boolean isNavigationFromTo(WriteCallbackContext context, String entitySetName, String navigationPropertyName) throws EdmException {
    if(entitySetName == null || navigationPropertyName == null) {
      return false;
    }
    EdmEntitySet sourceEntitySet = context.getSourceEntitySet();
    EdmNavigationProperty navigationProperty = context.getNavigationProperty();
    return entitySetName.equals(sourceEntitySet.getName()) && navigationPropertyName.equals(navigationProperty.getName());
  }
}
