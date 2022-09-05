package service;

import java.util.List;

/**
 * 该接口 主要进行curd操作
 */
public interface SqlSession {

    <T> T selectOne(String statmentid,Object... param);

    <T> List<T> selectList(String statmentid,Object... param);

    void close();

}
