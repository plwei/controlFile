package com.easyui.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;

public class GetMethod {
	
	private static Gson gson;
	
	public static Gson getGson() {
		if(gson == null)
			gson = new Gson();
		return gson;
	}
	
	public static String getId() {
		String sql1 = "replace into tickets64 (stub) values ('a')";
		JDBCTools.getTools().execute(sql1);
		String sql2 = "select * from tickets64";
		List<Map<String, String>> list = JDBCTools.getTools().query(sql2);
		String id = list.get(0).get("id");
		return id;
	}
	
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date());
		return date;
	}
	
	/**
	 * 通过xml中的配置信息找到对应的类和方法信息
	 * 利用反射机制通过字符串调用对应的方法
	 * @param para
	 * @return
	 */
	public static Map doInvoke(Map<String, Object> para) {
		Map map = null;
		try {
			//由于是静态方法，所以使用内部类获取路径
			String filePath = new Object() {
				public String getPath() throws URISyntaxException {
					return this.getClass().getClassLoader().getResource("").toURI().getPath();
				}
			}.getPath();
			File file = new File(filePath + "beanFactory.xml");
			//创建saxReader对象
			SAXReader reader = new SAXReader();
			// 通过read方法读取xml文件。 转换成Document对象
			Document document = reader.read(file);
			//获取根元素
			Element root = document.getRootElement();
			//遍历根节点
			List<Element> list = root.elements();
			outterLoop:for (Element e : list) {
				for(Iterator it = e.elementIterator(); it.hasNext();) {
					Element methodElement = (Element) it.next();
					String id = methodElement.attributeValue("id");
					if(id.equals((String)para.get("method"))) {
						//获取方法名称
						String methodName = methodElement.attributeValue("name");
						//获取类元素
						Element classElement = methodElement.getParent();
						//获取类路径
						String className = classElement.attributeValue("name");
						//根据类路径转成得到类
						Class cls = Class.forName(className);
						//获取类对象
						Object obj = cls.newInstance();
						//获取方法
						Method method = cls.getDeclaredMethod(methodName, Map.class);
						//执行方法
						map = (Map) method.invoke(obj, para);
						break outterLoop;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
