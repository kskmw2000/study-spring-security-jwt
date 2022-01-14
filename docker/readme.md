docker container run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1234 --name inflearn-security mariadb

```sql
create user 'cos'@'%' identified by 'cos1234';
GRANT ALL PRIVILEGES ON *.* TO 'cos'@'%';
create database security;
use security;
```