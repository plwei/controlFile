package com.easyui.util;

import java.util.ArrayList;
import java.util.HashMap;

public class DbMap extends HashMap {
	private String method = "";
	private ArrayList data = new ArrayList();
	
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ArrayList getData() {
		return data;
	}
	public void setData(ArrayList data) {
		this.data = data;
	}
	
}
