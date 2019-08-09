package com.woniu.action.action;

import com.woniu.action.core.Action;
import com.woniu.action.dao.UserDao;
import com.woniu.action.pojo.user;

@Action(url = "/dept.action")
public class DeptAction {

	UserDao ud=new UserDao();
	public String excute(user ur) {
		ud.useradd(ur);
		return "forward:index.jsp";
	}
	
	public String save(user ur) {
		ud.useradd(ur);
		return "redirect:dept.action?method=getAll";
	}
	
	public String delete(user ur) {
		ud.userdelete(ur.getUid());
		return"redirect:dept.action?method=getAll";
	}
}
