insert into `USER_ROLE` (`ROLE_ID`, `ROLE`) values (21, 'ADMIN');
insert into `USER_ROLE` (`ROLE_ID`, `ROLE`) values (22, 'USER');


insert into `USER` (`ID`, `PASSWORD`, `ROLE_ID`) values ('agraph', 'agraph', 21);
insert into `USER` (`ID`, `PASSWORD`, `ROLE_ID`) values ('test01', 'test01', 22);