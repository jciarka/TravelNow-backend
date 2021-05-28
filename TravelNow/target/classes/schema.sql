/*


DROP TABLE photos;
DROP TABLE comments;
DROP TABLE reservations;
DROP TABLE rooms;
DROP TABLE hotels;
DROP TABLE addresses;
DROP TABLE regions;
DROP TABLE countries;
DROP TABLE users;

DROP SEQUENCE users_seq;
DROP SEQUENCE hotels_seq;
DROP SEQUENCE rooms_seq;
DROP SEQUENCE addresses_seq;
DROP SEQUENCE regions_seq;
DROP SEQUENCE countries_seq;
DROP SEQUENCE photos_seq;
DROP SEQUENCE comments_seq;
DROP SEQUENCE reservations_seq;

/*
DROP TRIGGER users_bir;
DROP TRIGGER hotels_bir;
DROP TRIGGER rooms_bir;
DROP TRIGGER addresses_bir;
DROP TRIGGER region_bir;
DROP TRIGGER countries_bir;
DROP TRIGGER photos_bir;
DROP TRIGGER comments_bir;
DROP TRIGGER reservations_bir;
*/

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
    text       CLOB,
    rating     NUMBER(1) NOT NULL,
    users_id   NUMBER(10) NOT NULL,
    hotels_id  NUMBER(10) NOT NULL
);

ALTER TABLE comments ADD CHECK ( rating BETWEEN 1 AND 5 );

ALTER TABLE comments ADD CONSTRAINT comments_pk PRIMARY KEY ( id );

CREATE TABLE countries (
    id            NUMBER(10) NOT NULL,
    name          VARCHAR2(50 CHAR) NOT NULL,
    dialingcode   NUMBER(4) NOT NULL,
    abbreviation  VARCHAR2(5 CHAR) NOT NULL
);

ALTER TABLE countries ADD CONSTRAINT country_pk PRIMARY KEY ( id );

CREATE TABLE hotels (
    id            NUMBER(10) NOT NULL,
    name          VARCHAR2(255 CHAR) NOT NULL,
    tel           NUMBER(9) NOT NULL,
    description   CLOB,
    addresses_id  NUMBER(10) NOT NULL,
    users_id      NUMBER(10) NOT NULL
);

CREATE UNIQUE INDEX hotels__idx ON
    hotels (
        addresses_id
    ASC );

ALTER TABLE hotels ADD CONSTRAINT hotels_pk PRIMARY KEY ( id );

CREATE TABLE photos (
    id         NUMBER(10) NOT NULL,
    image      BLOB NOT NULL,
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


INSERT INTO users (username, password, active, roles, email)
VALUES ('dummy', '$2a$10$/gYgx7Tdzw9IV1zrotiWL.aqpClOPUDsPCjm/9O9V/Ub8P5gHC2/2', 'Y', 'ROLE_USER', 'dummy@o2.pl');

INSERT INTO users  (username, password, active, roles, email)
VALUES ('admin', '$2a$10$2wCMEnzE7o0gc2.1dfqgduui0R7V4ZTQW4VPzrjbZ4mZ4CuAUCWJK', 'Y', 'ROLE_USER,ROLE_ADMIN', 'admin@onet.pl');

INSERT INTO users (username, password, active, roles, email)
VALUES ('gołębiewski', '$2a$10$ZG0vJbwtK4hSoD88nvmeBe9yoSX32IBEABYZ8ifDKbgHuQ8Z52RRO', 'Y', 'ROLE_OWNER', 'golembiewski@o2.pl');

INSERT INTO users (username, password, active, roles, email)
VALUES ('hilton', '$2a$10$b8o275GROLtU3DmAkMkRT.UB.OxQ0rmIVBx3NWSOV3HITOGVx.nHy', 'Y', 'ROLE_OWNER', 'hilton@o2.pl');



-- Kraje
INSERT INTO countries  (name, dialingcode, abbreviation)
VALUES ('Polska', '0048', 'PL');

INSERT INTO countries  (name, dialingcode, abbreviation)
VALUES ('Niemcy', '0049', 'GER');

INSERT INTO countries  (name, dialingcode, abbreviation)
VALUES ('Hiszpania', '0034', 'HIS');

INSERT INTO countries  (name, dialingcode, abbreviation)
VALUES ('Włochy', '0039', 'ITA');


-- Regiony
INSERT INTO regions (name, countries_id)
    SELECT 'Mazowieckie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';

INSERT INTO regions (name, countries_id)
    SELECT 'Podkarpackie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Śląskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';

INSERT INTO regions (name, countries_id)
    SELECT 'Małopolskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';

INSERT INTO regions (name, countries_id)
    SELECT 'Dolnośląskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';

INSERT INTO regions (name, countries_id)
    SELECT 'Opolskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
    
INSERT INTO regions (name, countries_id)
    SELECT 'Pomorskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    

INSERT INTO regions (name, countries_id)
    SELECT 'Zachodnio-pomorskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
    
INSERT INTO regions (name, countries_id)
    SELECT 'Warmińsko-mazurskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Podlaskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Wielkopolskie', countries.id
    FROM countries
    WHERE countries.name = 'Polska';
    
    
INSERT INTO regions (name, countries_id)
    SELECT 'Bayer', countries.id
    FROM countries
    WHERE countries.name = 'Niemcy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Schleswig-holstein', countries.id
    FROM countries
    WHERE countries.name = 'Niemcy';
    
    
INSERT INTO regions (name, countries_id)
    SELECT 'Berlin', countries.id
    FROM countries
    WHERE countries.name = 'Niemcy';
    
    
INSERT INTO regions (name, countries_id)
    SELECT 'Hamburg', countries.id
    FROM countries
    WHERE countries.name = 'Niemcy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Norden-Westfalen', countries.id
    FROM countries
    WHERE countries.name = 'Niemcy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Andaluzja', countries.id
    FROM countries
    WHERE countries.name = 'Hiszpania';

INSERT INTO regions (name, countries_id)
    SELECT 'Katalonia', countries.id
    FROM countries
    WHERE countries.name = 'Hiszpania';
    
INSERT INTO region (name, countries_id)
    SELECT 'Katalonia', countries.id
    FROM countries
    WHERE countries.name = 'Walencja';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Katalonia', countries.id
    FROM countries
    WHERE countries.name = 'Hiszpania';

INSERT INTO regions (name, countries_id)
    SELECT 'Toskania', countries.id
    FROM countries
    WHERE countries.name = 'Włochy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Sycylia', countries.id
    FROM countries
    WHERE countries.name = 'Włochy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Kampania', countries.id
    FROM countries
    WHERE countries.name = 'Włochy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Lombardia', countries.id
    FROM countries
    WHERE countries.name = 'Włochy';
    
INSERT INTO regions (name, countries_id)
    SELECT 'Trydent', countries.id
    FROM countries
    WHERE countries.name = 'Włochy';
    
    
    
    
-- Adresy i Hotele
INSERT INTO addresses (street, "number", city, postalcode, regions_id)
    SELECT 'Karkonoska', 14, 'Karpacz', '58-540',  id
    FROM regions
    WHERE regions.name = 'Dolnośląskie';
    

INSERT INTO hotels (name, tel, description, addresses_id, users_id)
    SELECT 'Gołębiewski', 624048550, 'Hotel Gołebiewski w karpaczu z aquaparkiem i trzema restauracjami', addresses.id, users.id
    FROM addresses, users
    WHERE addresses.street = 'Karkonoska' AND addresses."number" = 14 AND users.username='gołębiewski';
    

INSERT INTO rooms (capacity, price, hotels_id)
    SELECT 2, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
        
INSERT INTO rooms (capacity, price, hotels_id)
    SELECT 2, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
        
INSERT INTO rooms (capacity, price, hotels_id)
    SELECT 2, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
            
            
            
    
    
INSERT INTO addresses (street, "number", city, postalcode, regions_id)
    SELECT 'al. Jerozolimskie', 65, 'Warszawa', '00-697',  id
    FROM regions
    WHERE regions.name = 'Mazowieckie';
    

INSERT INTO hotels (name, tel, description, addresses_id, users_id)
    SELECT 'Warsaw Marriott Hotel', 562157259, 'legendarny hotel w wieżowcu obok dworca centralnego',addresses.id, users.id
    FROM addresses, users
    WHERE addresses.street = 'al. Jerozolimskie' AND addresses."number" = 65 AND users.username = 'hilton';

INSERT INTO rooms (capacity, price, hotels_id)
    SELECT 2, 250, hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';

INSERT INTO rooms (capacity, price, hotels_id)
    SELECT 2, 250, hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';
   
    
SELECT hotels.name as hname, tel, description, city, street, "number", POSTALCODE, regions.name as rname, countries.name as cname
FROM hotels, addresses, regions, countries 
WHERE hotels.addresses_id = addresses.id AND
     addresses.regions_id = regions.id AND
     regions.countries_id = countries.id AND
     city='Warszawa';
     

SELECT hotels.id, hotels.name as hname, tel, description, city, street, "number", POSTALCODE, regions.name as rname, countries.name as cname, capacity, price
FROM hotels, addresses, regions, countries, rooms 
WHERE hotels.addresses_id = addresses.id AND
     addresses.regions_id = regions.id AND
     regions.countries_id = countries.id AND
     hotels.id = rooms.hotels_id AND
     city='Warszawa';
    



*/