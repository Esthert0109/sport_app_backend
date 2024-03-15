DROP TABLE IF EXISTS `fei_jing_basketball_team`;
CREATE TABLE `fei_jing_basketball_team`  (
  `team_id` int NOT NULL,
  `league_id` int NULL DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `name_en` varchar(255) DEFAULT NULL,
  `name_cn` varchar(255) DEFAULT NULL,
  coach_cn  varchar(255) default null,
  coach_en varchar(255) default null,
  PRIMARY KEY (`team_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4;



DROP TABLE IF EXISTS `fei_jing_basketball_match`;
CREATE TABLE `fei_jing_basketball_match` (
  `match_id` int NOT NULL  COMMENT 'match_id',
  `competition_id` int NOT NULL  COMMENT 'competition_id ',
  `competition_name` varchar(128) COMMENT 'competition name',
  `home_team_id` int NOT NULL  COMMENT 'home team id',
  `away_team_id` int NOT NULL  COMMENT 'away team id',
  `home_team_name` varchar(255) COMMENT 'home team name',
  `home_team_cns` varchar(255) COMMENT 'home team name cns',
  `away_team_name` varchar(255) COMMENT 'away team name',
  `away_team_cns` varchar(255) COMMENT 'away team name cns',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away team logo',
  `status` varchar(16) NOT NULL COMMENT 'status',
  `match_time` varchar(16)  COMMENT 'match time',
  `match_date` varchar(16)  COMMENT 'match date',
  `h_f_quarter` int(3) NOT NULL COMMENT 'hf_quarter',
  `h_s_quarter` int(3) NOT NULL COMMENT 'hs_quarter',
  `h_t_quarter` int(3) NOT NULL COMMENT 'ht_quarter',
  `h_4_quarter` int(3) NOT NULL COMMENT 'h4_quarter',
  `a_f_quarter` int(3) NOT NULL COMMENT 'af_quarter',
  `a_s_quarter` int(3) NOT NULL COMMENT 'as_quarter',
  `a_t_quarter` int(3) NOT NULL COMMENT 'at_quarter',
  `a_4_quarter` varchar(32)  COMMENT 'a4_quarter',
  `home_score` int(3)  COMMENT 'home scores',
  `away_score` int(3)  COMMENT 'away scores',
    `home_coach` varchar(255) default null,
    `away_coach` varchar(255) default null,
    `has_state` boolean default false,
  `season` varchar(32)  COMMENT 'season ',
  PRIMARY KEY (`match_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_basketball_line_up`;
CREATE TABLE `all_sports_basketball_line_up` (
  `id` int(11) auto_increment COMMENT 'id',
  `match_id` bigint(20) NOT NULL  COMMENT 'match_id',
  `type` int(1) not null COMMENT '0 home 1 away',
  `player_id` bigint(20) COMMENT 'player id',
  `player_name` varchar(128) NOT NULL COMMENT 'player_name',
  `minutes` varchar(8)  COMMENT 'minutes',
  `point` varchar(8)  COMMENT 'point',
  `assists` varchar(8)  COMMENT 'assists',
  `steals` varchar(8)  COMMENT 'steals',
  `total_rebounds` varchar(8)  COMMENT 'totalRebounds',
  `free_throws_goals_attempts` varchar(8)  COMMENT 'freeThrowsGoalsAttempts',
  `free_throws_goals_made` varchar(8)  COMMENT 'freeThrowsGoalsAttempts',
  `personal_fouls` varchar(8)  COMMENT 'personalFouls',
  `turnovers` varchar(8)  COMMENT 'turnovers',
  `three_point_goals_attempts` varchar(8)  COMMENT 'threePointGoalsAttempts',
  `three_point_goals_made` varchar(8)  COMMENT 'threePointGoalsMade',
  `blocks` varchar(8) COMMENT 'blocks',
  `field_goals_attempts` varchar(8)  COMMENT 'fieldGoalsAttempts',
  `field_goals_made` varchar(8)  COMMENT 'fieldGoalsMade',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_basketball_match_live_data`;
CREATE TABLE `all_sports_basketball_match_live_data` (
  `id` int(11) auto_increment COMMENT 'id',
  `match_id` bigint(20) NOT NULL  COMMENT 'match_id',
  `status` varchar(16) NOT NULL COMMENT 'status',
  `match_time` varchar(8) COMMENT 'match time',
  `match_date` varchar(32) COMMENT 'match_date',
  `home_team_name` varchar(255) NOT NULL COMMENT 'home team name',
  `away_team_name` varchar(255) NOT NULL COMMENT 'home team name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away team logo',
  `h_f_quarter` int(3) NOT NULL COMMENT 'hf_quarter',
  `h_s_quarter` int(3) NOT NULL COMMENT 'hs_quarter',
  `h_t_quarter` int(3) NOT NULL COMMENT 'ht_quarter',
  `h_4_quarter` int(3) NOT NULL COMMENT 'h4_quarter',
  `a_f_quarter` int(3) NOT NULL COMMENT 'af_quarter',
  `a_s_quarter` int(3) NOT NULL COMMENT 'as_quarter',
  `a_t_quarter` int(3) NOT NULL COMMENT 'at_quarter',
  `a_4_quarter` varchar(32)  COMMENT 'a4_quarter',
  `h_blocks` varchar(32)  COMMENT '盖帽',
  `h_field_goals` varchar(32)  COMMENT '投篮',
  `h_free_throws` varchar(32)  COMMENT '罚球',
  `h_personal_fouls` varchar(32)  COMMENT '犯规',
  `h_rebounds` varchar(32)  COMMENT '篮板',
  `h_steals` varchar(32)  COMMENT '抢断',
  `h_three_point_goals` varchar(32)  COMMENT '3分球进球数',
  `h_turn_overs` varchar(32)  COMMENT '失误',
  `a_blocks` varchar(32)  COMMENT '盖帽',
  `a_field_goals` varchar(32)  COMMENT '投篮',
  `a_free_throws` varchar(32)  COMMENT '罚球',
  `a_personal_fouls` varchar(32)  COMMENT '犯规',
  `a_rebounds` varchar(32)  COMMENT '篮板',
  `a_steals` varchar(32)  COMMENT '抢断',
  `a_three_point_goals` varchar(32)  COMMENT '3分球进球数',
  `a_turn_overs` varchar(32)  COMMENT '失误',
  `home_score` int(3)  COMMENT 'home score',
  `away_score` int(3)  COMMENT 'away score',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=utf8mb4;

