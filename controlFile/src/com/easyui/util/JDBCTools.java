package com.easyui.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

public class JDBCTools {

	private static JDBCTools tools;
	private final String DRIVER = "com.mysql.jdbc.Driver";
	private final String URL = "jdbc:mysql://localhost:3306/controlfiles?useUnicode=true&characterEncoding=UTF8";
	private final String USER_NAME = "root";
	private final String PASSWORD = "123456";
	
	public static JDBCTools getTools() {
		if(tools == null)
			tools = new JDBCTools();
		return tools;
	}
	
	/**
	 * 获取数据库连接
	 * @return 数据库连接
	 * @throws Exception
	 */
	public Connection getConnection(){
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭资源（通用）
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public void closeResource(Connection conn, PreparedStatement pst, ResultSet rs) {
		try {
			if(rs != null)
				rs.close();
				
			if(pst != null)
				pst.close();
				
			if(conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("成功关闭资源");
	}
	
	/**
	 * 关闭资源（存储过程）
	 * @param conn
	 * @param cs
	 * @param rs
	 */
	public void closeResource(Connection conn, CallableStatement cs, ResultSet rs) {
		try {
			if(rs != null)
				rs.close();
			if(cs != null)
				cs.close();
			if(conn != null)
				conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("成功关闭资源");
	}
	
	/**
	 * 查询
	 * @param sql
	 * @return 数据集合
	 */
	public List<Map<String, String>> query(String sql) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Map<String, String>> resultList = null;
		
		try {
			conn = getConnection();
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();//得到结果集(rs)的结构信息，如字段数、字段名等
			int columnCount = rsmd.getColumnCount();//得到结果集(rs)中的字段数
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
	
	/**
	 * 执行新建、修改、删除操作
	 * @param sql
	 * @return
	 */
	public int execute(String sql) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		int num = 0;
		
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			st = conn.prepareStatement(sql);
			num = st.executeUpdate();
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
	
	/**
	 * 调用存储过程返回结果集
	 * 一般是用在easyui的datagrid上
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryProcedure(ParaForProcedure map) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			String[] arr = map.getArr();
			String[] out = map.getOut();
			String procedure = map.getProcedure().toUpperCase();
			String sql = "{call " + procedure + "(";
			for(int i = 0; i < arr.length; i++) {
				sql += "?,";
			}
			sql += "?)}";
			cs = conn.prepareCall(sql);
			for (int i = 0; i < arr.length; i++) {
//				System.out.println(arr[i]);
				cs.setObject(i+1, arr[i]);//给存储过程传参
			}
			cs.registerOutParameter(arr.length+1, Types.VARCHAR);
			boolean hadResults = cs.execute();//执行存储过程
			int j = 0;
			while (hadResults) {
				String outer = "";
				if(j < out.length)
					outer = out[j];
				else
					break;
				rs = cs.getResultSet();//获取结果集
				if(outer.equals("total") || outer.equals("msg")) {
					while (rs != null && rs.next()) {
						map.put(outer, rs.getString(1));
					}
				} else {
					ResultSetMetaData rsmd = rs.getMetaData();//得到结果集(rs)的结构信息，如字段数、字段名等
					int columnCount = rsmd.getColumnCount();//返回结果集(rs)的字段数
					List<Map<String, String>> list = new ArrayList<Map<String,String>>();
					while(rs != null && rs.next()) {
						Map<String, String> m = new HashMap<String, String>();
						for (int i = 1; i <= columnCount; i++) {
							m.put(rsmd.getColumnLabel(i), rs.getString(i));
						}
						list.add(m);
					}
					JSONArray ja = JSONArray.fromObject(list);
					map.put(outer, ja);
				}
				j++;
				hadResults = cs.getMoreResults();
			}
			System.out.println("数据库查询成功,查的数据：" + map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, cs, rs);
		}
		return map;
	}
	
	
	public ArrayList tableColumn(String tableName) {
		String sql = "select * from " + tableName;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<Map<String, String>> list = new ArrayList<Map<String,String>>();
		try {
			conn = getConnection();
			pst = conn.prepareStatement(sql);
			ResultSetMetaData rsmd = pst.getMetaData();//返回表的结构信息，如字段名、字段数
			int columnCount = rsmd.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("columnName", rsmd.getColumnName(i+1));
				map.put("columnType", rsmd.getColumnTypeName(i+1));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return list;
	}
	
	/**
	 * 一个事务执行多个操作
	 * 在开始前设置conn.setAutoCommit(false)，结束后设置conn.commit()，如果发生异常一定要执行conn.rollback()
	 * @param map
	 */
	public void doDb(DbMap map) {
		Connection conn = null;
		String ret = "0";
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			String method = map.getMethod();
			if(method.equals("all")) {
				ret = doExecuteInsert(map.getData(), conn);
			} else {
				ret = doFor(map, conn);
			}
		} catch (SQLException e) {
			ret = "1";
			e.printStackTrace();
		} finally {
			try {
				map.put("ret", ret);
				//设置setAutoCommit(false)若没有在发生异常时中进行roolback操作，操作的表就会被锁住，造成数据库锁死
				//虽然在执行conn.close()的时候会释放锁，但若应用服务器使用了数据库连接池，连接不会被断开，从而不会放锁
				if(ret.equals("0"))
					conn.commit();
				else
					conn.rollback();
				if(conn != null)
					conn.close();
				System.out.println(map);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String doFor(DbMap map, Connection conn) {
		String ret = "0";
		String method = map.getMethod();
		if(method.equals("insert")) {
			ret = doExecuteInsert(map.getData(), conn);
		} else if(method.equals("update")) {
			
		}
		return ret;
	}
	
	public String doExecuteInsert(ArrayList<Map<String, String>> data, Connection conn) {
		String ret = "0";
		PreparedStatement pst = null;
		for (Map<String, String> map : data) {
			int num = 0;
			try {
				String tableName = map.get("tableName");
				ArrayList<Map<String, String>> tableColumn = tableColumn(tableName);
				String sql = "insert into " + tableName + "(";
				String values = " values(";
				Set<String> keySet = map.keySet();
				for (String key : keySet) {
					for (Map<String, String> m : tableColumn) {
						String columnName = m.get("columnName");
						if(key.equals(columnName)) {
							sql += key + ", ";
							values += "?, ";
						}
					}
				}
				sql = sql.substring(0, sql.length()-2) + ")";
				values = values.substring(0, values.length()-2) + ")";
				sql += values;
				pst = conn.prepareStatement(sql);
				int i = 1;
				for (String key : keySet) {
					for (Map<String, String> m : tableColumn) {
						String columnName = m.get("columnName");
						if(key.equals(columnName)) {
							String columnType = m.get("columnType").toLowerCase();
							String value = map.get(key);
							setPreparedPara(columnType, i, value, pst);
							i++;
						}
					}
				}
				num = pst.executeUpdate();
			} catch (Exception e) {
				ret = "1";
				e.printStackTrace();
				break;
			} finally {
				try {
					if(pst != null) {
						pst.close();
						pst = null;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	public void setPreparedPara(String columnType, int i, String value, PreparedStatement pst) 
			throws Exception {
		if(columnType.indexOf("varchar") != -1 || columnType.indexOf("char") != -1) {
			pst.setString(i, value);
		} else if(columnType.indexOf("bigint") != -1) {
			long v = Long.parseLong(value);
			pst.setLong(i, v);
		} else if(columnType.indexOf("int") != -1) {
			int v = Integer.parseInt(value);
			pst.setInt(i, v);
		} else if(columnType.indexOf("datetime") != -1) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(value);
			Timestamp v = new Timestamp(date.getTime());
			pst.setObject(i, v);
		}
	}
	
	public static void main(String[] args) throws Exception{
//		execute("");
		ParaForProcedure map = new ParaForProcedure();
		String[] para = {"","1","20","",""};
//		map.setPara(para);
		map.setProcedure("QueryTag");
		getTools().queryProcedure(map);
		
	}
}
