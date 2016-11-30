package com.session.dgjx.db;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.session.dgjx.AppInstance;
import com.session.dgjx.enity.MyMessage;

public class MyMessageDao
{
	private Dao<MyMessage, Long> dao;
	private DatabaseHelper helper;
	
	public MyMessageDao() throws SQLException 
	{
		helper = DatabaseHelper.getInstance(AppInstance.getInstance());
		dao = helper.getDao(MyMessage.class);
	}
	
	public void createOrUpdate(MyMessage msg) throws SQLException
	{
		dao.createOrUpdate(msg);
	}
	
	public int delete(long id) throws SQLException
	{
		return dao.deleteById(id);
	}
	
	public List<MyMessage> getMyMessages(String account) throws SQLException
	{
		QueryBuilder<MyMessage, Long> queryBuilder = dao.queryBuilder();
		queryBuilder.where().eq(MyMessage.ACCOUNT, account);
		queryBuilder.orderBy(MyMessage.SEND_TIME, false);
		return queryBuilder.query();
	}
	
	/**
	 * 获取消息数量
	 * @author YJ Liang
	 * 2016  上午10:14:09
	 * @param account，消息所属用户
	 * @param readed 是否已读，true表示已读，false表示未读
	 * @param msgTypes 消息类型，如果msgTypes为null或空，则获取全部类型
	 * @return
	 * @throws SQLException 
	 */
	public long count(String account,boolean readed, List<String> msgTypes) throws SQLException
	{
		QueryBuilder<MyMessage, Long> queryBuilder = dao.queryBuilder();
		queryBuilder.setCountOf(true);
		Where<MyMessage,Long> where = queryBuilder.where().eq(MyMessage.ACCOUNT, account).and().eq(MyMessage.READED, readed);
		if(msgTypes != null && !msgTypes.isEmpty())
		{
			where.and().in(MyMessage.MSG_TYPE, msgTypes);
		}
		PreparedQuery<MyMessage> preparedQuery = queryBuilder.prepare();
		return dao.countOf(preparedQuery);
	}
}
