package com.common.util;

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

public class CommonControlServlet extends HttpServlet {

	public final String TABLENAME = "tag";
	public Gson gson = new Gson();
	
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		String para = new String(request.getParameter("para").getBytes("ISO-8859-1"), "UTF-8");
		HashMap<String, String> map = gson.fromJson(para, new TypeToken<Map<String, String>>() {}.getType());
		System.out.println(map);
		String method = map.get("method");
		List list = null;
		if(method.equals("query_tag")) {
			list = query_tag(map);
		} else if(method.equals("insert_tag")) {
			list = insert_tag(map);
		} else if(method.equals("update_tag")) {
			list = update_tag(map);
		} else if(method.equals("delete_tag")) {
			list = delete_tag(map);
		}
		JSONArray result = JSONArray.fromObject(list);
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}
	
	
	public List<Map<String, String>> query_tag(Map<String, String> map) {
		String sql = "select * from tag where pid = '" + map.get("id") + "'";
		List<Map<String, String>> list = JDBCTools.query(sql);
		return list;
	}
	
	public List insert_tag(Map<String, String> map) {
		int i = 0;
		List<Map<String, String>> data = gson.fromJson(map.get("data"), new TypeToken<List<Map<String, String>>>() {}.getType());
		for(Map<String, String> m : data) {
			String sql = "insert into tag(id, name, pid, bqjb) values('"+GetMethod.getId()+"' ,'"+m.get("name")+
				"' ,'" + m.get("pid")+"' ,"+m.get("bqjb")+")";
			int j = JDBCTools.execute(sql);
			if(j > 0)
				i++;
		}
		List list = new ArrayList();
		list.add(i);
		return list;
	}
	
	public List update_tag(Map<String, String> map) {
		int i = 0;
		List<Map<String, String>> data = gson.fromJson(map.get("data"), new TypeToken<List<Map<String, String>>>() {}.getType());
		for(Map<String, String> m : data) {
			String sql = "update tag set name = '"+m.get("name")+
				"' where id = '"+m.get("id")+"'";
			int j = JDBCTools.execute(sql);
			if(j > 0)
				i++;
		}
		List list = new ArrayList();
		list.add(i);
		return list;
	}
	
	public List delete_tag(Map<String, String> map) {
		String sql = "delete from tag where id in ("+map.get("id")+")";
		int i = JDBCTools.execute(sql);
		List list = new ArrayList();
		list.add(i);
		return list;
	}

}
