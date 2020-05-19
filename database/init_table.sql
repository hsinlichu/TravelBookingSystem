DROP TABLE IF EXISTS `Trip`;
DROP TABLE IF EXISTS `TravelCode`;
DROP TABLE IF EXISTS `Account`;
DROP TABLE IF EXISTS `Order`;

CREATE TABLE `Trip`(
	trip_id VARCHAR(100) UNIQUE PRIMARY KEY NOT NULL,
	title VARCHAR(100) NOT NULL,
	travel_code Int NOT NULL,
	product_key VARCHAR(100) NOT NULL,
	price Int NOT NULL,
	start_date Datetime NOT NULL,
	end_date Datetime NOT NULL,
	lower_bound Int NOT NULL,
	upper_bound Int NOT NULL
);


CREATE TABLE `TravelCode`(
	travel_code INTEGER UNIQUE PRIMARY KEY NOT NULL,
	travel_code_name VARCHAR(100) NOT NULL
);

CREATE TABLE `Account`(
	account_id VARCHAR(100) UNIQUE PRIMARY KEY NOT NULL,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(100) NOT NULL
);

CREATE TABLE `Order`(
	order_id VARCHAR(100) UNIQUE PRIMARY KEY NOT NULL,
	account_id VARCHAR(100)  NOT NULL,
	trip_id VARCHAR(100) NOT NULL,
	quantity Int Not NULL
);