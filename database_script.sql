----------------------------------------------------------------
-- To log in
-- as: SYS_ADMIN — admin:password
-- as PROVIDER_ADMIN from "Тернопільводоканал" — provider-te:pass
-- as PROVIDER_ADMIN from "Львівводоканал" — provider-lv: pass
-----------------------------------------------------------------



----------------------- REGION ---------------------

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(1,'Тернопільська');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(2,'Львівська');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(3,'Івано-Франківська');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(4,'Київ');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(5,'Харківська');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(6,'Одеська');

INSERT INTO `measurement_devices`.`REGION`
(`id`,`designation`)
VALUES(7,'Дніпропетровська');





-- ------------------------- DISTRICT -------------------------- --
INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(1,'Тернопільський',1);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(2,'Львівський',2);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(3,'Івано-франківський',3);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(4,'Збаразький',4);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(5,'Бучацький',5);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(6,'Зборівський',6);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(7,'Чортківський',7);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(8,'Золочівський',1);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(9,'Буський',1);

INSERT INTO `measurement_devices`.`DISTRICT`
(`id`,`designation`,`region_id`)
VALUES(10,'Пустомитівський',2);




-- ------------------------------ LOCALITY -------------------------- --
INSERT INTO `measurement_devices`.`LOCALITY`
(`id`,`designation`,`district_id`)
VALUES(1,'м. Тернопіль',1);

INSERT INTO `measurement_devices`.`LOCALITY`
(`id`,`designation`,`district_id`)
VALUES(2,'м. Львів',2);

INSERT INTO `measurement_devices`.`LOCALITY`
(`id`,`designation`,`district_id`)
VALUES(3,'м. Івано-Франківськ',3);





-- ---------------------------- STREET ----------------------------------- --
INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(1,'вул. Симоненка',1);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(2,'вул. Морозенка',2);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(3,'вул. Генерала Тарнавського',3);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(4,'просп. Степана Бандери',1);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(5,'бульвар Тараса Шевченка',2);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(6,'вул. Вишневецького',3);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(7,'вул. Володимира Великого',1);

INSERT INTO `measurement_devices`.`STREET`
(`id`,`designation`,`locality_id`)
VALUES(8,'вул. Київська',2);


-- ---------------------------------------- BUILDING -------------------------- --
INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(1,'1',1);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(2,'2',1);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(3,'3',2);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(4,'3а',2);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(5,'4',3);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(6,'1',3);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(7,'2',4);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(8,'3',4);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(9,'3а',5);

INSERT INTO `measurement_devices`.`BUILDING`
(`id`,`designation`,`street_id`)
VALUES(10,'4',5);

-- ------------------------------- ORGANIZATION ---------------------------- --
INSERT INTO `measurement_devices`.`ORGANIZATION`
(`organization_type`,`id`,`name`,`email`,`phone`,`certificateGrantedDate`,`certificateNumber`,`region`,`district`,`locality`,`street`,`building`,`flat`)

VALUES

('PROVIDER',43,'ЛКП «Львівводоканал»','lvivvodokanal@gmail.com', '0322771690', null, null, 'Львівська','Львівський','м. Львів','вул. Морозенка','12а',null),
('PROVIDER',32,'КП «Тернопільводоканал»','vodokanal@te.ua', '0352282719', null, null,'Тернопільська','Тернопільський','м. Тернопіль','бульвар Тараса Шевченка','43',null),
('CALIBRATOR',52,'ПП «Повірик з Тернополя»','verification@te.ua', '0352285419', null, null,'Тернопільська','Тернопільський','м. Тернопіль','вул. Тарнавського','13б',null),
('CALIBRATOR',11,'ПП «Повірник зі Львова»','verification@lv.ua', '0322771690', null, null, 'Львівська','Львівський','м. Львів','вул. Петлюри','11а',null);


-- ------------------------------- USERS ---------------------------- --
INSERT INTO `USER` (`user_type`,`username`,`password`,`role`,`email`,`firstName`,`lastName`,`phone`,`organization_id`) VALUES ('SYS_ADMIN','admin','$2a$10$xTq90ybFNT/W0TfNHdQ4e.0DL1WO/7vebrpDZybGRwdEk/7F8ULEi','SYS_ADMIN',NULL,NULL,NULL,NULL,NULL);
INSERT INTO `USER` (`user_type`,`username`,`password`,`role`,`email`,`firstName`,`lastName`,`phone`,`organization_id`) VALUES ('PROVIDER_EMPLOYEE','provider-lv','$2a$10$59Mv7tEUrVH8iBeDsm9y7.zUcJoPHnnyOvMnC4zKRV8.wlnugQ2G2','PROVIDER_ADMIN',NULL,NULL,NULL,NULL,'43');
INSERT INTO `USER` (`user_type`,`username`,`password`,`role`,`email`,`firstName`,`lastName`,`phone`,`organization_id`) VALUES ('PROVIDER_EMPLOYEE','provider-te','$2a$10$E5l.KYBTC.VVxxjZ5vBMFeiZck0i4H.P84FO7809hm85XNIIkJ2eC','PROVIDER_ADMIN',NULL,NULL,NULL,NULL,'32');



-- ------------------------------- MANUFACTURERS ---------------------------- --
INSERT INTO `MANUFACTURER` (`id`,`name`) VALUES (1,'ТОВ \"Суперлічильники\"');


-- ------------------------------- DEVICE ---------------------------- --
INSERT INTO `DEVICE` (`id`,`deviceSign`,`deviceType`,`number`,`manufacturer_id`,`provider_id`) VALUES (65463,'X','WATER','56964653673',1,32);

-- ------------------------------- VERIFICATIONS ---------------------------- --
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('6aa78022-c717-4934-ab37-eed574d1b48d','24','Львівський','215','м. Львів','Львівська','вул. Київська','scala@scala.scala','Міша','Скала','Іванович','0671802641',NULL,'2015-05-24','SENT',NULL,NULL,NULL,43,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('b113b9f7-aab7-4b41-b7f6-37e2471bb154','1','Тернопільський','23','м. Тернопіль','Тернопільська','вул. В. Великого','xgile@gmail.com','Петро','Торбінс','Васильович','086315643','2016-06-24','2015-05-28','COMPLETED',NULL,NULL,65463,32,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('b113b9f7-aab7-4b41-b7f6-37e2471bb170','2','Тернопільський','267','м. Тернопіль','Тернопільська','вул. Симоненка','gwini777@gmail.com','Дмитро','Добровольський','Богданович','0987061802',NULL,'2015-05-24','SENT',NULL,NULL,NULL,32,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('1ae32320-c4d7-405e-8ffb-6d3480583377','2','Тернопільський','543','м. Тернопіль','Тернопільська','вул. Симоненка','gwini777@gmail.com','Гендальф','Сірий','Гендальфович','0987061802',NULL,'2015-05-31','SENT',11,NULL,NULL,32,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('2649de16-8a81-4fca-92f8-c53ba9874e00','2','Тернопільський','134','м. Тернопіль','Тернопільська','вул. Володимира Великого','prot@gmail.com','Вікторія','Процик','Олегівна','0979551133',NULL,'2015-05-31','SENT',11,NULL,NULL,32,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('54720638-4dac-46c2-a95b-12130ce791af','24','Львівський','253','м. Львів','Львівська','вул. Київська','gwini777@gmail.com','Міша','Скала','Скалович','0987061802',NULL,'2015-05-31','SENT',NULL,NULL,NULL,43,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('6cf7dcf1-3199-4d1b-a7aa-12a02b4444b4','2','Львівський','43','м. Львів','Львівська','вул. Морозенка','gwini777@gmail.com','Людмила','Шишка','Йосипівна','0954431423',NULL,'2015-05-31','SENT',NULL,NULL,NULL,43,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('bcf8d225-9dd7-4563-a736-faca9acbb521','2','Львівський','32','м. Львів','Львівська','вул. Київська','gwini777@gmail.com','Фродо','Торбінс','Юліанович','0235434455',NULL,'2015-05-31','SENT',NULL,NULL,NULL,43,NULL,NULL,NULL);
INSERT INTO `VERIFICATION` (`id`,`building`,`district`,`flat`,`locality`,`region`,`street`,`email`,`firstName`,`lastName`,`middleName`,`phone`,`expirationDate`,`initialDate`,`status`,`calibrator_id`,`calibratorEmployee_username`,`device_id`,`provider_id`,`providerEmployee_username`,`stateVerificator_id`,`stateVerificatorEmployee_username`) VALUES ('e07edc57-2378-4970-af0e-3cda650b12f6','2','Тернопільський','245','м. Тернопіль','Тернопільська','вул. Симоненка','gwini777@gmail.com','Леголас','Леголас','Трандуїлович','0970343695',NULL,'2015-05-31','RECEIVED',11,NULL,NULL,32,NULL,NULL,NULL);


-- ------------------------------- СALIBRATION TESTS ---------------------------- --
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'6aa78022-c717-4934-ab37-eed574d1b48d');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (2,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'b113b9f7-aab7-4b41-b7f6-37e2471bb170');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (4,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'b113b9f7-aab7-4b41-b7f6-37e2471bb154');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1ae32320-c4d7-405e-8ffb-6d3480583377');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (7,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2649de16-8a81-4fca-92f8-c53ba9874e00');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (12,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'e07edc57-2378-4970-af0e-3cda650b12f6');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (24,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'bcf8d225-9dd7-4563-a736-faca9acbb521');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (42,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'6cf7dcf1-3199-4d1b-a7aa-12a02b4444b4');
INSERT INTO `CALIBRATION_TEST` (`id`,`consumptionStatus`,`dateTest`,`latitude`,`longitude`,`document_name`,`document_sign`,`name`,`photoPath`,`settingNumber`,`temperature`,`testResult`,`verification_id`) VALUES (43,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'54720638-4dac-46c2-a95b-12130ce791af');

