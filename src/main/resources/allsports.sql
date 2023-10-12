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





