# MyBatis

![](http://www.mybatis.org/images/mybatis-logo.png)

## 什么是mybatis

mybatis它是轻量级持久层框架，由ibatis演化而来。它完成将数据库的结果集封装到对象中POJO。业务层控制层和使用Hibernate框架一样。Hibernate基于hql是完全面向对象。全自动ORM。Mybatis基于sql是半面向对象。半自动的ORM。

## 和Hibernate比较

|  hibernate |基于HQL   |   |
| ------------ | ------------ | ------------ |
|  核心配置文件 |POJO实体对象   |SessionFactory   |
| hibernate.cfg.xml  |映射文件User.hbm.xml   |session   |

|  MyBatis |基于SQL   |   |
| ------------ | ------------ | ------------ |
|  核心配置文件 |POJO实体对象   |SqlSessionFactory   |
| sqlMapConfig.xml  |映射文件UserMapper.xml   |Sqlsession   |

## mybatis内部对象结构图


|  执行的过程 | 执行的内容  | 执行的过程  |
| ------------ | ------------ | ------------ |
|   |  核心配置文件：sqlMapConfig.xml（事务,数据源，全局参数）  | ↓  |
|   |   映射文件配置文件：xxxMapper.xml(配置pojo和数据表之间的关系的SQL语句) |   ↓|
|   | sqlSessionFactory(会话工厂,创建sqlsesion,一级缓存线程安全)  |  ↓ |
|   |  sqlSession(会话,操作数据库，发出sql增删改差，二级缓存，配置第三方二级缓存,线程不安全) |   ↓ |
|  封装的参数-> |  Executor执行器(sqlsession内部通过执行器操作数据库) |->结果封装到对象   |

## 进一步了解mybatis
#### -->[简单的入门Demo](https://github.com/ScureHu/hibernate/blob/master/mybatis/demo.md)
#### -->[配置标签详解](https://github.com/ScureHu/hibernate/blob/master/mybatis/lable.md)
#### -->[mybatis的DAO层开发策略](https://github.com/ScureHu/hibernate/blob/master/mybatis/daoDevel.md)
#### -->[mybatis的多表开发](https://github.com/ScureHu/hibernate/blob/master/mybatis/tableDev.md)
#### -->[mybatis的延迟加载](https://github.com/ScureHu/hibernate/blob/master/mybatis/延迟加载.md)
#### -->[mybatis的缓存](https://github.com/ScureHu/hibernate/blob/master/mybatis/缓存.md)
