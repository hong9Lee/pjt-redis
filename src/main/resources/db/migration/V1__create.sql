CREATE TABLE campaign
(
    seq             bigint auto_increment comment '캠페인 시퀀스' primary key,
    campaign_id     VARCHAR(64)  NOT NULL COMMENT '캠페인 ID',
    name            VARCHAR(255) NOT NULL COMMENT '캠페인 이름',
    max_count       INT          NOT NULL COMMENT '최대 참여 횟수',
    start_date_time DATETIME(6)   NOT NULL COMMENT '캠페인 시작 일시',
    end_date_time   DATETIME(6)   NOT NULL COMMENT '캠페인 종료 일시',
    reg_date_time   DATETIME(6)   NOT NULL COMMENT '등록일시',
    mod_date_time   DATETIME(6)   NULL COMMENT '수정일시'
) COMMENT '캠페인 테이블';

CREATE TABLE campaign_condition
(
    seq                   bigint auto_increment comment '캠페인 참여 조건 시퀀스' primary key,
    campaign_condition_id VARCHAR(64) NOT NULL COMMENT '캠페인 조건 ID',
    campaign_id           VARCHAR(64) NOT NULL COMMENT '캠페인 ID',
    first_time_only       BOOLEAN     NOT NULL COMMENT '첫 참여만 허용 여부',
    min_participation     INT         NOT NULL COMMENT '최소 참여 횟수',
    required_previous_ad  BOOLEAN     NOT NULL COMMENT '이전 광고 참여 필수 여부',
    required_previous_ads TEXT NULL COMMENT '필수 이전 광고 ID 리스트',
    reg_date_time         DATETIME(6)   NOT NULL COMMENT '등록일시',
    mod_date_time         DATETIME(6)   NULL COMMENT '수정일시'
) COMMENT '캠페인 조건 테이블';

CREATE TABLE campaign_participation
(
    seq                       bigint auto_increment comment '시퀀스' primary key,
    campaign_participation_id VARCHAR(64) NOT NULL COMMENT '광고 참여 ID',
    campaign_id               VARCHAR(64) NOT NULL COMMENT '광고 캠페인 ID',
    max_participation         INT         NOT NULL COMMENT '최대 참여 가능 횟수',
    current_participation     INT         NOT NULL COMMENT '현재 참여 횟수',
    reg_date_time             datetime(6)    null comment '등록일자',
    mod_date_time             datetime(6)    null comment '수정일자'
);

CREATE TABLE campaign_participation_user
(
    seq                            bigint auto_increment comment '시퀀스' primary key,
    campaign_participation_user_id VARCHAR(64) NOT NULL COMMENT '유저별 광고 참여 ID',
    campaign_id                    VARCHAR(64) NOT NULL COMMENT '광고 캠페인 ID',
    user_id                        VARCHAR(64) NOT NULL COMMENT '유저 ID',
    participation_count            INT         NOT NULL DEFAULT 0 COMMENT '총 참여 횟수',
    last_participation_date_time   DATETIME(6)   NULL COMMENT '마지막 참여 일시',
    reg_date_time                  datetime(6)    null comment '등록일자',
    mod_date_time                  datetime(6)    null comment '수정일자'
);
