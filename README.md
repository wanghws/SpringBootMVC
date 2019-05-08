# Spring Boot 2.0 MVC 前后端分离 JDK1.8

## 主要组件

1.	Spring Boot 2.0
2.	Spring Security
3.  Spring Cache
4.  Redisson
5.  Druid
6.  Mybatis-plus
7.  Dynamic-Datasource
8.  Lombok
9.  Logback
10. Swagger-bootstrap-ui
11. JWT
12. OKHttp
13. Jackson


初始管理员账号：admin   密码：123456

## 数据库初始化

mysql> create database demo;

mysql> create user 'work'@'%' identified by '123456';

mysql> grant all privileges ON demo.* to 'work'@'%';

mysql> use demo;

mysql> source framework.sql;