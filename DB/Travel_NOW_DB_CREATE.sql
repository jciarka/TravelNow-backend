DROP TABLE comments CASCADE CONSTRAINTS;
DROP TABLE reservations CASCADE CONSTRAINTS;
DROP TABLE rooms CASCADE CONSTRAINTS;
DROP TABLE hotels CASCADE CONSTRAINTS;
DROP TABLE photos CASCADE CONSTRAINTS;
DROP TABLE addresses CASCADE CONSTRAINTS;
DROP TABLE regions CASCADE CONSTRAINTS;
DROP TABLE countries CASCADE CONSTRAINTS;
DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE reservations_history CASCADE CONSTRAINTS;
DROP TABLE users_stats CASCADE CONSTRAINTS;
DROP TABLE hotels_stats  CASCADE CONSTRAINTS;

DROP SEQUENCE users_seq;
DROP SEQUENCE hotels_seq;
DROP SEQUENCE rooms_seq;
DROP SEQUENCE addresses_seq;
DROP SEQUENCE regions_seq;
DROP SEQUENCE countries_seq;
DROP SEQUENCE photos_seq;
DROP SEQUENCE comments_seq;
DROP SEQUENCE reservations_seq;
DROP SEQUENCE reservations_history_seq;
DROP SEQUENCE users_stats_seq;
DROP SEQUENCE hotels_stats_seq;

DROP PROCEDURE MOVE_OLD_RESERVATIONS;
DROP PROCEDURE create_hotels_stats_row_if_not_exists;
DROP PROCEDURE create_users_stats_row_if_not_exists;
DROP FUNCTION get_hotels_stats_id;
DROP FUNCTION get_users_stats_id;
DROP FUNCTION get_reservation_days;
DROP FUNCTION get_reservation_days_direct;
DROP TRIGGER update_stats_on_reservation_insert;
DROP TRIGGER update_stats_on_reservation_remove;
DROP TRIGGER update_stats_on_comment_insert;


ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD';
   
CREATE TABLE addresses (
    id          NUMBER(10) NOT NULL,
    street      VARCHAR2(50 CHAR) NOT NULL,
    "number"    NUMBER(5) NOT NULL,
    city        VARCHAR2(50 CHAR) NOT NULL,
    postalcode  VARCHAR2(10 CHAR) NOT NULL,
    regions_id  NUMBER(10) NOT NULL
);

ALTER TABLE addresses ADD CONSTRAINT addresses_pk PRIMARY KEY ( id );

CREATE TABLE comments (
    id         NUMBER(12) NOT NULL,
    text       VARCHAR2(4000 CHAR),
    rating     NUMBER(1) NOT NULL,
    postdate   DATE  DEFAULT SYSDATE NOT NULL,
    users_id   NUMBER(10) NOT NULL,
    hotels_id  NUMBER(10) NOT NULL
);

ALTER TABLE comments ADD CHECK ( rating BETWEEN 1 AND 5 );

ALTER TABLE comments ADD CONSTRAINT comments_pk PRIMARY KEY ( id );

CREATE TABLE countries (
    id            NUMBER(10) NOT NULL,
    name          VARCHAR2(50 CHAR) NOT NULL,
    dialingcode   NUMBER(4),
    abbreviation  VARCHAR2(5 CHAR)
);

ALTER TABLE countries ADD CONSTRAINT country_pk PRIMARY KEY ( id );

CREATE TABLE hotels (
    id            NUMBER(10) NOT NULL,
    name          VARCHAR2(255 CHAR) NOT NULL,
    tel           NUMBER(9) NOT NULL,
    description   VARCHAR2(4000 CHAR),
    addresses_id  NUMBER(10) NOT NULL,
    users_id      NUMBER(10) NOT NULL,
    main_photo_id NUMBER(10)
);

CREATE UNIQUE INDEX hotels__idx ON
    hotels (
        addresses_id
    ASC );

ALTER TABLE hotels ADD CONSTRAINT hotels_pk PRIMARY KEY ( id );

CREATE TABLE photos (
    id         NUMBER(10) NOT NULL,
    hotels_id  NUMBER(10) NOT NULL
);

ALTER TABLE photos ADD CONSTRAINT photos_pk PRIMARY KEY ( id );

CREATE TABLE regions (
    id            NUMBER(10) NOT NULL,
    name          VARCHAR2(50 CHAR) NOT NULL,
    countries_id  NUMBER(10) NOT NULL
);

ALTER TABLE regions ADD CONSTRAINT region_pk PRIMARY KEY ( id );

CREATE TABLE reservations (
    id          NUMBER(10) NOT NULL,
    startdate   DATE NOT NULL,
    finishdate  DATE NOT NULL,
    users_id    NUMBER(10) NOT NULL,
    rooms_id    NUMBER(10) NOT NULL
);

ALTER TABLE reservations ADD CONSTRAINT reservations_pk PRIMARY KEY ( id );

CREATE TABLE rooms (
    id         NUMBER(10) NOT NULL,
    "number"   NUMBER(10) NOT NULL,
    capacity   NUMBER(3) NOT NULL,
    price      NUMBER NOT NULL,
    hotels_id  NUMBER(10) NOT NULL
);

ALTER TABLE rooms ADD CONSTRAINT rooms_pk PRIMARY KEY ( id );

CREATE TABLE users (
    id        NUMBER(10) NOT NULL,
    username  NVARCHAR2(50) NOT NULL,
    password  VARCHAR2(60 CHAR) NOT NULL,
    active    CHAR(1 CHAR) NOT NULL,
    roles     VARCHAR2(50 CHAR) NOT NULL,
    email     VARCHAR2(255 CHAR) NOT NULL
);

CREATE TABLE reservations_history (
    id          NUMBER(10) NOT NULL,
    startdate   DATE NOT NULL,
    finishdate  DATE NOT NULL,
    users_id    NUMBER(10) NOT NULL,
    rooms_id    NUMBER(10) NOT NULL    
);

ALTER TABLE reservations_history ADD CONSTRAINT reservations_history_pk PRIMARY KEY ( id );

CREATE TABLE users_stats (
    id               NUMBER(10) NOT NULL,
    users_id         NUMBER(10) NOT NULL,
    month            NUMBER     DEFAULT EXTRACT(YEAR FROM SYSDATE) NOT NULL,
    year             NUMBER     DEFAULT EXTRACT(MONTH FROM SYSDATE) NOT NULL,
    reservations_num NUMBER(10) DEFAULT 0 NOT NULL,
    cancelations_num NUMBER(10) DEFAULT 0 NOT NULL,
    comments_num     NUMBER(10) DEFAULT 0 NOT NULL
);

ALTER TABLE users_stats ADD CONSTRAINT user_statistics_pk PRIMARY KEY ( id ); 


            
CREATE TABLE hotels_stats (
    id               NUMBER(10) NOT NULL,
    hotels_id        NUMBER(10) NOT NULL,
    month            NUMBER     DEFAULT EXTRACT(YEAR FROM SYSDATE) NOT NULL,
    year             NUMBER     DEFAULT EXTRACT(MONTH FROM SYSDATE) NOT NULL,
    reservations_num NUMBER(10) DEFAULT 0 NOT NULL,
    cancelations_num NUMBER(10) DEFAULT 0 NOT NULL,
    comments_num     NUMBER(10) DEFAULT 0 NOT NULL,
    occupancy_days   NUMBER(10) DEFAULT 0 NOT NULL,
    income           NUMBER(10) DEFAULT 0 NOT NULL
);

ALTER TABLE hotels_stats ADD CONSTRAINT hotels_stats_pk PRIMARY KEY ( id );



ALTER TABLE users
    ADD CONSTRAINT value_yn CHECK ( active IN ( 'Y', 'N' ) );

ALTER TABLE users
    ADD CONSTRAINT email_const CHECK ( REGEXP_LIKE ( email,
                                                     '^[A-Za-z]+[A-Za-z0-9.]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$' ) );

ALTER TABLE users ADD CONSTRAINT users_pk PRIMARY KEY ( id );

ALTER TABLE addresses
    ADD CONSTRAINT addresses_regions_fk FOREIGN KEY ( regions_id )
        REFERENCES regions ( id );

ALTER TABLE comments
    ADD CONSTRAINT comments_hotels_fk FOREIGN KEY ( hotels_id )
        REFERENCES hotels ( id )
            ON DELETE CASCADE;

ALTER TABLE comments
    ADD CONSTRAINT comments_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id )
            ON DELETE CASCADE;

ALTER TABLE hotels
    ADD CONSTRAINT hotels_addresses_fk FOREIGN KEY ( addresses_id )
        REFERENCES addresses ( id )
            ON DELETE CASCADE;

ALTER TABLE hotels
    ADD CONSTRAINT hotels_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id );

ALTER TABLE hotels
    ADD CONSTRAINT hotels_main_photo_fk FOREIGN KEY ( main_photo_id ) 
        REFERENCES photos ( id ) ON DELETE SET NULL;

ALTER TABLE photos
    ADD CONSTRAINT photos_hotels_fk FOREIGN KEY ( hotels_id )
        REFERENCES hotels ( id )
            ON DELETE CASCADE;

ALTER TABLE regions
    ADD CONSTRAINT regions_countries_fk FOREIGN KEY ( countries_id )
        REFERENCES countries ( id );

ALTER TABLE reservations
    ADD CONSTRAINT reservations_rooms_fk FOREIGN KEY ( rooms_id )
        REFERENCES rooms ( id )
            ON DELETE CASCADE;

ALTER TABLE reservations
    ADD CONSTRAINT reservations_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id )
            ON DELETE CASCADE;

ALTER TABLE rooms
    ADD CONSTRAINT rooms_hotels_fk FOREIGN KEY ( hotels_id )
        REFERENCES hotels ( id )
            ON DELETE CASCADE;

ALTER TABLE reservations_history
    ADD CONSTRAINT reservations_history_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id )
            ON DELETE SET NULL;

ALTER TABLE reservations_history
    ADD CONSTRAINT reservations_history_rooms_fk FOREIGN KEY ( rooms_id )
        REFERENCES rooms ( id )
            ON DELETE SET NULL;


ALTER TABLE users_stats
    ADD CONSTRAINT users_stats_users_fk FOREIGN KEY ( users_id )
        REFERENCES users ( id )
            ON DELETE CASCADE;

ALTER TABLE hotels_stats
    ADD CONSTRAINT hotels_stats_hotles_fk FOREIGN KEY ( hotels_id )
        REFERENCES hotels ( id )
            ON DELETE CASCADE;
            

CREATE SEQUENCE users_seq START WITH 1;

CREATE OR REPLACE TRIGGER users_bir 
BEFORE INSERT ON users 
FOR EACH ROW
BEGIN
  SELECT users_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE hotels_seq START WITH 1;

CREATE OR REPLACE TRIGGER hotels_bir 
BEFORE INSERT ON hotels 
FOR EACH ROW
BEGIN
  SELECT hotels_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE rooms_seq START WITH 1;

CREATE OR REPLACE TRIGGER rooms_bir 
BEFORE INSERT ON rooms 
FOR EACH ROW
BEGIN
  SELECT rooms_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE addresses_seq START WITH 1;

CREATE OR REPLACE TRIGGER addresses_bir 
BEFORE INSERT ON addresses 
FOR EACH ROW
BEGIN
  SELECT addresses_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE regions_seq START WITH 1;

CREATE OR REPLACE TRIGGER regions_bir 
BEFORE INSERT ON regions 
FOR EACH ROW
BEGIN
  SELECT regions_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE countries_seq START WITH 1;

CREATE OR REPLACE TRIGGER countries_bir 
BEFORE INSERT ON countries 
FOR EACH ROW
BEGIN
  SELECT countries_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE photos_seq START WITH 1;

CREATE OR REPLACE TRIGGER photos_bir 
BEFORE INSERT ON photos 
FOR EACH ROW
BEGIN
  SELECT photos_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE comments_seq START WITH 1;

CREATE OR REPLACE TRIGGER comments_bir
BEFORE INSERT ON comments 
FOR EACH ROW
BEGIN
  SELECT comments_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE reservations_seq START WITH 1;

CREATE OR REPLACE TRIGGER reservations_bir
BEFORE INSERT ON reservations 
FOR EACH ROW
BEGIN
  SELECT reservations_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE reservations_history_seq START WITH 1;

CREATE OR REPLACE TRIGGER reservations_history_bir
BEFORE INSERT ON reservations_history
FOR EACH ROW
BEGIN
  SELECT reservations_history_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE users_stats_seq START WITH 1;

CREATE OR REPLACE TRIGGER users_stats_bir
BEFORE INSERT ON users_stats
FOR EACH ROW
BEGIN
  SELECT users_stats_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE SEQUENCE hotels_stats_seq START WITH 1;

CREATE OR REPLACE TRIGGER hotels_stats_bir
BEFORE INSERT ON hotels_stats
FOR EACH ROW
BEGIN
  SELECT hotels_stats_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/



CREATE OR REPLACE VIEW hotel_info AS
    SELECT hotels.id as id, hotels.name as hname, tel, description, users_id, main_photo_id as mphoto, addresses.id as aid, city, street, "number",  POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, 
           (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating 
    FROM hotels JOIN addresses ON (hotels.addresses_id = addresses.id)
                JOIN regions ON (addresses.regions_id = regions.id)
                JOIN countries ON (regions.countries_id = countries.id)
                LEFT JOIN (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo ON (hotels.id = roominfo.hotels_id);


create or replace PROCEDURE MOVE_OLD_RESERVATIONS
AS
BEGIN
    FOR v_reservation in (SELECT * 
             FROM reservations
             WHERE finishdate < SYSDATE)
    LOOP

        INSERT INTO reservations_history(startdate, finishdate, users_id, rooms_id)
        VALUES (v_reservation.startdate, v_reservation.finishdate, v_reservation.users_id, v_reservation.rooms_id);

    END LOOP;


    DELETE FROM reservations
    WHERE finishdate < SYSDATE;
END;
/

--exec MOVE_OLD_RESERVATIONS; 

create or replace PROCEDURE create_hotels_stats_row_if_not_exists(r_month number, r_year NUMBER, h_id NUMBER)
AS
    is_existing NUMBER;
    hotel_stat_id hotels_stats.id%type;
BEGIN 
    SELECT COUNT(*)
    INTO is_existing
    FROM hotels_stats
    WHERE hotels_id = h_id and
          year = r_year and
          month = r_month;
          
    IF is_existing = 0 then
        INSERT INTO hotels_stats (hotels_id, month, year)
        VALUES (h_id, r_month, r_year);        
    END IF;
END;
/

--exec create_hotels_stats_row_if_not_exists(10, 2021, 1);

create or replace PROCEDURE create_users_stats_row_if_not_exists(r_month number, r_year NUMBER, u_id NUMBER)
AS
    is_existing NUMBER;
    users_stat_id users_stats.id%type;
BEGIN 
    SELECT COUNT(*)
    INTO is_existing
    FROM users_stats
    WHERE users_id = u_id  and
          year = r_year and
          month = r_month;
          
    IF is_existing = 0 then
        INSERT INTO users_stats (users_id, month, year)
        VALUES (u_id, r_month, r_year);        
    END IF;
END;
/

-- exec create_users_stats_row_if_not_exists(1, 2021, 1);

create or replace FUNCTION get_hotels_stats_id(r_month number, r_year NUMBER, h_id NUMBER) RETURN NUMBER
AS
    hotel_stat_id hotels_stats.id%type;
BEGIN 
    SELECT id
    INTO hotel_stat_id
    FROM hotels_stats
    WHERE hotels_id = h_id and
          year = r_year and
          month = r_month;

    RETURN hotel_stat_id;
    
EXCEPTION WHEN NO_DATA_FOUND THEN
    RETURN -1;
END;
/

-- SELECT get_hotels_stats_id(11, 2021, 1) FROM DUAL; get_user_stat_id

create or replace FUNCTION get_users_stats_id(r_month number, r_year NUMBER, u_id NUMBER) RETURN NUMBER
AS
    user_stat_id hotels_stats.id%type;
BEGIN 
    SELECT id
    INTO user_stat_id
    FROM users_stats
    WHERE users_id = u_id and
          year = r_year and
          month = r_month;

    RETURN user_stat_id;
    
EXCEPTION WHEN NO_DATA_FOUND THEN
    RETURN -1;
END;
/

-- SELECT get_users_stats_id(1, 2021, 1) FROM DUAL; getReservationDays

create or replace FUNCTION get_reservation_days(r_month number, r_year NUMBER, res_id NUMBER) RETURN NUMBER
AS
    res reservations%ROWTYPE;
    begin_day NUMBER;
    end_day NUMBER;
    days_count NUMBER := 0;
BEGIN
    SELECT *
    INTO res
    FROM reservations
    WHERE id = res_id;

    IF extract(year from res.startdate) > r_year or 
       extract(year from res.startdate) = r_year and extract(month from res.startdate) > r_month or
       extract(year from res.finishdate) < r_year or 
       extract(year from res.finishdate) = r_year and extract(month from res.finishdate) < r_month then

        days_count := 0;

    ELSE
        IF extract(year from res.startdate) < r_year or
           extract(year from res.startdate) = r_year and extract(month from res.startdate) < r_month then

           begin_day := 1;

        ELSE
            begin_day := extract(day from res.startdate);
        END IF;

        IF extract(year from res.finishdate) > r_year or 
           extract(year from res.finishdate) = r_year and extract(month from res.finishdate) > r_month then

            end_day := extract(day from LAST_DAY(r_year || '-' || r_month || '-' || '01' ));
        ELSE
            end_day := extract(day from res.finishdate);
        END IF;

        days_count := end_day - begin_day + 1;
    END IF;

    days_count := NVL(days_count, 0);
    
    RETURN days_count;
END;
/

create or replace FUNCTION get_reservation_days_direct(r_month number, r_year NUMBER, r_start_date DATE,  r_finish_date DATE ) RETURN NUMBER
AS
    begin_day NUMBER;
    end_day NUMBER;
    days_count NUMBER := 0;
BEGIN

    IF extract(year from r_start_date) > r_year or 
       extract(year from r_start_date) = r_year and extract(month from r_start_date) > r_month or
       extract(year from r_finish_date) < r_year or 
       extract(year from r_finish_date) = r_year and extract(month from r_finish_date) < r_month then

        days_count := 0;

    ELSE
        IF extract(year from r_start_date) < r_year or
           extract(year from r_start_date) = r_year and extract(month from r_start_date) < r_month then

           begin_day := 1;

        ELSE
            begin_day := extract(day from r_start_date);
        END IF;

        IF extract(year from r_finish_date) > r_year or 
           extract(year from r_finish_date) = r_year and extract(month from r_finish_date) > r_month then

            end_day := extract(day from LAST_DAY(TO_DATE(r_year || '-' || r_month || '-' || '01', 'YYYY-MM-DD')));
        ELSE
            end_day := extract(day from r_finish_date);
        END IF;

        days_count := end_day - begin_day + 1;
    END IF;

    days_count := NVL(days_count, 0);
    
    RETURN days_count;
END;
/

-- SELECT get_reservation_days_direct(6, 2021, '2021-05-10', '2021-05-25') FROM DUAL;

create or replace TRIGGER update_stats_on_reservation_insert
AFTER INSERT ON reservations FOR EACH ROW
DECLARE
    v_month NUMBER;
    v_year NUMBER;
    v_h_id hotels.id%TYPE;
    stat_id NUMBER;
    v_reservation_days NUMBER := 0;
    v_capacity rooms.capacity%TYPE;
    v_price rooms.price%TYPE;

BEGIN


    -- update hotel stats
    v_month := extract(month from :new.startdate);
    v_year := extract(year from :new.startdate);

    SELECT hotels_id, capacity, price
    INTO v_h_id, v_capacity, v_price
    FROM rooms
    WHERE id = :new.rooms_id;

    LOOP
        v_reservation_days := get_reservation_days_direct(v_month, v_year, :new.startdate, :new.finishdate);
        EXIT WHEN v_reservation_days = 0;

        create_hotels_stats_row_if_not_exists(v_month, v_year, v_h_id);
        stat_id := get_hotels_stats_id(v_month, v_year, v_h_id);

        UPDATE hotels_stats 
        SET occupancy_days = occupancy_days + v_reservation_days * v_capacity,
            income = income + v_reservation_days * v_price
        WHERE id = stat_id;
        
        v_month := v_month + 1;
        
        IF v_month = 13 then
            v_month := 0;
            v_year := v_year + 1;
        END IF;

    END LOOP;

    create_hotels_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), v_h_id);
    stat_id := get_hotels_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), v_h_id);
    UPDATE hotels_stats 
        SET RESERVATIONS_NUM = RESERVATIONS_NUM + 1
        WHERE id = stat_id;
        
    -- update user stats
    create_users_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), :new.users_id);
    stat_id := get_users_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), :new.users_id);
    UPDATE users_stats 
        SET RESERVATIONS_NUM = RESERVATIONS_NUM + 1
        WHERE id = stat_id;
END;
/


create or replace TRIGGER update_stats_on_reservation_remove
AFTER DELETE ON reservations FOR EACH ROW
DECLARE
    v_month NUMBER;
    v_year NUMBER;
    v_h_id hotels.id%TYPE;
    stat_id NUMBER;
    v_reservation_days NUMBER := 0;
    v_capacity rooms.capacity%TYPE;
    v_price rooms.price%TYPE;

BEGIN


    v_month := extract(month from :old.startdate);
    v_year := extract(year from :old.startdate);

    SELECT hotels_id, capacity, price
    INTO v_h_id, v_capacity, v_price
    FROM rooms
    WHERE id = :old.rooms_id;

    LOOP
        v_reservation_days := get_reservation_days_direct(v_month, v_year, :old.startdate, :old.finishdate);
        EXIT WHEN v_reservation_days = 0;

        create_hotels_stats_row_if_not_exists(v_month, v_year, v_h_id);
        stat_id := get_hotels_stats_id(v_month, v_year, v_h_id);

        UPDATE hotels_stats 
        SET occupancy_days = occupancy_days - v_reservation_days * v_capacity,
            income = income - v_reservation_days * v_price
        WHERE id = stat_id;
        
        v_month := v_month + 1;
        
        IF v_month = 13 then  
            v_month := 0;
            v_year := v_year + 1;
        END IF;

    END LOOP;

    create_hotels_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), v_h_id);
    stat_id := get_hotels_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), v_h_id);
    UPDATE hotels_stats 
        SET cancelations_num = cancelations_num + 1
        WHERE id = stat_id; 

    -- update user stats
    create_users_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), :old.users_id);
    stat_id := get_users_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), :old.users_id);
    UPDATE users_stats 
        SET cancelations_num = cancelations_num + 1
        WHERE id = stat_id;
END;
/

create or replace TRIGGER update_stats_on_comment_insert
AFTER INSERT ON comments FOR EACH ROW
DECLARE
    stat_id NUMBER;
BEGIN

    -- update hotel stats
    create_hotels_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), :new.hotels_id);
    stat_id := get_hotels_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), :new.hotels_id);
    UPDATE hotels_stats 
        SET comments_num = comments_num + 1
        WHERE id = stat_id;
        
    -- update user stats
    create_users_stats_row_if_not_exists(extract(month from SYSDATE), extract(year from SYSDATE), :new.users_id);
    stat_id := get_users_stats_id(extract(month from SYSDATE), extract(year from SYSDATE), :new.users_id);
    UPDATE users_stats 
        SET comments_num = comments_num + 1
        WHERE id = stat_id;
END;
/

ALTER TRIGGER update_stats_on_reservation_insert ENABLE;
ALTER TRIGGER update_stats_on_reservation_remove ENABLE;
ALTER TRIGGER update_stats_on_comment_insert ENABLE;
COMMIT;


ALTER TRIGGER update_stats_on_reservation_insert DISABLE;
ALTER TRIGGER update_stats_on_reservation_remove DISABLE;
ALTER TRIGGER update_stats_on_comment_insert DISABLE;
COMMIT;

CREATE OR REPLACE PROCEDURE set_date_format
as
BEGIN
    ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD';
END;
/


--SELECT LAST_DAY(extract(year from sysdate) || '-' || extract(month from sysdate)  || '-' || '01')   from dual;



/*
BEGIN
  DBMS_SCHEDULER.CREATE_JOB (
   job_name           =>  'move_old_reservations_to_history',
   job_type           =>  'STORED_PROCEDURE',
   job_action         =>  'MOVE_OLD_RESERVATIONS',
   repeat_interval    =>  'FREQ=DAILY;INTERVAL=1', 
   auto_drop          =>   FALSE,
   job_class          =>  'batch_update_jobs',
   comments           =>  'Moves old reservations to history relation');
END;
/

BEGIN
  DBMS_SCHEDULER.DROP_JOB ('move_old_reservations_to_history');
END;
/

Error report -
ORA-27486: insufficient privileges
ORA-06512: at "SYS.DBMS_ISCHED", line 175
ORA-06512: at "SYS.DBMS_SCHEDULER", line 286
ORA-06512: at line 2
27486. 00000 -  "insufficient privileges"
*Cause:    An attempt was made to perform a scheduler operation without the
           required privileges.
*Action:   Ask a sufficiently privileged user to perform the requested
           operation, or grant the required privileges to the proper user(s).

*/