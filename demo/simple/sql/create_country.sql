CREATE DATABASE mybatis DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
SHOW CHARACTER SET LIKE `latin%`;

use mybatis;
CREATE TABLE `mybatis`.`country`(
	`id` int NOT NULL AUTO_INCREMENT,
	`countryname` varchar(255) NULL,
	`countrycode` varchar(255) NULL,
	PRIMARY KEY (`id`)
);

	insert  into `country`(`countryname`,`countrycode`) values('中国','CN'),('美国','US'),('俄罗斯','RU'),('英国','GB'),('法国','FR');