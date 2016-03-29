package org.csi.yucca.dataservice.metadataapi.delegate;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.csi.yucca.dataservice.metadataapi.util.Config;
import org.csi.yucca.dataservice.metadataapi.util.Constants;
import org.csi.yucca.dataservice.metadataapi.util.HttpUtil;

public class I18nDelegate {

	static Logger log = Logger.getLogger(I18nDelegate.class);

	public I18nDelegate() {

	}

	private static Map<String, Map<String, String>> tagsMap;
	private static Map<String, Map<String, String>> domainsMap;

	public static String translateTag(String tagCode, String lang) {

		Map<String, String> tagLanguages = getTagsMap().get(tagCode);
		if (tagLanguages != null)
			return tagLanguages.get(lang);
		return tagCode;
	}

	public static String[] translateTags(String[] tagCodes, String lang) {
		
		lang  = safeLanguage(lang);
		String[] tranlsated = null;
		if (tagCodes != null && tagCodes.length > 0) {
			tranlsated = new String[tagCodes.length];
			for (int i = 0; i < tagCodes.length; i++) {
				tranlsated[i] = translateTag(tagCodes[i], lang);
			}
		}
		return tranlsated;
	}

	private static Map<String, Map<String, String>> getTagsMap() {
		if (tagsMap == null)
			refreshTagTranslation();
		return tagsMap;
	}

	private static void refreshTagTranslation() {
		StreamTags streamTags = loadStreamTags();

		tagsMap = new HashMap<String, Map<String, String>>();

		for (TagElement streamTag : streamTags.getElement()) {
			Map<String, String> langMap = new HashMap<String, String>();
			langMap.put("it", streamTag.getLangIt());
			langMap.put("en", streamTag.getLangEn());

			tagsMap.put(streamTag.getTagCode(), langMap);

		}
	}

	private static StreamTags loadStreamTags() {

		String targetUrl = Config.getInstance().getServiceBaseUrl() + "misc/streamtags/";
		String tagsJson = HttpUtil.getInstance().doGet(targetUrl, null, null, null);
		return StreamTagsContainer.fromJson(tagsJson).getStreamTags();
	}



	
	
	public static String translateDomain(String domainCode, String lang) {

		Map<String, String> domainLanguages = getDomainsMap().get(domainCode);
		if (domainLanguages != null)
			return domainLanguages.get(lang);
		return domainCode;
	}

	public static String[] translateDomains(String[] domainCodes, String lang) {
		
		lang  = safeLanguage(lang);
		String[] tranlsated = null;
		if (domainCodes != null && domainCodes.length > 0) {
			tranlsated = new String[domainCodes.length];
			for (int i = 0; i < domainCodes.length; i++) {
				tranlsated[i] = translateDomain(domainCodes[i], lang);
			}
		}
		return tranlsated;
	}

	private static Map<String, Map<String, String>> getDomainsMap() {
		if (domainsMap == null)
			refreshDomainTranslation();
		return domainsMap;
	}

	private static void refreshDomainTranslation() {
		StreamDomains streamDomains = loadStreamDomains();

		domainsMap = new HashMap<String, Map<String, String>>();

		for (DomainElement streamDomain : streamDomains.getElement()) {
			Map<String, String> langMap = new HashMap<String, String>();
			langMap.put("it", streamDomain.getLangIt());
			langMap.put("en", streamDomain.getLangEn());

			domainsMap.put(streamDomain.getCodDomain(), langMap);

		}
	}

	private static StreamDomains loadStreamDomains() {

		String targetUrl = Config.getInstance().getServiceBaseUrl() + "misc/streamdomains/";
		String domainsJson = HttpUtil.getInstance().doGet(targetUrl, null, null, null);
		return StreamDomainsContainer.fromJson(domainsJson).getStreamDomains();
	}

	private static String safeLanguage(String lang) {
		for (String supportedLanguages : Constants.SUPPORTED_LANGUAGES) {
			if (supportedLanguages.equals(lang))
				return lang;
		}
		return Constants.SUPPORTED_LANGUAGES[0];

	}
}
