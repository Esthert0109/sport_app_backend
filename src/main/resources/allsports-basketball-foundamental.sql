DROP TABLE IF EXISTS `all_sports_basketball_match`;
CREATE TABLE `all_sports_basketball_match` (
   `match_id` bigint(20) NOT NULL  COMMENT 'match_id',
   `competition_id` bigint(20) NOT NULL  COMMENT 'competition_id ',
   `competition_name` varchar(128) COMMENT 'competition name',
   `home_team_id` bigint(20) NOT NULL  COMMENT 'home team id',
   `away_team_id` bigint(20) NOT NULL  COMMENT 'away team id',
   `home_team_name` varchar(255) COMMENT 'home team name',
   `away_team_name` varchar(255) COMMENT 'away team name',
   `home_team_logo` varchar(255) COMMENT 'home team logo',
   `away_team_logo` varchar(255) COMMENT 'away team logo',
   `status` varchar(64) NOT NULL COMMENT 'status',
   `match_time` varchar(16)  COMMENT 'match time',
   `match_date` varchar(16)  COMMENT 'match date',
   `home_score` int(3)  COMMENT 'home scores',
   `away_score` int(3)  COMMENT 'away scores',
   `season` varchar(32)  COMMENT 'season ',
   `event_live` char(2)  COMMENT 'event live 0 no playing 1 playing ',
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

create index all_sports_basketball_match_indexTime on all_sports_basketball_match(match_date);
create index all_sports_basketball_match_line_up_indexMatch on all_sports_basketball_line_up(match_id,player_id);
create index all_sports_basketball_match_live_data_indexMatch on all_sports_basketball_match_live_data(match_id);