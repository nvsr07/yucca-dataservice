package org.csi.yucca.dataservice.binaryapi.knoxapi.json;

import com.google.gson.annotations.Expose;

public class FileStatus {

	@Expose
	private Long accessTime;
	@Expose
	private Long blockSize;
	@Expose
	private String  group;
	@Expose
	private Long length;
	@Expose
	private Long modificationTime;
	@Expose
	private String owner;
	@Expose
	private String pathSuffix;
	@Expose
	private String permission;
	@Expose
	private Long replication;
	@Expose
	private String type;
	public Long getAccessTime() {
		return accessTime;
	}
	public void setAccessTime(Long accessTime) {
		this.accessTime = accessTime;
	}
	public Long getBlockSize() {
		return blockSize;
	}
	public void setBlockSize(Long blockSize) {
		this.blockSize = blockSize;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	public Long getModificationTime() {
		return modificationTime;
	}
	public void setModificationTime(Long modificationTime) {
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
	public Long getReplication() {
		return replication;
	}
	public void setReplication(Long replication) {
		this.replication = replication;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
