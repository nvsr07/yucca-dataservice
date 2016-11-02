package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import com.google.gson.annotations.Expose;

public class FileStatus {

	@Expose
	private Integer accessTime;
	@Expose
	private Integer blockSize;
	@Expose
	private String  group;
	@Expose
	private Integer length;
	@Expose
	private Integer modificationTime;
	@Expose
	private String owner;
	@Expose
	private String pathSuffix;
	
	
	public Integer getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Integer accessTime) {
		this.accessTime = accessTime;
	}
	public Integer getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(Integer blockSize) {
		this.blockSize = blockSize;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(Integer modificationTime) {
		this.modificationTime = modificationTime;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getPathSuffix() {
		return pathSuffix;
	}
	public void setPathSuffix(String pathSuffix) {
		this.pathSuffix = pathSuffix;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Integer getReplication() {
		return replication;
	}
	public void setReplication(Integer replication) {
		this.replication = replication;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String permission;
	private Integer replication;
	private String type;
	
}
