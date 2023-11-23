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

DROP TABLE IF EXISTS `basketball_match_live_data`;
CREATE TABLE `basketball_match_live_data` (
   `id` int(11) auto_increment COMMENT 'id',
   `match_id` bigint(20) NOT NULL  COMMENT 'match_id',
   `status` int(2) NOT NULL COMMENT 'status',
   `h_f_quarter` int(3) NOT NULL COMMENT 'hf_quarter',
   `h_s_quarter` int(3) NOT NULL COMMENT 'hs_quarter',
   `h_t_quarter` int(3) NOT NULL COMMENT 'ht_quarter',
   `h_4_quarter` int(3) NOT NULL COMMENT 'h4_quarter',
   `a_f_quarter` int(3) NOT NULL COMMENT 'af_quarter',
   `a_s_quarter` int(3) NOT NULL COMMENT 'as_quarter',
   `a_t_quarter` int(3) NOT NULL COMMENT 'at_quarter',
   `a_4_quarter` int(3) NOT NULL COMMENT 'a4_quarter',
   `h_P_KickGoal` int(3) NOT NULL COMMENT '罚球进球数',
   `h_Num_Pause_Remain` int(3) NOT NULL COMMENT '剩余暂停数',
   `h_Num_Of_Fouls` int(3) NOT NULL COMMENT '犯规数',
   `h_Free_Throw_Percentage` int(3) NOT NULL COMMENT '罚球命中率',
   `h_Total_Pause` int(2) NOT NULL COMMENT '总暂停数',
   `h_Two_Goal` int(3) NOT NULL COMMENT '3分球进球数',
   `h_Three_Goal` int(3) NOT NULL COMMENT '2分球进球数',
   `a_P_KickGoal` int(3) NOT NULL COMMENT 'season id',
   `a_Num_Pause_Remain` int(3) NOT NULL COMMENT 'season id',
   `a_Num_OfFouls` int(3) NOT NULL COMMENT 'season id',
   `a_Free_Throw_Percentage` int(3) NOT NULL COMMENT 'season id',
   `a_Total_Pause` int(3) NOT NULL COMMENT 'season id',
   `a_Two_Goal` int(3) NOT NULL COMMENT 'season id',
   `a_Three_Goal` int(3) NOT NULL COMMENT 'home scores',
   `home_score` int(3) NOT NULL COMMENT 'home score',
   `away_score` int(3) NOT NULL COMMENT 'away score',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `basketball_line_up`;
CREATE TABLE `basketball_line_up` (
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

create index basketball_match_indexTime on basketball_match(match_time);
create index basketball_match_line_up_indexMatch on basketball_line_up(match_id,player_id);
create index basketball_match_live_data_indexMatch on basketball_match_live_data(match_id);
alter table basketball_match_live_data add column status int(2) comment 'status';

















