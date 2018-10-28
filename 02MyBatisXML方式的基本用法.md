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

## insert用法

## update用法

## delete用法

## 多个接口参数的用法

## Mapper接口动态代理实现原理