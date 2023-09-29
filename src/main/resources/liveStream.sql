

--mysql:account:root password:livestream:ma6oH
CREATE USER 'springUser'@'%' IDENTIFIED BY 'livestream:ma6oH';
GRANT ALL PRIVILEGES ON *.* TO 'springUser'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

--GRANT SELECT, INSERT ON live_stream.* TO ‘springUser’@’localhost’;




DROP TABLE IF EXISTS `live_stream_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `live_stream_user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID，手机号码',
  `area_code` varchar(8) default '60' COMMENT ' area code',
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt) + salt)',
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT 'last time 登录时间',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



CREATE TABLE `live_stream_message` (
  `id` int NOT NULL auto_increment COMMENT 'message id',
  `summary` varchar(255)  COMMENT 'summary',
  `content` varchar(255) COMMENT 'content',
  `status` char(2) DEFAULT '0' COMMENT ' 0 up 1 down',
  `create_date` datetime DEFAULT current_timestamp()  COMMENT '注册时间',
  `edit_date` datetime DEFAULT current_timestamp() COMMENT 'edit time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_match`;
CREATE TABLE `football_match` (
  `id` int NOT NULL  COMMENT 'id',
  `season_id` int COMMENT 'season id',
  `competition_id` int NOT NULL COMMENT 'competition_id',
  `status_id` int  COMMENT 'status 0 比赛异常 1 未开赛 2 上半场 3 中场 4 下半场 5 
  加时赛 6加时赛(弃用) 7 点球决赛 8 完场 9 推迟 10 中断 11 腰斩 12 取消 13 待定 ',
  `match_time` bigint COMMENT 'match time',
  `home_team_id` int  COMMENT ' home team id',
  `away_team_id` int  COMMENT ' away team id',
  `home_team_score` int  COMMENT ' home team score',
  `away_team_score` int  COMMENT ' away team score',
  `line_up` int COMMENT 'if there is a line-up',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
 
 
 
