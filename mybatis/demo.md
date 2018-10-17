## 简单的入门DEMO

### 导入jar包
- asm-3.3.1.jar
- cglib-2.2.2.jar
- commons-logging-1.1.1.jar
- javassist-3.17.1-GA.jar
- log4j-1.2.17.jar
- slf4j-api-1.7.5.jar
- slf4j-log4j12-1.7.5.jar
- mybatis-3.2.2.jar						核心包
- mysql-connector-java-5.1.28-bin.jar	数据库驱动

### 创建核心配置文件SqlMapConfig.xml

> 在配置文件中我把数据库连接的数据放到了db.properties中，请自行创建

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
	<!-- 加载属性文件 -->
	<properties resource="db.properties"></properties>
	<!-- 在spring整合后environments配置将废除 -->
	<environments default="development">
		<environment id="development">
			<!-- 使用jdbc管理事务 -->
			<transactionManager type="JDBC"/>
			<!-- 数据库连接池 -->
			<dataSource type="POOLED">
				<property name="driver" value="${jdbc.driver}"/>
				<property name="url" value="${jdbc.url}"/>
				<property name="username" value="${jdbc.username}"/>
				<property name="password" value="${jdbc.password}"/>
			</dataSource>
		</environment>
	</environments>
</configuration>
```

### 创建pojo对象

> 创建pojo对象的时候,我们的属性名，先和表名一致,这样子我们操作起来会比较方便,当然我们也可以不相同,后续文章会提出来

```java
public class User {
	private Integer id;
	private String username;// 用户姓名
	private String sex;// 性别
	private Date birthday;// 生日
	private String address;// 地址
	//getters、setters、toString方法略	
}
```

### 创建pojo与之对应的映射配置文件User.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace命名空间,作用就是对sql进行分类话管理,可以理解为sql隔离
	注意:使用mapper代理方法开发,namespace有特殊重要的作用
 -->
<mapper namespace="test">
</mapper>
```
> 我们还需要在核心配置文件中添加这个pojo对应的配置文件

```xml
	<!-- 加载映射文件 -->
	<mappers>
		<mapper resource="sqlmap/User.xml"/>
	</mappers>
```

### 需求

> 在实际开发中,我们都是通过需求,对代码进行开发,所以我们现在通过几个需求，再来跟深入的了解mybatis的开发

- 根据ID查询对象
- 根据用户名称模糊查询
- 插入一条记录
- 删除一条记录
- 更新一条记录

#### 根据ID查询对象

> 首先,我们需要去映射配置文件中添加数据,我们了解一下几个重要的标签的作用

```xml
<!-- 通过ID查询用户表记录 -->
  <!-- 在映射文件中配置很多sql语句 -->
  <!-- 通过select进行数据库查询,id:标识映射文件中的sql 
  将sql语句封装到mapperStatement对象中,所以讲Id称为statement的ID -->
  <!-- parameterType:指定输入类型 ,这里指的是int-->
  <!-- 当输入类型为简单类型,#{}里面的参数名可以随便起名字 -->
  <!-- resultType:输出结果的类型,指定sql输出结果所映射的java对象 ,如果是select很多条记录可以是单条记录的结果-->
  <select id="findUserById" parameterType="int" resultType="cn.zcmu.pojo.User">
    select * from user where id= #{id}
  </select>
```

> 然后我们在写一段java代码,来了解mybatis的执行过程

```java
//根据ID查询对象
@Test
public void test1() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();

  //4.通过sqlsession操作数据库
  //第一个参数:映射文件中statement的名称 为namespace+.+id
  //第二个参数:为要传入的类型
  //返回的结果,resultType类型的结果
  User user = session.selectOne("test.findUserById", 1);

  System.out.println(user);

  //5.释放资源
  session.close();
}
```

> 执行结果

```xml
17:57:50,228 DEBUG findUserById:139 - ==>  Preparing: select * from user where id= ? 
17:57:50,353 DEBUG findUserById:139 - ==> Parameters: 1(Integer)
17:57:50,404 DEBUG findUserById:139 - <==      Total: 1
User [id=1, username=王五, sex=2, birthday=null, address=null]
```

我们可以通过日志文件,看到执行器向数据库发送的sql语句,还有查询到的结果

#### 根据用户名称模糊查询

> 第一步还是修改映射配置文件

```xml
<!-- 根据用户名称模糊查询 -->
  <select id="findUserLikeName" parameterType="string" resultType="cn.zcmu.pojo.User">
    select * from user where username like '%${value}%'
  </select>
```
> 这里不使用#{}做占位符,而是采用拼接符号${},我们不可能让用户自己传%，所以使用${}来进行字符串拼接,这样使用的话会有风险,如sql的注入


 **编写测试代码**

```java
//根据用户名称模糊查询
@Test
public void test2() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();

  //4.通过sqlsession操作数据库
  //第一个参数:映射文件中statement的名称 为namespace+.+id
  //第二个参数:为要传入的类型
  //返回的结果,resultType类型的结果
  List<User> list= session.selectList("test.findUserLikeName", "张");

  System.out.println(list);

  //5.释放资源
  session.close();
}
```

> 日志文件的输出

```xml
18:04:06,254 DEBUG findUserLikeName:139 - ==>  Preparing: select * from user where username like '%张%' 
18:04:06,285 DEBUG findUserLikeName:139 - ==> Parameters: 
18:04:06,314 DEBUG findUserLikeName:139 - <==      Total: 4
```

> 通过日志文件我们可以看到,执行器发送了正确的sql语句,并查询到了4条记录

#### 插入一条记录
> 插入跟上面的步骤是差不多的,主要是标签不同,其次输入参数是一个pojo的类型,mybatis通过ognl来获取对象的属性值,直接上代码

```xml
<!-- 添加用户 -->
<!-- #{}中指定pojo的属性名,接收到pojo对象的属性值,mybatis通过ognl获取对象的属性值 -->
<insert id="insertUser" parameterType="cn.zcmu.pojo.User">
  insert into user(username,birthday,sex,address) value(#{username},#{birthday},#{sex},#{address})
</insert>
```

```java
//插入用户名
@Test
public void test3() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();

  //4.通过sqlsession操作数据库
  User user = new User();
  user.setUsername("小龙");
  user.setBirthday(new Date());
  user.setAddress("5067");
  user.setSex("女");

  session.insert("test.insertUser", user);
  //5.提交事务
  session.commit();
  //6.释放资源
  session.close();
}
```

> 从上面的代码中,我们可以成功的插入数据，但是我们可以通过测试发现，我们并没有设置pojo的id值,因为mysql会自动帮我们做主键的增长,所以就算我们设置了值,mysql还是根据数据库的在自增主键值，所以我们设值就没有用处的,这时候我们就可能像，我们设值没用,那么我们如何把值获取出来,这时候我们就需要用到一条sql语句

```xml
select last_insert_id();
```
> 根据这个和添加操作一起,我们就可以获取添加自增长的主键值,所以我们在原来的插入的映射配置文件中添加如下

```xml
<insert id="insertUser" parameterType="cn.zcmu.pojo.User">
    <!--  keyProperty :将查询到主键值设置到parameterType指定的对象的哪个属性-->
    <!--  order : select last_insert_id执行顺序,相当于insert语句的执行顺序-->
    <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
      select last_insert_id();
    </selectKey>
    insert into user(username,birthday,sex,address) value(#{username},#{birthday},#{sex},#{address})
  </insert>
```

> 这样子，当我们在测试代码中可以看到用户属性的id值是从数据库中得到的,而不是我们设置的

#### 删除用户,修改用户

跟上面的代码类似,我就直接贴代码,大家多尝试

**删除用户**

```xml
  <!-- 删除用户 -->
  <delete id="deleteUser" parameterType="int">
    delete from user where id =#{value}
  </delete>
		
```

```java
	//删除用户名
@Test
public void test4() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();


  //4.通过sqlsession操作数据库
  session.delete("deleteUser",109);
  //5.提交事务
  session.commit();
  //6.释放资源
  session.close();
}
	
```

**修改用户**

```xml
  <!-- 更新用户 -->
  <update id="updateUser" parameterType="cn.zcmu.pojo.User">
    update user set username=#{username} where id=#{id}
  </update>
```

```java
@Test
public void test5() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();

  User user = new User();
  user.setId(25);
  user.setUsername("小龙");
  user.setBirthday(new Date());
  user.setAddress("5067");
  user.setSex("女");

  //4.通过sqlsession操作数据库
  session.update("updateUser", user);
  //5.提交事务
  session.commit();
  //6.释放资源
  session.close();
}
```
