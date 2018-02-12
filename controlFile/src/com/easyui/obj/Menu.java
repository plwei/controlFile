package com.easyui.obj;

import java.util.ArrayList;
import java.util.Map;

import com.easyui.util.DbMap;
import com.easyui.util.GetMethod;
import com.easyui.util.JDBCTools;

public class Menu {
	
	public Map menuIns(Map para) {
		ArrayList list = new ArrayList();
		para.put("tableName", "menu");
		para.put("id", GetMethod.getId());
		para.put("cjsj", GetMethod.getDate());
		list.add(para);
		DbMap map = new DbMap();
		map.setMethod("insert");
		map.setData(list);
		JDBCTools.getTools().doDb(map);
		return map;
	}
	
	public Map menuUpd(Map map) {
		return map;
	}
}
