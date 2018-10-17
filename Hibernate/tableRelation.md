## 多表增删改操作

### 一对多关系的操作

#### 保存操作

>保存的原则:先保存主表,再保存从表

```java
	//保存一个公司一个联系人
	@Test
	public void test2(){
		//1.自己封装的方法,获取当前线程的sesion对象
		Session session = HibernateUtils.getSession();
		//2.开启事务
		Transaction tx = session.beginTransaction();
		//3.创建一个company对象,瞬时态
		Company company = new Company();
		company.setComName("alibaba");
		company.setComPhone("1111111111");
		//4.创建一个employee对象,瞬时态
		Employee employee = new Employee();
		employee.setEmpName("马云");
		employee.setEmpPhone("6666666666");
		employee.setEmpSex("男");
		//5.建立他们双向的关系 ，下面两种情况都可以
		//employee.setCompany(company);
		company.getEmployees().add(employee);
		//6.按照要求,先保存主表,再保存从表
		//company对象从瞬时态变成了持久态
		session.save(company);
		//employee对象从瞬时态变成了持久态，同时
		session.save(employee);
		//默认此时会执行快照机制,当发现一级缓存和快照不一致了,使用一级缓存更新数据库
		tx.commit();
		
	}
```

输出结果
```xml
log4j:WARN No appenders could be found for logger (org.jboss.logging).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Hibernate: 
    insert 
    into
        t_company
        (com_Name, com_Phone) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        t_employee
        (emp_name, emp_phone, emp_sex, emp_com_id) 
    values
        (?, ?, ?, ?)
Hibernate: 
    update
        t_employee 
    set
        emp_com_id=? 
    where
        eid=?

```

> 从上面的输出结果可以看出来当保存company对象时进行了插入操作,保存employee对象时进行了插入操作之后又执行了一次更新外键的操作，这样就会产生了多于的sql,会降低我们工程的效率,这时我们应该怎么解决呢？

解决方法：我们只需要将一方放弃外键护权即可。也就是说关系不是双方去维护的,只需要一方去维护就行,在一对多的例子中,我们只需要在多的一方进行关系维护,这样子就可以解决这个地方效率不高的问题。

我们对company的映射文件进行如下配置：

```java
//inverse的默认配置是false，即代表不放弃外键维护权,true,代表放弃了外键的维护权
<set name="employees" table="t_employee" inverse="true">
```

我们在执行一次上面的插入操作,结果如下:
```java
log4j:WARN No appenders could be found for logger (org.jboss.logging).
log4j:WARN Please initialize the log4j system properly.
log4j:WARN See http://logging.apache.org/log4j/1.2/faq.html#noconfig for more info.
Hibernate: 
    insert 
    into
        t_company
        (com_Name, com_Phone) 
    values
        (?, ?)
Hibernate: 
    insert 
    into
        t_employee
        (emp_name, emp_phone, emp_sex, emp_com_id) 
    values
        (?, ?, ?, ?)

```
> 我们发现这样子就可以少执行一条update语句,会提升工程的效率问题

#### 修改操作

##### 什么是级联操作？
级联操作是指当主控方执行保存、更新或者删除操作时,其关联对象（被控方）也执行相同的操作.在映射文件中通过对cascade属性的设置来控制是否对关联对象采用级联操作,级联操作对各种关联关系都是有效的

> 级联操作的方向性：所谓的方向性指的是,在保存一的地方->保存多的地方；保存多的地方->保存一的地方

当我们想通过公司来这一方来添加一个员工,那么就是公司要级联员工,那么我们要在公司这方添加下面的配置：

```java
//保存级联更新
<set name="employees" table="t_employee" inverse="true" cascade="save-update">
```
修改操作:

```java

	//创建一个员工,公司为之间已经创建好的公司,向这个公司添加员工
	@Test
	public void test3(){
		//1.自己封装的方法,获取当前线程的sesion对象
		Session session = HibernateUtils.getSession();
		//2.开启事务
		Transaction tx = session.beginTransaction();
		//3.创建一个员工,瞬时态
		Employee employee = new Employee();
		employee.setEmpName("张三");
		employee.setEmpPhone("123123");
		employee.setEmpSex("男");
		//4.查询id为1的公司 持久态
		Company company = session.get(Company.class, 1);
		//5.建立他们双向的关系
		company.getEmployees().add(employee);
		//6.更新公司，因为向公司添加了一名新的员工
		session.update(company);
		tx.commit();
	}
```

> 但是我们可以根据数据库发现一个问题,添加的这条数据没有维护外键,是因为我们在上面设置了公司这方不去维护外键,所以添加的这条数据没有维护外键。

#### 删除操作

##### 当我们进行删除操作的时候，注意两点
- 删除从表数据:可以随意删除
- 删除主表数据：要根据配置文件的不同，会产生不同的结果,会下下面代码的注释上解释

##### 删除操作
```java
	//在公司配置文件中没有放弃关系维护的时候,删除公司对象一条记录,会把级联的员工表上对应这个公司的员工的公司ID置为空
	//在公司配置文件中放弃关系维护的时候,我们发现数据库中,什么都没有删除,即不能删除
	//在公司配置文件中没有放弃关系维护的时候,同时在cascade上添加级联删除的操作,则公司表的公司被删除,员工表中公司ID为删除公司的都被删除
	//<set name="employees" table="t_employee" inverse="true" cascade="save-update,delete">
	@Test
	public void test4(){
		//1.自己封装的方法,获取当前线程的sesion对象
		Session session = HibernateUtils.getSession();
		//2.开启事务
		Transaction tx = session.beginTransaction();
		//3.得到一个公司
		Company company = session.get(Company.class, 2);
		//4.删除这个公司
		session.delete(company);
		tx.commit();
	}
```

### 多对多关系的操作

#### 保存操作

```java
	@Test
	public void test5(){
		//1.自己封装的方法,获取当前线程的sesion对象
		Session session = HibernateUtils.getSession();
		//2.开启事务
		Transaction tx = session.beginTransaction();
		//3.设置多个课程
		Course course1 = new Course();
		course1.setCouName("数据结构");
		
		Course course2 = new Course();
		course2.setCouName("计算机网络");
		
		Course course3 = new Course();
		course3.setCouName("操作系统");
		//4.设置多个学生
		Student student1 = new Student();
		student1.setStuName("张三");
		
		Student student2 = new Student();
		student2.setStuName("李四");
		//5.建立关联关系
		course1.getStudents().add(student1);
		course1.getStudents().add(student2);
		
		course2.getStudents().add(student1);
		course3.getStudents().add(student2);
		
		tx.commit();
	}
```







