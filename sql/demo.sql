DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL,
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(128) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0:禁用 1:正常 2:锁定',
  `password_time` datetime DEFAULT NULL COMMENT '密码时间',
  `password_ip` int(10) DEFAULT NULL COMMENT '密码ip',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` int(10) NOT NULL COMMENT '注册IP',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `login_ip` int(10) DEFAULT NULL COMMENT '登录IP',
  PRIMARY KEY (`id`),
  KEY `email` (`email`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

DROP TABLE IF EXISTS `sys_office`;
CREATE TABLE `sys_office` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父部门ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1正常 2删除',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `operation_id` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统部门表';

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `permission` varchar(255) NOT NULL COMMENT '权限关键字',
  `sort` int(20) DEFAULT NULL COMMENT '排序ASC',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `hidden` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否显示 0显示1隐藏',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1正常 2删除',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `operation_id` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统权限表';


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `office_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1正常 2删除',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `operation_id` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色表';

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `permission_id` bigint(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1正常 2删除',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `permission_id` (`permission_id`),
  KEY `role_id_p` (`role_id`),
  KEY `operation_id` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色权限表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL,
  `office_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `login_name` varchar(128) NOT NULL COMMENT '昵称',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0:禁用 1:正常 2:锁定',
  `password_time` datetime DEFAULT NULL COMMENT '密码时间',
  `password_ip` int(10) DEFAULT NULL COMMENT '密码ip',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `login_ip` int(10) DEFAULT NULL COMMENT '登录IP',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` int(10) NOT NULL COMMENT '注册IP',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `login_name` (`login_name`),
  KEY `email` (`email`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统用户表';

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1正常 2删除',
  `operation_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `role_id_p` (`role_id`),
  KEY `operation_id` (`operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统用户角色表';