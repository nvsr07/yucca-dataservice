package org.csi.yucca.adminapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TagResponse extends Response{
	private Integer idTag;
	private String tagcode;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String langit;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
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
