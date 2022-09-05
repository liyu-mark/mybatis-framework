package service.impl;

import configuration.Configuration;
import configuration.MappedStatement;
import service.Executor;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor {
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws SQLException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        //获取连接器
        Connection connection = configuration.getDataSources().getConnection();
        String id = mappedStatement.getId();
        Class<?> paramterType = mappedStatement.getParamterType();
        Class<?> resultType = mappedStatement.getResultType();
        //select * from user where id = #{id} and username = #{username}
        String sql = mappedStatement.getSql();
        //处理，形成预编译格式
        String prepareText = getPrepareText(sql);
        List<String> prepareList = getPrepareList(sql);
        //预处理及赋值
        PreparedStatement preparedStatement = connection.prepareStatement(prepareText);

        for (int i = 0; i < prepareList.size(); i++) {
            //强制访问
            String field_name = prepareList.get(i);
            Field declaredField = paramterType.getDeclaredField(field_name);
            declaredField.setAccessible(true);
            Object object_val = declaredField.get(field_name);

            preparedStatement.setObject(i+1,object_val);
        }

        ResultSet resultSet = preparedStatement.executeQuery();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        ArrayList<E> ans = new ArrayList<>();
        while (resultSet.next()){
            E instance = (E)resultType.newInstance();
            for (int i = 0; i < count; i++) {
                String columnName = metaData.getColumnName(i+1);
                Object object = resultSet.getObject(columnName);

                Field declaredField = resultType.getDeclaredField(columnName);
                declaredField.setAccessible(true);
                Object o = declaredField.get(columnName);


            }


        }


        return null;
    }

    @Override
    public void close() {

    }

    public String getPrepareText(String sql){
        String[] strings = sql.split(" ");
        for (int i = 0;i < strings.length; i++) {
            String val = strings[i];
            if (val.startsWith("#{") && val.endsWith("}")){
                strings[i] = "?";
            }
        }
        return String.join(" ",strings);
    }

    public List<String> getPrepareList(String sql){
        ArrayList<String> list = new ArrayList<>();
        String[] strings = sql.split(" ");
        for (int i = 0;i < strings.length; i++) {
            String val = strings[i];
            if (val.startsWith("#{") && val.endsWith("}")){
                list.add(val.substring(2,val.length()-1));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        SimpleExecutor main = new SimpleExecutor();
//        System.out.println(main.getPrepareText("select * from user where id = #{id} and username = #{username}"));
        List<String> prepareList = main.getPrepareList("select * from user where id = #{id} and username = #{username}");
        for (String val : prepareList) {
            System.out.println(val);
        }
    }
}
