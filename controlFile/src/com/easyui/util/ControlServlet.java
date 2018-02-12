package com.easyui.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.groovy.GJson;

public class ControlServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
//		String paras = new String(request.getParameter("para").getBytes("ISO-8859-1"), "UTF-8");
		String paras = request.getParameter("para");
		Map<String, Object> para = GetMethod.getGson().fromJson(paras, new TypeToken<Map<String, String>>() {}.getType());
		System.out.println(para);
		Map map = GetMethod.doInvoke(para);
//		Map map = new HashMap<String, String>();
		PrintWriter out = response.getWriter();
		System.out.println(map);
		out.println(map);
		out.flush();
		out.close();
	}
	
	
	public List<Map<String, String>> query_tag(Map<String, String> map) {
		String sql = "select * from tag where pid = '" + map.get("id") + "'";
		List<Map<String, String>> list = JDBCTools.getTools().query(sql);
		return list;
	}
	
	public List insert_tag(Map<String, String> map) {
		int i = 0;
		List<Map<String, String>> data = GetMethod.getGson().fromJson(map.get("data"), new TypeToken<List<Map<String, String>>>() {}.getType());
		for(Map<String, String> m : data) {
			String sql = "insert into tag(id, name, pid, bqjb) values('"+GetMethod.getId()+"' ,'"+m.get("name")+
				"' ,'" + m.get("pid")+"' ,"+m.get("bqjb")+")";
			int j = JDBCTools.getTools().execute(sql);
			if(j > 0)
				i++;
		}
		List list = new ArrayList();
		list.add(i);
		return list;
	}
	
	public List update_tag(Map<String, String> map) {
		int i = 0;
		List<Map<String, String>> data = GetMethod.getGson().fromJson(map.get("data"), new TypeToken<List<Map<String, String>>>() {}.getType());
		for(Map<String, String> m : data) {
			String sql = "update tag set name = '"+m.get("name")+
				"' where id = '"+m.get("id")+"'";
			int j = JDBCTools.getTools().execute(sql);
			if(j > 0)
				i++;
		}
		List list = new ArrayList();
		list.add(i);
		return list;
	}
	
	public List delete_tag(Map<String, String> map) {
		String sql = "delete from tag where id in ("+map.get("id")+")";
		int i = JDBCTools.getTools().execute(sql);
		List list = new ArrayList();
		list.add(i);
		return list;
	}

}
