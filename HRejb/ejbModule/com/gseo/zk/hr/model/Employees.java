package com.gseo.zk.hr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employees implements Serializable {

	private static final long serialVersionUID = 6711978034273008103L;
	private List listEmp;
	
	public Employees(){
		listEmp = new ArrayList();
	}
	
	public int size(){
		return listEmp.size();
	}
	
	public void add(Employee emp){
		listEmp.add(emp);
	}
	
	public Employee get(int idx){
		return (Employee) listEmp.get(idx);
	}
}
