package it.csi.smartdata.dataapi.adminapi;

import it.csi.smartdata.dataapi.adminapi.edmprovider.AbstractOdataProvider;
import it.csi.smartdata.dataapi.constants.SDPDataApiConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.ComplexProperty;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.csi.yucca.adminapi.response.ComponentResponse;


public class Util {

	public static List<Property> convertFromComponentResponseToProperty(
			List<ComponentResponse> components, String nameSpace)
			throws Exception {
		try {
			AbstractOdataProvider.log.debug("[AbstractOdataProvider::getDatasetField] BEGIN");
	
			List<Property> propOut = new ArrayList<Property>();
	
			for (int i = 0; i < components.size(); i++) {
				ComponentResponse elemento = components.get(i);
	
				String propName = elemento.getName();
				String propType = elemento.getDataType().getDatatypecode();
	
				if (SDPDataApiConstants.SDP_DATATYPE_MAP.get(propType).equals(
						EdmSimpleTypeKind.Binary)) {
					propOut.add(new ComplexProperty()
							.setName(propName)
							.setType(
									new FullQualifiedName(
											nameSpace,
											SDPDataApiConstants.COMPLEX_TYPE_BINARYREF))
							.setFacets(new Facets().setNullable(true)));
				} else {
					propOut.add(new SimpleProperty()
							.setName(propName)
							.setType(
									SDPDataApiConstants.SDP_DATATYPE_MAP
											.get(propType))
							.setFacets(new Facets().setNullable(true)));
				}
			}
			return propOut;
		} catch (Exception e) {
			AbstractOdataProvider.log.error("[AbstractOdataProvider::getDatasetField] " + e);
			throw e;
		} finally {
			AbstractOdataProvider.log.debug("[AbstractOdataProvider::getDatasetField] END");
		}
	}
	
	public static String takeNvlValues(Object obj) {
		if (null==obj) return null;
		else return obj.toString();
	}
	
	public static String getPropertyName(Property prop) {
		String chiave=prop.getName();
		if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Boolean)) {
			return chiave+"_b";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.String)) {
			return chiave+"_s";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Int32)) {
			return chiave+"_i";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Int64)) {
			return chiave+"_i";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Double)) {
			//return chiave+"_d";
			return chiave+"_f";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.DateTimeOffset)) {
			return chiave+"_dt";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.DateTime)) {
			return chiave+"_dt";


		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Decimal)) {
			return chiave+"_d";
		} else if (((SimpleProperty)prop).getType().equals(EdmSimpleTypeKind.Binary)) {
			return chiave+"_by";


		}	
		return chiave; }

	public static Map<String, String> convertFromComponentResponseToMap(
			List<ComponentResponse> eleCapmpi) {
		Map<String, String> mappa = new HashMap<>();
		if (eleCapmpi!=null)
		{
			for (ComponentResponse componentResponse : eleCapmpi) {
				String nome=componentResponse.getName();
				String tipo=componentResponse.getDataType().getDatatypecode();
				mappa.put(nome, tipo);
			}
		}
		return mappa;
	}



}
