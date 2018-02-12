package com.common.util;

import java.util.List;
import java.util.Map;

public class GetMethod {
	
	public static String getId() {
		String sql1 = "replace into tickets64 (stub) values ('a')";
		JDBCTools.execute(sql1);
		String sql2 = "select * from tickets64";
		List<Map<String, String>> list = JDBCTools.query(sql2);
		String id = list.get(0).get("id");
		return id;
	}
}
