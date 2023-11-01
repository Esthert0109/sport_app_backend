DROP TABLE IF EXISTS `all_sports_football_competition`;
CREATE TABLE `all_sports_football_competition` (
  `id` bigint  COMMENT 'id',
  `competition_name` varchar(255) NOT NULL COMMENT 'competition name',
  `logo` varchar(255) COMMENT 'competition logo',
  `country` varchar(96)  COMMENT 'country',
  `country_logo` varchar(255) COMMENT 'country logo',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_football_team`;
CREATE TABLE `all_sports_football_team` (
  `id` bigint  COMMENT 'id',
  `competition_id` bigint  COMMENT 'competitionId',
  `team_name` varchar(255) NOT NULL COMMENT 'team name',
  `team_logo` varchar(255) COMMENT 'team logo',
  `coach_name` varchar(96)  COMMENT 'coach name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_home_match_line_up`;
CREATE TABLE `all_sports_home_match_line_up` (
  `id` int(11)  auto_increment  COMMENT 'id',
  `player_id` bigint COMMENT 'player id',
  `match_id` bigint not null COMMENT 'match id',
  `team_id` bigint COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` int  COMMENT '球员位置，',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_away_match_line_up`;
CREATE TABLE `all_sports_away_match_line_up` (
  `id` int(11) auto_increment COMMENT 'id',
  `player_id` bigint COMMENT 'player id',
  `match_id` bigint not null COMMENT 'match id',
  `team_id` bigint COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` int  COMMENT '球员位置',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_football_match`;
CREATE TABLE `all_sports_football_match` (
  `id` bigint NOT NULL  COMMENT 'id',
  `competition_id` bigint NOT NULL COMMENT 'competition_id',
  `home_team_id` bigint  COMMENT ' home team id',
  `away_team_id` bigint  COMMENT ' away team id',
  `competition_name` varchar(128) COMMENT 'competition name',
  `status` varchar(32)  COMMENT 'finished,',
  `match_time` varchar(8) COMMENT 'match time',
  `match_date` varchar(32) COMMENT 'match date',
  `home_team_name` varchar(255) COMMENT 'home team name',
  `away_team_name` varchar(255) COMMENT 'away home name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away home logo',
  `home_team_score` int  COMMENT ' home team score',
  `away_team_score` int  COMMENT ' away team score',
  `home_formation` varchar(255) COMMENT 'home formation',
  `away_formation` varchar(255) COMMENT 'away formation',
  `referee_name` varchar(64) COMMENT 'referee name',
  `venue_name` varchar(255) COMMENT 'venue name',
  `event_live` char(2) COMMENT 'event live 0 not yet playing 1 is playing',
  `line_up` int COMMENT 'if there is a line-up, 0 no 1 yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `all_sports_football_match_live_data`;
CREATE TABLE `all_sports_football_match_live_data` (
  `id` int  NOT NULL auto_increment    COMMENT 'id',
  `match_id` bigint not null COMMENT 'match id',
  `status` bigint COMMENT 'football status',
  `match_time` varchar(8) COMMENT 'match time',
  `match_date` varchar(32) COMMENT 'match_date',
  `home_team_name` varchar(255) NOT NULL COMMENT 'home team name',
  `away_team_name` varchar(255) NOT NULL COMMENT 'home team name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away team logo',
  `referee_name` varchar(255)  COMMENT 'referee name',
  `venue_name` varchar(255)  COMMENT 'stadium name',
  `home_formation` varchar(255)  COMMENT 'home formation',
  `away_formation` varchar(255)  COMMENT 'away formation',
  `home_coach` varchar(255) COMMENT 'home coach',
  `away_coach` varchar(255) COMMENT  'away coach',
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
  `home_score` int COMMENT 'homeScore',
  `away_score` int COMMENT 'awayScore',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index all_sports_football_match_live_data_index_matchId on all_sports_football_match_live_data(match_id);
create index all_sports_home_match_line_up_index_matchId on all_sports_home_match_line_up(match_id);
create index all_sports_away_match_line_up_index_matchId on all_sports_away_match_line_up(match_id);
create index all_sports_football_match_matchDate on all_sports_football_match(match_date);
alter table all_sports_football_match_live_data modify home_possession_rate varchar(16) COMMENT 'homePossessionRate';

alter table all_sports_football_match_live_data modify away_possession_rate varchar(16) COMMENT 'homePossessionRate';

alter table all_sports_football_match_live_data add column `home_coach` varchar(255) COMMENT 'home coach';
alter table all_sports_football_match_live_data add column `away_coach` varchar(255) COMMENT 'away coach';
alter table all_sports_home_match_line_up add column `player_id` bigint COMMENT 'player id';
alter table all_sports_away_match_line_up add column `player_id` bigint COMMENT 'player id';




