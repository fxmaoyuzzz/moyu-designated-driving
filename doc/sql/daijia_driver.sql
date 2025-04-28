

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for driver_account
-- ----------------------------
DROP TABLE IF EXISTS `driver_account`;
CREATE TABLE `driver_account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `driver_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '司机id',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户总金额',
  `lock_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '锁定金额',
  `available_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '可用金额',
  `total_income_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '总收入',
  `total_pay_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '总支出',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司机账户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for driver_account_detail
-- ----------------------------
DROP TABLE IF EXISTS `driver_account_detail`;
CREATE TABLE `driver_account_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `driver_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '司机id',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '交易内容',
  `trade_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '交易类型：1-奖励 2-补贴 3-提现',
  `amount` decimal(16, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司机账户明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for driver_face_recognition
-- ----------------------------
DROP TABLE IF EXISTS `driver_face_recognition`;
CREATE TABLE `driver_face_recognition`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `driver_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '司机id',
  `face_date` date NULL DEFAULT NULL COMMENT '识别日期',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '司机人脸识别记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for driver_info
-- ----------------------------
DROP TABLE IF EXISTS `driver_info`;
CREATE TABLE `driver_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `wx_open_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '微信openId',
  `nickname` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `avatar_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `gender` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '性别 1:男 2：女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `idcard_no` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证号码',
  `idcard_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证地址',
  `idcard_expire` date NULL DEFAULT NULL COMMENT '身份证有效期',
  `idcard_front_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证正面',
  `idcard_back_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证背面',
  `idcard_hand_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手持身份证',
  `driver_license_class` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '准驾车型',
  `driver_license_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驾驶证证件号',
  `driver_license_expire` date NULL DEFAULT NULL COMMENT '驾驶证有效期',
  `driver_license_issue_date` date NULL DEFAULT NULL COMMENT '驾驶证初次领证日期',
  `driver_license_front_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '驾驶证正面',
  `driver_license_back_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '行驶证副页正面',
  `driver_license_hand_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手持驾驶证',
  `contact_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '紧急联系人',
  `contact_phone` char(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '紧急联系人电话',
  `contact_relationship` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '紧急联系人关系',
  `face_model_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '腾讯云人脸模型id',
  `job_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机工号',
  `score` decimal(10, 2) NOT NULL DEFAULT 9.00 COMMENT '评分',
  `order_count` int(11) NOT NULL DEFAULT 0 COMMENT '订单量统计',
  `auth_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '认证状态 0:未认证  1：审核中 2：认证通过 -1：认证未通过',
  `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态，1正常，2禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '司机表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for driver_login_log
-- ----------------------------
DROP TABLE IF EXISTS `driver_login_log`;
CREATE TABLE `driver_login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `driver_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '司机id',
  `ipaddr` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '提示信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '司机登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for driver_set
-- ----------------------------
DROP TABLE IF EXISTS `driver_set`;
CREATE TABLE `driver_set`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `driver_id` bigint(20) NOT NULL COMMENT '司机ID',
  `service_status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '服务状态 1：开始接单 0：未接单',
  `order_distance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单里程设置',
  `accept_distance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '接单里程设置',
  `is_auto_accept` tinyint(3) NOT NULL DEFAULT 0 COMMENT '是否自动接单',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '司机设置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
