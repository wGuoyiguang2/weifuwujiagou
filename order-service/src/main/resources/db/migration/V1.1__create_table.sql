create table  user
(
    id        bigint                                   not null comment '主键id'
        primary key,
    username  varchar(64)  default ''                  not null comment '用户名',
    password  varchar(255) default ''                  not null comment '密码',
    email     varchar(64)  default ''                  not null comment '邮箱',
    mobile    varchar(11)  default ''                  not null comment '手机号',
    realname  varchar(32)  default ''                  not null comment '真实姓名',
    remark    varchar(255) default ''                  not null comment '备注',
    create_at datetime     default current_timestamp() not null comment '创建时间',
    update_at datetime     default current_timestamp() not null comment '修改时间',
    deleted   tinyint(1)   default 0                   not null comment '删除标记 0 未删除，1 删除',
    status    tinyint(1)   default 1                   not null comment '状态 0未启用，1启用',
    address   varchar(255) default ''                  not null comment '地址',
    create_by bigint       default 0                   not null comment '创建人',
    update_by bigint       default 0                   not null comment '修改人'
)
    comment '员用户信息表' charset = utf8mb4;

