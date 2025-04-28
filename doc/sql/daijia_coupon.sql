
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon_info
-- ----------------------------
DROP TABLE IF EXISTS `coupon_info`;
CREATE TABLE `coupon_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `coupon_type` tinyint(3) NOT NULL DEFAULT 1 COMMENT '优惠卷类型 1 现金券 2 折扣',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠卷名字',
  `amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '金额',
  `discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣：取值[1 到 10]',
  `condition_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '使用门槛 0->没门槛',
  `publish_count` int(11) NOT NULL DEFAULT 1 COMMENT '发行数量，0->无限制',
  `per_limit` int(11) NOT NULL DEFAULT 1 COMMENT '每人限领张数，0-不限制 1-限领1张 2-限领2张',
  `use_count` int(11) NOT NULL DEFAULT 0 COMMENT '已使用数量',
  `receive_count` int(11) NOT NULL DEFAULT 0 COMMENT '领取数量',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '优惠券描述',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态[0-未发布，1-已发布， -1-已过期]',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '优惠券信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_coupon
-- ----------------------------
DROP TABLE IF EXISTS `customer_coupon`;
CREATE TABLE `customer_coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `coupon_id` bigint(20) NULL DEFAULT NULL COMMENT '优惠券ID',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '乘客ID',
  `status` tinyint(3) NULL DEFAULT NULL COMMENT '优化券状态（1：未使用 2：已使用）',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '领取时间',
  `used_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单id',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(3) NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_coupon_id`(`coupon_id`) USING BTREE,
  INDEX `idx_customer_id`(`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '乘客优惠券关联表' ROW_FORMAT = Dynamic;

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
