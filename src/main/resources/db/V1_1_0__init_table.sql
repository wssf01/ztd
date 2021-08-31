-- ----------------------------
-- Table structure for `t_excel`
-- ----------------------------
DROP TABLE IF EXISTS `t_excel`;
CREATE TABLE `t_excel` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键id',
  `file_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '文件名',
  `excel_content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `oss_path` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'oss存放文件路径',
  `excel_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模版文件名',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='excel导出文件表';
-- ----------------------------
-- Records of t_excel
-- ----------------------------

-- ----------------------------
-- Table structure for `t_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `menu_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `perms` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权标识',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_menu_menu_name` (`menu_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='菜单表';
-- ----------------------------
-- Records of t_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `role_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色描述',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_role_role_name` (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色表';
-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'admin', '管理员',null,null);

-- ----------------------------
-- Table structure for `t_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `role_id` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色id',
  `menu_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_role_menu_role_id` (`role_id`) USING BTREE,
  KEY `idx_role_menu_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色权限表';
-- ----------------------------
-- Records of t_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `login_name` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录名',
  `password` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `gender` int(1) DEFAULT '0' COMMENT '性别',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机',
  `identity_card` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证',
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
  `user_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名',
  `user_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  UNIQUE KEY `uk_user_phone` (`phone`) USING BTREE,
  UNIQUE KEY `uk_user_login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'admin', '1110', '0', '123@bike.com', '17681820992', null, null, null, 'NORMAL',null,null );

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `role_id` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色id',
  `user_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_user_role_role_id` (`role_id`) USING BTREE,
  KEY `idx_user_role_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色表';
-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', '1', '1',null,null);

-- ----------------------------
-- Table structure for `t_waybill`
-- ----------------------------
DROP TABLE IF EXISTS `t_waybill`;
CREATE TABLE `t_waybill` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `complete_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '完成时间',
  `waybill_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `waybill_local` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地点',
  `longitude` decimal(10,0) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,0) DEFAULT NULL COMMENT '纬度',
  `number_collect` int(11) NOT NULL DEFAULT '0' COMMENT '收车总数',
  `remarks` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `number_disboard` int(11) NOT NULL DEFAULT '0' COMMENT '卸车总数',
  `user_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `local_complete` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地点',
  `longitude_complete` decimal(10,0) DEFAULT NULL COMMENT '经度',
  `latitude_complete` decimal(10,0) DEFAULT NULL COMMENT '纬度',
  `city` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '城市',
  `city_complete` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '完成城市',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_waybill_user_id` (`user_id`) USING BTREE,
  KEY `idx_waybill_city` (`city`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
-- ----------------------------
-- Records of t_waybill
-- ----------------------------

-- ----------------------------
-- Table structure for `t_waybill_photo`
-- ----------------------------
DROP TABLE IF EXISTS `t_waybill_photo`;
CREATE TABLE `t_waybill_photo` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `waybill_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收车/装车',
  `waybill_local` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地点',
  `longitude` decimal(10,0) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,0) DEFAULT NULL COMMENT '纬度',
  `photo` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图片路径',
  `waybill_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运单id',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_waybill_photo_waybill_id` (`waybill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
-- ----------------------------
-- Records of t_waybill_photo
-- ----------------------------

-- ----------------------------
-- Table structure for `t_waybill_car`
-- ----------------------------
DROP TABLE IF EXISTS `t_waybill_car`;
CREATE TABLE `t_waybill_car` (
  `pk_id` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `waybill_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收车/装车',
  `waybill_id` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运单id',
  `car_number` text COLLATE utf8mb4_unicode_ci COMMENT '车辆编号',
  `car_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '车辆类型',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE,
  KEY `idx_waybill_car_waybill_id` (`waybill_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
-- ----------------------------
-- Records of t_waybill_car
-- ----------------------------