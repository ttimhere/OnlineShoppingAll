CREATE DATABASE  IF NOT EXISTS `online_shop` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `online_shop`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: online_shop
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_user`
--

DROP TABLE IF EXISTS `admin_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nickname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` tinyint DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user`
--

LOCK TABLES `admin_user` WRITE;
/*!40000 ALTER TABLE `admin_user` DISABLE KEYS */;
INSERT INTO `admin_user` VALUES (1,'admin','超级管理员','$2a$10$nlce8c7M9/PzqqzZJigkK.AhkgutM.wlSCJVx7hd5A141CGHC3lB6',1,'2025-11-13 15:13:54','2025-11-13 15:23:05'),(2,'service','客服小李','$2a$10$nlce8c7M9/PzqqzZJigkK.AhkgutM.wlSCJVx7hd5A141CGHC3lB6',1,'2025-11-13 15:13:54','2025-11-13 15:23:05');
/*!40000 ALTER TABLE `admin_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_user_role`
--

DROP TABLE IF EXISTS `admin_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_user_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_user_id` (`admin_user_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `admin_user_role_ibfk_1` FOREIGN KEY (`admin_user_id`) REFERENCES `admin_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `admin_user_role_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_user_role`
--

LOCK TABLES `admin_user_role` WRITE;
/*!40000 ALTER TABLE `admin_user_role` DISABLE KEYS */;
INSERT INTO `admin_user_role` VALUES (1,1,1),(2,2,2);
/*!40000 ALTER TABLE `admin_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `selected` tinyint(1) DEFAULT '1',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_item_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cart_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (24,3,3,1,0,'2025-11-11 07:58:59'),(25,3,4,1,0,'2025-11-11 07:59:01'),(26,3,6,1,0,'2025-11-11 07:58:59'),(27,3,2,1,0,'2025-11-11 07:59:00'),(28,3,7,1,0,'2025-11-11 07:58:59'),(29,3,1,1,0,'2025-11-11 07:59:00'),(32,3,8,1,0,'2025-11-12 12:33:06'),(34,1,8,1,1,'2025-11-12 03:27:43'),(35,1,5,1,1,'2025-11-12 03:27:45');
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` int DEFAULT '1',
  `sort` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,NULL,'电子产品',1,1,1),(2,NULL,'服装鞋帽',1,2,1),(3,NULL,'家居用品',1,3,1),(4,1,'手机',2,1,1),(5,1,'笔记本电脑',2,2,1),(6,2,'男装',2,1,1),(7,2,'女装',2,2,1),(8,3,'厨房用品',2,1,1),(9,3,'清洁工具',2,2,1);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu`
--

DROP TABLE IF EXISTS `menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `path` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `permission` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `menu` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES (1,'商品管理','/admin/product','product:manage',NULL),(2,'分类管理','/admin/category','category:manage',NULL),(3,'订单管理','/admin/order','order:manage',NULL),(4,'销售报表','/admin/report','report:sale',NULL),(5,'客户管理','/admin/customer','customer:manage',NULL),(6,'权限管理','/admin/auth','admin:manage',NULL);
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `op_log`
--

DROP TABLE IF EXISTS `op_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `op_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_user_id` bigint NOT NULL,
  `actor` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `action` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `target_id` bigint DEFAULT NULL,
  `ip` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `op_log`
--

LOCK TABLES `op_log` WRITE;
/*!40000 ALTER TABLE `op_log` DISABLE KEYS */;
INSERT INTO `op_log` VALUES (1,1,NULL,NULL,'新增商品','product:null',NULL,NULL,NULL,'2025-11-14 07:04:54'),(2,1,NULL,NULL,'编辑商品','product:9',NULL,NULL,NULL,'2025-11-14 07:08:12'),(3,1,NULL,NULL,'下架商品','product:9',NULL,NULL,NULL,'2025-11-14 07:08:46'),(4,1,NULL,NULL,'发货订单','order:12',NULL,NULL,NULL,'2025-11-15 10:23:16'),(5,1,NULL,NULL,'取消订单','order:10',NULL,NULL,NULL,'2025-11-15 10:24:21');
/*!40000 ALTER TABLE `op_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `pay_amount` decimal(10,2) NOT NULL,
  `status` tinyint DEFAULT '0',
  `pay_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `pay_time` timestamp NULL DEFAULT NULL,
  `ship_time` timestamp NULL DEFAULT NULL,
  `finish_time` timestamp NULL DEFAULT NULL,
  `cancel_time` timestamp NULL DEFAULT NULL,
  `address_snapshot` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `idx_order_user` (`user_id`,`status`,`create_time`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,'O1762001986727-54d340aa',1,17998.00,17998.00,1,'MOCK','2025-11-01 12:59:46','2025-11-01 12:59:46',NULL,NULL,NULL,NULL),(2,'O1762002160798-6533eb02',3,17998.00,17998.00,1,'MOCK','2025-11-01 13:02:40','2025-11-01 13:02:40',NULL,NULL,NULL,NULL),(3,'O1762843222641-cacf9947',1,16398.00,16398.00,1,'MOCK','2025-11-11 06:40:22','2025-11-11 06:40:23',NULL,NULL,NULL,NULL),(4,'O1762843225849-15d3d7ef',1,16398.00,16398.00,1,'MOCK','2025-11-11 06:40:25','2025-11-11 06:40:26',NULL,NULL,NULL,NULL),(5,'O1762843258042-c918b6da',1,8999.00,8999.00,1,'MOCK','2025-11-11 06:40:58','2025-11-11 06:40:58',NULL,NULL,NULL,NULL),(6,'O1762843383080-11a61bf1',1,7399.00,7399.00,1,'MOCK','2025-11-11 06:43:03','2025-11-11 06:43:03',NULL,NULL,NULL,NULL),(7,'O1762844037055-98f1835d',1,198.00,198.00,1,'MOCK','2025-11-11 06:53:57','2025-11-11 06:53:57',NULL,NULL,NULL,NULL),(8,'O1762844051485-608894ab',1,8598.00,8598.00,1,'MOCK','2025-11-11 06:54:11','2025-11-11 06:54:11',NULL,NULL,NULL,NULL),(9,'O1762845664792-06852b29',1,159.00,159.00,1,'MOCK','2025-11-11 07:21:04','2025-11-11 07:21:04',NULL,NULL,NULL,NULL),(10,'O1762845678297-5634d4c9',1,8999.00,8999.00,4,'MOCK','2025-11-11 07:21:18','2025-11-11 07:21:18',NULL,NULL,'2025-11-15 10:24:21',NULL),(11,'O1762845760454-3dd87921',3,8999.00,8999.00,1,'MOCK','2025-11-11 07:22:40','2025-11-11 07:22:40',NULL,NULL,NULL,NULL),(12,'O1762846301800-71e06cd0',3,99.00,99.00,2,'MOCK','2025-11-11 07:31:41','2025-11-11 07:31:41','2025-11-15 10:23:16',NULL,NULL,NULL),(13,'O1762846308000-7d5b9953',3,4299.00,4299.00,1,'MOCK','2025-11-11 07:31:48','2025-11-11 07:31:48',NULL,NULL,NULL,NULL),(14,'O1762846448485-44a591d7',3,4299.00,4299.00,1,'MOCK','2025-11-11 07:34:08','2025-11-11 07:34:08',NULL,NULL,NULL,NULL),(15,'O1762847208868-25fe7caa',3,4299.00,4299.00,1,'MOCK','2025-11-11 07:46:48','2025-11-11 07:46:48',NULL,NULL,NULL,NULL),(16,'O1762847215750-57c833e3',3,99.00,99.00,1,'MOCK','2025-11-11 07:46:55','2025-11-11 07:46:55',NULL,NULL,NULL,NULL),(17,'O1762847261840-a34fa693',3,4299.00,4299.00,1,'MOCK','2025-11-11 07:47:41','2025-11-11 07:47:41',NULL,NULL,NULL,NULL),(18,'O1762847384361-6c4906d1',3,99.00,99.00,1,'MOCK','2025-11-11 07:49:44','2025-11-11 07:49:44',NULL,NULL,NULL,NULL),(19,'O1762847394143-1d2a81f9',3,4299.00,4299.00,1,'MOCK','2025-11-11 07:49:54','2025-11-11 07:49:54',NULL,NULL,NULL,NULL),(20,'O1762847906179-072819f2',3,99.00,99.00,1,'MOCK','2025-11-11 07:58:26','2025-11-11 07:58:26',NULL,NULL,NULL,NULL),(21,'O1762847951727-2fc78816',3,99.00,99.00,1,'MOCK','2025-11-11 07:59:11','2025-11-11 07:59:11',NULL,NULL,NULL,NULL),(22,'O1762848382917-ea4197e3',3,259.00,259.00,1,'MOCK','2025-11-11 08:06:22','2025-11-11 08:06:22',NULL,NULL,NULL,NULL),(23,'O1762848698342-eadafd43',1,259.00,259.00,1,'MOCK','2025-11-11 08:11:38','2025-11-11 08:11:38',NULL,NULL,NULL,NULL),(24,'O1762950792775-427297ae',3,99.00,99.00,1,'MOCK','2025-11-12 12:33:12','2025-11-12 12:33:12',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `product_title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,'iPhone 15 Pro',8999.00,2,'/images/product/iphone15pro.jpg'),(2,2,1,'iPhone 15 Pro',8999.00,2,'/images/product/iphone15pro.jpg'),(3,3,2,'华为 MateBook X',7399.00,1,'/uploads/product/matebookx.jpg'),(4,3,4,'联想 ThinkPad X1',8999.00,1,'/uploads/product/thinkpadx1.jpg'),(5,4,2,'华为 MateBook X',7399.00,1,'/uploads/product/matebookx.jpg'),(6,4,4,'联想 ThinkPad X1',8999.00,1,'/uploads/product/thinkpadx1.jpg'),(7,5,4,'联想 ThinkPad X1',8999.00,1,'/uploads/product/thinkpadx1.jpg'),(8,6,2,'华为 MateBook X',7399.00,1,'/uploads/product/matebookx.jpg'),(9,7,5,'纯棉圆领T恤',99.00,2,'/uploads/product/tshirt.jpg'),(10,8,3,'小米 14',4299.00,2,'/uploads/product/xiaomi14.jpg'),(11,9,6,'牛仔裤',159.00,1,'/uploads/product/jeans.jpg'),(12,10,4,'联想 ThinkPad X1',8999.00,1,'/uploads/product/thinkpadx1.jpg'),(13,11,4,'联想 ThinkPad X1',8999.00,1,'/uploads/product/thinkpadx1.jpg'),(14,12,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg'),(15,13,3,'小米 14',4299.00,1,'/uploads/product/xiaomi14.jpg'),(16,14,3,'小米 14',4299.00,1,'/uploads/product/xiaomi14.jpg'),(17,15,3,'小米 14',4299.00,1,'/uploads/product/xiaomi14.jpg'),(18,16,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg'),(19,17,3,'小米 14',4299.00,1,'/uploads/product/xiaomi14.jpg'),(20,18,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg'),(21,19,3,'小米 14',4299.00,1,'/uploads/product/xiaomi14.jpg'),(22,20,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg'),(23,21,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg'),(24,22,8,'针织外套',259.00,1,'/uploads/product/sweater.jpg'),(25,23,8,'针织外套',259.00,1,'/uploads/product/sweater.jpg'),(26,24,5,'纯棉圆领T恤',99.00,1,'/uploads/product/tshirt.jpg');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pay_channel` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `status` tinyint DEFAULT '0',
  `notify_payload` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,'O1762001986727-54d340aa','MOCK','P1762001986743',17998.00,1,NULL,'2025-11-01 12:59:46'),(2,'O1762002160798-6533eb02','MOCK','P1762002160806',17998.00,1,NULL,'2025-11-01 13:02:40'),(3,'O1762843222641-cacf9947','MOCK','P1762843222797',16398.00,1,NULL,'2025-11-11 06:40:22'),(4,'O1762843225849-15d3d7ef','MOCK','P1762843226894',16398.00,1,NULL,'2025-11-11 06:40:26'),(5,'O1762843258042-c918b6da','MOCK','P1762843258052',8999.00,1,NULL,'2025-11-11 06:40:58'),(6,'O1762843383080-11a61bf1','MOCK','P1762843383095',7399.00,1,NULL,'2025-11-11 06:43:03'),(7,'O1762844037055-98f1835d','MOCK','P1762844037063',198.00,1,NULL,'2025-11-11 06:53:57'),(8,'O1762844051485-608894ab','MOCK','P1762844051494',8598.00,1,NULL,'2025-11-11 06:54:11'),(9,'O1762845664792-06852b29','MOCK','P1762845664820',159.00,1,NULL,'2025-11-11 07:21:04'),(10,'O1762845678297-5634d4c9','MOCK','P1762845678315',8999.00,1,NULL,'2025-11-11 07:21:18'),(11,'O1762845760454-3dd87921','MOCK','P1762845760465',8999.00,1,NULL,'2025-11-11 07:22:40'),(12,'O1762846301800-71e06cd0','MOCK','P1762846301809',99.00,1,NULL,'2025-11-11 07:31:41'),(13,'O1762846308000-7d5b9953','MOCK','P1762846308009',4299.00,1,NULL,'2025-11-11 07:31:48'),(14,'O1762846448485-44a591d7','MOCK','P1762846448494',4299.00,1,NULL,'2025-11-11 07:34:08'),(15,'O1762847208868-25fe7caa','MOCK','P1762847208876',4299.00,1,NULL,'2025-11-11 07:46:48'),(16,'O1762847215750-57c833e3','MOCK','P1762847215759',99.00,1,NULL,'2025-11-11 07:46:55'),(17,'O1762847261840-a34fa693','MOCK','P1762847261868',4299.00,1,NULL,'2025-11-11 07:47:41'),(18,'O1762847384361-6c4906d1','MOCK','P1762847384373',99.00,1,NULL,'2025-11-11 07:49:44'),(19,'O1762847394143-1d2a81f9','MOCK','P1762847394152',4299.00,1,NULL,'2025-11-11 07:49:54'),(20,'O1762847906179-072819f2','MOCK','P1762847906188',99.00,1,NULL,'2025-11-11 07:58:26'),(21,'O1762847951727-2fc78816','MOCK','P1762847951736',99.00,1,NULL,'2025-11-11 07:59:11'),(22,'O1762848382917-ea4197e3','MOCK','P1762848382945',259.00,1,NULL,'2025-11-11 08:06:22'),(23,'O1762848698342-eadafd43','MOCK','P1762848698351',259.00,1,NULL,'2025-11-11 08:11:38'),(24,'O1762950792775-427297ae','MOCK','P1762950792792',99.00,1,NULL,'2025-11-12 12:33:12');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint DEFAULT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sub_title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int DEFAULT '0',
  `status` tinyint DEFAULT '1',
  `main_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `attrs` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_product_category` (`category_id`,`status`,`price`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,4,'iPhone 15 Pro','苹果最新旗舰手机',8999.00,46,1,'/uploads/product/iphone15pro.jpg','{\"brand\": \"Apple\", \"color\": \"银色\", \"storage\": \"256GB\"}','2025-10-27 08:02:38','2025-11-10 07:14:36'),(2,5,'华为 MateBook X','轻薄笔记本电脑',7399.00,27,1,'/uploads/product/matebookx.jpg','{\"ram\": \"16GB\", \"brand\": \"Huawei\", \"color\": \"灰色\"}','2025-10-27 08:02:38','2025-11-11 06:43:03'),(3,4,'小米 14','高性能智能手机',4299.00,93,1,'/uploads/product/xiaomi14.jpg','{\"brand\": \"Xiaomi\", \"color\": \"黑色\", \"storage\": \"128GB\"}','2025-10-27 08:02:38','2025-11-11 07:49:54'),(4,5,'联想 ThinkPad X1','商务笔记本电脑',8999.00,36,1,'/uploads/product/thinkpadx1.jpg','{\"ram\": \"32GB\", \"brand\": \"Lenovo\", \"color\": \"黑色\"}','2025-10-27 08:02:38','2025-11-15 10:24:21'),(5,6,'纯棉圆领T恤','舒适透气，夏季必备',99.00,192,1,'/uploads/product/tshirt.jpg','{\"size\": \"L\", \"brand\": \"Uniqlo\", \"color\": \"白色\"}','2025-10-27 08:02:39','2025-11-12 12:33:12'),(6,6,'牛仔裤','修身版型，四季皆宜',159.00,149,1,'/uploads/product/jeans.jpg','{\"size\": \"32\", \"brand\": \"Levis\", \"color\": \"蓝色\"}','2025-10-27 08:02:39','2025-11-11 07:21:04'),(7,7,'女士连衣裙','优雅气质款',299.00,80,1,'/uploads/product/dress.jpg','{\"size\": \"M\", \"brand\": \"Zara\", \"color\": \"粉色\"}','2025-10-27 08:02:39','2025-11-10 07:16:28'),(8,7,'针织外套','秋冬保暖首选',259.00,88,1,'/uploads/product/sweater.jpg','{\"size\": \"L\", \"brand\": \"H&M\", \"color\": \"灰色\"}','2025-10-27 08:02:39','2025-11-11 08:11:38'),(9,1,'管理员修改后的标题','管理员操作',199.90,50,0,'/uploads/product/new.jpg','{}','2025-11-14 06:53:06','2025-11-14 07:08:46'),(10,1,'管理员测试商品','子标题',99.90,100,1,'/uploads/product/dress.jpg','{}','2025-11-14 06:54:27','2025-11-14 06:54:27'),(11,1,'管理员测试商品','子标题',99.90,100,1,'/uploads/product/dress.jpg','{}','2025-11-14 07:01:44','2025-11-14 07:01:44'),(12,1,'管理员测试商品','子标题',99.90,100,1,'/uploads/product/dress.jpg','{}','2025-11-14 07:04:54','2025-11-14 07:04:54');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `url` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort` int DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'SUPER_ADMIN','超级管理员'),(2,'CUSTOMER_SERVICE','客服');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_menu`
--

DROP TABLE IF EXISTS `role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `menu_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  KEY `menu_id` (`menu_id`),
  CONSTRAINT `role_menu_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `role_menu_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(6,1,6),(7,2,3),(8,2,5);
/*!40000 ALTER TABLE `role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_flow`
--

DROP TABLE IF EXISTS `stock_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_flow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `delta` int DEFAULT NULL,
  `type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ref_no` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `stock_flow_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_flow`
--

LOCK TABLES `stock_flow` WRITE;
/*!40000 ALTER TABLE `stock_flow` DISABLE KEYS */;
INSERT INTO `stock_flow` VALUES (1,5,0,'SHIP','O1762846301800-71e06cd0','2025-11-15 10:23:16'),(2,4,1,'CANCEL','O1762845678297-5634d4c9','2025-11-15 10:24:21');
/*!40000 ALTER TABLE `stock_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` tinyint DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'test@example.com',NULL,'$2a$10$RSYCL28H9CqVSgaJhUz3bu51bc1vh22eEQl/vSmdCKVsR4hH70boW',NULL,1,'2025-10-20 11:08:08','2025-10-20 11:08:08'),(2,'string','string','$2a$10$9VjSVMkwKgA4Y/BgkjJH6.nmM5AoduQPjoZPWIQFEk9miEgtScLnS','string',1,'2025-10-28 07:00:51','2025-10-28 07:00:51'),(3,'isasefm@163.com',NULL,'$2a$10$nlce8c7M9/PzqqzZJigkK.AhkgutM.wlSCJVx7hd5A141CGHC3lB6',NULL,1,'2025-11-01 13:02:06','2025-11-01 13:02:06'),(4,'test2@qq.com',NULL,'$2a$10$gVWOifneK0mOiRDPZ47PPuDiBBuFu./WxOK5Cb0JuvkLZ/FhcCzJy','',1,'2025-11-08 16:04:09','2025-11-08 16:04:09');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_address`
--

DROP TABLE IF EXISTS `user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `receiver` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `district` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `detail` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_default` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_address_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_address`
--

LOCK TABLES `user_address` WRITE;
/*!40000 ALTER TABLE `user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_event`
--

DROP TABLE IF EXISTS `user_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_event` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `event_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` json DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_event` (`user_id`,`event_type`,`created_at`),
  CONSTRAINT `user_event_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_event`
--

LOCK TABLES `user_event` WRITE;
/*!40000 ALTER TABLE `user_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'online_shop'
--

--
-- Dumping routines for database 'online_shop'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-17 14:23:18
