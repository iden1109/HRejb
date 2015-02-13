package com.gseo.zk.hr.model;

public class OverTime implements java.io.Serializable{
	
	private static final long serialVersionUID = 5100305417827912004L;
	
	public static final String HOURS =  "小時";
	public static final String REASON = "原因";
	public static final String MULTI_APPLICANT = "等(多人申請)";
	public static final String OVERTIME_APPLICANT = "加班申請";
	
	private String levelValue;
	private String directLeaderCodes; // direct leader codes
	private String level1LeaderCode;  // Level 1 Leader ID
	private String level2LeaderCode; // Level 2 Leader ID
	private String dirInDir;  // is direct or indirect
	private String deptDescribe; // dept description
	private String overtimeName; // The employee name for overtime
	private String overtimeHours; // hours for overtime
	private String overtimeReason;  // reason for overtime
	private String overtimeNameHrsReason; // name + hours + reason

	
	public String getOvertimeNameHrsReason() {
		return overtimeNameHrsReason;
	}
	public void setOvertimeNameHrsReason(String overtimeNameHrsReason) {
		this.overtimeNameHrsReason = overtimeNameHrsReason;
	}
	public String getOvertimeName() {
		return overtimeName;
	}
	public void setOvertimeName(String overtimeName) {
		this.overtimeName = overtimeName;
	}
	public String getOvertimeHours() {
		return overtimeHours;
	}
	public void setOvertimeHours(String overtimeHours) {
		this.overtimeHours = overtimeHours;
	}
	public String getOvertimeReason() {
		return overtimeReason;
	}
	public void setOvertimeReason(String overtimeReason) {
		this.overtimeReason = overtimeReason;
	}
	public String getLevelValue() {
		return levelValue;
	}
	public void setLevelValue(String levelValue) {
		this.levelValue = levelValue;
	}
	public String getDirectLeaderCodes() {
		return directLeaderCodes;
	}
	public void setDirectLeaderCodes(String directLeaderCodes) {
		this.directLeaderCodes = directLeaderCodes;
	}
	public String getLevel1LeaderCode() {
		return level1LeaderCode;
	}
	public void setLevel1LeaderCode(String level1LeaderCode) {
		this.level1LeaderCode = level1LeaderCode;
	}
	public String getLevel2LeaderCode() {
		return level2LeaderCode;
	}
	public void setLevel2LeaderCode(String level2LeaderCode) {
		this.level2LeaderCode = level2LeaderCode;
	}
	public String getDirInDir() {
		return dirInDir;
	}
	public void setDirInDir(String dirInDir) {
		this.dirInDir = dirInDir;
	}
	public String getDeptDescribe() {
		return deptDescribe;
	}
	public void setDeptDescribe(String deptDescribe) {
		this.deptDescribe = deptDescribe;
	}
	
	
}
