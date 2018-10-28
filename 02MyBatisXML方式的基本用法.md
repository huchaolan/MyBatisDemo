# MyBatisXML的基本用法

采用RBAC(Role-Based Access Control,基于角色的访问控制)的示例，完成权限管理的常见业务类学习MyBatisXML方式的基本用方法

## 一个简单的权限控制需求

一个用户用哦于若干角色，一个角色拥有若干权限，权限对某个资源的某种操作，构成了用户-角色-权限的授权模型。

### 数据库表

```SQL
create table sys_user(
    id bigint not null auto_increment comment '用户ID',
    user_name varchar(50) comment '用户名',
    user_password varchar(50) comment '密码',
    user_email varchar(50) comment '邮箱',
    user_info text comment '简介',
    head_img blob comment '头像',
    create_time datetime comment '创建时间',
    primary key (id)
);
alter table sys_user comment '用户表';

create table sys_role(
    id bigint not null auto_increment comment '角色ID',
    role_name varchar(50) comment '角色名',
    enabled int comment '有效标志',
    create_by bigint comment '创建人',
    create_time datetime comment '创建时间',
    primary key (id)
);
alter table sys_user comment '角色表';

create table sys_privilege(
    id bigint not null auto_increment comment '权限ID',
    privilege_name varchar(50) comment '权限名称',
    privilege_url varchar(200) comment '权限URL',
    primary key (id)
);
alter table sys_privilege comment '权限表';

create table sys_user_role(
    user_id bigint comment '用户ID',
    role_id bigint comment '角色ID'
);
alter table sys_user_role comment '用户角色关联表';

create table sys_role_privilege(
    role_id bigint comment '角色ID',
    privilege_id bigint comment '权限ID'
);
alter table sys_role_privilege comment '角色权限关联表';

INSERT INTO `sys_user` VALUES('1','admin','123456','admin@mybatis.tk','管理员',null,'2016-04-01 17:00:58');
INSERT INTO `sys_user` VALUES('1001','test','123456','test@mybatis.tk','测试用户',null,'2016-04-01 17:01:58');

INSERT INTO `sys_role` VALUES('1','管理员','1','1','2016-04-01 17:02:58');
INSERT INTO `sys_role` VALUES('2','普通用户','1','1','2016-04-01 17:02:58');

INSERT INTO `sys_user_role` VALUES('1','1');
INSERT INTO `sys_user_role` VALUES('1','2');
INSERT INTO `sys_user_role` VALUES('1001','2');

INSERT INTO `sys_privilege` VALUES('1','用户管理','/users');
INSERT INTO `sys_privilege` VALUES('2','角色管理','/roles');
INSERT INTO `sys_privilege` VALUES('3','系统日志','/logs');
INSERT INTO `sys_privilege` VALUES('4','人员维护','/persons');
INSERT INTO `sys_privilege` VALUES('5','单位维护','/companies');

INSERT INTO `sys_role_privilege` VALUES('1','1');
INSERT INTO `sys_role_privilege` VALUES('1','3');
INSERT INTO `sys_role_privilege` VALUES('1','2');
INSERT INTO `sys_role_privilege` VALUES('2','4');
INSERT INTO `sys_role_privilege` VALUES('2','5');
```

### 创建实体类

MyBatis默认是**遵循下划线转驼峰**的命名方式,比如数据字段：user_info，那么实体bean：userInfo,实际上所有字段可以通过resultMap配置。在写实体类的过程中注意：

+ 对于Blob和LongVarbinary保存二进制的字段类型要使用byte[]访问。
+ 为了避免初始化值对于基本类型要使用其包装类

## 使用XML方式

### 创建对应的xml文件

在src/main/resources目录下的tk.mybatis.simple.mapper目录下创建xml文件。分别对应上面的各个表，名称分别是PrivilegeMapper.xml，RoleMapper.xml，RolePrivilege.xml，UserMapper.xml，UserRoleMapper.xml，大致内容如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="tk.mybatis.simple.mapper.RolePrivilege">
</mapper>
```

注意：namespace属性的值是定义接口的全名称限定

### 创建对应的接口

在src/main/java目录下创建tk.mybatis.simple.mapper包，并且创建xml相同名称的接口:PrivilegeMapper.java,RoleMapper.java,RolePrivilege.java,UserMapper.java,UserRoleMapper.java

### 向MyBatis添加Mapper文件

在mybatis-config.xml的mappers元素添加新增的xml文件。添加方式有两种：1.一个个添加,2.添加一个包名,mybatis会扫描

```xml
<mappers>
    <mapper resource="tk/mybatis/simple/mapper/PrivilegeMapper.xml" />
    <mapper resource="tk/mybatis/simple/mapper/RoleMapper.xml" />
    <mapper resource="tk/mybatis/simple/mapper/RolePrivilege.xml" />
    <mapper resource="tk/mybatis/simple/mapper/UserMapper.xml" />
    <mapper resource="tk/mybatis/simple/mapper/UserRoleMapper.xml" />
</mappers>
```

```xml
<mappers>
    <package name="tk.mybatis.simple.mapper" />
</mappers>
```

这种配置方式会先查找tk.mybatis.simple.mapper包下所有的接口，循环对接口进行如下操作

1. 判断接口对应的命名空间是否已经存在，如果存在就抛出异常，不存在就继续进行接下来的操作。
2. 加载接口对应的XML映射文件，将接口全限定名转换为路径，例如，将接口tk.mybatis.simple.mapper.UserMapper转换为tk/mybatis/simple/mapper/UserMapper.xml，以.xml为后缀搜索XML资源，如果找到就解析XML。
3. 处理接口中的注解方法。

## select用法

为UserMapper接口添加查询方法

```java
public interface UserMapper {
    public SysUser selectById(Long id);
}
```

在UserMapper.xml添加resultMap和select元素，用于结果集映射和查询

```xml
<mapper namespace="tk.mybatis.simple.mapper.UserMapper">
    <resultMap id="userMap" type="tk.mybatis.simple.model.SysUser">
        <id property="id" column="id" />
        <result property="username" column="user_name" />
        <result property="userPassword" column="user_password" />
        <result property="userEmail" column="user_email" />
        <result property="userInfo" column="user_info" />
        <result property="headImg" column="head_img"  jdbcType="BLOB"/>
        <result property="createTime" column="create_time" jdbcType="TIMESTAMP"/>
    </resultMap>
    <select id="selectById" resultMap="userMap">
        select * from sys_user where id=#{id}
    </select>
</mapper>
```

MyBatis使用select元素的id和接口的名称一致，通过这种方式将把方法调用和sql联系在一起。命名规则如下：

1. 当namespace不使用接口，只要不重复即可
2. id属性值不能有.且不能重复
3. 接口方法可以重载导致出现方法名称相同但参数不同的方法，但是xml中id不能有重复的，会出现多个方法对应一个xml的id

### select元素

+ select元素映射查询语句使用的标签
+ id命名空间的唯一表示
+ resultMap用于返回值的类型和映射关系，属性值是resultMap元素的id
+ select &lowast; form sys_user where id=#{id}是查询语句，使用*查询所有列时MyBatis也可以进行映射
+ #{id}是预编译参数的一种方式，{id}表示传入的参数名

### resultMap元素

+ resultMap是一种配置结果映射的方法
+ id：必填，并且唯一。
+ type：必填，用于配置查询列所映射到的Java对象类型
+ extends：选填，当前resultMap继承其他的resultMap
+ autoMapping:选填，用于配置是否启用非映射字段的自动映射功能，可以覆盖全局的autoMappingBehavior配置。

resultMap的子元素：

+ constructor：配置使用构造方法注入结果，包含idArg和arg两个元素
+ id：一个id结果，标记结果作为id，可以帮助提高整体性能
+ result：注入到Java对象属性的普通结果
+ association:复杂的类型关联，许多结果将包成的类型
+ collection：复杂类型的集合
+ discriminator:根据结果值决定使用哪个结果映射
+ case 基于值的结果映射

id和result包含的属性：

+ column:从数据库中得到列名或者它的别名
+ property:java对象的属性名称，可以使用点分的形式表示嵌套对象的属性:address.street.number.
+ javaType:Java类的完全限定名称或者类型别名，通过typeAlias配置或者默认的类型,映射到javaBean，会自动判断是属性类型，映射Map需要指定javaType属性。
+ jdbcType:列对应的数据库类型，JDBC类型仅仅需要对插入，更新，删除操作可能为空的列进行处理。
+ typeHandler：默认类型处理器

### 定义返回值类型

结果定义的返回值类型必须和XML中配置的resultType类型一致，否则就抛出异常。**返回值类型是有XML中resultType或者resultMap中type决定的，不是有接口中写的返回值类型决定的**，比如：新增查询全部用户的方法

```java
public List<SysUser> selectAllUser();
```

对应的xml

```xml
<select id="selectAllUser" resultType="tk.mybatis.simple.model.SysUser">
    select id,
        user_name userName,
        user_password userPassword,
        user_email userEmail,
        user_info userInfo,
        head_img headImg,
        create_time createTime
    from sys_user
</select>
```

MyBatis中如果结果定义是一个对象，而查询结果是多个对象那么就会抛出TooManyResultsException异常。
这里使用resultType和resultMap的不同是在于resultType需要在sql使用别名来调整sql中列名和javabean的属性名不一致的情况

### 名称映射规则

property属性或者别名要和对象属性的名字相同，MyBatis会先将两者转换成大写形式，然后判断是否相同，所以usename和userName是一个属性，不需要考虑大小写是否一致。
数据库的字段由于设置不区分大小写，常常使用下划线的命名很常见，user_mail,而java中常常使用驼峰式命名，MyBatis提供了mapUnderscoreToCamelCase属性配置是否自动将下划线方式转换成驼峰方式。

### 测试相关方法

```java
UserMapper userMapper = session.getMapper(UserMapper.class);
SysUser user = userMapper.selectById(1L);
List<SysUser> userList = userMapper.selectAllUser();
```

通过session.getMapper方法可以返回代理类，使用代理可以直接查询数据库并将结果集映射成javabean返回

### 查询复杂结果集

对于嵌套javaBean有两种方式：对于少量字段需要处理的，可以新建一个类，继承原来的javaBean，如果字段很多那么就需要改造原有的javaBean，比如：在SysRole中需要增加用户的信息。

```xml
<select id="selectRoleById" resultType="tk.mybatis.simple.model.SysRole">
    SELECT r.id,
        r.role_name roleName,
        r.enabled,
        r.create_by createBy,
        r.create_time createTime,
        u.user_name 'user.userName',
        u.user_email 'user.userEmail'
    FROM
        sys_role r
    INNER JOIN sys_user_role ur ON ur.role_id = r.id
    INNER JOIN sys_user u ON u.id = ur.user_id
    WHERE
        u.id = #{userid}
</select>
```

在别名中设置user.userName就可以设置嵌套对象了，上面的配置只写入用户的邮箱和名称

## insert用法

## update用法

## delete用法

## 多个接口参数的用法

## Mapper接口动态代理实现原理