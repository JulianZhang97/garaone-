DROP SCHEMA IF EXISTS carPerf CASCADE;
CREATE SCHEMA carPerf;

SET SEARCH_PATH to carPerf;

CREATE TABLE cars(
	id int primary key,
	make text,
	model text,
	model_year int,
	drive text,
	transmission text,
	accel float,
	quarter_mile_time float,
	quarter_mile_speed float
);


\COPY cars FROM 'performanceinfo.csv' DELIMITER ',' CSV header;