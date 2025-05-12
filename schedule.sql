CREATE TABLE `schedule` (
    `id`         BIGINT    PRIMARY KEY NOT NULL AUTO_INCREMENT	COMMENT '일정 식별자',
    `contents`   VARCHAR(1000)	NOT NULL	COMMENT '할 일',
    `name`       VARCHAR(50)  	NOT NULL	COMMENT '작성자명',
    `password`   VARCHAR(20)    NOT NULL	COMMENT '비밀번호',
    `created_at` DATETIME       NOT NULL  COMMENT '생성일시',
    `updated_at` DATETIME       NOT NULL  COMMENT '수정일시'
);