package com.gseo.zk.hr.model;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class Employee implements Serializable {

	private static final long serialVersionUID = -3365677627229370888L;
	
	private Map keys;
	
	public Employee(){
		keys = new Hashtable();
	}
	
	public void setKey(String key){
		keys.put(key, "");
	}
	
	public void setValue(String key, String value){
		if(hasKey(key))
			keys.put(key, value);
	}
	
	public void removeKey(String key){
		if(hasKey(key))
			keys.remove(key);
	}
	
	public String getValue(String key){
		return (String)keys.get(key);
	}
	
	public boolean hasKey(String key){
		return keys.containsKey(key);
	}
	
	public int size(){
		return keys.size();
	}
	
	public String[] keys(){
		Iterator iter = keys.entrySet().iterator();
		String[] ary = new String[size()];
		int i=0;
		while (iter.hasNext()) { 
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    ary[i] = (String) entry.getKey(); 
		    i++;
		}
		return ary;
	}
	
}
