package com.session.dgjp.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.session.dgjp.AppInstance;
import com.session.dgjp.enity.Init;

import java.sql.SQLException;

/**
 * Created by user on 2016-11-21.
 */
public class InitDao {
    private Dao<Init, Integer> dao;
    private DatabaseHelper helper;

    public InitDao() throws SQLException {
        helper = DatabaseHelper.getInstance(AppInstance.getInstance());
        dao = helper.getDao(Init.class);
    }

    public void createOrUpdate(Init msg) throws SQLException {
        dao.createOrUpdate(msg);
    }

    public int delete(Integer id) throws SQLException {
        return dao.deleteById(id);
    }

    public String getInitContent() throws SQLException {
        QueryBuilder<Init, Integer> queryBuilder = dao.queryBuilder();
//        queryBuilder.where().eq(MyMessage.ACCOUNT, account);
//        queryBuilder.orderBy(MyMessage.SEND_TIME, false);
//        return queryBuilder.query();
        return null;
    }

}
