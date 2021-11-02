create table ACCOUNT
(
    ID               integer          not null,
    CUSTOMER_ID      integer          not null,
    UNIQUE_ID        varchar(10 char) not null,
    NAME             varchar(50 char) not null,
    IBAN             varchar(35 char) not null,
    BIC              varchar(11 char) not null,
    AMOUNT           decimal(10, 2)   not null,
    INSERT_TIMESTAMP timestamp,
    UPDATE_TIMESTAMP timestamp
);

alter table ACCOUNT
    add constraint ACCOUNT_pk
        primary key (ID);

alter table ACCOUNT
    add constraint ACCOUNT_fk
        foreign key (CUSTOMER_ID) references CUSTOMER (ID)
            on delete cascade;

alter table ACCOUNT
    add constraint ACCOUNT_uk
        unique (CUSTOMER_ID, UNIQUE_ID);


create sequence ACCOUNT_ID_SEQ nocache;
create sequence ACCOUNT_UNIQUE_ID_SEQ nocache;
