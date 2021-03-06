create user '<username>'@'%' identified by '<password>';
GRANT ALL PRIVILEGES ON *.* TO '<username>'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;
create database covid19tracker;
use covid19tracker;
create table authorities (authority varchar(255) not null, username varchar(255) not null, primary key (authority, username));
create table hospital (hospital_code varchar(255) not null, hospital_name varchar(255), primary key (hospital_code));
create table patient (application_id varchar(255) not null, age integer not null, country_code varchar(255), created_time datetime(6), email varchar(255), gender varchar(255), mobile_number varchar(10), name varchar(255), otp varchar(255), otp_expires_in datetime(6), status varchar(255), updated_time datetime(6), hospital_code varchar(255), primary key (application_id));
create table users (username varchar(255) not null, enabled integer not null, password varchar(255), hospital_code varchar(255), primary key (username));
alter table authorities add constraint AUTH_USERNAME_UK unique (username);
alter table authorities add constraint AUTH_USERNAME_FK foreign key (username) references users (username);
alter table patient add constraint PATIENT_HOSPITAL_FK foreign key (hospital_code) references hospital (hospital_code);
alter table users add constraint USERS_HOSPITAL_FK foreign key (hospital_code) references hospital (hospital_code);
insert into users(username,enabled,password) values('admin',1,'$2a$10$ZDhVWze6iT4.ppNd76AszOM5OfSnRT5hS/WZ0TXRU7/sTpQ7SRcFe');
insert into authorities(authority,username) values('admin','admin');
