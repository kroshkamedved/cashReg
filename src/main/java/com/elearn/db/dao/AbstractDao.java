package com.elearn.db.dao;


import com.elearn.db.entity.AbstractEntity;
import com.elearn.db.utils.DBManager;
import com.elearn.db.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDao<E extends AbstractEntity, Integer> implements AbstractController<E, Integer> {

     Connection connection;

    public abstract String getSelectQuery();

    protected abstract String getIdSelectQuery();

    protected abstract List<E> parseList(ResultSet resultSet);

    @Override
    public List<E> getAll() {
        String sql = getSelectQuery();
        PreparedStatement statement = null;
        List<E> list = null;
        try {
            connection = DBManager.getInstance().getConnection();
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            list = parseList(resultSet);
        } catch (SQLException e) {
            System.err.println("SQL ecxeption: connection or statement ERROR");
            e.printStackTrace();
        } finally {
            JdbcUtils.closePreparedStatement(statement);
            JdbcUtils.closeConnection(connection);
        }


        return list;
    }

    @Override
    public E getEntityById(Integer id) {
        String sql = getIdSelectQuery();
        PreparedStatement statement = null;
        E entity = null;
        try {
            connection = JdbcUtils.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, (java.lang.Integer) id);
            ResultSet resultSet = statement.executeQuery();
            List<E> list = parseList(resultSet);
            try {

                entity = list.get(0);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("WORNG ID ");
            }
        } catch (SQLException e) {
            System.err.println("SQL ecxeption: connection or statement ERROR");
            e.printStackTrace();
        } finally {
            JdbcUtils.closePreparedStatement(statement);
            JdbcUtils.closeConnection(connection);
        }

        return entity;
    }

}
