## 写一个简单的DEMO来进一步认识一下Hibernate
###搭建Hibernate环境
我这里使用的是Hibernate 5.0.X的版本，最新版本->[Hibernate官网下载](http://hibernate.org/orm/)

主要使用的jar包如下:
- antlr-2.7.7.jar
>Another Tool for Language Recognition 可以构造语言识别器，为解析HQL(hibernate Query language)时使用
- dom4j-1.6.1.jar
>解析xml文件所用
- hibernate-commons-annotations-5.0.1.Final.jar
>加入注解解析
- hibernate-core-5.0.12.Final.jar
> hibernate的核心Jar包
- hibernate-entitymanager-5.0.12.Final.jar
> 是围绕提供JPA编程接口的Hibernate Core的一个包装，支持JPA实体实例的生命周期，并允许你用标准的Java Persistence查询语言编写查询。
- hibernate-jpa-2.1-api-1.0.0.Final.jar
> 使用hibernate所依赖的jar包，jpa是一种规范，而hibernate是它的一种实现
- javassist-3.18.1-GA.jar
> java字节编码器
- jandex-2.0.0.Final.jar
> 通用的 XPath 处理引擎
- mysql-connector-java-5.1.28-bin.jar
> 连接mysql数据的jar包
- log4j-1.2.17.jar
>log4j 库,Apache 的日志工具
- slf4j.api-1.6.1.jar
> 整合其它日志的规范接口，也就是如果要将其它日志的jar包整合进来就要符合该规范
- slf4j-log4j12-1.7.5.jar
> 用来整合log4j和规范接口，让log4j符合规范，这样才能使用

###创建实体类
```java
public class User {
	/*
	 * hibernate要求实体类有一个属性唯一的
	 */
	private int uid;
	private String username;
	private String password;
	private String address;
    //...getters/setters/toString
}
```
###创建实体类与数据表对应的XML配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<!-- class标签   name属性：实体类全路径,table属性：数据库表名称 -->
	<class name="cn.zcmu.entity.User" table="t_user">
		<!-- 2.配置实体类id和表id对应 hibernate要求实体类有一个属性唯一的值
         hibernate要求表有字段作为唯一的值 -->
    	<!-- id标签 name属性，实体类里面id属性名称 
		           column属性：生成的表字段名称 -->
		<id name="uid" column="uid">
	    <!-- 设置数据表id增长策略  native：生成表id值就是主键自动增长  -->
			<generator class="native"/>
		</id>
	    <!-- 配置其他属性和表字段对应 name属性：实体类属性名称 
								column属性：生成表字段名称-->
		<property name="username" column="username"/>
		<property name="password" column="password"/>
		<property name="address" column="address"/>
	</class>
</hibernate-mapping>
```
**命名规范:** 要写成XXX.hbm.xml
**主键生产策略：**
1、increment：主键自动增长、由hibernate来管理 (注意：如果数据库也设置了自动增长，就会发生主键冲突问题)

2、identity：由底层数据库来管理生成，不由hibernate管理也就是说底层数据库怎么设置的主键就怎么来 (注意：mysql、sql server可以，oracle不可以)

3、sequence：标识符生成器，就是底层数据库来管理生成，利用底层数据库提供的序列来生成标识符，不由hibernate管理(注意：mysql不支持序列  oracle支持)

4、native：由底层数据库自己来决定使用什么策略，hibernate不管(注意：mysql自动选择identity、oracle自动选择sequence)

5、uuid：随机生成32位不相同的字符串。

### 创建hibernate.cfg.xml
有了xxx.hbm.xml这个映射文件还不够，因为hibernate需要连接数据库，那么这些操作放哪里呢，就提取出一个公用的配置文件出来，hibernate.cfg.xml就是这种公共的配置文件，加载数据库连接的信息，和将各种映射文件加载进来，其实就是抽取出来的，因为有很多映射文件，每个映射文件都需要连接数据库等操作，那么久将共同的操作提取出来形成了hibernate.cfg.xml。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC  
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"  
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">  
    <hibernate-configuration>
<session-factory>
	<!-- 第一部分：配置数据库信息   必须要有-->
	<property name="hibernate.connection.driver_class">
		com.mysql.jdbc.Driver
	</property>
	<property name="hibernate.connection.url">
		jdbc:mysql://localhost:3306/hibernate
	</property>
	<property name="hibernate.connection.username">root</property>
	<property name="hibernate.connection.password">root</property>

	<!-- 第二部分：配置hibernate信息  可选的-->
	<!-- 输出底层的sql语句 -->
	<property name="hibernate.show_sql">true</property>
	<!-- 输出底层的sql语句格式化-->
	<property name="hibernate.format_sql">true</property>
	<!-- hibernate帮创建表，需要配置之后 update：如果已经有表，更新，如果没有，创建-->
	<property name="hibernate.hbm2ddl.auto">update</property>
	<!-- 配置数据库方言 在mysql里面实现分页  关键字limit，只能使用mysql里面
	    在oracle数据库，实现分页rownum
		让hibernate框架识别不同数据库的自己特有的语句
	-->
	<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property> -->
	<property name="hibernate.current_session_context_class">thread</property>

	<!-- 第三部分：把映射文件放到核心配置文件中  必须的-->
	<property name="dialect"></property>
	<mapping resource="cn/zcmu/entity/User.hbm.xml" />
</session-factory>
</hibernate-configuration>
```
**要求:** 1.必须在src下面 2.必须名为hibernate.cfg.xml
### 创建测试Test
```java
@Test
	public void test1(){
		//第一步:加载核心配置文件,即在src下面找到名称为Hibernate.cfg.xml文件
		Configuration cfg = new Configuration().configure();
		//第二步:创建SessionFactory创建session对象,读取核心配置文件创建sessionFactory对象, 在过程中，根据映射关系，在配置数据库里面把表创建
		SessionFactory sessionFactory = cfg.buildSessionFactory();
		//第三步:使用SessionFactory创建session对象
		Session session = sessionFactory.openSession();
		//第四步:开启事务
		Transaction ts = session.beginTransaction();
		//第五步:操作
		User user = new User();
		user.setUsername("张三");
		user.setPassword("123456");
		user.setAddress("zcmu");
		//调用session的方法实现添加
		session.save(user);
		//第六步:提交事务
		ts.commit();
		//第七步：关闭资源
		session.close();
		sessionFactory.close();
	}
```
##### 输出结果
```
log4j:WARN No appenders could be found for logger (org.jboss.logging).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Hibernate: 
    insert 
    into
        t_user
        (username, password, address) 
    values
        (?, ?, ?)

```
- 在数据库中会成功添加一条数据,同时之前在数据库中没有创建表,但是hibernate会自动帮我生成一张表,是因为之前核心配置文件hibernate.cfg.xml中设置名字为hibernate.hbm2ddl.auto的属性。
- 实现对数据库的操作除了上面demo的第五个步骤代码不一样，其他代码都是一样的
