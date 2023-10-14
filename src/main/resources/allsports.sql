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
  `id` bigint NOT NULL   COMMENT 'id',
  `match_id` bigint not null COMMENT 'match id',
  `team_id` bigint COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  NOT null COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` int  COMMENT '球员位置，',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_away_match_line_up`;
CREATE TABLE `all_sports_away_match_line_up` (
  `id` bigint NOT NULL   COMMENT 'id',
  `match_id` bigint not null COMMENT 'match id',
  `team_id` bigint COMMENT 'team id',
  `first` int COMMENT 'first 0 no 1 yes ',
  `captain` int COMMENT 'captain 0 no 1 yes',
  `player_name` varchar(255) NOT NULL COMMENT 'player name',
  `player_logo` varchar(255)  NOT null COMMENT 'player logo',
  `shirt_number` int COMMENT 'shirt number',
  `position` int  COMMENT '球员位置',
  `rating` varchar(8) COMMENT 'rating',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `all_sports_football_match`;
CREATE TABLE `all_sports_football_match` (
  `id` bigint NOT NULL  COMMENT 'id',
  `competition_id` bigint NOT NULL COMMENT 'competition_id',
  `status` int  COMMENT 'finished,',
  `match_time` varchar(8) COMMENT 'match time',
  `match_date` varchar(32) COMMENT 'match date',
  `home_team_id` bigint  COMMENT ' home team id',
  `away_team_id` bigint  COMMENT ' away team id',
  `home_team_name` varchar(255) COMMENT 'home team name',
  `away_team_name` varchar(255) COMMENT 'away home name',
  `home_team_logo` varchar(255) COMMENT 'home team logo',
  `away_team_logo` varchar(255) COMMENT 'away home logo',
  `home_team_score` int  COMMENT ' home team score',
  `away_team_score` int  COMMENT ' away team score',
  `home_formation` varchar(255) COMMENT 'home formation',
  `away_formation` varchar(255) COMMENT 'away formation',
  `referee` varchar(64) COMMENT 'referee name',
  `venue` varchar(255) COMMENT 'venue name',
  `event_live` char(2) COMMENT 'event live 0 not yet playing 1 is playing',
  `line_up` int COMMENT 'if there is a line-up, 0 no 1 yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;







