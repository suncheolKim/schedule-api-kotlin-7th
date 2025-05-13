CREATE TABLE `user` (
    `id`         BIGINT    PRIMARY KEY NOT NULL AUTO_INCREMENT	COMMENT '사용자 식별자',
    `name`       VARCHAR(50)  	NOT NULL	COMMENT '사용자명',
    `email`      VARCHAR(100)  	NOT NULL	COMMENT '이메일',
    `created_at` DATETIME       NOT NULL  COMMENT '생성일시',
    `updated_at` DATETIME       NOT NULL  COMMENT '수정일시'
);

CREATE TABLE `schedule` (
    `id`         BIGINT    PRIMARY KEY NOT NULL AUTO_INCREMENT	COMMENT '일정 식별자',
    `user_id`    BIGINT 	    NOT NULL	COMMENT '사용자 ID',
    `contents`   VARCHAR(1000)	NOT NULL	COMMENT '할 일',
    `password`   VARCHAR(20)    NOT NULL	COMMENT '비밀번호',
    `created_at` DATETIME       NOT NULL  COMMENT '생성일시',
    `updated_at` DATETIME       NOT NULL  COMMENT '수정일시',

    CONSTRAINT `fk_schedule_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);

INSERT INTO `user` (`name`, `email`, `created_at`, `updated_at`) VALUES ( '김순순', 'sckim@sparta.co.kr', NOW(), NOW());

INSERT INTO `schedule` (`user_id`, `contents`, `password`, `created_at`, `updated_at`) VALUES ( 1, '팟타이 먹기', '1234', NOW(), NOW());
INSERT INTO `schedule` (`user_id`, `contents`, `password`, `created_at`, `updated_at`) VALUES ( 1, '일어나지 말기', '1234', NOW(), NOW());
INSERT INTO `schedule` (`user_id`, `contents`, `password`, `created_at`, `updated_at`) VALUES ( 1, '맥주 외면하지 말기', '1234', NOW(), NOW());
