package com.cf.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCTools {
	
	public static String HOST = "localhost";
	public static String POST = "3306";
	public static String DATABASE_NAME = "controlfiles";
	public static String USER_NAME = "root";
	public static String PASSWORD = "123456";
	
	/**
	 * 获取数据库连接
	 * @return 数据库连接
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("成功加载驱动");
		String url = "jdbc:mysql://" + HOST + ":" + POST + "/" + DATABASE_NAME + "?user=" +
			USER_NAME + "&password=" + PASSWORD + "&useUnicode=true&characterEncoding=UTF8";
		Connection conn = DriverManager.getConnection(url);
		System.out.println("成功获取连接");
		return conn;
	}
	
	/**
	 * 关闭资源
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void closeResource(Connection conn, Statement st, ResultSet rs) {
		try {
			if(rs != null) 
				rs.close();
				
			if(st != null) 
				st.close();
				
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("成功关闭资源");
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return 数据集合
	 */
	public static List<Map<String, String>> query(String sql) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Map<String, String>> resultList = null;
		
		try {
			conn = getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] columnNames = new String[columnCount+1];
			for (int i = 0; i < columnCount; i++) {
				columnNames[i] = rsmd.getColumnName(i);
			}
			
			resultList = new ArrayList<Map<String,String>>();
			Map<String, String> resultMap = new HashMap<String, String>();
			rs.beforeFirst();
			while (rs.next()) {
				for (int i = 0; i < columnCount; i++) {
					resultMap.put(columnNames[i], rs.getString(i));
				}
				resultList.add(resultMap);
			}
			System.out.println("数据库查询成功,查的数据：" + resultList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, st, rs);
		}
		return resultList;
	}
	
	public static int execute(String sql) throws Exception {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int num = 0;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			st = conn.createStatement();
			num = st.executeUpdate(sql);
			System.out.println("成功操作数据库，影响数据：" + num);
			
			//模拟异常，用于测试事务
			/*if(1 == 1) {
				throw new RuntimeException();
			}*/
			
			conn.commit();
		} catch (Exception e) {
			//处理异常，事务回滚后抛出异常
			e.printStackTrace();
			conn.rollback();
			System.out.println("事务回滚");
			throw e;
		} finally {
			closeResource(conn, st, rs);
		}
		return num;
	}
	
	public static void main(String[] args) throws Exception{
		execute("");
	}
}
