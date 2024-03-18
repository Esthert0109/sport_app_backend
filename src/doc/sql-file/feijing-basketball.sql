DROP TABLE IF EXISTS `fei_jing_basketball_all_match`;
CREATE TABLE `fei_jing_basketball_all_match`
(
    `match_id`       bigint NOT NULL COMMENT '赛程ID',
    `competition_id` bigint NOT NULL COMMENT '联赛ID',
    `league_en`      varchar(255) COMMENT '联赛名-英文',
    `league_chs`     varchar(255) COMMENT '联赛名-简体',
    `match_time`     varchar(255) COMMENT '比赛时间 GMT+8 格式:yyyy/MM/dd',
    `match_state`    int        NOT NULL COMMENT '比赛状态 0：未开赛、1：一节、2：二节、3：三节、4：四节、5：1\'OT、6：2\'OT、7：3\'OT、50：中场、-1：完场、-2：待定、-3：中断、-4：取消、-5：推迟',
    `home_team_id`   bigint NOT NULL COMMENT '主队ID',
    `home_team_en`   varchar(255) COMMENT '主队名-英文',
    `home_team_chs`  varchar(255) COMMENT '主队名-简体',
    `away_team_id`   bigint NOT NULL COMMENT '客队ID',
    `away_team_en`   varchar(255) COMMENT '客队名-英文',
    `away_team_chs`  varchar(255) COMMENT '客队名-简体',
    `home_score`     int        NOT NULL COMMENT '主队总分',
    `away_score`     int        NOT NULL COMMENT '客队总分',
    `season`         varchar(255) COMMENT '赛季',
    `kind`           varchar(255) COMMENT '比赛阶段 1：常规、2：季后、3：季前、-1：无分类',
    `updated_date`   varchar(255) COMMENT '更新时间',
    PRIMARY KEY (`match_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
