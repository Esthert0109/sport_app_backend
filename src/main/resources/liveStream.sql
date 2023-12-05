

--mysql:account:root password:livestream:ma6oH
CREATE USER 'springUser'@'%' IDENTIFIED BY 'livestream:ma6oH';
GRANT ALL PRIVILEGES ON *.* TO 'springUser'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

--GRANT SELECT, INSERT ON live_stream.* TO ‘springUser’@’localhost’;




DROP TABLE IF EXISTS `live_stream_user`;
CREATE TABLE `live_stream_user` (
  `id` bigint(20) NOT NULL COMMENT '用户ID，手机号码',
  `area_code` varchar(8) default '60' COMMENT ' area code',
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt) + salt)',
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT 'last time 登录时间',
  `role` char(1) default '0' comment '0 normal user 1 anchor',
  `login_total` int(11) DEFAULT '0' COMMENT '登录次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `live_stream_message`;
CREATE TABLE `live_stream_message` (
  `id` int NOT NULL auto_increment COMMENT 'message id',
  `summary` varchar(255)  COMMENT 'summary',
  `content` varchar(255) COMMENT 'content',
  `status` char(2) DEFAULT '1' COMMENT ' 0 down 1 up',
  `create_date` datetime DEFAULT current_timestamp()  COMMENT '注册时间',
  `edit_date` datetime DEFAULT current_timestamp() COMMENT 'edit time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `live_stream_collection`;
CREATE TABLE `live_stream_collection` (
  `id` int NOT NULL auto_increment COMMENT 'message id',
  `user_id` bigint  COMMENT 'user id',
  `match_id` int COMMENT 'match_id',
  `category` char(2) COMMENT '0 football 1 basketball',
  `status` char(2) DEFAULT '1' COMMENT ' 0 down 1 up',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `football_competition`;
CREATE TABLE `football_competition` (
  `id` int NOT NULL  COMMENT 'id',
  `logo` varchar(255) COMMENT 'logo',
  `name_zh` varchar(255) NOT NULL COMMENT 'chinese name',
  `name_en` varchar(255)  COMMENT 'english name',
  `short_name_zh` varchar(255) COMMENT 'short chinese name',
  `short_name_en` int  COMMENT 'short english name',
  `type` int  COMMENT 'type 赛事类型，0-未知、1-联赛、2-杯赛、3-友谊赛 ',
  `updated_at` bigint   COMMENT 'update time',
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
  `home_team_name` varchar(255) COMMENT 'home team name',
  `away_team_name` varchar(255) COMMENT 'away home name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away home logo',
  `home_team_score` int  COMMENT ' home team score',
  `away_team_score` int  COMMENT ' away team score',
  `home_formation` varchar(255) COMMENT 'home formation',
  `away_formation` varchar(255) COMMENT 'away formation',
  `referee_id` int COMMENT 'referee id',
  `venue_id` int COMMENT 'venue id',
  `line_up` int COMMENT 'if there is a line-up, 0 no 1 yes',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_team`;
CREATE TABLE `football_team` (
  `id` int NOT NULL  COMMENT 'id',
  `competition_id` int NOT NULL  COMMENT 'competition_id',
  `name_zh` varchar(255) NOT NULL COMMENT 'chinese team name',
  `name_en` varchar(255)  COMMENT 'english name',
  `logo` varchar(255) COMMENT 'logo',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `update_football_data`;
CREATE TABLE `update_football_data` (
  `id` int NOT NULL auto_increment  COMMENT 'id',
  `match_id` int not null COMMENT 'match id',
  `season_id` int COMMENT 'season id',
  `competition_id` int NOT NULL COMMENT 'competition_id',
  `pub_time` int  COMMENT 'pub time',
  `update_time` bigint  COMMENT ' update time',
  `unique_key` bigint COMMENT 'unique key=match_id + season_id + competition_id + put_time + update_time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `home_match_line_up`;
CREATE TABLE `home_match_line_up` (
  `id` int NOT NULL   COMMENT 'id',
  `player_id` int COMMENT 'player id',
  `match_id` int not null COMMENT 'match id',
  `team_id` int COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  NOT null COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` varchar(255)  COMMENT '球员位置，F前锋、M中场、D后卫、G守门员、其他为未知',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `away_match_line_up`;
CREATE TABLE `away_match_line_up` (
  `id` int NOT NULL   COMMENT 'id',
  `player_id` int COMMENT 'player id',
  `match_id` int not null COMMENT 'match id',
  `team_id` int COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  NOT null COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` varchar(255)  COMMENT '球员位置，F前锋、M中场、D后卫、G守门员、其他为未知',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `football_match_live_data`;
CREATE TABLE `football_match_live_data` (
  `id` int auto_increment NOT NULL   COMMENT 'id',
  `match_id` int not null COMMENT 'match id',
  `status_id` int COMMENT 'status id',
  `home_team_id` int COMMENT 'home team id ',
  `away_team_id` int COMMENT 'away team id',
  `home_team_name` varchar(255) NOT NULL COMMENT 'home team name',
  `away_team_name` varchar(255) NOT NULL COMMENT 'away team name',
  `home_attack_num` int COMMENT 'homeAttackNum',
  `away_attack_num` int COMMENT 'awayAttackNum',
  `home_attack_danger_num` int COMMENT 'homeAttackDangerNum',
  `away_attack_danger_num` int COMMENT 'awayAttackDangerNum',
  `home_possession_rate` int COMMENT 'homePossessionRate',
  `away_possession_rate` int COMMENT 'awayPossessionRate',
  `home_shoot_goal_num` int COMMENT 'homeShootGoalNum',
  `away_shoot_goal_num` int COMMENT 'awayShootGoalNum',
  `home_bias_num` int COMMENT 'homeBiasNum',
  `away_bias_num` int COMMENT 'awayBiasNum',
  `home_corner_kick_num` int COMMENT 'homeCornerKickNum',
  `away_corner_kick_num` int COMMENT 'awayCornerKickNum',
  `home_red_card_num` int COMMENT 'homeRedCardNum',
  `away_red_card_num` int COMMENT 'awayRedCardNum',
  `home_yellow_card_num` int COMMENT 'homeYellowCardNum',
  `away_yellow_card_num` int COMMENT 'awayYellowCardNum',
  `home_penalty_Num` int COMMENT 'home penalty num',
  `away_penalty_Num` int COMMENT 'away penalty num',
  `home_score` int COMMENT 'homeScore',
  `away_score` int COMMENT 'awayScore',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_live_address`;
CREATE TABLE `football_live_address` (
  `id` int NOT NULL auto_increment COMMENT 'id',
  `sport_id` int not null COMMENT '1-足球、2-篮球、3-网球',
  `match_id` int not null COMMENT 'match id',
  `match_time` bigint COMMENT 'match time',
  `match_status` int not null COMMENT 'match status',
  `comp_id` int not null COMMENT 'competition id',
  `comp` varchar(255) COMMENT 'competition description ',
  `home_team` varchar(255) COMMENT 'home team description',
  `away_team` varchar(255)  COMMENT 'away team description',
  `push_url1` varchar(255)  COMMENT 'push url 1',
  `push_url2` varchar(255)  COMMENT 'push url 2',
  `push_url3` varchar(255) COMMENT 'push url 3',
  `create_date` datetime DEFAULT current_timestamp()  COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_live_video`;
CREATE TABLE `football_live_video` (
  `id` int NOT NULL auto_increment   COMMENT 'id',
  `match_id` int not null COMMENT 'match id',
  `type` int COMMENT '类型，1-集锦、2-录像',
  `title` varchar(255) COMMENT 'title',
  `mobile_link` varchar(255)  COMMENT 'mobile link',
  `pc_link` varchar(255) COMMENT 'pc link',
  `create_date` datetime DEFAULT current_timestamp()  COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `football_venue`;
CREATE TABLE `football_venue` (
  `id` int NOT NULL  COMMENT 'id',
  `name_zh` varchar(255)  COMMENT 'chinese team name',
  `name_en` varchar(255)  COMMENT 'english name',
  `capacity` int COMMENT 'capacity',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_referee`;
CREATE TABLE `football_referee` (
  `id` int NOT NULL  COMMENT 'id',
  `name_zh` varchar(255)  COMMENT 'chinese team name',
  `name_en` varchar(255)  COMMENT 'english name',
  `birthday` int COMMENT 'birthday',
  `age` int  COMMENT 'age',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `football_coach`;
CREATE TABLE `football_coach` (
  `id` int NOT NULL  COMMENT 'id',
  `name_zh` varchar(255)  COMMENT 'chinese team name',
  `name_en` varchar(255)  COMMENT 'english name',
  `logo` varchar(255)  COMMENT 'logo',
  `preferred_formation` varchar(255)  COMMENT 'preferred_formation',
  `team_id` int COMMENT 'team id',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `anchor_follow`;
create table anchor_follow  (
    id int(11) auto_increment comment 'id',
    anchor_id int(11) comment 'anchor id',
    follower_id int(11) comment 'follower_id',
    follow_date datetime default null comment 'follow date',
    PRIMARY KEY (id)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `popular_search`;
create table popular_search (
    id int(11) auto_increment comment 'id',
    popular_keywords varchar(255) default null comment 'popular competition or popular team',
    create_date datetime default null comment 'create time',
    PRIMARY KEY (id)
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8mb4;
/* status(0 apply 1 approve)*/
DROP TABLE IF EXISTS `apply_for_live`;
create table apply_for_live(
    id int(11) auto_increment comment '编号',
    user_id bigint(20) comment '用户ID',
    apply_date datetime default null comment '申请时间',
    updated_at datetime default null comment '更新时间',
    edit_id bigint(20) comment '审核人员ID',
    status char(1) default '0' comment '审核状态',
    PRIMARY KEY (id)
)ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `live_stream_detail`;
create table live_stream_detail(
    id int(11) auto_increment comment '编号',
    user_id bigint(20) comment '用户ID',
    cover varchar(255) default null comment '封面',
    push_host varchar(255) default null comment '推流域名',
    push_code varchar(255) comment '推流码',
    live_date datetime default null comment '直播时间',
    is_popular char(1) default '0' comment '热门',
    PRIMARY KEY (id)
);

--create index
create index football_match_indexTime on football_match(match_time);
create index football_match_indexHomeTeamName on football_match(home_team_name);
create index football_match_indexAwayTeamName on football_match(away_team_name);
create index home_match_line_up_indexMatch on home_match_line_up(match_id);
create index away_match_line_up_indexMatch on away_match_line_up(match_id);
create index football_match_live_data_indexMatch on football_match_live_data(match_id);
create index football_competition_index_name on football_competition(name_zh);
create index update_football_data_index_uniqueKey on update_football_data(unique_key);
create index football_live_address_index_matchId on football_live_address(match_id);
create index live_stream_detail_index on live_stream_detail(user_id);
alter table football_match_live_data add column `home_penalty_Num` int COMMENT 'home penalty num';
alter table football_match_live_data add column `away_penalty_Num` int COMMENT 'home penalty num';
alter table live_stream_user add column `role` char(1) default '0' comment '0 normal user 1 anchor';
  `away_penalty_Num` int COMMENT 'away penalty num',






 
 
 
