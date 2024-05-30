create database shoes_mall;
use shoes_mall;

create table if not exists shoes_mall.ad
(
    id bigint auto_increment comment 'id'
        primary key,
    company_name varchar(16) not null comment '公司名',
    price int not null comment '*100价格',
    cover varchar(128) not null comment '图片',
    url varchar(128) not null comment 'url',
    expire_time datetime not null comment '广告有效期',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    comment '广告' charset = utf8
                   row_format = DYNAMIC;

create table if not exists shoes_mall.comments
(
    id bigint auto_increment comment '主键'
        primary key,
    user_id bigint not null comment '用户id',
    pid bigint default 0 null comment '顶级父评论的id',
    product_id bigint not null comment '商品id',
    answer_id bigint default 0 null comment '回复的评论id',
    content varchar(255) not null comment '回复的内容',
    is_delete tinyint default 0 null comment '逻辑删除',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    order_id bigint not null comment '订单号'
)
    comment '帖子评论' charset = utf8
                       row_format = COMPACT;

create table if not exists shoes_mall.cooperation
(
    id bigint auto_increment comment 'id'
        primary key,
    company_name varchar(16) not null comment '公司名',
    url varchar(32) not null comment 'url',
    cover varchar(128) not null comment '图片',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    comment '合作厂家' charset = utf8
                       row_format = DYNAMIC;

create table if not exists shoes_mall.`order`
(
    id bigint unsigned auto_increment comment 'id'
        primary key,
    product_id bigint not null comment '商品id',
    order_no varchar(64) not null comment '订单号',
    user_id bigint not null comment '用户id',
    system_base_setting_size_id bigint not null comment '鞋码id',
    system_base_setting_color_id bigint not null comment '颜色',
    pay_type int default 2 null comment '1-微信支付 2-支付宝',
    total_price int not null comment '订单总价 *100',
    pay_status int default 0 not null comment '支付状态:0.未支付,1.支付成功,-1:支付失败',
    order_status int default 0 not null comment '订单状态:0.待支付 2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单',
    pay_time datetime null comment '支付时间',
    remark varchar(100) default '' not null comment '备注',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    address_id bigint not null comment '地址',
    num int default 1 not null comment '数量'
)
    comment '订单' charset = utf8
                   row_format = DYNAMIC;

create table if not exists shoes_mall.product
(
    id bigint auto_increment comment 'id'
        primary key,
    system_base_setting_brand_id bigint null comment '系统表品牌id',
    system_base_setting_type_id bigint not null comment '版型',
    is_on int default 0 not null comment '上下架 0-下架 1-上架 默认0',
    create_by bigint not null comment '用户id',
    used_price int not null comment '原价价格扩大100',
    now_price int not null comment '现价扩大100',
    title varchar(128) not null comment '标题',
    `desc` text not null comment '描述',
    total_num int not null comment '总货存量',
    rest_num int not null comment '剩余货量',
    cover varchar(128) not null comment '封面',
    main_picture varchar(1024) not null comment '详情主图片 ,分割',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    comment '商品' charset = utf8
                   row_format = DYNAMIC;

create table if not exists shoes_mall.product_color
(
    id bigint auto_increment comment '主键'
        primary key,
    system_base_setting_id bigint not null comment '系统基础配置id',
    product_id bigint not null comment '鞋子id',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete tinyint default 0 null comment '逻辑删除'
)
    comment '鞋颜色' charset = utf8;

create table if not exists shoes_mall.product_size
(
    id bigint auto_increment comment '主键'
        primary key,
    system_base_setting_id bigint not null comment '系统基础配置id',
    product_id bigint not null comment '鞋子id',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete tinyint default 0 null comment '逻辑删除'
)
    comment '鞋子尺码' charset = utf8;

create table if not exists shoes_mall.shopping_cart
(
    id bigint unsigned auto_increment comment 'id'
        primary key,
    user_id bigint not null comment '用户id',
    product_id bigint not null comment '商品id',
    system_base_setting_size bigint not null comment '鞋码',
    system_base_setting_color bigint not null comment '颜色',
    num int not null comment '数量',
    price int not null comment '*100单价',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    comment '购物车' charset = utf8
                     row_format = DYNAMIC;

create table if not exists shoes_mall.system_base_setting
(
    id bigint auto_increment comment '主键'
        primary key,
    type tinyint not null comment '类型 1鞋码 2颜色 3品牌 4鞋类型',
    label varchar(8) not null comment '标签名/类型名',
    ban int default 0 null comment '0-正常 1-禁用',
    deleted tinyint(1) default 0 null comment '已删除(0否 1是)',
    create_by bigint default 0 null comment '操作人',
    create_time datetime default (now()) null comment '创建时间',
    update_time datetime default (now()) null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '系统设置' charset = utf8;

create table if not exists shoes_mall.user
(
    id bigint unsigned auto_increment comment '用户id'
        primary key,
    username varchar(16) not null comment '用户名',
    email varchar(32) not null comment '邮箱',
    password varchar(512) not null comment '密码md5',
    role varchar(16) default 'user' not null comment '权限 admin user',
    gender int not null comment '性别 0-女 1-男 2-未知',
    avatar varchar(128) null comment '头像',
    security_question varchar(128) not null comment '密保问题',
    security_answer varchar(128) not null comment '密保答案',
    default_address_id bigint default 0 not null comment '默认收货地址id',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间',
    ban int default 0 not null comment '禁用 0-正常（默认） 1-禁止',
    phone varchar(16) not null comment '电话'
)
    comment '用户' charset = utf8
                   row_format = DYNAMIC;

create table if not exists shoes_mall.user_address
(
    id bigint unsigned auto_increment comment '收货地址id'
        primary key,
    user_id bigint not null comment '用户id',
    phone varchar(16) not null comment '手机号',
    address varchar(128) not null comment '地址',
    name varchar(16) not null comment '姓名',
    province varchar(16) not null comment '省',
    city varchar(16) not null comment '市',
    district varchar(16) not null comment '区',
    is_deleted int default 0 not null comment '删除标识字段(0-未删除 1-已删除)',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null comment '修改时间'
)
    comment '收货地址' charset = utf8
                       row_format = DYNAMIC;

