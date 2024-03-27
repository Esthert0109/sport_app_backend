DROP TABLE IF EXISTS `fei_jing_football_team`;
CREATE TABLE `fei_jing_football_team`  (
   `team_id` int NOT NULL,
   `league_id` int NULL DEFAULT NULL,
   `logo` varchar(255) DEFAULT NULL,
   `name_en` varchar(255) DEFAULT NULL,
   `name_cn` varchar(255) DEFAULT NULL,
   coach_cn  varchar(255) default null,
   coach_en varchar(255) default null,
   coach_id varchar(255) default null,
   PRIMARY KEY (`team_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;

DROP TABLE IF EXISTS `fei_jing_football_line_up`;
CREATE TABLE `fei_jing_football_line_up` (
    `id` int(11) auto_increment COMMENT 'id',
    `type` int(1) COMMENT 'type 0 home 1 away',
    `player_id` int COMMENT 'player id',
    `match_id` int not null COMMENT 'match id',
    `first` int COMMENT 'first 0 no 1 yes ',
    `captain` int COMMENT 'captain 0 no 1 yes',
    `player_name` varchar(255) NOT NULL COMMENT 'player name',
    `shirt_number` int COMMENT 'shirt number',
    `position` int  COMMENT '球员位置',
    `rating` varchar(8) COMMENT 'rating',
    PRIMARY KEY (`id`),
    UNIQUE unq_player_match (player_id,match_id),
    index idx_match_id (match_id)
) ENGINE=InnoDB auto_increment = 1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `fei_jing_football_match`;
CREATE TABLE `fei_jing_football_match` (
  `match_id` int NOT NULL  COMMENT 'id',
  `season` varchar(32) COMMENT 'season ',
  `kind` int COMMENT '1：联赛 2：杯赛',
  `competition_id` int NOT NULL COMMENT ' competition',
  `league_en` varchar(255) not NULL COMMENT ' league en name',
  `league_en_short` varchar(255) not NULL COMMENT ' league en name',
  `league_cns_short` varchar(255) not NULL COMMENT ' league en name',
  `status_id` int  COMMENT 'status 0：未开 1：上半场 2：中场 3：下半场 4：加时 5：点球 -1：完场 -10：取消 -11：待定 -12：腰斩 -13：中断 -14：推迟 ',
  `match_time` varchar(8) COMMENT 'match time',
  `match_date` varchar(32) COMMENT 'match date',
  `home_team_id` int  COMMENT ' home team id',
  `away_team_id` int  COMMENT ' away team id',
  `home_team_name_en` varchar(255) COMMENT 'home team en name',
  `home_team_name_cn` varchar(255) COMMENT 'home team cn name',
  `away_team_name_en` varchar(255) COMMENT 'away home en name',
  `away_team_name_cn` varchar(255) COMMENT 'away home cn name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away home logo',
  `home_team_score` int  COMMENT ' home team score',
  `away_team_score` int  COMMENT ' away team score',
  `home_formation` varchar(255) COMMENT 'home formation',
  `away_formation` varchar(255) COMMENT 'away formation',
  `venue` varchar(255) COMMENT 'venue',
  `line_up` int COMMENT 'if there is a line-up, 0 no 1 yes',
  `updated_at` varchar(32)   COMMENT 'updated time',
   PRIMARY KEY (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `fei_jing_football_match_live_data`;
CREATE TABLE `fei_jing_football_match_live_data` (
  `match_id` int not null COMMENT 'match id',
  `home_attack_num` int COMMENT 'homeAttackNum',
  `away_attack_num` int COMMENT 'awayAttackNum',
  `home_attack_danger_num` int COMMENT 'homeAttackDangerNum',
  `away_attack_danger_num` int COMMENT 'awayAttackDangerNum',
  `home_possession_rate` varchar(16) COMMENT 'homePossessionRate',
  `away_possession_rate` varchar(16) COMMENT 'awayPossessionRate',
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
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `fei_jing_live_address`;
CREATE TABLE `fei_jing_live_address` (
  `id` int NOT NULL auto_increment COMMENT 'id',
  `sport_id` int not null COMMENT '1-足球、2-篮球、3-网球',
  `match_id` int not null COMMENT 'match id',
  `push_url1` varchar(255)  COMMENT 'push url 1',
  `push_url2` varchar(255)  COMMENT 'push url 2',
  `push_url3` varchar(255) COMMENT 'push url 3',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `fei_jing_football_animation`;
CREATE TABLE `fei_jing_football_animation` (
  `id` int NOT NULL auto_increment COMMENT 'id',
  `match_id` int not null COMMENT 'match id',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created date',
  PRIMARY KEY (`id`),
  index idx_match_id(`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `fei_jing_basketball_animation`;
CREATE TABLE `fei_jing_basketball_animation` (
  `id` int NOT NULL auto_increment COMMENT 'id',
  `match_id` int not null COMMENT 'match id',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created date',
  PRIMARY KEY (`id`),
  index idx_match_id(`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `fei_jing_basketball_infor`;
CREATE TABLE `fei_jing_basketball_infor` (
  `record_id` int NOT NULL  COMMENT 'record_id',
  `type` int not null COMMENT 'type',
  `title` varchar(255) not null comment 'title',
  `content` varchar(255) not null comment 'content',
  `img_url` varchar(255) default null comment 'img url',
  `category_id` int default null comment 'category id',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created date',
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `fei_jing_infor`;
CREATE TABLE `fei_jing_infor` (
  `id` int not null auto_increment comment 'id',
  `record_id` int NOT NULL  COMMENT 'record_id',
  `category_id` int default null comment 'category id',
  `type` int not null COMMENT 'type',
  `sport_type` int not null COMMENT '1 football 2 basketball',
  `title` varchar(255) not null comment 'title',
  `content` text not null comment 'content',
  `img_url` varchar(255) default null comment 'img url',
  `popular` char(1) default '0' comment '是否热门 0 是 1 否',
    `is_top` char(1) default '0' comment '是否置顶 0 否 1 是',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created date',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table if exists info_category;
create table info_category(
  `category_id` int not null auto_increment comment 'id',
  `category` varchar(255) not NULL  COMMENT 'category',
  `sort_num` int default null comment 'sort num',
  `created_date` timestamp DEFAULT current_timestamp()  COMMENT 'created date',
  PRIMARY KEY (`category_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



