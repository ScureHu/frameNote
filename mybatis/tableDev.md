## mybatis的多表开发

## 一对多的开发

假设我们有一张用户表和一张订单表,一个用户可以有多个订单,所以是一对多的关系

我们通过数据库查询所有的用户的订单记录

```sql
SELECT * FROM `user`,`order` WHERE user.id = order.user_id
```

> 我们会发现数据库表字段有两列的列名是相同的都是ID,多个同名字段，拿第一个匹配值就会放入，业务逻辑就错了。mybaits不管。注意：mybatis有一个bug，查询的结果集中不能有同名字段,为了满足我们的开发需求，我们需要新增一个对User对象包装的po类,同时增加Order的list集合

```java
public class UserOrder extends User implements Serializable{
	//一个用户可以有多个订单
	private List<Order> orders;
	//getters和setters方法略
}
```

修改我们的sql语句
```sql
SELECT u.id u_id,
	u.username u_uname,
	u.birthday u_bir,
	u.sex u_sex,
	u.address u_add,
	o.id o_id,
	o.number o_num,
	o.createtime o_create,
	o.note o_nate
 FROM `user` u,`order` o WHERE u.id = o.user_id
```
在UserMapper.xml中添加一个resultMap与之对应

```xml
<resultMap type="userOrder" id="userOrderResultMap">
		<id column="u_id" property="id"/>	
		<result column="u_uname" property="username"/>
		<result column="u_bir" property="birthday"/>
		<result column="u_sex" property="sex"/>
		<result column="u_add" property="address"/>
		<!-- 关联关系,对多。property对应POJO对象中配置的属性，ofType集合中元素类型 -->
		<collection property="orders" ofType="cn.zcmu.pojo.Order">
			<id column="o_id" property="id"/>
			<result column="o_num" property="number"/>
			<result column="o_create" property="createtime"/>
			<result column="o_nate" property="note"/>
		</collection>
	</resultMap>
```

在UserMapper.xml中添加新的statement

```xml
<select id="findByAllUserOrders" resultMap="userOrderResultMap">
		SELECT u.id u_id,
		u.username u_uname,
		u.birthday u_bir,
		u.sex u_sex,
		u.address u_add,
		o.id o_id,
		o.number o_num,
		o.createtime o_create,
		o.note o_nate
		FROM `user` u,`order` o WHERE u.id = o.user_id
	</select>
```
在UserMapper接口中添加新的方法

```xml
List<UserOrder> findByAllUserOrders();
```

测试

```java
@Test
public void test7() throws IOException {

	//1.加载mybatis配置文件
	String resource = "SqlMapConfig.xml";
	InputStream resourceAsStream = Resources.getResourceAsStream(resource);
	//2.创建会话工厂,传入mybatis的配置文件信息
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
	//3.通过工厂得到sqlsesion
	SqlSession session = sessionFactory.openSession();
	UserMapper userMapper = session.getMapper(UserMapper.class);
	List<UserOrder> userOrders = userMapper.findByAllUserOrders();

	System.out.println(userOrders);

}
```

通过debug我们可以看到每一个UserOrder中都正确的添加了Orders这个集合

```java
09:17:21,235 DEBUG findByAllUserOrders:139 - ==>  Preparing: SELECT u.id u_id, u.username u_uname, u.birthday u_bir, u.sex u_sex, u.address u_add, o.id o_id, o.number o_num, o.createtime o_create, o.note o_nate FROM `user` u,`order` o WHERE u.id = o.user_id 
09:17:21,308 DEBUG findByAllUserOrders:139 - ==> Parameters: 
09:17:21,367 DEBUG findByAllUserOrders:139 - <==      Total: 3
```
## 一对一的开发

假设我们有一张用户表和一张用户扩展属性表,是一对一的关系

我们通过数据库查询单个用户的全部信息

```sql
SELECT * FROM `user`,userext WHERE user.id=userext.id AND user.id = 31
```
> 根据需求我们可以有两种方法解决这个问题,第一种就是包装user类,添加相应的字段,我们可以直接通过resultType就行映射关系,简单快速,第二种就是创建另外一个表的对象,然后在把user对象包装起来加一个另外表的对象,然后通过resultMap进行映射

第一种>>>>>>>>>>>>>>>>>>>>>>>>>>>>
```java
public class UserInfo extends User implements Serializable {
	
	private String school;
	private Integer age;
	//略get和set方法	
}
```
第二种>>>>>>>>>>>>>>>>>>>>>>>>>>>>

```java
public class UserInfo2 extends User implements Serializable {
	
	private UserExt userExt;
	//略get和set方法	
}
```

为了显示assoication这个标签,我们采用第二种方式

修改我们的sql语句

```sql
SELECT u.id u_id,
	u.username u_name,
	u.birthday u_bir,
	u.sex u_sex,
	u.address u_add,
	ux.id ux_id,
	ux.school u_sch,
	ux.age u_age 
FROM `user` u,userext ux WHERE u.id=ux.id AND u.id = 31
```
在UserMapper.xml中添加一个resultMap与之对应

```xml
<resultMap type="userInfo2" id="userUserInfoRM">
	<id column="u_id" property="id"/>	
	<result column="u_uname" property="username"/>
	<result column="u_bir" property="birthday"/>
	<result column="u_sex" property="sex"/>
	<result column="u_add" property="address"/>
	<association property="userExt" javaType="userExt">
		<id column="ux_id" property="id"/>
		<result column="u_sch" property="school"/>
		<result column="u_age" property="age"/>
	</association>
</resultMap>
```

在UserMapper.xml中添加新的statement

```xml
<select id="findUserInfoById" parameterType="integer" resultMap="userUserInfoRM">
	SELECT u.id u_id,
	u.username u_name,
	u.birthday u_bir,
	u.sex u_sex,
	u.address u_add,
	ux.id ux_id,
	ux.school u_sch,
	ux.age u_age 
	FROM `user` u,userext ux WHERE u.id=ux.id AND u.id = #{id}
</select>
```

在UserMapper接口中添加新的方法

```xml
UserInfo2 findUserInfoById(Integer id);
```

测试

```java
@Test
	public void test8() throws IOException {

	//1.加载mybatis配置文件
	String resource = "SqlMapConfig.xml";
	InputStream resourceAsStream = Resources.getResourceAsStream(resource);
	//2.创建会话工厂,传入mybatis的配置文件信息
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
	//3.通过工厂得到sqlsesion
	SqlSession session = sessionFactory.openSession();
	UserMapper userMapper = session.getMapper(UserMapper.class);
	UserInfo2 userInfo = userMapper.findUserInfoById(31);

	System.out.println(userInfo);

}
```

通过debug我们可以看userExt成功的映射到UserInfo属性里

```java
09:57:19,974 DEBUG findUserInfoById:139 - ==>  Preparing: SELECT u.id u_id, u.username u_name, u.birthday u_bir, u.sex u_sex, u.address u_add, ux.id ux_id, ux.school u_sch, ux.age u_age FROM `user` u,userext ux WHERE u.id=ux.id AND u.id = ? 
09:57:20,006 DEBUG findUserInfoById:139 - ==> Parameters: 31(Integer)
09:57:20,045 DEBUG findUserInfoById:139 - <==      Total: 1
```

> 多对多也是采用第一种collection来设置,所以我就不再多写,大家可以回去多试
