

create table user (
                    id bigint,
                    name varchar(100),
                    password varchar(100),
                    enable char(1),
                    primary key (id)
);
create table role(
                   id int(10),
                   roleDesc varchar(100),
                   primary key (id)
);
create table user_role(
                        user_id bigint,
                        role_id int(10),
                        foreign key (user_id) references user(id),
                        foreign key (role_id) references role(id),
                        primary key (user_id, role_id)
);
create table permission(
                         id int(10),
                         name varchar(20),
                         primary key (id)
);
create table role_permission(
                              role_id int(10),
                              permission_id int(10),
                              primary key (role_id, permission_id),
                              foreign key (role_id) references role(id),
                              foreign key (permission_id) references permission(id)
);
insert into permission(id, name)
values
(1000, 'read'),
(1001, 'write'),
(1002, 'execute');

show variables like 'time_zone';
set global time_zone='+8:00'

alter table user_role drop foreign key user_role_ibfk_1;
alter table user_role drop foreign key user_role_ibfk_2;
alter table user_role add foreign key (user_id) references user(id) on delete cascade ;
alter table user_role add foreign key (role_id) references role(id) on delete cascade ;

alter table role_permission drop foreign key role_permission_ibfk_1;
alter table role_permission drop foreign key role_permission_ibfk_2;
alter table role_permission add foreign key (role_id) references role(id) on delete cascade ;
alter table role_permission add foreign key (permission_id) references permission(id) on delete cascade ;

alter table user add constraint unique (name);

select * from user;
delete from user;

set global time_zone='+8:00';
insert into user_role(user_id, role_id) values
                                               (1543735282404, 5000),
                                               (1543735282404, 5001),
                                               (1543735282404, 5002);
