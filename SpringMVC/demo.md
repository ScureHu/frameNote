# 入门Demo

### 第一步:创建web工程

###### 在eclipse下新建 ->Dynamic Web Project
###### 选择2.5的版本,tomcat7

### 第二步:导入jar包

###### 在 WebContent/WEB-INF/lib下导入jar包

- commons.logging-1.1.1.jar
- spring-aop-4.2.2.jar
- spring-aspects-4.2.2.jar
- spring-beans-4.2.2.jar
- spring-context-4.2.2.jar
- spring-context-support-4.2.2.jar
- spring-core-4.2.2.jar
- spring-expression-4.2.2.jar
- spring-web-4.2.2.jar
- spring-webmvc-4.2.2.jar

### 第三步:配置前端控制器

在web.xml文件中配置前端控制器

```xml
  <servlet>
  	<servlet-name>springMVC</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<!-- contextConfigLocation配置springMVC加载的配置文件(处理器映射器,处理器适配器)-->
  	<!-- 如果不配置contextConfigLocation,默认加载的是/WEB-INF/servlet名称-servlet.xml(springmvc-servlet.xml) -->
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:springMVC.xml</param-value>
  	</init-param>
  </servlet>
  
  <servlet-mapping>
  	<servlet-name>springMVC</servlet-name>
  	<!-- 
		第一种：*.action，以访问.action结尾由DsipatcherServlet进行解析
		第二种：/ 所有访问的地址都有DispatcherServlet进行解析,对于静态文件的解析需要配置不让DispatcherServlet进行解析
		使用上面第二种方法可以实现RESTful风格的url
		第三种:/*，这种配置不对，使用这种配置，最终要转发到一个jsp页面时，仍然会有DispatcherServlet解析jsp地址，不能根据jsp页面找到Handler，会报错
 	 -->
  	<url-pattern>*.action</url-pattern>
  </servlet-mapping>
```

### 第四步:创建springMVC.xml文件在classpath下,添加内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:p="http://www.springframework.org/schema/p"
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.0.xsd
        http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd
        http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">
        
    <!-- 配置处理器适配器 -->
	<bean class="org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter"/>

</beans>
```

### 第五步:创建Handler处理类(Controller类)

所有的Handler都要实现Controller接口

```java
public class HelloController implements Controller{

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//返回ModelAndView
		ModelAndView modelAndView = new ModelAndView();
		//相当于request的setAttribute
		modelAndView.addObject("Hello", "你好springMVC!!!");
		
		//指定视图
		modelAndView.setViewName("/hello.jsp");
		return modelAndView;
	}

}
```
### 第六步:创建一个简单的jsp页面
```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>${Hello }</h1>
</body>
</html>
```

### 第七步：在springMVC.xml上添加处理器映射器和试图解析器

```xml
<!-- 配置处理器映射器 将bean的name作为url进行查找，需要在配置handler指定beanname（就是url）-->
	<bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
	<!-- 配置视图解析器 
		解析jsp，默认使用jstl标签，classpath下得有jstl的包
	-->     
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"/>
<!-- 配置Handler -->
	<bean name="/helloWorld.action" class="cn.zcmu.controller.HelloController"/>
```


### 第八步：测试

启动tomcat,然后访问localhost:8080/springMVC/helloWorld.action
###### 第二个名称是项目名,第三个名称是在springMVC.xml中配置的bean的名称

我们成功得到了我们想要的记过

### 思考
大家是不是并没有发现springmvc比servlet好到哪里啊？无非是把servlet换成了controller，HelloController实现了Controller接口，不还是一个方法的调用，也不能实现多个方法在一个controller中，servlet要在web.xml中声明，不就换成在springMVC.xml中声明吗？工作量一点没减少，还出来一堆乱七八糟的东西，什么DispatcherServlet核心分发器、HandlerMapping映射处理器、HandlerAdapter适配器、ViewResolver视图解析器。

> 通过我们这一番思考,为什么springMVC这么流行呢？答案就是 **注解**

从spring2.5开始引入注解方式，特别到了3.0就全面引入注解方式，号称xml零配置。spring3.0配置注解引入后也就是这个点成为了它和struts2的分水岭。随着springmvc的成熟，struts2开始落幕，趋于被市场淘汰。

## 注解开发SpringMVC

前三步跟上面的步骤相同

### 第四步:配置springMVC.xml文件

```html
<!-- 配置3.1之后的注解处理器映射器和处理器适配器 -->
	<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping"/>
	<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter"/>
	
	<!-- 使用mvc:annotation-driven代替上面的注解适配器和映射器的配置
		mvc:annotation-driven默认加载很多的参数绑定方法：比如json转化解析器就默认加载了，
		如果使用mvc:annotation-driven就不用再上面的两个配置中进行配置
		实际开发也使用这个
	  -->	
	<mvc:annotation-driven></mvc:annotation-driven>
	
	<!-- 对于注解的Handler可以单个配置
		实际开发中建议使用组件扫描
	 -->
	<!-- 可以扫描controller、service、。。。
		这里让扫描controller，指定controller的包
		 -->
	<context:component-scan base-package="cn.zcmu.controller"></context:component-scan>
	<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"></bean>
```
### 第五步:编写Handler(controller类)

```java
@Controller
public class Hello1Controller {
	
	@RequestMapping("/hello")
	public String HelloWorld(HttpServletRequest  request) {
		request.setAttribute("Hello", "你好springMVC!!!");
		return "/hello.jsp";
	}
}
```

### 第六步：测试
启动tomcat,然后访问localhost:8080/springMVC/hello.action
###### 第二个名称是项目名,第三个名称是在controller方法体上定义的名称

### XML配置方式和注解方式差异

- Controller无需继承，不受限制，灵活度高
- Controller无需在xml中声明，加个@Controller注解即可，这样再多的Controller也不怕了。只需xml中配置一次注解驱动<mvc:annotation-driven />
- 原来要在xml配置bean来指明映射关系，现在只加注解@RequestMapping无需xml配置
- 无需返回ModelAndView对象，直接返回逻辑名即可
- 参数自动封装，自动类型转换

### 注解驱动

<mvc:annotation-driven /> 是一种简写形式，完全可以手动配置替代这种简写形式，简写形式可以让初学都快速应用默认配置方案。
<mvc:annotation-driven /> 会自动注册DefaultAnnotationHandlerMapping与AnnotationMethodHandlerAdapter 两个bean,是spring MVC为@Controllers分发请求所必须的。
并提供了：数据绑定支持，@NumberFormatannotation支持，@DateTimeFormat支持，@Valid支持，读写XML的支持（JAXB），读写JSON的支持（Jackson）。

### 注解的工作原理

启动tomcat，加载项目的web.xml创建并初始化spring容器，同时创建springmvc容器。创建完springmvc容器后，进行包扫描，扫描所有的controller。找到配置的包路径下的所有含有@Controller的类，然后扫描这个类的所有@RequestMapping的映射，把这个注解形成一个map，这个map的key就是@RequestMapping中的字符串，val就是@RequestMapping所在类的所在方法。然后当用户在浏览器访问某个链接url时，先经过Servlet映射，如action结尾的，满足要求后进入DispatcherServlet。按映射规则去掉url中的协议、端口等，最终留下servlet映射后的部分，去map中找有无对应的key，如果有对应的key，就找到了对应类的对应的方法，就执行这个方法；如果没有找到，就抛出404错误。



