## Hiberate的事务操作

### 简单提一下事务的一些概念

#### 事务的特性（个人理解）
- 原子性：要么都成功，要么都失败
- 一致性：在事务的开始和事务结束之后,数据库的完整性约束没有被破坏，例如转账业务,无论事务执行是否成功与否,参与转账的两个账户的余额之后应该是不会变的
- 隔离性：隔离状态执行事务,使他们好像是系统在给定时间内执行的唯一操作.如果有两个事务，运行在相同的时间内,执行相同的功能,事物的隔离性将确保每一事务在系统中认为只有该事务在使用系统。这种属性有时称为串行化，为了防止事务操作间的混淆，必须串行化或序列化请求，使得在同一时间仅有一个请求用于同一数据。
- 持久性：在事务完成以后，该事务对所有数据库的更改操作就会保存在数据库中，并不会被回滚

#### 不考虑隔离性会出现的问题
- 脏读：读到另一个事务的未提交更新数据,即读到脏数据
- 不可重复读：对同一记录的两次读取不一致,因为另一事务对该记录做了修改
- 幻读：对同一张表的两次查询的结果不一致，因为添加了一条数据

#### 四大隔离级别
|  隔离级别 |脏读   |不可重复读   |幻读   |
| ------------ | ------------ | ------------ | ------------ |
| READ UNCOMMINTED(读未提交)  |√   |√   |√   |
| READ COMMINTED(读已提交)（ORACLE默认隔离级别）  | ×   |√    | √   |
| REPEATABLE READ(可重复读)（mysql默认隔离级别）  |  ×  | ×   |√   |
| SERIALIZABLE(串行读)  |  ×  |   × |  ×  |
#### 配置Hibernate的事务隔离级别
在hibernate核心配置文件中添加
```xml
<!--1：读未提交，2：读已提交，4：可重复读，8：串行读-->
<property name="hibernate.connection.isolation">4</property>
```


## 配置与线程绑定的SESSION

### 为什么要配置与线程绑定的SESSION?

>当我们真正进行对事务的管理的时候,需要考虑事务的应用的场景,也就是说事务的控制要放在service层实现,并且service中调用的多个dao实现一个业务逻辑。 ***最主要*** 如何保证service中开启的事务时使用的session对象和dao中多个操作使用的是同一个session对象？
 

**有两种办法可以实现:**


 - 可以在业务层获取到session,并将session作为参数传递给dao
 - 可以使用ThreadLocal将业务层获取的session绑定到当前线程中,然后在dao中获取session的时候,都从当前线程中获取
 
 >其实使用第二种方式肯定是最优方案,那么具体的实现已经不用我们自己来完成了.hibernate的内部已经将这个事情做完了。我们只需要进行配置即可

#### 第一步：修改核心配置文件
```xml
<!-- thread:Session对象的生命周期与本地线程绑定-->
<!-- jta:session对象的生命周期与JTA事务绑定-->
<!-- managed:Hibernate委托程序来管理session对象的生命周期-->
<!-- 在hibernate.cfg.xml中添加一条配置,如下 -->
<property name="hibernate.current_session_context_class">thread</property>
```

#### 第二步：在调用session的时候使用工厂中的getCurrentSession()即可获取与线程绑定的session
```java
	Configuration configuration = new Configuration().configure();
	SessionFactory sessionFactory = configuration.buildSessionFactory();
	Session session = sessionFactory.getCurrentSession();
```
