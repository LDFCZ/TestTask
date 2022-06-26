LOCK TABLES `roles` WRITE;

INSERT INTO `roles` (`ID`, `NAME`) VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');

UNLOCK TABLES;


UPDATE `user_roles` SET `role_id` = '1' WHERE `user_roles`.`user_id` = 1 AND `user_roles`.`role_id` = 2