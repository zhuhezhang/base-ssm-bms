## 1 整合SSM

### 1.1 整合的思路

<img src="C:/Users/zhuhezhang/Desktop/狂神说java笔记和源码/SpringMVC.assets/1586921554980.png" alt="1586921554980" style="zoom: 50%;float:left" />

#### ① spirng与mybatis整合

- mapper 和映射文件，数据源交给spring管理
- ==spring与mybatis整合，jar的类来管理==
- 创建mapper对象

#### ②spring与service整合

- service对象交给spring管理：扫描service包
- 创建service对象
- service可以调用mapper对象 
- service层要使用spring的声明式事务

#### ③spring与springmvc整合

- 无缝整合
- handler交给spring管理
- handler对象可以调用service对象

### 1.2  数据库环境

创建一个存放书籍数据的数据库表

```sql
CREATE DATABASE `ssmbuild`;
USE `ssmbuild`;
DROP TABLE IF EXISTS `books`;
CREATE TABLE `books` (
  `bookID` INT(10) NOT NULL AUTO_INCREMENT COMMENT '书id',
  `bookName` VARCHAR(100) NOT NULL COMMENT '书名',
  `bookCounts` INT(11) NOT NULL COMMENT '数量',
  `detail` VARCHAR(200) NOT NULL COMMENT '描述',
  KEY `bookID` (`bookID`)
) ENGINE=INNODB DEFAULT CHARSET=utf8
INSERT  INTO `books`(`bookID`,`bookName`,`bookCounts`,`detail`)VALUES 
(1,'Java',1,'从入门到放弃'),
(2,'MySQL',10,'从删库到跑路'),
(3,'Linux',5,'从进门到进牢');
```

### 1.3  基本环境搭建

#### 一、搭建框架

##### ①拷jar

1. 数据库的jar

   - 数据库驱动jar

     ```css
     ojdbc14.jar
     ```

   - 连接池jar

     - c3p0

       ```css
       c3p0-0.9.5.2.jar
       mchange-commons-java-0.2.11.jar
       ```

   - dbcp

     ```css
     commons-dbcp-1.2.1.jar
     commons-pool-1.2.jar
     ```

2. mybatis的jar

   ```css
   1og4j-1.2.17.jar 
   mybatis-3.4.2.jar
   ```

   - 懒加载

     ```css
     asm-3.3.1.jar
     cglib-2.2.2.jar 
     ```

3. spring的15个jar

   ```css
   com.springsource.net.sf.cglib-2.2.0.jar
   com.springsource.org.apache.commons.logging-1.1.1.jar
   com.springsource.org.aspectj.weaver-1.6.8.RELEASE.jar
   log4j-1.2.17.jar
   spring-aop-4.3.9.RELEASE.jar
   spring-aspects-4.3.9.RELEASE.jar
   spring-beans-4.3.9.RELEASE.jar
   spring-context-4.3.9.RELEASE.jar
   spring-core-4.3.9.RELEASE.jar
   spring-expression-4.3.9.RELEASE.jar
   spring-jdbc-4.3.9.RELEASE.jar
   spring-orm-4.3.9.RELEASE.jar
   spring-tx-4.3.9.RELEASE.jar
   spring-web-4.3.9.RELEASE.jar
   spring-webmvc-4.3.9.RELEASE.jar
   ```

4. springmvc的jar

   - 文件上传

     ```css
     commons-fileupload-1.3.1.jar
     commons-io-2.2.jar
     ```

   - jackson

     ```css
     jackson-core-as1-1.9.11.jar
     jackson-mapper-as1-1.9.11.jar
     ```

   - jsr303：validation

     ```css
     validation-api-1.1.0.Final.jar
     hibernate-validator-5.2.0.Final.jar
     jboss-logging-3.2.1.Final.jar
     classmate-1.1.0.jar
     ```

5. spring和mybatis整合jar

   ```css
   mybatis-spring-1.3.0.jar
   ```

##### ② 属性文件

```css
1og4j.properties
oracleDB.properties
```

##### ③ 拷核心文件

1. ==mybatis核心文件==

   - **mybatis.xml**

     ```css
     <!--程序执行路径-->
     <settings>
         <setting name="lazyLoadingEnabled" value="true" />
         <setting name=" aggressiveLazyLoading" value="false" />
         <setting name= " autoMappingBehavior" value="PARTIAL" />
     </settings>
     <!--别名-->
     <typeAliases>
         <package name=" com. java05. model"/>
     </typeAliases>
     ```

2. ==spring的核心文件==

   - **applicationContext-mapper.xml** 
   - **applicationContext-service.xml** 
   - **applicationContext-trans.xml** 

3. ==springmvc的核心文件==

   - **springmvc.xml** 

------

#### 二、Maven 搭建框架

##### ① 导入pom依赖

新建一Maven项目！ ssmbuild ， 添加web的支持

导入相关的pom依赖！

```xml
<dependencies>
    <!--Junit-->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
    </dependency>
    <!--数据库驱动-->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.47</version>
    </dependency>
    <!-- 数据库连接池 -->
    <dependency>
        <groupId>com.mchange</groupId>
        <artifactId>c3p0</artifactId>
        <version>0.9.5.2</version>
    </dependency>

    <!--Servlet - JSP -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>2.5</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>
        <version>2.2</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>

    <!--Mybatis-->
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>3.5.2</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>2.0.2</version>
    </dependency>

    <!--Spring-->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>5.1.9.RELEASE</version>
    </dependency>
</dependencies>
```

##### ② Maven资源过滤设置

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
        <resource>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
</build>
```

##### ③ 建立基本结构和配置框架！

- com.kuang.pojo

- com.kuang.dao

- com.kuang.service：处理事务

- com.kuang.controller

- mybatis-config.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-config.dtd">
  <configuration>
  
  </configuration>
  ```

- applicationContext.xml

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans.xsd">
  
  </beans>
  ```

### 1.4  Mybatis层

#### ① 数据库配置文件

```properties
# database.properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/ssmbuild?useSSL=true&useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=123456
```

#### ② IDEA关联数据库

#### ③ MyBatis的核心配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    
    <typeAliases>
        <package name="com.kuang.pojo"/>
    </typeAliases>
    <mappers>
        <mapper resource="com/kuang/dao/BookMapper.xml"/>
    </mappers>

</configuration>
```

#### ④ 数据库对应的实体类

使用lombok插件！

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Books {    
    private int bookID;
    private String bookName;
    private int bookCounts;
    private String detail;    
}
```

#### ⑤ Dao层的 Mapper接口

```java
package com.kuang.dao;

import com.kuang.pojo.Books;
import java.util.List;

public interface BookMapper {

    //增加一个Book
    int addBook(Books book);

    //根据id删除一个Book
    int deleteBookById(int id);

    //更新Book
    int updateBook(Books books);

    //根据id查询,返回一个Book
    Books queryBookById(int id);

    //查询全部Book,返回list集合
    List<Books> queryAllBook();

}
```

#### ⑥ 接口对应的 Mapper.xml 

- 需要导入MyBatis的包

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.kuang.dao.BookMapper">

    <!--增加一个Book-->
    <insert id="addBook" parameterType="Books">
        insert into ssmbuild.books(bookName,bookCounts,detail)
        values (#{bookName}, #{bookCounts}, #{detail})
    </insert>

    <!--根据id删除一个Book-->
    <delete id="deleteBookById" parameterType="int">
        delete from ssmbuild.books where bookID=#{bookID}
    </delete>

    <!--更新Book-->
    <update id="updateBook" parameterType="Books">
        update ssmbuild.books
        set bookName = #{bookName},bookCounts = #{bookCounts},detail = #{detail}
        where bookID = #{bookID}
    </update>

    <!--根据id查询,返回一个Book-->
    <select id="queryBookById" resultType="Books">
        select * from ssmbuild.books
        where bookID = #{bookID}
    </select>

    <!--查询全部Book-->
    <select id="queryAllBook" resultType="Books">
        SELECT * from ssmbuild.books
    </select>

</mapper>
```

#### ⑦ Service层的接口和实现类

##### 接口：

```java
//BookService:底下需要去实现,调用dao层
public interface BookService {
    //增加一个Book
    int addBook(Books book);
    //根据id删除一个Book
    int deleteBookById(int id);
    //更新Book
    int updateBook(Books books);
    //根据id查询,返回一个Book
    Books queryBookById(int id);
    //查询全部Book,返回list集合
    List<Books> queryAllBook();
}
```

##### 实现类：

```java
package com.kuang.service;

import com.kuang.dao.BookMapper;
import com.kuang.pojo.Books;
import java.util.List;

public class BookServiceImpl implements BookService {

    //调用dao层的操作，设置一个set接口，方便Spring管理
    private BookMapper bookMapper;

    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }
    
    public int addBook(Books book) {
        return bookMapper.addBook(book);
    }
    
    public int deleteBookById(int id) {
        return bookMapper.deleteBookById(id);
    }
    
    public int updateBook(Books books) {
        return bookMapper.updateBook(books);
    }
    
    public Books queryBookById(int id) {
        return bookMapper.queryBookById(id);
    }
    
    public List<Books> queryAllBook() {
        return bookMapper.queryAllBook();
    }
}
```

**OK，到此，底层需求操作编写完毕！**

### 1.5  Spring层

#### ① Spring整合MyBatis

- 我们这里数据源使用c3p0连接池；

- 我们去编写Spring整合Mybatis的相关的配置文件； spring-dao.xml

  1. 关联数据库文件

  2. 连接数据库
  3. 配置SqlSessionFactory对象
     - 注入数据库连接池
     - 配置MyBaties全局配置文件:mybatis-config.xml

  4. 配置扫描Dao接口包，动态实现Dao接口注入到spring容器中：MapperScannerConfigurer
     - 注入sqlSessionFactory
     - ==扫描Dao接口包==

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/context
https://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 配置整合mybatis -->
    <!-- 1.关联数据库文件 -->
    <context:property-placeholder location="classpath:database.properties"/>

    <!-- 2.数据库连接池 -->
    <!--数据库连接池
    dbcp  半自动化操作  不能自动连接
    c3p0  自动化操作（自动的加载配置文件 并且设置到对象里面）
    -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!-- 配置连接池属性 -->
        <property name="driverClass" value="${jdbc.driver}"/>
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="user" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>

        <!-- c3p0连接池的私有属性 -->
        <property name="maxPoolSize" value="30"/>
        <property name="minPoolSize" value="10"/>
        <!-- 关闭连接后不自动commit -->
        <property name="autoCommitOnClose" value="false"/>
        <!-- 获取连接超时时间 -->
        <property name="checkoutTimeout" value="10000"/>
        <!-- 当获取连接失败重试次数 -->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>

    <!-- 3.配置SqlSessionFactory对象：配置连接数据库的驱动，URL，账号和密码-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 配置MyBaties全局配置文件:mybatis-config.xml -->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
    </bean>

    <!-- 4.配置扫描Dao接口包，动态实现Dao接口注入到spring容器中 -->
    <!--解释 ： https://www.cnblogs.com/jpfss/p/7799806.html-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 注入sqlSessionFactory -->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!-- 给出需要扫描Dao接口包 -->
        <property name="basePackage" value="com.kuang.dao"/>
    </bean>
</beans>
```

- **MapperScannerConfigurer 自动扫描并创建的是java 的mapper文件，即编译后的class文件，并不是xml文件**

- 可以使用一个 MapperScannerConfigurer , 它 将 会 查 找 类 路 径 下 的 映 射 器 并 自 动 将 它 们 创 建 成 MapperFactoryBean。没 有 必 要 去 指 定 SqlSessionFactory 或 SqlSessionTemplate , 因 为 MapperScannerConfigurer 将会创建 MapperFactoryBean,之后自动装配

  

- **mappper接口可以直接调用的原因？**

  1. 为了代替手工使用 SqlSessionDaoSupport 或 SqlSessionTemplate **编写数据访问对象** (DAO)的代码,**MyBatis-Spring** 提供了一个动态代理的实现:MapperFactoryBean。
  2. 这个类 可以让你直接注入数据映射器接口到你的 service 层 bean 中。当使用映射器时,你仅仅如调 用你的 DAO 一样调用它们就可以了,但是你不需要编写任何 DAO 实现的代码,因为 MyBatis-Spring 将会为你创建代理。
  3. 上面的配置有一个很大的缺点，就是系统有很多的配置文件时 全部需要手动编写，所以上述的方式替换成MapperScannerConfigurer 形式的配置

#### ② Spring整合Service层

==**Spring 的核心功能：IOC 和 AOP**==

1. ==扫描service==
2. 注入到IOC容器中：使用注解方式，就不需要写这个bean
3. ==事务管理器==：提交，回滚

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 扫描service -->
    <context:component-scan base-package="com.kuang.service" />

    <!--BookServiceImpl注入到IOC容器中：使用注解方式，就不需要写这个bean-->
    <bean id="BookServiceImpl" class="com.kuang.service.BookServiceImpl">
        <property name="bookMapper" ref="bookMapper"/>
    </bean>

    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource" />
    </bean>

</beans>
```

Spring层搞定！再次理解一下，Spring就是一个大杂烩，一个容器！对吧！

### 1.6  SpringMVC层

#### ① web.xml

```xml
<!--配置Spring框架的信息-->
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/spring-*.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<!--SpringMVC的调度Servlet，前端控制器-->
<servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/springmvc.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
<filter>
    <filter-name>encodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>utf-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
<!--Session过期时间-->
<session-config>
    <session-timeout>15</session-timeout>
</session-config>
```

#### ② spring-mvc.xml

1. SpringMVC注解驱动
2. 静态资源
3. 视图解析器
4. ==扫描controller==

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://www.springframework.org/schema/context
    http://www.springframework.org/schema/context/spring-context.xsd
    http://www.springframework.org/schema/mvc
    https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 配置SpringMVC -->
    <!-- 1.开启SpringMVC注解驱动 -->
    <mvc:annotation-driven />
    <!-- 2.静态资源默认servlet配置-->
    <mvc:default-servlet-handler/>

    <!-- 3.配置jsp 显示ViewResolver视图解析器 -->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
        <property name="prefix" value="/WEB-INF/jsp/" />
        <property name="suffix" value=".jsp" />
    </bean>

    <!-- 4.扫描controller-->
    <context:component-scan base-package="com.kuang.controller" />

</beans>
```

#### ③ applicationContext.xml

- Spring配置整合文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <import resource="spring-dao.xml"/>
    <import resource="spring-service.xml"/>
    <import resource="spring-mvc.xml"/>
    
</beans>
```

**配置文件，暂时结束！**

### 1.7 Controller 和 视图层

#### ①  方法一：查询全部书籍

##### Ⅰ、BookController 类

```java
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    @Qualifier("BookServiceImpl")//自动装配把autowire = “byName”
    private BookService bookService;

    @RequestMapping("/allBook")
    public String list(Model model) {
        List<Books> list = bookService.queryAllBook();
        model.addAttribute("list", list);
        return "allBook";
    }
}
```

##### Ⅱ、首页 index.jsp

```html
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>首页</title>
    <style type="text/css">
        a {
            text-decoration: none;
            color: black;
            font-size: 18px;
        }
        h3 {
            width: 180px;
            height: 38px;
            margin: 100px auto;
            text-align: center;
            line-height: 38px;
            background: deepskyblue;
            border-radius: 4px;
        }
    </style>
</head>
<body>

<h3>
 	<!--${pageContext.request.contextPath}-->   
    <a href="${pageContext.request.contextPath}/book/allBook">点击进入列表页</a>
</h3>
</body>
</html>
```

**==${pageContext.request.contextPath}==**

- 是JSP取得==绝对路径==的方法，等价于<%=request.getContextPath()%> 。

- 也就是取出部署的应用程序名或者是当前的项目名称

- 比如我的**项目名称是demo1**在浏览器中输入为**http://localhost:8080/demo1/a.jsp **

- **${pageContext.request.contextPath**}或**<%=request.getContextPath()%>**取出来的就是**/demo1**,

- 而"/"代表的含义就是**http://localhost:8080**

- 故有时候项目中这样写**${pageContext.request.contextPath}/a.jsp**是JSP取得绝对路径的方法，等价于<%=request.getContextPath()%> 


##### Ⅲ、书籍列表页面 allbook.jsp

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>书籍列表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>书籍列表 —— 显示所有书籍</small>
                </h1>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 column">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/book/toAddBook">新增</a>
        </div>
    </div>

    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover table-striped">
                <thead>
                <tr>
                    <th>书籍编号</th>
                    <th>书籍名字</th>
                    <th>书籍数量</th>
                    <th>书籍详情</th>
                    <th>操作</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="book" items="${requestScope.get('list')}">
                    <tr>
                        <td>${book.getBookID()}</td>
                        <td>${book.getBookName()}</td>
                        <td>${book.getBookCounts()}</td>
                        <td>${book.getDetail()}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/book/toUpdateBook?id=${book.getBookID()}">更改</a> |
                            <a href="${pageContext.request.contextPath}/book/del/${book.getBookID()}">删除</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
```

#### ② 方法二：添加书籍

##### Ⅰ、BookController 类

```java
@RequestMapping("/toAddBook")
public String toAddPaper() {
    return "addBook";
}

@RequestMapping("/addBook")
public String addPaper(Books books) {
    System.out.println(books);
    bookService.addBook(books);
    return "redirect:/book/allBook";
}
```

##### Ⅱ、添加书籍页面：**addBook.jsp**

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>新增书籍</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>新增书籍</small>
                </h1>
            </div>
        </div>
    </div>
    <form action="${pageContext.request.contextPath}/book/addBook" method="post">
        书籍名称：<input type="text" name="bookName"><br><br><br>
        书籍数量：<input type="text" name="bookCounts"><br><br><br>
        书籍详情：<input type="text" name="detail"><br><br><br>
        <input type="submit" value="添加">
    </form>
</div>
```

#### ③ 方法三：修改书籍

##### Ⅰ、BookController 类

```java
@RequestMapping("/toUpdateBook")
public String toUpdateBook(Model model, int id) {
    Books books = bookService.queryBookById(id);
    System.out.println(books);
    model.addAttribute("book",books );
    return "updateBook";
}

@RequestMapping("/updateBook")
public String updateBook(Model model, Books book) {
    System.out.println(book);
    bookService.updateBook(book);
    Books books = bookService.queryBookById(book.getBookID());
    model.addAttribute("books", books);
    return "redirect:/book/allBook";
}
```

##### Ⅱ、修改书籍页面  **updateBook.jsp**

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改信息</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- 引入 Bootstrap -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">

    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="page-header">
                <h1>
                    <small>修改信息</small>
                </h1>
            </div>
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/book/updateBook" method="post">
        <input type="hidden" name="bookID" value="${book.getBookID()}"/>
        书籍名称：<input type="text" name="bookName" value="${book.getBookName()}"/>
        书籍数量：<input type="text" name="bookCounts" value="${book.getBookCounts()}"/>
        书籍详情：<input type="text" name="detail" value="${book.getDetail() }"/>
        <input type="submit" value="提交"/>
    </form>

</div>
```

#### ④ 方法四：删除书籍

##### BookController 类

```java
@RequestMapping("/del/{bookId}")
public String deleteBook(@PathVariable("bookId") int id) {
    bookService.deleteBookById(id);
    return "redirect:/book/allBook";
}
```

**配置Tomcat，进行运行！**

到目前为止，这个SSM项目整合已经完全的OK了，可以直接运行进行测试！这个练习十分的重要，大家需要保证，不看任何东西，自己也可以完整的实现出来！

**项目结构图** 

<img src="C:/Users/zhuhezhang/Desktop/狂神说java笔记和源码/Spring.assets/1086489242.png" alt="1570186854191.png" style="zoom:80%;float:left" />

<img src="C:/Users/zhuhezhang/Desktop/狂神说java笔记和源码/Spring.assets/2326667495.png" alt="1570186868239.png" style="zoom:80%;float:left" />

### 小结及展望

第一个SSM整合案例，一定要烂熟于心！

SSM框架的重要程度是不言而喻的，学到这里，已经可以进行基本网站的单独开发。但是这只是增删改查的基本操作。可以说学到这里，才算是真正的步入了后台开发的门。也就是能找一个后台相关工作的底线。