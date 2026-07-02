DROP TABLE IF EXISTS `product_review`;
DROP TABLE IF EXISTS `user_coupon`;
DROP TABLE IF EXISTS `coupon`;
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `combo_group_item`;
DROP TABLE IF EXISTS `combo_group`;
DROP TABLE IF EXISTS `combo`;
DROP TABLE IF EXISTS `product_sku`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user_address`;
DROP TABLE IF EXISTS `user_balance_log`;
DROP TABLE IF EXISTS `user`;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '加盐密码哈希',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色：0-顾客，1-管理员，2-骑手',
  `balance` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额',
  `points` INT NOT NULL DEFAULT 0 COMMENT '用户积分',
  `total_recharge` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计充值金额',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常，1-删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 资金审计流水表
CREATE TABLE IF NOT EXISTS `user_balance_log` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `amount` DECIMAL(10, 2) NOT NULL COMMENT '变动金额(正数为增，负数为减)',
  `type` TINYINT NOT NULL COMMENT '变动类型：1-充值，2-消费，3-退款',
  `order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联订单号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金审计流水表';

-- 3. 用户地址簿表
CREATE TABLE IF NOT EXISTS `user_address` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `contact_name` VARCHAR(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `address_detail` VARCHAR(255) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否为默认地址：0-否，1-是',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址簿表';

-- 4. 商品分类表
CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序值',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 5. 商品主表
CREATE TABLE IF NOT EXISTS `product` (
  `id` BIGINT AUTO_INCREMENT COMMENT '商品ID',
  `category_id` BIGINT NOT NULL COMMENT '分类ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '商品描述',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '云端OSS图片URL',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品主表';

-- 6. 商品规格 SKU 表
CREATE TABLE IF NOT EXISTS `product_sku` (
  `id` BIGINT AUTO_INCREMENT COMMENT 'SKU ID',
  `product_id` BIGINT NOT NULL COMMENT '关联商品ID',
  `spec_name` VARCHAR(100) NOT NULL COMMENT '规格名称 (如: 8寸/草莓味)',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '价格',
  `stock` INT NOT NULL DEFAULT 0 COMMENT 'MySQL库存',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '独立启用状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格 SKU 表';

-- 7. 套餐主表
CREATE TABLE IF NOT EXISTS `combo` (
  `id` BIGINT AUTO_INCREMENT COMMENT '套餐ID',
  `name` VARCHAR(100) NOT NULL COMMENT '套餐名称',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '套餐描述',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '套餐图片URL',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '套餐打包价格',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '套餐状态：0-下架，1-上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐主表';

-- 8. 套餐分组表
CREATE TABLE IF NOT EXISTS `combo_group` (
  `id` BIGINT AUTO_INCREMENT COMMENT '套餐分组ID',
  `combo_id` BIGINT NOT NULL COMMENT '所属套餐ID',
  `group_name` VARCHAR(50) NOT NULL COMMENT '分组名称 (如: 主蛋糕可选, 饮料可选)',
  `choose_count` INT NOT NULL DEFAULT 1 COMMENT '此分组内必须选择的SKU件数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_combo_id` (`combo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐分组表';

-- 9. 套餐分组内可选 SKU 关联表
CREATE TABLE IF NOT EXISTS `combo_group_item` (
  `id` BIGINT AUTO_INCREMENT COMMENT '关联项ID',
  `group_id` BIGINT NOT NULL COMMENT '分组ID',
  `sku_id` BIGINT NOT NULL COMMENT '可选SKU ID',
  `extra_price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '补差价/换购溢价',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='套餐分组内可选 SKU 关联表';

-- 10. 订单主表
CREATE TABLE IF NOT EXISTS `orders` (
  `id` BIGINT AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '系统全局唯一订单号',
  `user_id` BIGINT NOT NULL COMMENT '下单用户ID',
  `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '支付总金额',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未支付，1-已支付(待骑手抢单)，2-配送中/制作中，3-已送达/已完成，4-已取消',
  `delivery_type` TINYINT NOT NULL DEFAULT 1 COMMENT '配送类型：1-骑手外卖配送，2-到店自提',
  `rider_id` BIGINT DEFAULT NULL COMMENT '接单骑手ID',
  `address_snapshot` VARCHAR(500) DEFAULT NULL COMMENT '收货地址与联系人快照JSON',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `appointment_time` DATETIME DEFAULT NULL COMMENT '预约送达时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `pay_method` TINYINT DEFAULT NULL COMMENT '支付方式：1-钱包余额，2-微信支付',
  `version` INT NOT NULL DEFAULT 1 COMMENT '乐观锁版本控制',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_rider_id` (`rider_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 11. 订单明细快照表
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` BIGINT AUTO_INCREMENT COMMENT '明细项ID',
  `order_id` BIGINT NOT NULL COMMENT '所属订单ID',
  `product_id` BIGINT DEFAULT NULL COMMENT '商品ID',
  `sku_id` BIGINT DEFAULT NULL COMMENT '规格ID',
  `combo_id` BIGINT DEFAULT NULL COMMENT '套餐ID',
  `combo_details` JSON DEFAULT NULL COMMENT '套餐选择细节快照',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '下单时单价',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '购买数量',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_combo_id` (`combo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细快照表';

-- 12. 优惠券表
CREATE TABLE IF NOT EXISTS `coupon` (
  `id` BIGINT AUTO_INCREMENT COMMENT '优惠券ID',
  `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `type` TINYINT NOT NULL COMMENT '1-满减券 2-折扣券 3-无门槛券',
  `min_amount` DECIMAL(10,2) DEFAULT '0.00' COMMENT '最低消费金额',
  `discount_amount` DECIMAL(10,2) DEFAULT NULL COMMENT '满减/无门槛抵扣金额',
  `discount_rate` DECIMAL(4,2) DEFAULT NULL COMMENT '折扣率(如0.8表示8折)',
  `stock` INT NOT NULL DEFAULT '0' COMMENT '发放总库存',
  `start_time` DATETIME DEFAULT NULL COMMENT '有效期开始',
  `end_time` DATETIME DEFAULT NULL COMMENT '有效期结束',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 13. 用户领券记录表
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-未使用 1-已使用 2-已过期',
  `used_time` DATETIME DEFAULT NULL COMMENT '使用时间',
  `order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联使用订单号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户领券记录表';

-- 14. 商品评价表
CREATE TABLE IF NOT EXISTS `product_review` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '关联订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `product_id` BIGINT NOT NULL COMMENT '商品ID',
  `sku_id` BIGINT DEFAULT NULL COMMENT 'SKU ID',
  `rating` TINYINT NOT NULL DEFAULT 5 COMMENT '评分 1-5星',
  `content` TEXT COMMENT '评价内容',
  `images` TEXT COMMENT '晒图(逗号分隔的URL)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';
