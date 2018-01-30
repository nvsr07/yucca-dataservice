package org.csi.yucca.adminapi.request;

public class TagRequest {

	private Integer idTag;
	private String tagcode;
	private String langit;
	private String langen;
	private Integer idEcosystem;

	public Integer getIdTag() {
		return idTag;
	}

	public void setIdTag(Integer idTag) {
		this.idTag = idTag;
	}

	public String getTagcode() {
		return tagcode;
	}

	public void setTagcode(String tagcode) {
		this.tagcode = tagcode;
	}

	public String getLangit() {
		return langit;
	}

	public void setLangit(String langit) {
		this.langit = langit;
	}

	public String getLangen() {
		return langen;
	}

	public void setLangen(String langen) {
		this.langen = langen;
	}

	public Integer getIdEcosystem() {
		return idEcosystem;
	}

	public void setIdEcosystem(Integer idEcosystem) {
		this.idEcosystem = idEcosystem;
	}

}
