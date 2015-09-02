/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50520
Source Host           : localhost:3306
Source Database       : drill

Target Server Type    : MYSQL
Target Server Version : 50520
File Encoding         : 65001

Date: 2015-09-02 22:04:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_advert`
-- ----------------------------
DROP TABLE IF EXISTS `t_advert`;
CREATE TABLE `t_advert` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '广告ID',
  `title` varchar(200) DEFAULT NULL COMMENT '广告标题',
  `content` varchar(200) DEFAULT NULL COMMENT '广告内容',
  `type` char(2) DEFAULT NULL COMMENT '广告类型  0为默认文字广告，1为图片广告',
  `adurl` varchar(200) DEFAULT NULL COMMENT '广告图片地址',
  `publisher` varchar(50) DEFAULT NULL COMMENT '广告发布人',
  `addtime` datetime DEFAULT NULL COMMENT '添加时间',
  `uptime` datetime DEFAULT NULL COMMENT '修改时间',
  `starttime` varchar(50) DEFAULT NULL COMMENT '开始时间',
  `endtime` varchar(50) DEFAULT NULL COMMENT '结束时间',
  `status` char(2) DEFAULT '0' COMMENT '状态  0为默认未启用，1为启用',
  `tourl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_advert
-- ----------------------------
INSERT INTO `t_advert` VALUES ('6', 'mmmmmmmmmmmmmmmmm', '<p>fasfasfasf</p>', '0', '', '', '2015-08-26 17:18:07', '2015-08-31 10:56:43', '', '', '0', '');
INSERT INTO `t_advert` VALUES ('7', 'fdsa', '<p>fdasfasfs</p>', '0', '', '', '2015-08-26 17:47:33', '2015-08-26 17:47:33', '', '', '0', '');

-- ----------------------------
-- Table structure for `t_dict`
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `level` int(10) DEFAULT NULL,
  `orderId` int(10) DEFAULT NULL,
  `parentId` varchar(100) DEFAULT NULL,
  `parentCode` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_dict
-- ----------------------------
INSERT INTO `t_dict` VALUES ('01ff49267db1487b8bc173f08f34036d', '负载算法', 'loadbanlance', '1', '4', '0', 'loadbanlance');
INSERT INTO `t_dict` VALUES ('212a6765fddc4430941469e1ec8c8e6c', '人事部', '001', '2', '1', 'c067fdaf51a141aeaa56ed26b70de863', 'BM_001');
INSERT INTO `t_dict` VALUES ('3cec73a7cc8a4cb79e3f6ccc7fc8858c', '行政部', '002', '2', '2', 'c067fdaf51a141aeaa56ed26b70de863', 'BM_002');
INSERT INTO `t_dict` VALUES ('48724375640341deb5ef01ac51a89c34', '北京', 'dq001', '2', '1', 'cdba0b5ef20e4fc0a5231fa3e9ae246a', 'DQ_dq001');
INSERT INTO `t_dict` VALUES ('4e125b23b10d47d58188ed96c3b08885', '书籍', 'book', '1', '5', '0', 'book');
INSERT INTO `t_dict` VALUES ('5a1547632cca449db378fbb9a042b336', '研发部', '004', '2', '4', 'c067fdaf51a141aeaa56ed26b70de863', 'BM_004');
INSERT INTO `t_dict` VALUES ('7f9cd74e60a140b0aea5095faa95cda3', '财务部', '003', '2', '3', 'c067fdaf51a141aeaa56ed26b70de863', 'BM_003');
INSERT INTO `t_dict` VALUES ('830ae0dffabf4af5b946b420d69c4e93', '互联网', 'internet', '1', '3', '0', 'internet');
INSERT INTO `t_dict` VALUES ('b72e7fa102984bd8b57bf2ba68acd473', '轮询', 'poll', '2', '1', '01ff49267db1487b8bc173f08f34036d', 'loadbanlance_poll');
INSERT INTO `t_dict` VALUES ('c067fdaf51a141aeaa56ed26b70de863', '部门', 'BM', '1', '1', '0', 'BM');
INSERT INTO `t_dict` VALUES ('ca1447c75e0b40deb3d6f702e2f79146', '计算机', 'computer', '2', '1', '4e125b23b10d47d58188ed96c3b08885', 'book_computer');
INSERT INTO `t_dict` VALUES ('cdba0b5ef20e4fc0a5231fa3e9ae246a', '地区', 'DQ', '1', '2', '0', 'DQ');
INSERT INTO `t_dict` VALUES ('f184bff5081d452489271a1bd57599ed', '上海', 'SH', '2', '2', 'cdba0b5ef20e4fc0a5231fa3e9ae246a', 'DQ_SH');
INSERT INTO `t_dict` VALUES ('f30bf95e216d4ebb8169ff0c86330b8f', '客服部', '006', '2', '6', 'c067fdaf51a141aeaa56ed26b70de863', 'BM_006');

-- ----------------------------
-- Table structure for `t_line`
-- ----------------------------
DROP TABLE IF EXISTS `t_line`;
CREATE TABLE `t_line` (
  `LINE_ID` varchar(100) NOT NULL,
  `TITLE` varchar(255) DEFAULT NULL COMMENT '名称',
  `LINE_URL` varchar(255) DEFAULT NULL COMMENT '链接',
  `LINE_ROAD` varchar(255) DEFAULT NULL COMMENT '线路',
  `TYPE` varchar(255) DEFAULT NULL COMMENT '类型',
  `LINE_ORDER` int(10) DEFAULT NULL COMMENT '排序',
  `PARENT_ID` varchar(255) DEFAULT NULL COMMENT '父类ID',
  PRIMARY KEY (`LINE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_line
-- ----------------------------
INSERT INTO `t_line` VALUES ('9210bcf4b27f4ac1a62c81e312be9909', 'fdsafsda', 'fsda', 'fdsfas', 'fsdafs', '1', '');

-- ----------------------------
-- Table structure for `t_link`
-- ----------------------------
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '友情链接ID',
  `stiename` varchar(100) DEFAULT NULL COMMENT '友情链接网站名称',
  `sitecontent` varchar(200) DEFAULT NULL COMMENT '友情链接网站内容',
  `type` char(2) DEFAULT '0' COMMENT '类型  0为文字连接，1为图片连接',
  `stieurl` varchar(200) DEFAULT NULL COMMENT '图片连接地址',
  `addtime` varchar(50) DEFAULT NULL COMMENT '添加时间',
  `uptime` varchar(50) DEFAULT NULL COMMENT '修改时间',
  `status` char(2) DEFAULT '0' COMMENT '状态  0为未启用，1为启用',
  `sequence` int(10) DEFAULT NULL COMMENT '排序',
  `tourl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_link
-- ----------------------------
INSERT INTO `t_link` VALUES ('3', 'xxxxx', '<p>xxxxx<br/></p>', '0', '', '2015-08-26 17:53:59', '2015-08-26 17:54:06', '0', '0', '');

-- ----------------------------
-- Table structure for `t_member`
-- ----------------------------
DROP TABLE IF EXISTS `t_member`;
CREATE TABLE `t_member` (
  `USER_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `LAST_LOGIN` varchar(255) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `STATUS` varchar(32) DEFAULT NULL,
  `BZ` varchar(255) DEFAULT NULL,
  `PHONE` varchar(100) DEFAULT NULL,
  `SFID` varchar(100) DEFAULT NULL,
  `START_TIME` varchar(100) DEFAULT NULL,
  `END_TIME` varchar(100) DEFAULT NULL,
  `YEARS` int(10) DEFAULT NULL,
  `NUMBER` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_member
-- ----------------------------
INSERT INTO `t_member` VALUES ('db5868a0835b442c904f471419e0504c', 'xxx', 'c4ca4238a0b923820dcc509a6f75849b', 'xiaobai', '', '68f23fc0caee475bae8d52244dea8444', '', '', '1', '', '', '4343', '2015-08-28', '2015-08-31', '0', 'xxxxxx', 'krisibm@163.com');

-- ----------------------------
-- Table structure for `t_menu`
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `MENU_ID` int(11) NOT NULL,
  `MENU_NAME` varchar(255) DEFAULT NULL,
  `MENU_URL` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(100) DEFAULT NULL,
  `MENU_ORDER` varchar(100) DEFAULT NULL,
  `MENU_ICON` varchar(30) DEFAULT NULL,
  `MENU_TYPE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES ('1', '系统管理', '#', '0', '1', 'icon-desktop', '1');
INSERT INTO `t_menu` VALUES ('2', '组织管理', 'role', '1', '2', null, '1');
INSERT INTO `t_menu` VALUES ('4', '会员管理', 'member/listUser', '1', '4', null, '1');
INSERT INTO `t_menu` VALUES ('5', '系统用户', 'user/list', '1', '3', null, '1');
INSERT INTO `t_menu` VALUES ('6', '信息管理', '#', '0', '2', 'icon-list-alt', '2');
INSERT INTO `t_menu` VALUES ('7', '图片管理', 'pic/list', '6', '1', null, '2');
INSERT INTO `t_menu` VALUES ('8', '性能监控', 'druid/index.html', '9', '1', null, '1');
INSERT INTO `t_menu` VALUES ('9', '系统工具', '#', '0', '3', 'icon-th', '1');
INSERT INTO `t_menu` VALUES ('10', '接口测试', 'tool/interface', '9', '2', null, '1');
INSERT INTO `t_menu` VALUES ('11', '发送邮件', 'mail/toSendEmailM', '9', '3', null, '1');
INSERT INTO `t_menu` VALUES ('12', '置二维码', 'tool/toQrCode', '9', '4', null, '1');
INSERT INTO `t_menu` VALUES ('14', '地图工具', 'tool/map', '9', '6', null, '1');
INSERT INTO `t_menu` VALUES ('15', '广告管理', 'adv/list', '6', '2', null, '2');
INSERT INTO `t_menu` VALUES ('16', '特别推荐', 'recommend/list', '6', '3', null, '2');
INSERT INTO `t_menu` VALUES ('17', '线路管理', 'line/list', '6', '4', null, '2');
INSERT INTO `t_menu` VALUES ('18', '友情链接', 'link/list', '6', '6', null, '2');
INSERT INTO `t_menu` VALUES ('19', '新闻管理', 'news/list', '6', '7', null, '2');
INSERT INTO `t_menu` VALUES ('20', '公告管理', 'notice/list', '6', '8', null, '2');
INSERT INTO `t_menu` VALUES ('21', '在线管理', 'onlineUser/list', '1', '5', null, '1');
INSERT INTO `t_menu` VALUES ('23', '微信管理', '#', '0', '4', 'icon-comments', '2');
INSERT INTO `t_menu` VALUES ('24', '应用命令', 'command/list', '23', '1', null, '2');
INSERT INTO `t_menu` VALUES ('25', '图文回复', 'imgmsg/list', '23', '2', null, '2');
INSERT INTO `t_menu` VALUES ('26', '文本回复', 'textMsg/list', '23', '3', null, '2');
INSERT INTO `t_menu` VALUES ('27', '关注回复', 'textMsg/goSubscribe', '23', '4', null, '2');
INSERT INTO `t_menu` VALUES ('28', '数据挖掘', '#', '0', '6', 'icon-edit', '2');
INSERT INTO `t_menu` VALUES ('29', '数据采集', 'crawl', '28', '1', null, '2');

-- ----------------------------
-- Table structure for `t_news`
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '新闻ID',
  `title` varchar(150) DEFAULT NULL COMMENT '新闻标题',
  `content` text COMMENT '新闻内容',
  `publisher` varchar(50) DEFAULT NULL COMMENT '发布人',
  `addtime` varchar(50) DEFAULT NULL COMMENT '发布时间',
  `uptime` varchar(50) DEFAULT NULL COMMENT '修改时间',
  `pip` varchar(50) DEFAULT NULL COMMENT '发布IP',
  `hits` int(11) DEFAULT '0' COMMENT '点击数',
  `sequence` int(10) DEFAULT '0' COMMENT '排序',
  `recommand` char(2) DEFAULT '0' COMMENT '推荐  0默认未推荐，1推荐',
  `status` char(2) DEFAULT '0' COMMENT '状态  0默认未发布，1发布',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_news
-- ----------------------------
INSERT INTO `t_news` VALUES ('2', '新闻标题2', '<p>新闻内容2</p>', '张三2', '2014-12-01 20:59:33', '2015-02-08 23:51:26', '127.0.0.1', '0', '2', '1', '1');
INSERT INTO `t_news` VALUES ('3', '世界大事件', '<p>wwww</p>', 'fdgf', '2014-12-02 01:07:41', '2015-04-19 03:02:29', '127.0.0.1', '0', '0', '0', '1');
INSERT INTO `t_news` VALUES ('5', '新闻三十分', '<p>4565656</p>', '456465', '2014-12-15 11:38:59', '2015-04-19 03:01:58', '127.0.0.1', '0', '4564', '0', '1');
INSERT INTO `t_news` VALUES ('6', 'aaaa', '<p><img src=\"http://img.baidu.com/hi/jx2/j_0024.gif\"/></p>', 'ddddd', '2014-12-24 10:55:34', '2015-08-26 17:54:27', '0:0:0:0:0:0:0:1', '0', '0', '0', '1');
INSERT INTO `t_news` VALUES ('7', '足球新闻部', '<p>sdfs</p>', 'sdf', '2015-04-19 03:01:06', '2015-04-19 03:02:13', '127.0.0.1', '0', '3', '0', '0');
INSERT INTO `t_news` VALUES ('8', 'fsdafsadfsadfsdaf', '<p>dsafdasfasdfafa</p>', 'fdsaf', '2015-08-24 21:20:42', '2015-08-24 21:20:48', '0:0:0:0:0:0:0:1', '0', '12121', '1', '1');
INSERT INTO `t_news` VALUES ('9', 'safsadf', '<p>sadfasf</p>', 'dsfsafa', '2015-08-26 17:54:33', '2015-08-26 17:54:33', '0:0:0:0:0:0:0:1', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `t_notice`
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) NOT NULL COMMENT '公告标题',
  `content` varchar(500) NOT NULL COMMENT '公告内容',
  `publisher` varchar(50) NOT NULL COMMENT '公告发布人',
  `addtime` datetime NOT NULL COMMENT '公告发布时间',
  `uptime` datetime NOT NULL COMMENT '修改时间',
  `status` char(2) NOT NULL DEFAULT '0' COMMENT '状态  0默认未启用，1启用',
  `sequence` int(10) DEFAULT '0' COMMENT '公告排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_notice
-- ----------------------------

-- ----------------------------
-- Table structure for `t_pic`
-- ----------------------------
DROP TABLE IF EXISTS `t_pic`;
CREATE TABLE `t_pic` (
  `PICTURES_ID` varchar(100) NOT NULL,
  `TITLE` varchar(255) DEFAULT NULL COMMENT '标题',
  `NAME` varchar(255) DEFAULT NULL COMMENT '文件名',
  `PATH` varchar(255) DEFAULT NULL COMMENT '路径',
  `CREATETIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `MASTER_ID` varchar(255) DEFAULT NULL COMMENT '属于',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`PICTURES_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_pic
-- ----------------------------
INSERT INTO `t_pic` VALUES ('1e67c0bde5d344919f4262419aa192b6', 'web', 'e6d9f070f1dd4d189001890bcecd564e.jpg', '20150901/e6d9f070f1dd4d189001890bcecd564e.jpg', '2015-08-26 17:47:07', '1', '图片管理处上传');
INSERT INTO `t_pic` VALUES ('27a927b19e6948f29792a50615b0fb56', '图片', '3f1285b86f5044c8be3a1ab36da470c4.png', '20150826/3f1285b86f5044c8be3a1ab36da470c4.png', '2015-08-26 17:47:07', '1', '图片管理处上传');
INSERT INTO `t_pic` VALUES ('301e8674e4614f83aff88773f8433f88', '图片', '2dc04199899648f094b4853462e33387.png', '20150826/2dc04199899648f094b4853462e33387.png', '2015-08-26 17:17:07', '1', '图片管理处上传');
INSERT INTO `t_pic` VALUES ('34c8955dbd8949b981bc16a8750fe7d0', '图片', '06979a45ab094c768c3f9ef6fa2e29aa.jpg', '20150826/06979a45ab094c768c3f9ef6fa2e29aa.jpg', '2015-08-26 17:47:07', '1', '图片管理处上传');
INSERT INTO `t_pic` VALUES ('37bfee0081fa41ca885eade0504bc76b', '图片', '2d91b417af3a46aeb4df48f30c667a54.png', '20150826/2d91b417af3a46aeb4df48f30c667a54.png', '2015-08-26 17:47:07', '1', '图片管理处上传');
INSERT INTO `t_pic` VALUES ('87d0d8c77ae0448da5128c70ace11bb2', '图片', 'c2930b72970443129f70f82afeede796.png', '20150826/c2930b72970443129f70f82afeede796.png', '2015-08-26 17:46:57', '1', '图片管理处上传');

-- ----------------------------
-- Table structure for `t_recommend`
-- ----------------------------
DROP TABLE IF EXISTS `t_recommend`;
CREATE TABLE `t_recommend` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '特别推荐ID',
  `title` varchar(200) DEFAULT NULL COMMENT '特别推荐标题',
  `content` varchar(300) DEFAULT NULL COMMENT '特别推荐内容',
  `url` varchar(300) DEFAULT NULL COMMENT '特别推荐连接地址',
  `heat` char(2) DEFAULT '0' COMMENT '热度  按1 2 3 4 5级',
  `stars` char(2) DEFAULT '0' COMMENT '星级  按1 2 3 4 5级',
  `sequence` int(10) DEFAULT '0' COMMENT '排序',
  `addtime` datetime DEFAULT NULL COMMENT '添加时间',
  `uptime` datetime DEFAULT NULL COMMENT '修改时间',
  `status` char(2) DEFAULT '0' COMMENT '状态 0为未启用，1为启用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_recommend
-- ----------------------------
INSERT INTO `t_recommend` VALUES ('3', 'dsfdsfdasf', '<p>afdsafsadfdsafsafs</p>', '', '1', '1', '0', '2015-08-26 17:47:39', '2015-08-26 17:47:45', '0');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `perms` varchar(255) DEFAULT NULL,
  `permId` varchar(100) DEFAULT NULL,
  `parentId` varchar(100) DEFAULT NULL,
  `permAdd` varchar(255) DEFAULT NULL,
  `permEdit` varchar(255) DEFAULT NULL,
  `permDel` varchar(255) DEFAULT NULL,
  `permQuery` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', '系统管理员', '1069547510', '1', '0', '1', '1', '1', '1');
INSERT INTO `t_role` VALUES ('2', '超级管理员', '1069547510', '2', '1', '2097206', '805306368', '', '264241142');
INSERT INTO `t_role` VALUES ('4', '用户组', '118', null, '0', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('55896f5ce3c0494fa6850775a4e29ff6', '特级会员', '498', '55896f5ce3c0494fa6850775a4e29ff6', '7', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('6', '客户组', '18', null, '0', '1', '1', '1', '1');
INSERT INTO `t_role` VALUES ('68f23fc0caee475bae8d52244dea8444', '中级会员', '498', '68f23fc0caee475bae8d52244dea8444', '7', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('6f610a47f262435ba546b75781213b28', 'cccc', '54', '6f610a47f262435ba546b75781213b28', '4', '805306368', '0', '0', '4161782');
INSERT INTO `t_role` VALUES ('7', '会员组', '498', null, '0', '0', '0', '0', '1');
INSERT INTO `t_role` VALUES ('7dfd8d1f7b6245d283217b7e63eec9b2', '管理员B', '1069547510', '7dfd8d1f7b6245d283217b7e63eec9b2', '1', '246', '0', '0', '1069547510');
INSERT INTO `t_role` VALUES ('81ea69a7095e481c9776c40816514ce9', 'xx', '118', '81ea69a7095e481c9776c40816514ce9', '4', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('a0723aa56a7c439cb1e7e09ddac4096f', '钻石会员', '', 'a0723aa56a7c439cb1e7e09ddac4096f', '7', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('ac66961adaa2426da4470c72ffeec117', '管理员A', '1069547510', 'ac66961adaa2426da4470c72ffeec117', '1', '54', '54', '0', '1069547510');
INSERT INTO `t_role` VALUES ('b0c77c29dfa140dc9b14a29c056f824f', '高级会员', '498', 'b0c77c29dfa140dc9b14a29c056f824f', '7', '0', '0', '0', '0');
INSERT INTO `t_role` VALUES ('e74f713314154c35bd7fc98897859fe3', '黄金客户', '2064594', 'e74f713314154c35bd7fc98897859fe3', '6', '260046848', '1', '1', '1');
INSERT INTO `t_role` VALUES ('f944a9df72634249bbcb8cb73b0c9b86', '初级会员', '', 'f944a9df72634249bbcb8cb73b0c9b86', '7', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for `t_role_perm`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_perm`;
CREATE TABLE `t_role_perm` (
  `GL_ID` varchar(100) NOT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `FX_QX` int(10) DEFAULT NULL,
  `FW_QX` int(10) DEFAULT NULL,
  `QX1` int(10) DEFAULT NULL,
  `QX2` int(10) DEFAULT NULL,
  `QX3` int(10) DEFAULT NULL,
  `QX4` int(10) DEFAULT NULL,
  PRIMARY KEY (`GL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role_perm
-- ----------------------------
INSERT INTO `t_role_perm` VALUES ('1', '2', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_role_perm` VALUES ('2', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_role_perm` VALUES ('55896f5ce3c0494fa6850775a4e29ff6', '7', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_role_perm` VALUES ('68f23fc0caee475bae8d52244dea8444', '7', '0', '0', '1', '1', '0', '0');
INSERT INTO `t_role_perm` VALUES ('6f610a47f262435ba546b75781213b28', '4', '0', '1', '0', '0', '0', '0');
INSERT INTO `t_role_perm` VALUES ('7dfd8d1f7b6245d283217b7e63eec9b2', '1', '1', '1', '1', '1', '0', '0');
INSERT INTO `t_role_perm` VALUES ('81ea69a7095e481c9776c40816514ce9', '4', '0', '1', '0', '0', '0', '0');
INSERT INTO `t_role_perm` VALUES ('a0723aa56a7c439cb1e7e09ddac4096f', '7', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_role_perm` VALUES ('ac66961adaa2426da4470c72ffeec117', '1', '1', '1', '1', '1', '0', '0');
INSERT INTO `t_role_perm` VALUES ('b0c77c29dfa140dc9b14a29c056f824f', '7', '1', '0', '1', '1', '0', '0');
INSERT INTO `t_role_perm` VALUES ('e74f713314154c35bd7fc98897859fe3', '6', '1', '1', '1', '1', '0', '0');
INSERT INTO `t_role_perm` VALUES ('f944a9df72634249bbcb8cb73b0c9b86', '7', '1', '1', '1', '1', '0', '0');

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `USER_ID` varchar(100) NOT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `RIGHTS` varchar(255) DEFAULT NULL,
  `ROLE_ID` varchar(100) DEFAULT NULL,
  `LAST_LOGIN` varchar(255) DEFAULT NULL,
  `IP` varchar(100) DEFAULT NULL,
  `STATUS` varchar(32) DEFAULT NULL,
  `BZ` varchar(255) DEFAULT NULL,
  `SKIN` varchar(100) DEFAULT NULL,
  `EMAIL` varchar(32) DEFAULT NULL,
  `NUMBER` varchar(100) DEFAULT NULL,
  `PHONE` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'admin', 'de41b7fb99201d8334c23c014db35ecd92df81bc', '系统管理员', '1133671055321055258374707980945218933803269864762743594642571294', '1', '2015-09-02 21:54:35', '0:0:0:0:0:0:0:1', '0', 'xxx', 'default', 'admin@main.com', '001', '18788888888');
INSERT INTO `t_user` VALUES ('9e23643380d74fd2947ab25af748ff7d', 'zhangsan', '5ee5d458d02fde6170b9c3ebfe06af85dacd131d', '张三', '', '2', '', '', '0', 'zhang', 'default', 'krisibm@163.com', '111', '18810489560');

-- ----------------------------
-- Table structure for `t_user_perm`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_perm`;
CREATE TABLE `t_user_perm` (
  `U_ID` varchar(100) NOT NULL,
  `C1` int(10) DEFAULT NULL,
  `C2` int(10) DEFAULT NULL,
  `C3` int(10) DEFAULT NULL,
  `C4` int(10) DEFAULT NULL,
  `Q1` int(10) DEFAULT NULL,
  `Q2` int(10) DEFAULT NULL,
  `Q3` int(10) DEFAULT NULL,
  `Q4` int(10) DEFAULT NULL,
  PRIMARY KEY (`U_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user_perm
-- ----------------------------
INSERT INTO `t_user_perm` VALUES ('1', '1', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('2', '1', '1', '1', '1', '1', '1', '1', '1');
INSERT INTO `t_user_perm` VALUES ('55896f5ce3c0494fa6850775a4e29ff6', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('68f23fc0caee475bae8d52244dea8444', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('6f610a47f262435ba546b75781213b28', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('7dfd8d1f7b6245d283217b7e63eec9b2', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('81ea69a7095e481c9776c40816514ce9', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('a0723aa56a7c439cb1e7e09ddac4096f', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('ac66961adaa2426da4470c72ffeec117', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('b0c77c29dfa140dc9b14a29c056f824f', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('e74f713314154c35bd7fc98897859fe3', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `t_user_perm` VALUES ('f944a9df72634249bbcb8cb73b0c9b86', '0', '0', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for `t_weixin_command`
-- ----------------------------
DROP TABLE IF EXISTS `t_weixin_command`;
CREATE TABLE `t_weixin_command` (
  `COMMAND_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `COMMANDCODE` varchar(255) DEFAULT NULL COMMENT '应用路径',
  `CREATETIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(1) NOT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`COMMAND_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_weixin_command
-- ----------------------------
INSERT INTO `t_weixin_command` VALUES ('2636750f6978451b8330874c9be042c2', '锁定服务器', 'rundll32.exe user32.dll,LockWorkStation', '2015-05-10 21:25:06', '1', '锁定计算机');
INSERT INTO `t_weixin_command` VALUES ('46217c6d44354010823241ef484f7214', '打开浏览器', 'C:/Program Files/Internet Explorer/iexplore.exe', '2015-05-09 02:43:02', '1', '打开浏览器操作');
INSERT INTO `t_weixin_command` VALUES ('576adcecce504bf3bb34c6b4da79a177', '关闭浏览器', 'taskkill /f /im iexplore.exe', '2015-05-09 02:36:48', '1', '关闭浏览器操作');
INSERT INTO `t_weixin_command` VALUES ('854a157c6d99499493f4cc303674c01f', '关闭QQ', 'taskkill /f /im qq.exe', '2015-05-10 21:25:46', '1', '关闭QQ');
INSERT INTO `t_weixin_command` VALUES ('ab3a8c6310ca4dc8b803ecc547e55ae7', '打开QQ', 'D:/SOFT/QQ/QQ/Bin/qq.exe', '2015-05-10 21:25:25', '1', '打开QQ');

-- ----------------------------
-- Table structure for `t_weixin_imgmsg`
-- ----------------------------
DROP TABLE IF EXISTS `t_weixin_imgmsg`;
CREATE TABLE `t_weixin_imgmsg` (
  `IMGMSG_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `CREATETIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(11) NOT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  `TITLE1` varchar(255) DEFAULT NULL COMMENT '标题1',
  `DESCRIPTION1` varchar(255) DEFAULT NULL COMMENT '描述1',
  `IMGURL1` varchar(255) DEFAULT NULL COMMENT '图片地址1',
  `TOURL1` varchar(255) DEFAULT NULL COMMENT '超链接1',
  `TITLE2` varchar(255) DEFAULT NULL COMMENT '标题2',
  `DESCRIPTION2` varchar(255) DEFAULT NULL COMMENT '描述2',
  `IMGURL2` varchar(255) DEFAULT NULL COMMENT '图片地址2',
  `TOURL2` varchar(255) DEFAULT NULL COMMENT '超链接2',
  `TITLE3` varchar(255) DEFAULT NULL COMMENT '标题3',
  `DESCRIPTION3` varchar(255) DEFAULT NULL COMMENT '描述3',
  `IMGURL3` varchar(255) DEFAULT NULL COMMENT '图片地址3',
  `TOURL3` varchar(255) DEFAULT NULL COMMENT '超链接3',
  `TITLE4` varchar(255) DEFAULT NULL COMMENT '标题4',
  `DESCRIPTION4` varchar(255) DEFAULT NULL COMMENT '描述4',
  `IMGURL4` varchar(255) DEFAULT NULL COMMENT '图片地址4',
  `TOURL4` varchar(255) DEFAULT NULL COMMENT '超链接4',
  `TITLE5` varchar(255) DEFAULT NULL COMMENT '标题5',
  `DESCRIPTION5` varchar(255) DEFAULT NULL COMMENT '描述5',
  `IMGURL5` varchar(255) DEFAULT NULL COMMENT '图片地址5',
  `TOURL5` varchar(255) DEFAULT NULL COMMENT '超链接5',
  `TITLE6` varchar(255) DEFAULT NULL COMMENT '标题6',
  `DESCRIPTION6` varchar(255) DEFAULT NULL COMMENT '描述6',
  `IMGURL6` varchar(255) DEFAULT NULL COMMENT '图片地址6',
  `TOURL6` varchar(255) DEFAULT NULL COMMENT '超链接6',
  `TITLE7` varchar(255) DEFAULT NULL COMMENT '标题7',
  `DESCRIPTION7` varchar(255) DEFAULT NULL COMMENT '描述7',
  `IMGURL7` varchar(255) DEFAULT NULL COMMENT '图片地址7',
  `TOURL7` varchar(255) DEFAULT NULL COMMENT '超链接7',
  `TITLE8` varchar(255) DEFAULT NULL COMMENT '标题8',
  `DESCRIPTION8` varchar(255) DEFAULT NULL COMMENT '描述8',
  `IMGURL8` varchar(255) DEFAULT NULL COMMENT '图片地址8',
  `TOURL8` varchar(255) DEFAULT NULL COMMENT '超链接8',
  PRIMARY KEY (`IMGMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_weixin_imgmsg
-- ----------------------------
INSERT INTO `t_weixin_imgmsg` VALUES ('380b2cb1f4954315b0e20618f7b5bd8f', '首页', '2015-05-10 20:51:09', '1', '图文回复', '图文回复标题', '图文回复描述', 'http://a.hiphotos.baidu.com/image/h%3D360/sign=c6c7e73ebc389b5027ffe654b535e5f1/a686c9177f3e6709392bb8df3ec79f3df8dc55e3.jpg', 'www.baidu.com', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '');

-- ----------------------------
-- Table structure for `t_weixin_textmsg`
-- ----------------------------
DROP TABLE IF EXISTS `t_weixin_textmsg`;
CREATE TABLE `t_weixin_textmsg` (
  `TEXTMSG_ID` varchar(100) NOT NULL,
  `KEYWORD` varchar(255) DEFAULT NULL COMMENT '关键词',
  `CONTENT` varchar(255) DEFAULT NULL COMMENT '内容',
  `CREATETIME` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `STATUS` int(11) DEFAULT NULL COMMENT '状态',
  `BZ` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`TEXTMSG_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_weixin_textmsg
-- ----------------------------
INSERT INTO `t_weixin_textmsg` VALUES ('303c190498a045bdbba4c940c2f0d9f9', '1ss', '1ssddd', '2015-05-18 20:17:02', '1', '1ssdddsd');
INSERT INTO `t_weixin_textmsg` VALUES ('63681adbe7144f10b66d6863e07f23c2', '你好', '你也好', '2015-05-09 02:39:23', '1', '文本回复');
INSERT INTO `t_weixin_textmsg` VALUES ('695cd74779734231928a253107ab0eeb', '吃饭', '吃了噢噢噢噢', '2015-05-10 22:52:27', '1', '文本回复');
INSERT INTO `t_weixin_textmsg` VALUES ('d4738af7aea74a6ca1a5fb25a98f9acb', '关注', '关注', '2015-05-11 02:12:36', '1', '关注回复');

-- ----------------------------
-- Function structure for `get_count`
-- ----------------------------
DROP FUNCTION IF EXISTS `get_count`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `get_count`(p_id varchar(100)) RETURNS tinyint(4)
BEGIN
		DECLARE p_count INT(11);
		SELECT COUNT('LINE_ID') into p_count from tb_line where PARENT_ID = p_id;
    RETURN p_count;
 END
;;
DELIMITER ;
