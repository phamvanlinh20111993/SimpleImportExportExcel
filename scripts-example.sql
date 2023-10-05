create database fake;

use fake;

create table if not exists employee
(
	userId varchar(100) NOT NULL, 
	name varchar (200),
    department varchar(255),
    ageName TINYINT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


select * from employee;

truncate table employee;




use fake;

 Select * FROM  kiot_viet_price;

 DROP TABLE  kiot_viet_price;
create table if not exists kiot_viet_price
(
	code varchar(100) NOT NULL, 
	name varchar (200),
    unit varchar(10) default "",
    good_group varchar(50),
    invent_num double,
    cost_price decimal(14, 4),
	last_cost_price decimal(14, 4),
    market_cost_price decimal(14, 4)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

select * from kiot_viet_price;

select count(*) from kiot_viet_price;
truncate kiot_viet_price;