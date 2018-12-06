package cn.cibnmp.ott.bean;

import java.io.Serializable;

public class AppUpdateBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2039774932396942355L;
	
	private String LatestVersion;
	private int Mandatory;
	private long LaunchTime;
	private long FileCount;
	private long FileLength;
	private String FID;
	private int MimeID ;
	private String VerUrl;
	private String VerDesc;
	private String FilePath;
	private long FileSize;
	private int isupgrade;
	public String getLatestVersion() {
		return LatestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		LatestVersion = latestVersion;
	}
	public int getMandatory() {
		return Mandatory;
	}
	public void setMandatory(int mandatory) {
		Mandatory = mandatory;
	}
	public long getLaunchTime() {
		return LaunchTime;
	}
	public void setLaunchTime(long launchTime) {
		LaunchTime = launchTime;
	}
	public long getFileCount() {
		return FileCount;
	}
	public void setFileCount(long fileCount) {
		FileCount = fileCount;
	}
	public long getFileLength() {
		return FileLength;
	}
	public void setFileLength(long fileLength) {
		FileLength = fileLength;
	}
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
	}
	public int getMimeID() {
		return MimeID;
	}
	public void setMimeID(int mimeID) {
		MimeID = mimeID;
	}
	public String getVerUrl() {
		return VerUrl;
	}
	public void setVerUrl(String verUrl) {
		VerUrl = verUrl;
	}
	public String getVerDesc() {
		return VerDesc;
	}
	public void setVerDesc(String verDesc) {
		VerDesc = verDesc;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
	
	public long getFileSize() {
		return FileSize;
	}
	public void setFileSize(long fileSize) {
		FileSize = fileSize;
	}
	public int getIsupgrade() {
		return isupgrade;
	}
	public void setIsupgrade(int isupgrade) {
		this.isupgrade = isupgrade;
	}
}
