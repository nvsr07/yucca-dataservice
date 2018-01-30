package org.csi.yucca.adminapi.model;

public class TagJson {
	
	private Integer id_tag;
	private String tagcode;
	private String langit;
	private String langen;
	private Integer id_ecosystem;
	
	public Integer getId_tag() {
		return id_tag;
	}
	public void setId_tag(Integer id_tag) {
		this.id_tag = id_tag;
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
	public Integer getId_ecosystem() {
		return id_ecosystem;
	}
	public void setId_ecosystem(Integer id_ecosystem) {
		this.id_ecosystem = id_ecosystem;
	}
	
}
