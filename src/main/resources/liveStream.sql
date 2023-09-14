

mysql:account:root password:livestream:ma6oH
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

 private Integer id;
    private String content;
    private String summary;
    private Date createDate;
    private Date editDate;
    private String status;