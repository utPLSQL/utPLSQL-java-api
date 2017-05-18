#!/bin/bash
set -ev

sqlplus -S -L / AS SYSDBA <<EOF
create user $DB_USER identified by $DB_PASS
quota unlimited on USERS
default tablespace USERS;
grant create session,
      create procedure,
	  create type,
	  create table,
	  create sequence,
	  create view
to $DB_USER;
grant select any dictionary to $DB_USER;
exit;
EOF
