package org.csi.yucca.dataservice.metadataapi.util;

import java.util.HashMap;
import java.util.Map;

import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatAgent;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatObject;
import org.csi.yucca.dataservice.metadataapi.model.dcat.DCatTheme;
import org.csi.yucca.dataservice.metadataapi.model.dcat.IdString;

public class DCatSdpHelper {

	private static Map<String, DCatTheme> themeMap;
	private static Map<String, String> subjectsMap;

	private static DCatAgent csiAgentDcat;

	private DCatSdpHelper() {

	}

	public static DCatTheme getDcatTheme(String sdpDomainCode) {
		if (themeMap == null) {
			themeMap = new HashMap<String, DCatTheme>();

			themeMap.put("AGRICULTURE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/AGRI", "Agricoltura", "Agriculture"));
			themeMap.put("ENERGY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ENER", "Energia", "Energy"));
			themeMap.put("ENVIRONMENT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ENVI", "Ambiente", "Environment"));
			themeMap.put("HEALTH", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/HEAL", "Salute", "Health"));
			themeMap.put("SCHOOL", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Scuola", "School"));
			themeMap.put("SECURITY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/JUST", "Sicurezza", "Security"));
			themeMap.put("SMART_COMMUNITY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Smart Community", "Smart Community"));
			themeMap.put("CULTURE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Cultura", "Culture"));
			themeMap.put("TRANSPORT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/TRAN", "Trasporti", "Transport"));
			themeMap.put("GOVERNMENT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/GOVE", "Pubblica Amministrazione e Politica",
					"Public Administration and Politics"));
			themeMap.put("PRODUCTION", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Attività produttive", "Production"));
			themeMap.put("EMPLOYMENT_TRAINING", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Lavoro e Formazione Professionale",
					"Employment and Professional Training"));
			themeMap.put("TERRITORY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/REGI", "Territorio", "Territory"));
			themeMap.put("TOURISM_SPORT", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/EDUC", "Turismo,  Sport e tempo libero",
					"Tourism, sport and leisure"));
			themeMap.put("SCIENCE_TECHNOLOGY", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/TECH", "Scienza tecnologia e innovazione",
					"Science, Technology and Innovation"));
			themeMap.put("TRADE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Commercio", "Trade"));
			themeMap.put("POPULATION_SOCIAL_ISSUE", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/SOCI", "Popolazione e questioni sociali",
					"Population and social issues"));
			themeMap.put("ECONOMY_FINANCES_TAXES", new DCatTheme("http://publications.europa.eu/resource/authority/data-theme/ECON", "Economia, finanze e tributi",
					"Economy, finance and tax"));
		}
		return themeMap.get(sdpDomainCode);
	}

	public static String getDcatSubject(String sdpSubdomainCode) {
		if (subjectsMap == null) {
			subjectsMap = new HashMap<String, String>();
			subjectsMap.put("FARM_ANIMALS", "http://eurovoc.europa.eu/711");
			subjectsMap.put("AGRICULTURAL_POLICIES", "http://eurovoc.europa.eu/2442");
			subjectsMap.put("FISHERIES", "http://eurovoc.europa.eu/1652");
			subjectsMap.put("FOOD", "http://eurovoc.europa.eu/656");
			subjectsMap.put("FORESTRY", "http://eurovoc.europa.eu/4352");
			subjectsMap.put("COMMUNICATION", "http://eurovoc.europa.eu/1370");
			subjectsMap.put("LIBRARY", "http://eurovoc.europa.eu/4865");
			subjectsMap.put("RESEARCH", "http://eurovoc.europa.eu/2914");
			subjectsMap.put("CREDIT", "http://eurovoc.europa.eu/285");
			subjectsMap.put("ECONOMY", "http://eurovoc.europa.eu/637");
			subjectsMap.put("FINANCES", "http://eurovoc.europa.eu/8469");
			subjectsMap.put("PRICES", "http://eurovoc.europa.eu/2632");
			subjectsMap.put("PUBLIC_FINANCE", "http://eurovoc.europa.eu/1018");
			subjectsMap.put("TAXES", "http://eurovoc.europa.eu/1310");
			subjectsMap.put("TAX_SYSTEM", "http://eurovoc.europa.eu/1021");
			subjectsMap.put("COMPANIES", "http://eurovoc.europa.eu/5985");
			subjectsMap.put("DEVELOPMENT_AND_RESOURCE_MANAGEMENT", "http://eurovoc.europa.eu/2807");
			subjectsMap.put("EMPLOYMENT", "http://eurovoc.europa.eu/2468");
			subjectsMap.put("LABOUR_MARKET", "http://eurovoc.europa.eu/1802");
			subjectsMap.put("PROFESSIONAL_TRAINING", "http://eurovoc.europa.eu/1073");
			subjectsMap.put("ENERGY", "http://eurovoc.europa.eu/2498");
			subjectsMap.put("AIR", "http://eurovoc.europa.eu/3967");
			subjectsMap.put("CLIMATE_AND_WEATHER_CONDITIONS", "http://eurovoc.europa.eu/6011");
			subjectsMap.put("ENVIRONMENTAL_POLICY", "http://eurovoc.europa.eu/5794");
			subjectsMap.put("LANDSCAPE_CONSERVATION", "http://eurovoc.europa.eu/2841");
			subjectsMap.put("MUNICIPAL_WASTE", "http://eurovoc.europa.eu/343");
			subjectsMap.put("NATURAL_RESOURCES", "http://eurovoc.europa.eu/3549");
			subjectsMap.put("POLLUTION", "http://eurovoc.europa.eu/2524");
			subjectsMap.put("RADIATIONS", "http://eurovoc.europa.eu/c_be0de7b7");
			subjectsMap.put("URBAN_AREA", "http://eurovoc.europa.eu/4809");
			subjectsMap.put("WATER", "http://eurovoc.europa.eu/597");
			subjectsMap.put("BUDGET_AND_FINANCIAL_STATEMENTS", "http://eurovoc.europa.eu/1012");
			subjectsMap.put("ELECTIONS", "http://eurovoc.europa.eu/2186");
			subjectsMap.put("INSTITUTIONAL_ACTIVITIES", "http://eurovoc.europa.eu/4179");
			subjectsMap.put("POLITICAL_INSTITUTIONS", "http://eurovoc.europa.eu/1453");
			subjectsMap.put("POLITICAL_LIFE", "http://eurovoc.europa.eu/4704");
			subjectsMap.put("PUBLIC_ADMINISTRATION", "http://eurovoc.europa.eu/77");
			subjectsMap.put("TRASPARENCY", "http://eurovoc.europa.eu/5936");
			subjectsMap.put("DISTRICT_CARE", "http://eurovoc.europa.eu/3357");
			subjectsMap.put("E_HEALTH", "http://eurovoc.europa.eu/c_31da5694");
			subjectsMap.put("HEALTH_SYSTEM", "http://eurovoc.europa.eu/5764");
			subjectsMap.put("HEALTH_WORKFORCE", "http://eurovoc.europa.eu/2789");
			subjectsMap.put("HOSPITAL_CARE", "http://eurovoc.europa.eu/3368");
			subjectsMap.put("PUBLIC_HEALTH", "http://eurovoc.europa.eu/3885");
			subjectsMap.put("ASSOCIATIONS", "http://eurovoc.europa.eu/5657");
			subjectsMap.put("COMMUNITY", "http://eurovoc.europa.eu/8428");
			subjectsMap.put("FAMILY", "http://eurovoc.europa.eu/965");
			subjectsMap.put("NO_PROFIT_INSTITUTIONS", "http://eurovoc.europa.eu/4204");
			subjectsMap.put("POPULATION", "http://eurovoc.europa.eu/3318");
			subjectsMap.put("SOCIAL_WELFARE", "http://eurovoc.europa.eu/1004");
			subjectsMap.put("SOLIDARITY", "http://eurovoc.europa.eu/8411");
			subjectsMap.put("WELFARE", "http://eurovoc.europa.eu/4050");
			subjectsMap.put("CRAFT_INDUSTRIES", "http://eurovoc.europa.eu/2712");
			subjectsMap.put("ECONOMIC_DEVELOPMENT", "http://eurovoc.europa.eu/427");
			subjectsMap.put("INDUSTRY", "http://eurovoc.europa.eu/2720");
			subjectsMap.put("MASTER_DATA", "http://eurovoc.europa.eu/6030");
			subjectsMap.put("SERVICES", "http://eurovoc.europa.eu/4099");
			subjectsMap.put("EDUCATION", "http://eurovoc.europa.eu/668");
			subjectsMap.put("INFORMATION", "http://eurovoc.europa.eu/1422");
			subjectsMap.put("RIGHT_TO_STUDY", "http://eurovoc.europa.eu/512");
			subjectsMap.put("SC_CULTURE", "http://eurovoc.europa.eu/317");
			subjectsMap.put("SC_RESEARCH", "http://eurovoc.europa.eu/2914");
			subjectsMap.put("BIOLOGY", "http://eurovoc.europa.eu/4921");
			subjectsMap.put("CHEMISTRY", "http://eurovoc.europa.eu/5966");
			subjectsMap.put("ICT", "http://eurovoc.europa.eu/5188");
			subjectsMap.put("INNOVATION", "http://eurovoc.europa.eu/1439");
			subjectsMap.put("PHYSICS", "http://eurovoc.europa.eu/3946");
			subjectsMap.put("SCIENCE", "http://eurovoc.europa.eu/3949");
			subjectsMap.put("SPACE", "http://eurovoc.europa.eu/6382");
			subjectsMap.put("ST_PRODUCTION", "http://eurovoc.europa.eu/2707");
			subjectsMap.put("ST_RESEARCH", "http://eurovoc.europa.eu/2914");
			subjectsMap.put("TECHNOLOGY", "http://eurovoc.europa.eu/4415");
			subjectsMap.put("CIVIL_PROTECTION", "http://eurovoc.europa.eu/3057");
			subjectsMap.put("IT_SECURITY", "http://eurovoc.europa.eu/c_04ae3ba8");
			subjectsMap.put("LAW", "http://eurovoc.europa.eu/4222");
			subjectsMap.put("PUBLIC_SECURITY", "http://eurovoc.europa.eu/4045");
			subjectsMap.put("SMART_COMMUNITY", "http://eurovoc.europa.eu/3607");
			subjectsMap.put("FORESTRY_POLICIES", "http://eurovoc.europa.eu/2505");
			subjectsMap.put("FORESTS", "http://eurovoc.europa.eu/1063");
			subjectsMap.put("GEOGRAPHICAL_INFORMATION", "http://eurovoc.europa.eu/7218");
			subjectsMap.put("HOUSING_AND_CITY_PLANNING", "http://eurovoc.europa.eu/4619");
			subjectsMap.put("MOUNTAIN", "http://eurovoc.europa.eu/1987");
			subjectsMap.put("PUBLIC_WORKS", "http://eurovoc.europa.eu/4565");
			subjectsMap.put("SOIL_PROTECTION", "http://eurovoc.europa.eu/2842");
			subjectsMap.put("LEISURE", "http://eurovoc.europa.eu/1700");
			subjectsMap.put("SPORT", "http://eurovoc.europa.eu/4245");
			subjectsMap.put("TOURISM", "http://eurovoc.europa.eu/4470");
			subjectsMap.put("TS_CULTURE", "http://eurovoc.europa.eu/317");
			subjectsMap.put("ECONOMIC_AND_COMMERCIAL_EXCHANGES", "http://eurovoc.europa.eu/3185");
			subjectsMap.put("INTERNATIONAL_TRADE", "http://eurovoc.europa.eu/11");
			subjectsMap.put("TR_SERVICES", "http://eurovoc.europa.eu/614");
			subjectsMap.put("INCIDENTS", "http://eurovoc.europa.eu/730");
			subjectsMap.put("MOBILITY", "http://eurovoc.europa.eu/6850");
			subjectsMap.put("RISK", "http://eurovoc.europa.eu/4047");
			subjectsMap.put("TRAFFIC", "http://eurovoc.europa.eu/6849");
			subjectsMap.put("TRANSPORT", "http://eurovoc.europa.eu/2181");
		}
		return subjectsMap.get(sdpSubdomainCode);
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

	public static boolean isCSIAgent(String name) {
		boolean result = false;
		if (name != null) {
			name = name.toUpperCase();
			result = name.indexOf("CSI") >= 0 && name.indexOf("PIEMONTE") >= 0;
		}

		return result;
	}


}
