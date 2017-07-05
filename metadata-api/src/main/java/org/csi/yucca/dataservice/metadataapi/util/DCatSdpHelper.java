package org.csi.yucca.dataservice.metadataapi.util;

import java.util.HashMap;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatAgent;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatTheme;
import org.csi.yucca.dataservice.metadataapi.model.dcat.IdString;

public class DCatSdpHelper {

	private static Map<String, DCatTheme> map;

	private static DCatAgent csiAgentDcat;

	private DCatSdpHelper() {

	}

	public static DCatTheme getDcatTheme(String sdpDomainCode) {
		if (map == null) {
			map = new HashMap<String, DCatTheme>();

			map.put("AGRICULTURE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/AGRI", "Agricoltura", "Agriculture"));
			map.put("ENERGY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ENER", "Energia", "Energy"));
			map.put("ENVIRONMENT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ENVI", "Ambiente", "Environment"));
			map.put("HEALTH", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/HEAL", "Salute", "Health"));
			map.put("SCHOOL", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Scuola", "School"));
			map.put("SECURITY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/JUST", "Sicurezza", "Security"));
			map.put("SMART_COMMUNITY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Smart Community", "Smart Community"));
			map.put("CULTURE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Cultura", "Culture"));
			map.put("TRANSPORT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/TRAN", "Trasporti", "Transport"));
			map.put("GOVERNMENT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/GOVE", "Pubblica Amministrazione e Politica",
					"Public Administration and Politics"));
			map.put("PRODUCTION", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Attività produttive", "Production"));
			map.put("EMPLOYMENT_TRAINING", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Lavoro e Formazione Professionale",
					"Employment and Professional Training"));
			map.put("TERRITORY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/REGI", "Territorio", "Territory"));
			map.put("TOURISM_SPORT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Turismo,  Sport e tempo libero",
					"Tourism, sport and leisure"));
			map.put("SCIENCE_TECHNOLOGY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/TECH", "Scienza tecnologia e innovazione",
					"Science, Technology and Innovation"));
			map.put("TRADE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Commercio", "Trade"));
			map.put("POPULATION_SOCIAL_ISSUE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Popolazione e questioni sociali",
					"Population and social issues"));
			map.put("ECONOMY_FINANCES_TAXES", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Economia, finanze e tributi",
					"Economy, finance and tax"));
		}
		return map.get(sdpDomainCode);
	}

	public static DCatAgent getCSIAgentDcat() {
		if (csiAgentDcat == null) {
			csiAgentDcat = new DCatAgent();
			csiAgentDcat.setId("01995120019");
			csiAgentDcat.setName("CSI PIEMONTE");
			csiAgentDcat.setDcterms_identifier("01995120019");
			csiAgentDcat.addDcterms_type(new IdString("http://purl.org/adms/publishertype/Company"));
		}
		return csiAgentDcat;

	}

	public static String cleanForId(String dcatCreatorName) {
		if (dcatCreatorName != null)
			return dcatCreatorName.replaceAll("[^a-zA-Z0-9]", "");
		return "";
	}
}
