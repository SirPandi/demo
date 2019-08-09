package com.woniu.action.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.woniu.action.pojo.user;

public class UserDao {

	Connection conn;
	PreparedStatement stat;
	ResultSet rs;
	//更新方法
	public void update(String sql,Object[] objs) {
		try {
			conn=jdbcTolls.open();
			PreparedStatement stat=conn.prepareStatement(sql);
			for(int i=0;i<objs.length;i++) {
				stat.setObject(i+1, objs[i]);
			}
			stat.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
	}
	//增加方法
	public void useradd(user ur) {
		String sql="insert into user values(?,?,?)";
		Object[] objs= {ur.getUid(),ur.getUser(),ur.getUpsw()};
		update(sql, objs);
	}
	//删除方法
	public void userdelete(int uid) {
		String sql="delete from user where uid=?";
		Object[] objs= {uid};
		update(sql, objs);
	}
	//修改方法
	public void userupdate(user ur) {
		String sql="update user set user=?,upsw=? where uid=?";
		Object[] objs= {ur.getUser(),ur.getUpsw(),ur.getUid()};
		update(sql, objs);
	}
	//查询一个
	public user queryOne(int uid) {
		String sql="select*from user where uid=?";
		user ur=null;
		try {
			conn=jdbcTolls.open();
			stat=conn.prepareStatement(sql);
			stat.setInt(1, uid);
			rs=stat.executeQuery();
			if(rs.next()) {
				ur=new user(rs.getInt(1),rs.getString(2),rs.getString(3));   
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
		return ur;
	}
	//登陆
	public user queryuse(user usel) {
		String sql="select*from user where user=? and upsw=?";
		user ur=null;
		try {
			conn=jdbcTolls.open();
			stat=conn.prepareStatement(sql);
			stat.setString(1, usel.getUser());
			stat.setString(2, usel.getUpsw());
			rs=stat.executeQuery();
			if(rs.next()) {
				ur=new user(rs.getInt(1),rs.getString(2),rs.getString(3));   
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
		return ur;
	}
	//查询全部
	public List<user> queryAll() {
		String sql="select*from user";
		List<user> urs=new ArrayList<user>();
		try {
			conn=jdbcTolls.open();
			stat=conn.prepareStatement(sql);
			rs=stat.executeQuery();
			while(rs.next()) {
				user ur=new user(rs.getInt(1),rs.getString(2),rs.getString(3));  
				urs.add(ur);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
		return urs;
	}

	public List<user> pageall(int pageNum,int pageRow) {
		String sql="select*from user limit ?,?";
		List<user> urs=new ArrayList<user>();
		try {
			conn=jdbcTolls.open();
			stat=conn.prepareStatement(sql);
			stat.setInt(1, (pageNum-1)*pageRow);
			stat.setInt(2, pageRow);
			rs=stat.executeQuery();
			while(rs.next()) {
				user ur=new user(rs.getInt(1),rs.getString(2),rs.getString(3));  
				urs.add(ur);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
		return urs;
	}
	
	public List<user> select(String temp, String temp2) {
		String sql="select * from user where 1=1 ";
		List parms=new ArrayList<>();
		if(temp!=null&&!temp.equals("")) {
			sql+="and user like ?";
			parms.add("%"+temp+"%");
		}
		if(temp2!=null&&!temp2.equals("")) {
			sql+="and upsw like ?";
			parms.add("%"+temp2+"%");
		}
		List<user> urs=new ArrayList<user>();
		try {
			conn=jdbcTolls.open();
			stat=conn.prepareStatement(sql);
			for(int i=0;i<parms.size();i++) {
				stat.setObject(i+1, parms.get(i));
			}
			rs=stat.executeQuery();
			while(rs.next()) {
				user ur=new user(rs.getInt(1),rs.getString(2),rs.getString(3));  
				urs.add(ur);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}finally {
			jdbcTolls.close(rs, stat, conn);
		}
		return urs;
	}
	public boolean checkUname(String uname) {
			boolean flag = true;
			String sql = "select * from user where user=?";
			try {
				conn=jdbcTolls.open();
				stat = conn.prepareStatement(sql);
				stat.setString(1,uname);
				rs = stat.executeQuery();
				if(rs.next()){
					flag = false;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				jdbcTolls.close(rs, stat, conn);
			}
			return flag;
	}
	
	
	
	
}	
	

