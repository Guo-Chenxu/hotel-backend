create table if not exists hotel.customer
(
    id         bigint                               not null comment '主键id'
        primary key,
    name       varchar(50)                          not null comment '顾客名',
    room       bigint                               not null comment '房间号',
    start_time timestamp  default CURRENT_TIMESTAMP not null comment '入住时间',
    leave_time timestamp  default CURRENT_TIMESTAMP not null comment '离店时间',
    deleted    tinyint(1) default 0                 not null comment '软删除'
)
    comment '顾客表';

create index deleted_idx
    on hotel.customer (deleted)
    comment '软删除';

create index name_room_idx
    on hotel.customer (name, room)
    comment '名字房间号索引';

create table if not exists hotel.food
(
    id      bigint               not null comment '主键id'
        primary key,
    name    varchar(50)          not null comment '食物名',
    price   decimal              not null comment '价格',
    img     varchar(255)         null comment '照片',
    deleted tinyint(1) default 0 not null comment '软删除'
)
    comment '食物表';

create index deleted_idx
    on hotel.food (deleted);

create table if not exists hotel.room
(
    id          bigint auto_increment comment '主键id'
        primary key,
    price       decimal               not null comment '价格/晚',
    temperature float      default 27 not null comment '房间当前温度',
    deleted     tinyint(1) default 0  not null comment '软删除'
)
    comment '房间';

create index deleted_idx
    on hotel.room (deleted)
    comment '软删除';

create table if not exists hotel.staff
(
    id         bigint                               not null comment '主键id'
        primary key,
    username   varchar(50)                          not null comment '用户名',
    password   varchar(255)                         not null comment '员工密码',
    permission varchar(25)                          not null comment '权限',
    create_at  timestamp  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '创建日期',
    deleted    tinyint(1) default 0                 not null comment '软删除',
    constraint user_pwd
        unique (username, password)
)
    comment '酒店服务人员表';

create index deleted_idx
    on hotel.staff (deleted)
    comment '软删除';

