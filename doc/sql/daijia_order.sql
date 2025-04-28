

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for branch_table
-- ----------------------------
DROP TABLE IF EXISTS `branch_table`;
CREATE TABLE `branch_table`  (
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `resource_group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `branch_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime(6) NULL DEFAULT NULL,
  `gmt_modified` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`branch_id`) USING BTREE,
  INDEX `idx_xid`(`xid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for distributed_lock
-- ----------------------------
DROP TABLE IF EXISTS `distributed_lock`;
CREATE TABLE `distributed_lock`  (
  `lock_key` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `lock_value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `expire` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`lock_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for global_table
-- ----------------------------
DROP TABLE IF EXISTS `global_table`;
CREATE TABLE `global_table`  (
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  `application_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transaction_service_group` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transaction_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `timeout` int(11) NULL DEFAULT NULL,
  `begin_time` bigint(20) NULL DEFAULT NULL,
  `application_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`xid`) USING BTREE,
  INDEX `idx_status_gmt_modified`(`status`, `gmt_modified`) USING BTREE,
  INDEX `idx_transaction_id`(`transaction_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lock_table
-- ----------------------------
DROP TABLE IF EXISTS `lock_table`;
CREATE TABLE `lock_table`  (
  `row_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `xid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `transaction_id` bigint(20) NULL DEFAULT NULL,
  `branch_id` bigint(20) NOT NULL,
  `resource_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pk` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0:locked ,1:rollbacking',
  `gmt_create` datetime NULL DEFAULT NULL,
  `gmt_modified` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`row_key`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  INDEX `idx_branch_id`(`branch_id`) USING BTREE,
  INDEX `idx_xid`(`xid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_bill
-- ----------------------------
DROP TABLE IF EXISTS `order_bill`;
CREATE TABLE `order_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `fee_rule_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '费用规则id',
  `total_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '总金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '应付款金额',
  `distance_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '里程费',
  `wait_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '等时费用',
  `long_distance_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '远程费',
  `toll_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '路桥费',
  `parking_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '停车费',
  `other_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '其他费用',
  `favour_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '顾客好处费',
  `reward_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '系统奖励费',
  `reward_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '系统奖励规则id',
  `coupon_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `base_distance` smallint(6) NOT NULL DEFAULT 0 COMMENT '基础里程（公里）',
  `base_distance_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '基础里程费',
  `exceed_distance` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '超出基础里程的里程（公里）',
  `exceed_distance_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '超出基础里程的价格',
  `base_wait_minute` smallint(6) NOT NULL DEFAULT 0 COMMENT '基础等时分钟',
  `exceed_wait_minute` smallint(6) NULL DEFAULT NULL COMMENT '超出基础等时的分钟',
  `exceed_wait_minute_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '超出基础分钟的价格',
  `base_long_distance` smallint(6) NOT NULL DEFAULT 0 COMMENT '基础远途里程（公里）',
  `exceed_long_distance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '超出基础远程里程的里程',
  `exceed_long_distance_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '超出基础远程里程的价格',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单账单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_comment
-- ----------------------------
DROP TABLE IF EXISTS `order_comment`;
CREATE TABLE `order_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `driver_id` bigint(20) NOT NULL COMMENT '司机ID',
  `customer_id` bigint(20) NOT NULL COMMENT '顾客ID',
  `rate` tinyint(4) NOT NULL COMMENT '评分，1星~5星',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) NOT NULL COMMENT '状态，1未申诉，2已申诉，3申诉失败，4申诉成功',
  `instance_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '申诉工作流ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单评价表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `customer_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户ID',
  `order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `start_location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '起始地点',
  `start_point_longitude` decimal(10, 7) NOT NULL DEFAULT 0.0000000 COMMENT '起始地点经度',
  `start_point_latitude` decimal(10, 7) NOT NULL DEFAULT 0.0000000 COMMENT '起始点伟度',
  `end_location` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '结束地点',
  `end_point_longitude` decimal(10, 7) NOT NULL DEFAULT 0.0000000 COMMENT '结束地点经度',
  `end_point_latitude` decimal(10, 7) NOT NULL DEFAULT 0.0000000 COMMENT '结束地点经度',
  `expect_distance` decimal(10, 2) NULL DEFAULT NULL COMMENT '预估里程',
  `real_distance` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际里程',
  `expect_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '预估订单金额',
  `real_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际订单金额',
  `favour_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '顾客好处费',
  `driver_id` bigint(20) NULL DEFAULT NULL COMMENT '司机ID',
  `accept_time` datetime NULL DEFAULT NULL COMMENT '司机接单时间',
  `arrive_time` datetime NULL DEFAULT NULL COMMENT '司机到达时间',
  `start_service_time` datetime NULL DEFAULT NULL COMMENT '开始服务时间',
  `end_service_time` datetime NULL DEFAULT NULL COMMENT '结束服务时间',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '微信付款时间',
  `cancel_rule_id` bigint(20) NULL DEFAULT NULL COMMENT '订单取消规则ID',
  `car_license` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '车牌号',
  `car_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '车型',
  `car_front_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机到达拍照：车前照',
  `car_back_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '司机到达拍照：车后照',
  `transaction_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信支付订单号',
  `job_id` bigint(20) NULL DEFAULT NULL,
  `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '订单状态：1等待接单，2已接单，3司机已到达，4开始代驾，5结束代驾，6未付款，7已付款，8订单已结束，9顾客撤单，10司机撤单，11事故关闭，12其他',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注信息',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_order_no`(`order_no`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id`) USING BTREE,
  INDEX `idx_driver_id`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_monitor
-- ----------------------------
DROP TABLE IF EXISTS `order_monitor`;
CREATE TABLE `order_monitor`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '订单ID',
  `file_num` int(11) NOT NULL DEFAULT 0 COMMENT '文件个数',
  `audit_num` int(3) NOT NULL DEFAULT 0 COMMENT '需要审核的个数',
  `is_alarm` tinyint(3) NOT NULL DEFAULT 0 COMMENT '是否报警',
  `status` tinyint(3) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单监控表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_monitor_record
-- ----------------------------
DROP TABLE IF EXISTS `order_monitor_record`;
CREATE TABLE `order_monitor_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `file_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '文件路径',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `result` tinyint(3) NULL DEFAULT NULL COMMENT '审核结果',
  `keywords` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '风险关键词',
  `status` tinyint(3) NULL DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单监控记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_profitsharing
-- ----------------------------
DROP TABLE IF EXISTS `order_profitsharing`;
CREATE TABLE `order_profitsharing`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `rule_id` bigint(20) NOT NULL COMMENT '规则ID',
  `order_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单金额',
  `payment_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '微信支付平台费率',
  `payment_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '微信支付平台费用',
  `driver_tax_rate` decimal(10, 2) NULL DEFAULT NULL COMMENT '代驾司机代缴个税税率',
  `driver_tax_fee` decimal(10, 2) NULL DEFAULT NULL COMMENT '代驾司机税率支出费用',
  `platform_income` decimal(10, 2) NULL DEFAULT NULL COMMENT '平台分账收入',
  `driver_income` decimal(10, 2) NOT NULL COMMENT '司机分账收入',
  `status` tinyint(3) NOT NULL DEFAULT 1 COMMENT '分账状态，1未分账，2已分账',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单分账表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_status_log
-- ----------------------------
DROP TABLE IF EXISTS `order_status_log`;
CREATE TABLE `order_status_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(11) NULL DEFAULT NULL,
  `order_status` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `operate_time` datetime NULL DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 94 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单状态日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_track
-- ----------------------------
DROP TABLE IF EXISTS `order_track`;
CREATE TABLE `order_track`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `driver_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '司机id',
  `customer_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '客户id',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `speed` decimal(10, 2) NULL DEFAULT NULL COMMENT '速度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uniq_order_no`(`driver_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单追踪表' ROW_FORMAT = Dynamic;

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
