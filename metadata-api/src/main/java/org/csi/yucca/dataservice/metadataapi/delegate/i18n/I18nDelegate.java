package org.csi.yucca.dataservice.metadataapi.delegate.i18n;

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

	private static Map<String, Map<String, String>> translationMap;

	public static String translate(String code, String lang) {
		lang = safeLanguage(lang);
		Map<String, String> translations = getTranslationMap().get(code);
		if (translations != null)
			return translations.get(lang);
		return code;
	}

	public static String[] translateMulti(String[] codes, String lang) {

		lang = safeLanguage(lang);
		String[] tranlsated = null;
		if (codes != null && codes.length > 0) {
			tranlsated = new String[codes.length];
			for (int i = 0; i < codes.length; i++) {
				tranlsated[i] = translate(codes[i].trim(), lang);
			}
		}
		return tranlsated;
	}

	private static Map<String, Map<String, String>> getTranslationMap() {
		if (translationMap == null)
			refreshTranslation();
		return translationMap;
	}

	private static void refreshTranslation() {
		StreamTags streamTags = loadStreamTags();

		translationMap = new HashMap<String, Map<String, String>>();

		for (TagElement streamTag : streamTags.getElement()) {
			Map<String, String> langMap = new HashMap<String, String>();
			langMap.put("it", streamTag.getLangIt());
			langMap.put("en", streamTag.getLangEn());

			translationMap.put(streamTag.getTagCode(), langMap);
		}

		StreamDomains streamDomains = loadStreamDomains();

		for (DomainElement streamDomain : streamDomains.getElement()) {
			Map<String, String> langMap = new HashMap<String, String>();
			langMap.put("it", streamDomain.getLangIt());
			langMap.put("en", streamDomain.getLangEn());

			translationMap.put(streamDomain.getCodDomain(), langMap);

		}

		StreamSubDomains streamSubDomains = loadStreamSubDomains();

		for (SubDomainElement streamSubDomain : streamSubDomains.getElement()) {
			Map<String, String> langMap = new HashMap<String, String>();
			langMap.put("it", streamSubDomain.getLangIt());
			langMap.put("en", streamSubDomain.getLangIt());

			translationMap.put(streamSubDomain.getCodSubDomain(), langMap);

		}
	}

	private static StreamTags loadStreamTags() {

		String targetUrl = Config.getInstance().getServiceBaseUrl() + "misc/streamtags/";
		String tagsJson = HttpUtil.getInstance().doGet(targetUrl, null, null, null);
		return StreamTagsContainer.fromJson(tagsJson).getStreamTags();
	} 

	public static void clearCache() {
		translationMap = null;
	}

	public static Map<String, Map<String, String>> viewTranslationsMap() {
		return getTranslationMap();
	}

	private static StreamDomains loadStreamDomains() {

		String targetUrl = Config.getInstance().getServiceBaseUrl() + "misc/streamdomains/";
		String domainsJson = HttpUtil.getInstance().doGet(targetUrl, null, null, null);
		return StreamDomainsContainer.fromJson(domainsJson).getStreamDomains();
	}

	private static StreamSubDomains loadStreamSubDomains() {

		String targetUrl = Config.getInstance().getServiceBaseUrl() + "misc/streamsubdomains/";
		String subDomainsJson = HttpUtil.getInstance().doGet(targetUrl, null, null, null);
		return StreamSubDomainsContainer.fromJson(subDomainsJson).getStreamSubDomains();
	}

	private static String safeLanguage(String lang) {
		for (String supportedLanguages : Constants.SUPPORTED_LANGUAGES) {
			if (supportedLanguages.equals(lang))
				return lang;
		}
		return Constants.SUPPORTED_LANGUAGES[0];

	}
}
