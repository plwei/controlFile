package com.easyui.util;

import java.util.HashMap;

public class ParaForProcedure extends HashMap{
	String[] arr = {};
	String procedure = "";
	String[] out = {"msg", "total", "rows", "footer"};
	
	public String[] getArr() {
		return arr;
	}
	public void setArr(String[] arr) {
		this.arr = arr;
	}
	public String getProcedure() {
		return procedure;
	}
	public void setProcedure(String procedure) {
		this.procedure = procedure;
	}
	public String[] getOut() {
		return out;
	}
	public void setOut(String[] out) {
		this.out = out;
	}
}
