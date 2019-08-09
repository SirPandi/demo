package com.woniu.action.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

public class DispatcherServlet extends HttpServlet {

	Map<String,Object> mapping = new HashMap<String,Object>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//获取客户发送的请求信息 /dept.action
		String reqUrl = req.getServletPath();
		//通过请求信息从map中获取对应的对象
		Object obj = mapping.get(reqUrl);
		//获取表单提交的信息
		Map<String, String[]> params = req.getParameterMap();
		//默认声明请求的方法
		String reqMethod = "execute";
		if(req.getParameter("method")!=null){
			reqMethod = req.getParameter("method");
		}
		
		if(obj!=null){
			try {
				//获得该对象中的Method对象  DeptAction的Class对象
				Class c = obj.getClass();
				//获得DeptAction所有的方法
				Method[] ms = c.getDeclaredMethods();
				for(Method m:ms){
					//找到执行的方法
					if(m.getName().equals(reqMethod)){
						Class[] cs = m.getParameterTypes();
						//声明方法调用传递参数的数组
						Object[] paramsVal = new Object[cs.length];
						//将方法的参数类型获取
						
						for (int i = 0; i < cs.length; i++) {
							// 只获得第一个参数类型 Dept.class
							Class type = cs[i];

							if (type == HttpServletRequest.class) {
								paramsVal[i] = req;
							} else if (type == HttpSession.class) {
								paramsVal[i] = req.getSession();
							} else {
								// Dept dept = new Dept();
								paramsVal[i] = type.newInstance();
								// 将map的值封装到方法指定参数里
								BeanUtils.populate(paramsVal[i], params);
							}
							}
							//运行Method对象
							Object result = m.invoke(obj,paramsVal);
						String str = result.toString();
						String[] strArr = str.split(":");
						if (strArr[0].equals("redirect")) {
							resp.sendRedirect(strArr[1]);
						} else if (strArr[0].equals("forward")) {
							req.getRequestDispatcher(strArr[1]).forward(req, resp);
						}
						
						
					}
				}
				
				
			}  catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("自定义框架初始化开始");
		URL url = DispatcherServlet.class.getResource("/");
		try {
			File dir = new File(url.toURI());
			String pack = "com.woniu.action";
			//com\\woniu\\action
			String packDir = pack.replace(".","\\");
			File target = new File(dir,packDir);
			//获得action包下的所有类
			File[] classes = target.listFiles();
			for(File clazz:classes){
				//DeptAction.class
				String fileName = clazz.getName();
				//获取类名
				String className = fileName.replace(".class", "");
				//生成Class的实例
				Class c = Class.forName(pack+"."+className);
				//获得注解中请求信息
				Action action = (Action) c.getDeclaredAnnotation(Action.class);
				String reqUrl = action.url();
				//获得当前类的实例
				Object obj = c.newInstance();
				mapping.put(reqUrl, obj);
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("自定义框架初始化结束");
	}
	
	

}
