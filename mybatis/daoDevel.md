## mybatis的Dao层开发

原始的Dao层开发,都是先写接口，然后在写实现类,这种在mybatis里是绝对可以实现的,只要写好接口,在实现类中加载好配置文件，或者封装好sqlsessionFactory这种工具类来使用,就可以实现原始的Dao层的开发,要注意的一点就是sqlsession是线程不安全的,所以我们要把它写在方法体内，不能写成工具类直接获取和写在全局变量上,当然可以使用sqlsesion和ThreadLocal进行绑定,从而实现线程的安全的问题。

> 说到线程安全,突然想到前几天的structs2框架中,action类获取前端传过来的数据是通过模型驱动,或者set注入,然而模型驱动,set注入又是放在全局变量上的,所以为了保证线程的安全,在和spring进行整合开发的时候,我们让action类都是多例的,来保证线程的安全的问题

### 使用Mapper代理对象的开发模式开发Dao层

前面说了原始的Dao层开发模式,我们发现在实现类中还是有很多的冗余代码,比如说,每次都要因为线程安全,让每一个方法都获取一次sqlSession对象等。

这时就出现了Mapper代理的方式来开发Dao层,我们只需要写 ***配置文件和接口***  就可以了,突然一下子就简单了很多,mybaits可以自动生成mapper接口实现类的代理对象,也就是动态代理！！我们可以翻翻源码会发现,底层采用jdk动态代理实现接口的代理对象，增强程序。

#### 马上就写一个使用动态代理实现的Dao层开发

##### 配置文件UserMapper.xml

我们复制之前的user.xml配置文件,为了好区分,改名为UserMapper.xml文件,还是把代码贴出来, **注意！！！！** 要把namespace改名为我们的接口的全路径

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- namespace命名空间,作用就是对sql进行分类话管理,理解sql隔离
	注意:使用mapper代理方法开发,namespace有特殊重要的作用
 -->
<mapper namespace="cn.zcmu.mapper.UserMapper">
		<!-- 通过ID查询用户表记录 -->

  <select id="findUserById" parameterType="int" resultType="cn.zcmu.pojo.User">
    select * from user where id= #{id}
  </select>
  <!-- 根据用户名称模糊查询 -->
  <select id="findUserLikeName" parameterType="string" resultType="cn.zcmu.pojo.User">
    select * from user where username like '%${value}%'
  </select>
  <!-- 添加用户 -->
  <insert id="insertUser" parameterType="cn.zcmu.pojo.User">
    <selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer">
      select last_insert_id();
    </selectKey>
    insert into user(username,birthday,sex,address) value(#{username},#{birthday},#{sex},#{address})
  </insert>

  <!-- 删除用户 -->
  <delete id="deleteUser" parameterType="int">
    delete from user where id =#{value}
  </delete>

  <!-- 更新用户 -->
  <update id="updateUser" parameterType="cn.zcmu.pojo.User">
    update user set username=#{username} where id=#{id}
  </update>
</mapper>
```

##### UserMapper.java接口

> 这个接口和映射配置文件要实现一定的规范,才能生成动态代理对象
- mapper中的nameSpace是等于mapper接口的地址
- mapper接口中的方法名要和mapper.xml中的statement标签ID一致
- mapper接口中的方法输入参数类型要和mapper.xml中的statement的parameterType指定的类型一致
- mapper接口中的方法返回参数类型要和mapper.xml中的statement的resultType指定的类型一致


```java
public interface UserMapper {
	
	User findUserById(int id);
	
	List<User> findUserLikeName(String name);
	
	void insertUser(User user);
	
	void deleteUser(int id);
	
	void updateUser(User user);
}

```

测试代码,我这里就测试第一个方法,其他的大家去尝试测试

```java
//根据ID查询对象,得到一条记录
@Test
public void test1() throws IOException {
		
	//1.加载mybatis配置文件
	String resource = "SqlMapConfig.xml";
	InputStream resourceAsStream = Resources.getResourceAsStream(resource);
	//2.创建会话工厂,传入mybatis的配置文件信息
	SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
	//3.通过工厂得到sqlsesion
	SqlSession session = sessionFactory.openSession();
	//4.通过session得到代理对象,通过代理对象执行方法
	UserMapper userMapper = session.getMapper(UserMapper.class);
	User user = userMapper.findUserById(1);
	
	System.out.println(user);
	//5.释放资源
	session.close();
}
```


