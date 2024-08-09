create table if not exists Words 
(
	ID_Words Serial not null constraint PK_Word primary key,
	Words Varchar(100) not null,
	Translation_ Varchar(100) not null
);

drop table words;