package com.easyui.obj;

import java.util.Map;

import com.easyui.util.JDBCTools;
import com.easyui.util.ParaForProcedure;

public class SelectTree {
	
	public Map getTree(Map<String, String> para) {
		ParaForProcedure pfp = new ParaForProcedure();
		pfp.setProcedure(para.get("procedure"));
		Map map = JDBCTools.getTools().queryProcedure(pfp);
		return map;
	}
	
	public Map getSelect(Map<String, String> para) {
		ParaForProcedure pfp = new ParaForProcedure();
		pfp.setProcedure(para.get("procedure"));
		String[] arr = {para.get("type"), ""};
		pfp.setArr(arr);
		String[] out = {"rows"};
		pfp.setOut(out);
		Map map = JDBCTools.getTools().queryProcedure(pfp);
		return map;
	}
}
