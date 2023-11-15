DROP TABLE IF EXISTS `basketball_competition`;
CREATE TABLE `basketball_competition` (
  `competition_id` bigint(20) NOT NULL  COMMENT 'id',
  `category_id` int(11) COMMENT 'logo',
  `name_zh` varchar(255) NOT NULL COMMENT 'chinese name',
  `name_en` varchar(255)  COMMENT 'english name',
  `logo` varchar(255) COMMENT 'logo',
  `type` int  COMMENT 'type 赛事类型，0-未知、1-联赛、2-杯赛',
  `updated_at` bigint   COMMENT 'update time',
  PRIMARY KEY (`competition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `basketball_team`;
CREATE TABLE `basketball_team` (
  `team_id` bigint(20) NOT NULL  COMMENT 'team id',
  `competition_id` bigint(20) NOT NULL  COMMENT 'competition_id ',
  `conference_id` int(11) NOT NULL  COMMENT ' /*赛区id，1-大西洋赛区、2-中部赛区、3-东南赛区、4-太平洋赛区、5-西北赛区、6-西南赛区、
    7-A组赛区、8-B组赛区、9-C组赛区、10-D组赛区（1~6:NBA 7~10:CBA）、0-无*/',
  `name_zh` varchar(255) NOT NULL COMMENT 'chinese team name',
  `name_en` varchar(255)  COMMENT 'english name',
  `logo` varchar(255) COMMENT 'logo',
  `updated_at` bigint(20)   COMMENT 'update time',
  PRIMARY KEY (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `basketball_match`;
CREATE TABLE `basketball_match` (
  `match_id` bigint(20) NOT NULL  COMMENT 'match_id',
  `competition_id` bigint(20) NOT NULL  COMMENT 'competition_id ',
  `home_team_id` bigint(20) NOT NULL  COMMENT 'home team id',
  `away_team_id` bigint(20) NOT NULL  COMMENT 'away team id',
  `home_team_name` varchar(255) COMMENT 'home team name',
  `away_team_name` varchar(255) COMMENT 'away team name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away team logo',
  `kind` int(2) NOT NULL  COMMENT '1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无',
  `period_count` int(1) NOT NULL  COMMENT '',
  `status_id` int(2) NOT NULL COMMENT 'status',
  `match_time` bigint(20)  COMMENT 'english name',
  `home_score` int(3) NOT NULL COMMENT 'home scores',
  `away_score` int(3) NOT NULL COMMENT 'away scores',
  `season_id` int(2) NOT NULL COMMENT 'season id',
  `updated_at` bigint(20)   COMMENT 'update time',
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;










