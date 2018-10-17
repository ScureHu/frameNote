# Hibernate
![](http://hibernate.org/images/hibernate-logo.svg)
### 什么是Hibernate?
1.在持久层使用的框架
2.在持久层对数据库进行CRUD操作的，hibernate对JDBC进行了封装，使用hibernate的好处,就不需要重复写复杂的JDBC的代码了,不需要写sql语句实现，是一个完全的ORM框架。
### 什么是ORM思想？
1.ORM(Object relational mapping) 对象关系映射
2.让实体类和数据库表进行 一 一 对应关系（实体类属性和表里面字段对应）
3.不需要直接操作数据库表，而操作表对应实体类对象
###不得不提的JPA，什么又是JPA呢？
之前在学习JDBC的时候,java提供了一系列的JDBC规范,接口。从而各数据库厂商从而来实现这个接口来实现java对自己家数据库连接的支持。
##### JPA简介
>JPA是Java Persistence API的简称，中文名Java持久层API，是JDK 5.0注解或XML描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。Sun引入新的JPA ORM规范出于两个原因：其一，简化现有Java EE和Java SE应用开发工作；其二，Sun希望整合ORM技术，实现天下归一

##### 简单来说JPA
我们可以把它想象成跟JDBC一样的，java提供给各种持久化层框架实体对象持久化到数据库中的一些规范和接口等。
### Hibernate的框架体系
![](https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1913819471,1519777660&fm=26&gp=0.jpg)
## 进一步认识Hibernate
#### ---->[写一个简单的DEMO来进一步认识一下Hibernate](https://github.com/ScureHu/hibernate/blob/master/hibernate/Demo.md)
#### ---->[Hibernate API介绍和缓存机制](https://github.com/ScureHu/hibernate/blob/master/hibernate/API.md)
#### ---->[Hibernate的事务操作与session与线程的绑定](https://github.com/ScureHu/hibernate/blob/master/hibernate/Transaction.md)
#### ---->[Hibernate的多表关系和映射文件配置](https://github.com/ScureHu/hibernate/blob/master/hibernate/table.md)
#### ---->[Hibernate的多表级联操作](https://github.com/ScureHu/hibernate/blob/master/hibernate/tableRelation.md)
