
# Hibernate API介绍
## Configuration类

|方法  |作用  |
| :------------ | :------------ |
|configure()	 |加载src下名为Hibernate.cfg.xml配置文件  |
|configure(String resource)|加载src下制定名称的核心配置文件   |
|configure(String resource)   |导入一个指定位置的映射文件   |
|addClass(Class clazz)	 |导入与指定类同一个包中的以类名为前缀，后缀为.hbm.xml的映射文件   |
|buildSessionFactory()|创建一个SessionFactory工厂|
## SessionFactory 接口
|方法  |作用  |
| :------------ | :------------ |
| openSession()  | 得到一个Session对象，这个对象事务结束后要手动关闭  |
| getCurrentSession()  |得到一个与线程绑定的Session对象,这个对象事务结束后自动关闭   |
## Session 接口
|方法  |作用  |
| :------------ | :------------ |
| save(Object)  |保存实体类到数据库   |
| update(Object)  |更新实体类到数据库   |
|delete(Object)   | 删除实体类到数据库  |
| get(),load() |  根据主键查询 |
| createQuery(),createSQLQuery()|用于数据库操作对象   |
| createCriteria() |条件查询   |
|beginTransaction()|开启事务，返回事务对象|
## Transaction 接口
|方法  |作用  |
| :------------ | :------------ |
|commit()  |事务的提交  |
|rollback()  |事务的回滚  |
|delete(Object)   | 删除实体类到数据库  |
| get(),load() |  根据主键查询 |
| createQuery(),createSQLQuery()|用于数据库操作对象   |
| createCriteria() |条件查询   |
## 实体类编写原则
- 实体类里面的属性是私有的
- 是有属性使用公开的set和get方法操作
- 要求实体类有属性作为唯一值（一般使用ID值）
- 实体类属性建议不使用基本数据类型,使用基本数据类型的对应的包装类

## 实体类操作方法介绍
- 添加操作

	 调用session里面的 ***session.save(要保存的对象)*** 方法就可以实现
- 根据ID查询得到对象

	 调用 session里面的get方法:***session.get（实体类.class,ID值）***
- 修改操作

	 1. 先用session中的get方法根据ID得到对象
	 1. 再向session中设置要修改的值
	 1. 调用session中的 ***session.update(要修改的对象)*** 方法进行修改
 
	 **执行过程：** 到对象中找到主键ID的值,根据ID值进行修改
 
- 删除操作

 	先用session中的get方法根据ID得到对象,然后执行 ***session.delete(要删除的对象)*** 方法进行删除
	
## 实体类对象状态概念
- 瞬时态：对象里面没有ID值，对象与Session没有关联（即新创建的对象，没有设置任何值）

- 持久态：对象里面有ID值，对象与Session关联

	    User user = session.get(User.class,1)

- 托管态:对象有ID值,对象与Session没有关联
	```java
	User user = new User();
	user.setUid(3);


	```	
## Hibernate的缓存

### 有什么作用？

 Hibernate框架中提供了很多优化的方式,Hibernate缓存就是一种优化方式

### Hibernate中的一级缓存是什么？

Hibernate的一级缓存就是指Session缓存,Session缓存是一块内存空间，用来存放相互管理的Java对象,在使用hibernate查询对象的时候，首先会使用对象属性的OID值在Hibernate的一级缓存中进行查找,如果找到匹配OID值的对象，就直接从一级缓存找到相应数据.当从数据库查询到所需数据时,该信息也会直接放到一级缓存中.**Hibernate的一级缓存的作用就是减少对数据库的访问次数**

#### Hibernate的一级缓存的特点

- hibernate的一级缓存默认是打开的
- hibernate的一级缓存使用范围是从session创建到session关闭范围
- hibernate的一级缓存中,存储数据必须是持久态数据
- 当应用程序调用session接口中的save,update,saveOrUpdata时,如果session缓存中没有相应的对象,Hibernate就会自动的把数据库中查询到的相应对象信息加入到一级缓存中
- 当调用session接口的load或者get方法,以及query接口中的list，iterator方法时,会判断缓存中是否存在该对象，有则返回，不会查询数据库，如果缓存中没有要查询对象,再去数据库中查询对应对象,并添加到一级缓存中.

![hibernate一级缓存](http://ae01.alicdn.com/kf/HTB1Lk22XjzuK1RjSspeq6ziHVXad.jpg "hibernate一级缓存")

#### 快照机制

>Hibernate向一级缓存放入数据时,同时复制一份数据放入到Hibernate快照中，当使用commit（）方法提交事务时，同时会清理Session的一级缓存,这时会使用OID判断一级缓存中的对象和快照中的对象是否一致，如果对象中的属性发生变化,则执行update语句,将缓存的内容同步到数据库,并更新快照；如果一致，则不执行update语句.Hibernate快照的作用就是确保一级缓存中的数据和数据库中的数据一致

![快照](http://thyrsi.com/t6/378/1538374811x-1566661157.jpg "快照")

