/*
Navicat MySQL Data Transfer

Source Server         : sunfiyes
Source Server Version : 50558
Source Host           : localhost:3306
Source Database       : css

Target Server Type    : MYSQL
Target Server Version : 50558
File Encoding         : 65001

Date: 2018-03-12 14:09:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `content`
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '摘要',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `saleprice` decimal(10,2) DEFAULT NULL COMMENT '促销价',
  `icon` blob COMMENT '图片',
  `text` blob COMMENT '正文',
  `status` varchar(255) DEFAULT '1' COMMENT '内容状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='内容表';

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES ('1', '123', '123', '22.00', '33.00', 0x687474703A2F2F3132372E302E302E313A383038302F696D6167652F37643661613935342D633864372D343231302D383439302D6561353562366430663039392E706E67, 0x313233, '0');

-- ----------------------------
-- Table structure for `inventory`
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cid` int(11) DEFAULT NULL COMMENT '商品ID',
  `num` int(11) DEFAULT NULL COMMENT '进货数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='库存表';

-- ----------------------------
-- Records of inventory
-- ----------------------------
INSERT INTO `inventory` VALUES ('1', '1', '12');
INSERT INTO `inventory` VALUES ('2', '1', '1');

-- ----------------------------
-- Table structure for `temporary`
-- ----------------------------
DROP TABLE IF EXISTS `temporary`;
CREATE TABLE `temporary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` int(11) DEFAULT NULL COMMENT '用户ID',
  `cid` int(11) DEFAULT NULL COMMENT '商品ID',
  `num` int(11) DEFAULT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='购物车表';

-- ----------------------------
-- Records of temporary
-- ----------------------------

-- ----------------------------
-- Table structure for `trx`
-- ----------------------------
DROP TABLE IF EXISTS `trx`;
CREATE TABLE `trx` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `contentId` int(11) DEFAULT NULL COMMENT '内容ID',
  `personId` int(11) DEFAULT NULL COMMENT '用户ID',
  `price` int(11) DEFAULT NULL COMMENT '购买价格',
  `num` int(11) DEFAULT NULL COMMENT '购买数量',
  `payment` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `time` bigint(20) DEFAULT NULL COMMENT '购买时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='交易记录表';

-- ----------------------------
-- Records of trx
-- ----------------------------
INSERT INTO `trx` VALUES ('1', '1', '1', '33', '1', '33.00', '1520825319934');
INSERT INTO `trx` VALUES ('2', '1', '1', '33', '2', '66.00', '1520825420903');
INSERT INTO `trx` VALUES ('3', '1', '1', '33', '3', '99.00', '1520825436796');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码md5加密',
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `usertype` tinyint(3) DEFAULT NULL COMMENT '类型，买家0，卖家1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'buyer', '37254660e226ea65ce6f1efd54233424', 'buyer', '0');
INSERT INTO `user` VALUES ('2', 'seller', '981c57a5cfb0f868e064904b8745766f', 'seller', '1');
