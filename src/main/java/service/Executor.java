package service;

import configuration.Configuration;
import configuration.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement,Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException;

    void close();

}
