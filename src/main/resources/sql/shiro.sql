# drop database if exists shiro;
# create database shiro;
use shiro;

create table users (
  id bigint auto_increment,
  username varchar(100),
  password varchar(100),
  password_salt varchar(100),
  primary key (id),
  unique (username)
) charset=utf8 ENGINE=InnoDB;

create table user_roles(
  id bigint auto_increment,
  username varchar(100),
  role_name varchar(100),
  primary key (id),
  unique (username, role_name)
);
create table roles_permissions(
  id bigint auto_increment,
  role_name varchar(100),
  permission varchar(100),
  primary key (id),
  unique (role_name, permission)
);
insert into users(username, password) values('zhang','123');



