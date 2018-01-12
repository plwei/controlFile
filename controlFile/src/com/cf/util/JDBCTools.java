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

	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/controlfiles?useUnicode=true&characterEncoding=UTF8";
	public static final String USER_NAME = "root";
	public static final String PASSWORD = "123456";
	
	/**
	 * 获取数据库连接
	 * @return 数据库连接
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception{
		Class.forName(DRIVER);
		System.out.println("成功加载驱动");
		Connection conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
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
			
			ResultSetMetaData rsmd = rs.getMetaData();//得到结果集(rs)的结构信息，如字段数、字段名等
			int columnCount = rsmd.getColumnCount();//返回结果集(rs)中的列数
			resultList = new ArrayList<Map<String,String>>();
			while (rs.next()) {
				Map<String, String> resultMap = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					resultMap.put(rsmd.getColumnName(i), rs.getString(i));
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
	
	public static int execute(String sql) {
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
			try {
				conn.rollback();
				System.out.println("事务回滚");
				throw e;
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			closeResource(conn, st, rs);
		}
		return num;
	}
	
	public static void main(String[] args) throws Exception{
		execute("");
	}
}
