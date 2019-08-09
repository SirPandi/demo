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
		//��ȡ�ͻ����͵�������Ϣ /dept.action
		String reqUrl = req.getServletPath();
		//ͨ��������Ϣ��map�л�ȡ��Ӧ�Ķ���
		Object obj = mapping.get(reqUrl);
		//��ȡ���ύ����Ϣ
		Map<String, String[]> params = req.getParameterMap();
		//Ĭ����������ķ���
		String reqMethod = "execute";
		if(req.getParameter("method")!=null){
			reqMethod = req.getParameter("method");
		}
		
		if(obj!=null){
			try {
				//��øö����е�Method����  DeptAction��Class����
				Class c = obj.getClass();
				//���DeptAction���еķ���
				Method[] ms = c.getDeclaredMethods();
				for(Method m:ms){
					//�ҵ�ִ�еķ���
					if(m.getName().equals(reqMethod)){
						Class[] cs = m.getParameterTypes();
						//�����������ô��ݲ���������
						Object[] paramsVal = new Object[cs.length];
						//�������Ĳ������ͻ�ȡ
						
						for (int i = 0; i < cs.length; i++) {
							// ֻ��õ�һ���������� Dept.class
							Class type = cs[i];

							if (type == HttpServletRequest.class) {
								paramsVal[i] = req;
							} else if (type == HttpSession.class) {
								paramsVal[i] = req.getSession();
							} else {
								// Dept dept = new Dept();
								paramsVal[i] = type.newInstance();
								// ��map��ֵ��װ������ָ��������
								BeanUtils.populate(paramsVal[i], params);
							}
							}
							//����Method����
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
		System.out.println("�Զ����ܳ�ʼ����ʼ");
		URL url = DispatcherServlet.class.getResource("/");
		try {
			File dir = new File(url.toURI());
			String pack = "com.woniu.action";
			//com\\woniu\\action
			String packDir = pack.replace(".","\\");
			File target = new File(dir,packDir);
			//���action���µ�������
			File[] classes = target.listFiles();
			for(File clazz:classes){
				//DeptAction.class
				String fileName = clazz.getName();
				//��ȡ����
				String className = fileName.replace(".class", "");
				//����Class��ʵ��
				Class c = Class.forName(pack+"."+className);
				//���ע����������Ϣ
				Action action = (Action) c.getDeclaredAnnotation(Action.class);
				String reqUrl = action.url();
				//��õ�ǰ���ʵ��
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
		System.out.println("�Զ����ܳ�ʼ������");
	}
	
	

}
