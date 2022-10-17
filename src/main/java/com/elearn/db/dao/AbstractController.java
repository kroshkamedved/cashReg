package com.elearn.db.dao;




import com.elearn.db.entity.AbstractEntity;

import java.sql.SQLException;
import java.util.List;

public interface AbstractController<E extends AbstractEntity,ID> {

    List<E> getAll()throws SQLException;
    E getEntityById(ID id)throws SQLException;
    boolean insertEntity(E entity)throws SQLException;
    boolean deleteEntity(E entity)throws SQLException;
    boolean updateEntity(E entity)throws SQLException;
}
