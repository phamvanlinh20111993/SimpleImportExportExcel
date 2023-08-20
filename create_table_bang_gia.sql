

use `example`;

create table table_price if not exists (
	good_code varchar(10) NOT NULL PRIMARY KEY,
	good_name varchar(100) NOT NULL,
	unit varchar(20),
	good_group varchar(50),
	inventory_number DOUBLE default 0.0,
	cost_price DECIMAL(15, 9) default 0.0,
	last_cost_price DECIMAL(15, 9) default 0.0,
	market_cost_price DECIMAL(15, 9) default 0.0

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;