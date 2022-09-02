package jdbc;

import com.mysql.jdbc.Driver;

import java.sql.*;

public class Jdbc_Connect {

    /**
     * 缺点：
     * 1、频繁的建立与释放连接
     * 2、数据库连接信息存在硬编码问题
     * 3、sql语句存在硬编码问题
     * 4、解析结果存在硬编码问题 不灵活
     * @param args
     */
    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        User user_pojo;
        try {
            //注册驱动
            DriverManager.registerDriver(new Driver());
            String url = "jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8";
            String user = "root";
            String password = "123456";
            //获取连接
            connection = DriverManager.getConnection(url, user, password);
            //获取查询语句
            //如果使用prepareStatement预编译就不会了（多次查询效率高），因为SQL语句在程序运行前已经进行了预编译，
            //在程序运行时第一次操作数据库之前，SQL语句已经被数据库分析和编译，对应的执行计划也会缓存下来，之后数据库就会以参数化的形式进行查询。set值永远是把占位符当成data处理。
            prepareStatement = connection.prepareStatement("select * from user where username = ?");
            prepareStatement.setString(1,"tom");
            //解析反参
            resultSet = prepareStatement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");

                user_pojo = new User();
                user_pojo.setId(id);
                user_pojo.setUsername(username);

                System.out.println(user_pojo.toString());
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if (resultSet!=null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (prepareStatement!=null){
                try {
                    prepareStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    static class User{

        int id;
        String username;

        public User() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    '}';
        }
    }
}

