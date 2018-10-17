## 多表设计

### 为什么我们要学习多表映射？

###### 在实际开发中,数据库中的表之前肯定会存在一定的联系，在操作表的时候就有可能会涉及到多张表的操作，但是我们之前在Hibernate中操作都是对同一张表进行操作,如果使用ORM的思想,又如何实现通过实体类对数据库进行多表操作呢？所以要实现多表映射，也就是配置实体类之间的关联关系

### 我们要实现多表映射,要先遵循以下的步骤
- 首先确定两张表之间的关系
- 在数据库中实现两张表的关系
- 在实体类中描述出两个实体的关系
- 配置出实体类和数据库表的关系映射,配置的方式支持注解和XML,我们以注解为重

#### 数据库表之间的关系
- 一对一(实际开发中不常用)
- 一对多(实际开发中常用)
- 多对多(实际开发中常用)
- 多对一

#### 如何在数据库中建立表与表之间的关系
- 一对多,多对一：需要在多的哪一方,建立与另外一张表的主键,即这张表的外键
- 多对多：需要再多建立一张表,记录两张表的主键关系

#### 如何在实体类中建立表与表之间的关系

##### 建立一对多的关系
> 例如：假设我们有两张表，一张是公司表,一张是员工表,一个公司里可以有多个员工,即一对多的关系,所以在公司的实体类里应该设置员工的集合，即如下：

```java
public class Company implements Serializable{
	private Integer cid;
	private String comName;
	private String comPhone;
	
	private Set<Employee> employees = new HashSet<Employee>();
	...//getters和setters方法
}
```

> 在员工实体类里应该设置一个公司的对象,即如下

```java
public class Employee implements Serializable {
	private Integer eid;
	private String empName;
	private String empPhone;
	private String empSex;
	
	private Company company;
	...//getters和settres方法
}
```

##### 建立多对多的关系

>例如：假设我们有两张表,一张表是课程表,一张是学生表,一个课程可以有多个学生,一个学生可以选多门课,即多对多的关系,所以在学生表的实体类中要添加课程的集合,如下：

```java
public class Student implements Serializable{
	private Integer sid;
	private String stuName;
	
	private Set<Course> courses = new HashSet<Course>();
	...//getters和settres方法
}
```

> 一个课程的实体类中添加学生的集合,如下：

```java

public class Course implements Serializable {
	private Integer cid;
	private String couName;
	
	private Set<Student> students = new HashSet<Student>();
	...//getters和settres方法
}
```

> 实体类之间的表与表之间的关系建立好了以后,如何才能把公司里的set集合和员工的company对象与数据库建立起关系来呢？

#### 通过XML或者注解的方式来实现对数据库建立起关系

##### 使用xml配置一对多的关系

###### 公司xml文件的配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="cn.zcmu.entity.Company" table="t_company">
		<id name="cid" column="cid">
			<generator class="native"/>
		</id>
		<property name="comName" column="com_Name"/>
		<property name="comPhone" column="com_Phone"/>
		
		<!-- 一对多关系映射 -->
		<!-- set：用于映射set集合属性 -->
		<!-- name：指定集合属性的名称-->
		<!-- table：指定数据库的表的名称（即set集合中泛型元素的那个对应的表） -->
		<set name="employees" table="t_employee">
			<!-- Hibernate机制:双向维护外键,在一和多的一方都配置外键 -->
			<!-- column:字段名称（员工表中的外键字段名称） -->
			<key column="emp_com_id"></key>
			<!-- class：对应员工表的实体类的全路径 -->
			<one-to-many class="cn.zcmu.entity.Employee"/>
		</set>
	</class>
</hibernate-mapping>
```
###### 员工xml文件的配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="cn.zcmu.entity.Employee" table="t_employee">
		<id name="eid" column="eid">
			<generator class="native"/>
		</id>
		<property name="empName" column="emp_name"/>
		<property name="empPhone" column="emp_phone"/>
		<property name="empSex" column="emp_sex"/>
		<!-- name属性：因为员工实体类使用company对象表示，写company名称-->
		<!-- class属性：Company全路径 -->
		<!-- column属性：本表的外键名称  -->
		<many-to-one name="company" class="cn.zcmu.entity.Company" column="emp_com_id"/>
	</class>
</hibernate-mapping>
```

##### 使用xml配置多对多的关系

###### 学生xml文件的配置
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="cn.zcmu.entity.Student" table="t_student">
		<id name="sid" column="sid">
			<generator class="native"/>
		</id>	
		<property name="stuName" column="stu_name"/>
		<!--name:集合属性的名称 -->
		<!--table:指定的是中间表的名称,在多对多配置中一定要写  -->
		<set name="courses" table="stu_cour_tab">
			<!--column：指定的是当前映射文件所对应的实体类在中间表的外键字段名  -->
			<key column="s_id"/>
			<!--class:指定集合元素所对应的实体类  -->
			<!--column：指定的是集合元素所对应的实体在中间表的外键字段名称  -->
			<many-to-many class="cn.zcmu.entity.Course" column="c_id"/>
		</set>
	</class>
</hibernate-mapping>
```

###### 课程xml文件的配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="cn.zcmu.entity.Course" table="t_course">
		<id name="cid" column="cid">
			<generator class="native"/>
		</id>	
		<property name="couName" column="cou_name"/>
		<!--name:集合属性的名称 -->
		<!--table:指定的是中间表的名称,在多对多配置中一定要写  -->
		<set name="students" table="stu_cour_tab">
			<!--column：指定的是当前映射文件所对应的实体类在中间表的外键字段名  -->
			<key column="c_id"/>
			<!--class:指定集合元素所对应的实体类  -->
			<!--column：指定的是集合元素所对应的实体在中间表的外键字段名称  -->
			<many-to-many class="cn.zcmu.entity.Student" column="s_id"/>
		</set>
	</class>
</hibernate-mapping>
```

## 下一篇文章推荐继续学习---->[Hibernate的多表级联操作](https://github.com/ScureHu/hibernate/blob/master/hibernate/tableRelation.md)
