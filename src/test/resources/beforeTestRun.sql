INSERT INTO `user` VALUES (1,'company@mail.com','$2a$10$iYrrY3gmuGs.feuhHqb42eepl6n7uWjqGtjAHWN8hbZ5asYo4QI.u','ROLE_COMPANY','company'),(2,'companywithsites@mail.com','$2a$10$Xrm3YpSaQIUJ/cOM0gCGMeiBnEAiib/8eNKtUIuhbQNGrvn9KIEBi','ROLE_COMPANY','companywithsites'),(3,'companywithcars@mail.com','$2a$10$E6Ic4WKPC0L0yaLRQhJcqOHCRQbEiq5bfKI1vdoC.g72onpEoG8NS','ROLE_COMPANY','companywithcars'),(4,'customer@mail.com','$2a$10$FYt0ThkinkfnA30U6TYyxO6r5V4.ET.2DGNv.kYLU5Te8I5k6GcyS','ROLE_CUSTOMER','customer'),(5,'customeractivereservation@mail.com','$2a$10$WgrDMb31.kgj0KKtsyCjHOXyU7gMFuuZfnpQF.CLdFSp7NyEl.oUe','ROLE_CUSTOMER','customeractivereservation'),(6,'customerclosedreservation@mail.com','$2a$10$eGPDF6nZgh6Myfhg6IMZlOTqghzntqe64nMLkoO4.7QxrS4uQNtc2','ROLE_CUSTOMER','customerclosedreservation'),(7,'customerreservation@mail.com','$2a$10$6VWfih9s7SnwLEzclLlICuotDO24FDF9UZHNQApJbqWrJ2RJntNhK','ROLE_CUSTOMER','customerreservation'),(8,'manager@mail.com','$2a$10$8EI4njE8nPQu4LVM2pZ/auBMNAKjlppQgNAuOffz7PLtFYI9kWpBi','ROLE_MANAGER','manager'),(9,'managerwithsite@mail.com','$2a$10$IJe1DvX/P1RFaNNFXBe5pOdzgZ6LEYTddj70jEakJncSLK5XcVUv2','ROLE_MANAGER','managerwithsite');
INSERT INTO `company` VALUES (1,'Address','City','company@mail.com','Company Owner','Company','11111111111',1111,1),(2,'Address','City','companywithsites@mail.com','Company With Sites Owner','Company With Sites','22222222222',2222,2),(3,'Address','City','companywithcars@mail.com','Company With Cars Owner','Company With Cars','22222222222',3333,3);
INSERT INTO `manager` VALUES (1,'Address','City','Manager User','11111111111',1111,8),(2,'Address','City','Manager With Site','22222222222',2222,9);
INSERT INTO `customer` VALUES (1,'Address','City','Customer User','11111111111',1111,4),(2,'Address','City','Customer Active Reservation','22222222222',2222,5),(3,'Address','City','Customer Closed Reservation','33333333333',3333,6),(4,'Address','City','Customer Reservation','44444444444',4444,7);
INSERT INTO `site` VALUES (1,'Address1','City1','site1@mail.com','OpeningHours1','11111111111',1111,2,NULL),(2,'Address2','City2','site2@mail.com','OpeningHours2','22222222222',2222,2,NULL),(3,'Address3','City3','site3@mail.com','OpeningHours3','33333333333',3333,3,2),(4,'Address4','City4','site4@mail.com','OpeningHours4','44444444444',4444,3,NULL);
INSERT INTO `car` VALUES (1,'Brand1','SUV',1.1,'Petrol','Model1',11111,5,11,11,2011,3),(2,'Brand2','Luxury',2.2,'Diesel','Model2',22222,5,22,22,2012,3),(3,'Brand3','Minivan',3.3,'Petrol','Model3',33333,5,33,33,2013,4),(4,'Brand4','Compact',4.4,'Electric','Model4',44444,5,44,44,2014,4);
INSERT INTO `reservation` VALUES (1,now(),22222,now(),NULL,2,3,2),(2,now(),33333,now(),now(),3,3,3),(3,now() - interval 1 day,44444,now() - interval 1 day,now() - interval 1 day,4,3,4),(4,now() + interval 1 day,44444,now() + interval 1 day,NULL,4,3,4);