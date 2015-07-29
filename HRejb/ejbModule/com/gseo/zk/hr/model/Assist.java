package com.gseo.zk.hr.model;

import java.io.Serializable;

public class Assist implements Serializable{
	private String assist; //協助事項
	private String grade; //職等
	
	public String getAssist() {
		return assist;
	}
	public void setAssist(String assist) {
		this.assist = assist;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
}
