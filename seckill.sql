/*
SQLyog Professional v12.14 (64 bit)
MySQL - 5.7.19 : Database - seckill
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`seckill` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `seckill`;

/*Table structure for table `t_goods` */

DROP TABLE IF EXISTS `t_goods`;

CREATE TABLE `t_goods` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
  `goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` VARCHAR(64) DEFAULT NULL COMMENT '商品图片',
  `goods_detail` LONGTEXT COMMENT '商品详情',
  `goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品价格',
  `goods_stock` INT(11) DEFAULT '0' COMMENT '商品库存,-1表示无限制',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `t_goods` */

INSERT  INTO `t_goods`(`id`,`goods_name`,`goods_title`,`goods_img`,`goods_detail`,`goods_price`,`goods_stock`) VALUES 
(1,'iphone12 64GB','iphone12','/img/iphone12.png','iphone12','6299.00',100),
(2,'iphone12Pro Max ','iphone12Pro','/img/iphone12ProMax.png','iphone12','9299.00',100);

/*Table structure for table `t_order` */

DROP TABLE IF EXISTS `t_order`;

CREATE TABLE `t_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
  `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
  `delivery_addr_id` BIGINT(20) DEFAULT NULL COMMENT '收货地址id',
  `goods_name` VARCHAR(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
  `goods_count` INT(11) DEFAULT '0' COMMENT '商品数量',
  `goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `order_channel` TINYINT(4) DEFAULT '0' COMMENT '1pc,2android,3ios',
  `status` TINYINT(4) DEFAULT '0' COMMENT '订单状态,0 1 2 3 4 5',
  `create_date` DATETIME DEFAULT NULL COMMENT '订单创建时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

/*Data for the table `t_order` */

/*Table structure for table `t_seckill_goods` */

DROP TABLE IF EXISTS `t_seckill_goods`;

CREATE TABLE `t_seckill_goods` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
  `seckill_price` DECIMAL(12,0) DEFAULT '0' COMMENT '秒杀价',
  `stock_count` INT(10) DEFAULT NULL COMMENT '库存数量',
  `start_date` DATETIME DEFAULT NULL COMMENT 'seckill开始时间',
  `end_date` DATETIME DEFAULT NULL COMMENT 'seckill结束时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `t_seckill_goods` */

INSERT  INTO `t_seckill_goods`(`id`,`goods_id`,`seckill_price`,`stock_count`,`start_date`,`end_date`) VALUES 
(1,1,'629',10,'2022-07-05 08:00:00','2022-07-05 09:00:00'),
(2,2,'929',10,'2022-07-05 08:00:00','2022-07-05 09:00:00');

/*Table structure for table `t_seckill_order` */

DROP TABLE IF EXISTS `t_seckill_order`;

CREATE TABLE `t_seckill_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` BIGINT(20) DEFAULT NULL COMMENT '用户id',
  `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单id',
  `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

/*Data for the table `t_seckill_order` */

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` BIGINT(20) NOT NULL COMMENT '用户ID, 手机号码',
  `nickname` VARCHAR(255) NOT NULL,
  `password` VARCHAR(32) DEFAULT NULL COMMENT 'MD5(MD5(pwd明文+固定salt)+salt)',
  `salt` VARCHAR(10) DEFAULT NULL,
  `head` VARCHAR(128) DEFAULT NULL COMMENT '头像',
  `register_date` DATETIME DEFAULT NULL COMMENT '注册时间',
  `last_login_date` DATETIME DEFAULT NULL COMMENT '最后一次登录时间',
  `login_count` INT(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

INSERT  INTO `t_user`(`id`,`nickname`,`password`,`salt`,`head`,`register_date`,`last_login_date`,`login_count`) VALUES 
(17739043311,'admin','b7797cce01b4b131b433b6acf4add449','1a2b3c4d',NULL,NULL,NULL,0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
