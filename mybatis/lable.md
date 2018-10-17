## SqlMapConfig.xml标签

#### properties属性

主要是用来加载一些外置写的配置文件,当然我们还可以在这个标签里面写一些自定配置

```xml
<!-- 加载属性文件 -->
	<properties resource="db.properties">
		<property name="自定义key" value="自定义value"/>
	</properties>
```
#### settings属性

mybatis全局配置参数,全局参数将会影响mybatis的运行行为
这个是我去网上找的配置信息和作用,作为参考学习

| 设置参数  |描述 |有效值 |默认值  |
| ------------ | ------------ | ------------ | ------------ |
|   cacheEnabled|  	该配置影响的所有映射器中配置的缓存的全局开关  | 	true， false   | true  |
|  lazyLoadingEnabled | 	延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态   | 	true， false   |  false |
|aggressiveLazyLoading   | 	当启用时，对任意延迟属性的调用会使带有延迟加载属性的对象完整加载；反之，每种属性将会按需加载。   | 	true， false   |  true |
|multipleResultSetsEnabled   |是否允许单一语句返回多结果集（需要兼容驱动）。   |	true， false    |true   |
|useColumnLabel   |使用列标签代替列名。不同的驱动在这方面会有不同的表现， 具体可参考相关驱动文档或通过测试这两种不同的模式来观察所用驱动的结果。   |	true， false  |true   |
|useGeneratedKeys   | 	允许 JDBC 支持自动生成主键，需要驱动兼容。 如果设置为 true 则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍可正常工作（比如 Derby）。   |  	true， false  |False   |
| autoMappingBehavior  |指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示取消自动映射；PARTIAL 只会自动映射没有定义嵌套结果集映射的结果集。 FULL 会自动映射任意复杂的结果集（无论是否嵌套）。   | NONE, PARTIAL, FULL  |PARTIAL   |
|defaultExecutorType   |配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（prepared statements）； BATCH 执行器将重用语句并执行批量更新。   |SIMPLE REUSE BATCH   |SIMPLE   |
|defaultStatementTimeout   |设置超时时间，它决定驱动等待数据库响应的秒数。   |  Any positive integer | 	Not Set (null)   |
| defaultFetchSize  | Sets the driver a hint as to control fetching size for return results. This parameter value can be override by a query setting.  | Any positive integer  |Not Set (null)   |
|safeRowBoundsEnabled   |允许在嵌套语句中使用分页（RowBounds）。   | 		true， false    | 	False   |
|mapUnderscoreToCamelCase   |是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。   | 	true， false   | False  |
|localCacheScope   |MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。   | SESSION ， STATEMENT  |SESSION   |
|  jdbcTypeForNull | 当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。 某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。  | JdbcType enumeration. Most common are: NULL, VARCHAR and OTHER  |OTHER   |
|lazyLoadTriggerMethods   | 指定哪个对象的方法触发一次延迟加载。  |  	A method name list separated by commas  | equals,clone,hashCode,toString  |
| defaultScriptingLanguage  | 指定动态 SQL 生成的默认语言。  |  	A type alias or fully qualified class name.  | XMLDynamicLanguageDriver类 |
|callSettersOnNulls   | 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这对于有 Map.keySet() 依赖或 null 值初始化的时候是有用的。注意基本类型（int、boolean等）是不能设置成 null 的。  | true ， false  |   	false |
|logPrefix   |指定 MyBatis 增加到日志名称的前缀。   | 	Any String   |Not set   |
| logImpl  | 	指定 MyBatis 所用日志的具体实现，未指定时将自动查找。   | SLF4J，LOG4J ， LOG4J2 ， JDK_LOGGING ， COMMONS_LOGGING ， STDOUT_LOGGING ， NO_LOGGING  | Not set  |
|proxyFactory   | 指定 Mybatis 创建具有延迟加载能力的对象所用到的代理工具。  |   CGLIB , JAVASSIST| JAVASSIST (MyBatis 3.3 or above)  |

#### typeAliases(别名)

```xml
<!-- 自动扫描包中的po类，自动定义别名，别名就是类名（首字母大小写都可以） -->
	<typeAliases>
		<package name="cn.zcmu.pojo"/>
	</typeAliases>
```

#### typeHandlers（类型处理器）

mybatis中通过typeHandler完成jdbc类型和java类型的转换
mybatis提供的类型处理器满足日常需求,不需要自定义

#### mappers(映射配置）

- 通过resource加载单个映射文件

```xml
<mappers>
	<mapper resource="sqlmap/User.xml"/>
</mappers>
```
- 通过mapper接口加载,也就是类加载

> 规范：将mapper接口和mapper.xml映射文件名称保持一致,且在一个目录中,使用mapper代理模式

```xml
<mappers>
	<mapper class="cn.zcmu.mapper.UserMapper"/>
</mappers>
```

- 批量加载mapper

> 规范：将mapper接口和mapper.xml映射文件名称保持一致,且在一个目录中,使用mapper代理模式,mybatis自动扫描包下边所有mapper接口进行加载

```xml
<mappers>	
	<package name="cn.zcmu.mapper"/>
</mappers>
```

#### paramterType（输入映射）

通过paramterType指定输入参数的类型,类型可以是简单类型,hashMap,pojo包装类型

之间简单类型和pojo类型都在之前的Demo使用过了,我们现在主要说一下pojo的包装类型的使用

##### 需求

对用户进行综合查询,需要传入查询参数条件可能很复杂，所以我们需要包装User这对象

于是乎我们就创建了一个pojo包装对象,当然我这里是演示,没有对pojo真正的进行封装其他的属性进去,只是演示！！！

public class UserQueryVo {    	
  private User user;    
  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
  } 	
}


在映射UserMapper.xml配置文件中添加statement

```xml
<!-- 用户信息综合查询 -->
<select id="findUserList" parameterType="userQueryVo" resultType="user">
  select * from user where user.sex = #{user.sex} and and user.username like '%${user.username}%'
</select>
```

在接口中定义方法

```java
List<User> findUserList(UserQueryVo userQueryVo);
```

测试代码
```java
@Test
public void test6() throws IOException {

  //1.加载mybatis配置文件
  String resource = "SqlMapConfig.xml";
  InputStream resourceAsStream = Resources.getResourceAsStream(resource);
  //2.创建会话工厂,传入mybatis的配置文件信息
  SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
  //3.通过工厂得到sqlsesion
  SqlSession session = sessionFactory.openSession();
  //4.得到userMapper的代理对象
  UserMapper mapper = session.getMapper(UserMapper.class);
  //5.设置查询条件
  UserQueryVo userQueryVo = new UserQueryVo();
  User user = new User();
  user.setSex("1");
  user.setUsername("张");
  userQueryVo.setUser(user);
  //6.得到查询条件结果
  List<User> list = mapper.findUserList(userQueryVo );
  //7.打印
  System.out.println(list);
  //8.释放资源
  session.close();
}
```

测试结果
```java
19:47:50,486 DEBUG findUserList:139 - ==>  Preparing: select * from user WHERE user.sex = ? and user.username like '%张%' 
19:47:50,515 DEBUG findUserList:139 - ==> Parameters: 1(String)
19:47:50,544 DEBUG findUserList:139 - <==      Total: 4
```
我们从上面可以看出我们发送的sql语句是正确的

#### resultType（输出映射）
resultType 是输出的类型,可以使简单类型,hashMap,Pojo，和pojo的包装类型

> 当我们查询的结果集只有一行一列,可以使用简单类型进行输出映射

**我们这里要注意一个地方**

 > 使用rusultType进行输出映射,只有查询出来的列名和pojo的属性名一致,该列才可以映射成功！！，如果查出来的没有pojo的一个属性,则那个属性为初始值。
 
例如:我们修改第一个根据ID查询数据userMapper.xml修改为

```xml
<select id="findUserById" parameterType="int" resultType="cn.zcmu.pojo.User">
			select id,username,birthday,sex s from user where id= #{id}
</select>
```
 
 进行测试的结果为
 
 ```xml
19:57:30,363 DEBUG findUserById:139 - ==>  Preparing: select id,username,birthday,sex s from user where id= ? 
19:57:30,400 DEBUG findUserById:139 - ==> Parameters: 1(Integer)
19:57:30,425 DEBUG findUserById:139 - <==      Total: 1
User [id=1, username=王五, sex=null, birthday=null, address=null]
```
 证实了我们之前说的要注意的地方,当然我们也可以让名字和列名不相同,也能映射成功,就是我们接下来要说的一个属性
 
 #### resultMap
 
 >mybatis使用resultMap完成高级输出映射
 
 如果查询出来的结果和pojo的属性名不一致,通过定义一个resultMap对列名和pojo属性名之间作一个映射关系。
 
######  第一步 定义rusultMap

```xml
<!--select id id_,username username_ ,birthday bir,sex s from user where id= #{id}  -->
<!--type:resultMap最终映射的java对象类型,可以使用别名-->
<!--id:对resultMap的唯一标识  -->
<resultMap type="user" id="userResultMap">
  <!-- id:表示查询结果集中唯一的标识 -->
  <!-- column:查询出来的列名 -->
  <!-- property:type指定的pojo类型的属性名 -->
  <id column="id_" property="id"/>

  <result column="username_" property="username"/>
  <result column="bir" property="birthday"/>
  <result column="s" property="sex"/>
</resultMap>
```
######  第二步 使用resultMap作为statement的输出映射类型

```xml
<select id="findUserById" parameterType="int" resultMap="userResultMap">
			select id id_,username username_ ,birthday bir,sex s from user where id= #{id}
</select>
```
 
###### 测试 

```xml
20:13:29,310 DEBUG findUserById:139 - ==>  Preparing: select id id_,username username_ ,birthday bir,sex s from user where id= ? 
20:13:29,384 DEBUG findUserById:139 - ==> Parameters: 1(Integer)
20:13:29,411 DEBUG findUserById:139 - <==      Total: 1
User [id=1, username=王五, sex=2, birthday=null, address=null]
```
 
 如果表名和pojo的名称不一样也可以使用这种方式来设置映射关系
 
#### 动态sql

mybatis核心对sql语句进行灵活操作,通过表达式进行判断,对sql进行灵活拼接和组装

假设我们有一条语句为

```xml
select * from user where user.sex = #{user.sex} and and user.username like '%${user.username}%'
```

我们可以把里面的两个属性进行动态的拼接，因为如果我们没有那个参数,那么sql还会正常的执行
改造:
```xml
select * from user
	
<where>
	<if test="user!=null">
		<if test="user.sex!=null and user.sex!=''">
			and user.sex = #{user.sex}
		</if>
		<if test="user.username!=null and user.username!=''">
			and user.username like '%${user.username}%'</if>
	</if>
/where>
```
这里面的where标签可以自动去掉条件中的第一个and

#### sql片段

我们可以把之前写的sql语句专门放在一个sql代码块里,这样很多地方都可以复用这个语句

最后改造成这样

```xml
<select id="findUserList" parameterType="userQueryVo" resultType="user">
  select * from user
  <where>
    <include refid="query_user_where"/>
  </where>
</select>
<!-- 自定义一个sql片段 -->
<sql id="query_user_where">
    <if test="user!=null">
      <if test="user.sex!=null and user.sex!=''">
        and user.sex = #{user.sex}
      </if>
      <if test="user.username!=null and user.username!=''">
         and user.username like '%${user.username}%'</if>
    </if>
</sql>	
```
