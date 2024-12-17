package com.gm.easecode.frame.common;

public class Dependey {

	private String groupId;
	private String artifactId;
	private String version;
	
	public Dependey() {
		
	}
	
	public Dependey(String groupId, String artifactId) {
		this(groupId, artifactId, null);
	}
	
	public Dependey(String groupId, String artifactId, String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	public String getGroupId() {
		return groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
