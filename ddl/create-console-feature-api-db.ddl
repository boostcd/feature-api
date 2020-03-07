create sequence REPO_COMMIT_ID_SEQ start 1 increment 1;
create table ENV (ENV_ID varchar(255) not null, LIVE boolean not null, UPDATED_DATE varchar(255) not null, primary key (ENV_ID));
create table ENV_FEATURE (DEPLOYED_DATE varchar(255) not null, MIGRATED_DATE varchar(255), ENV_ID varchar(255) not null, FEATURE_ID varchar(255) not null, primary key (ENV_ID, FEATURE_ID));
create table ENV_MICROSERVICE (MICROSERVICE varchar(255) not null, DEPLOYED_DATE varchar(255) not null, VERSION varchar(255) not null, ENV_ID varchar(255) not null, REPO_ID varchar(255) not null, primary key (ENV_ID, MICROSERVICE));
create table FEATURE (FEATURE_ID varchar(255) not null, DESCRIPTION varchar(255), STATUS varchar(255), TITLE varchar(255), primary key (FEATURE_ID));
create table REPO (REPO_ID varchar(255) not null, MICROSERVICE varchar(255) not null, primary key (REPO_ID));
create table REPO_COMMIT (MATCHED_TYPE varchar(31) not null, REPO_COMMIT_ID int8 not null, SHA varchar(255) not null, VERSION varchar(255) not null, REPO_ID varchar(255) not null, FEATURE_ID varchar(255), primary key (REPO_COMMIT_ID));
alter table REPO add constraint MICROSERVICE_KEY unique (MICROSERVICE);
alter table ENV_FEATURE add constraint ENV_FEATURE_TO_ENV_FK foreign key (ENV_ID) references ENV;
alter table ENV_FEATURE add constraint ENV_FEATURE_TO_FEATURE_FK foreign key (FEATURE_ID) references FEATURE;
alter table ENV_MICROSERVICE add constraint MICROSERVICE_TO_ENV_FK foreign key (ENV_ID) references ENV;
alter table ENV_MICROSERVICE add constraint MICROSERVICE_TO_REPO_FK foreign key (REPO_ID) references REPO;
alter table REPO_COMMIT add constraint COMMIT_TO_REPO_FK foreign key (REPO_ID) references REPO;
alter table REPO_COMMIT add constraint COMMIT_TO_FEATURE_FK foreign key (FEATURE_ID) references FEATURE;
