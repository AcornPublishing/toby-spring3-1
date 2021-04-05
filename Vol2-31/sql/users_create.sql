create table users (
	id varchar(10) primary key,	
	name varchar(20) not null,
	password varchar(10) not null,
	email varchar(50) null,
	level tinyint null,
	login int null,
	recommend int null
)