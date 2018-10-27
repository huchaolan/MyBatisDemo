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