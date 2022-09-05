package service.impl;

import service.SqlSession;

import java.util.List;

public class DefaultSqlSession implements SqlSession {
    @Override
    public <T> T selectOne(String statmentid, Object... param) {
        return null;
    }

    @Override
    public <T> List<T> selectList(String statmentid, Object... param) {
        return null;
    }

    @Override
    public void close() {

    }
}
