# SpringMVC
![](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538817490825&di=dd400f0821b7f0ba34e11d42e6d992d7&imgtype=0&src=http%3A%2F%2Fincdn1.b0.upaiyun.com%2F2015%2F04%2Fb3f9d25891a67e53f08ae6023f03a418.png)
## 什么是springMvc?

> Spring Web MVC是一种基于Java的实现了Web MVC设计模式的请求驱动类型的轻量级Web框架，即使用了MVC架构模式的思想，将web层进行职责解耦，基于请求驱动指的就是使用请求-响应模型，框架的目的就是帮助我们简化开发，Spring Web MVC也是要简化我们日常Web开发的。Spring Web MVC也是服务到工作者模式的实现，但进行可优化。而且springMvc是spring框架的一个模块,springMVC和spring无需通过中间整合层进行整合。

前端控制器是DispatcherServlet；应用控制器其实拆为处理器映射器(Handler Mapping)进行处理器管理和视图解析器(View Resolver)进行视图管理；页面控制器/动作/处理器为Controller接口（仅包含ModelAndView handleRequest(request, response) 方法）的实现（也可以是任何的POJO类）；支持本地化（Locale）解析、主题（Theme）解析及文件上传等；提供了非常灵活的数据验证、格式化和数据绑定机制；提供了强大的约定大于配置（惯例优先原则）的契约式编程支持。

## 什么是mvc?
> mvc是一种设计模式

![](https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1539412400&di=5ecce5c4a8da002f1a4f172ab6b4248d&imgtype=jpg&er=1&src=http%3A%2F%2Fs2.sinaimg.cn%2Fmw690%2F001Zi52bgy6JCNyWBnr71%26amp%3B690)

## springMVC的框架图

![](http://sishuok.com/forum/upload/2012/7/14/57ea9e7edeebd5ee2ec0cf27313c5fb6__2.JPG)

- 第一步：用户发起请求到前端控制器（DispatcherServlet）
- 第二步：前端控制器请求HandlerMapper查找Handler(可以根据xml配置,注解进行查找),处理器映射器HandlerMapping向前端控制器返回Handler(其实返回的是一个执行链)
- 第三步：前端控制器调用处理器适配器
- 第四步：处理器适配器去执行Handler（我们写的controller类）,向前端控制器返回ModelAndView(ModelAndView是springMvc的一个底层对象,包括Model和view)
- 第五步：前端控制器请求视图解析器去进行视图解析,根据逻辑视图解析成真正的视图（jsp）,视图解析器向前端控制器返回view
- 第六步:前端控制器进行试图渲染,将试图渲染模型数据填充到request域中

## springMVC重要组件作用
- 前端控制器（DispatcherServlet）:接收请求,响应结果,相当于转发器,中央处理器，减少与其他组件的耦合度

- 处理器映射器（HandlerMapping）:根据请求的url查找Handler（controller类）

- 处理器适配器（HandlerAdapter）:按照特定规则（HandlerAdapter要求的规则）去执行Handler（controller类）（编写Handler时按照HandlerAdapter的要求去做,这样适配器才可以正确执行Handler）

- 视图解析器（View resolver）:进行视图解析,根据逻辑视图名解析成真正的视图（view）

- 视图(View)：View是一个接口,实现类支持不同的View类型（jsp,freemaker,pdf...）
