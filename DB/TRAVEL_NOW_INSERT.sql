INSERT INTO users (username, password, active, roles, email)
VALUES ('dummy', '$2a$10$/gYgx7Tdzw9IV1zrotiWL.aqpClOPUDsPCjm/9O9V/Ub8P5gHC2/2', 'Y', 'ROLE_USER', 'dummy@o2.pl');

INSERT INTO users  (username, password, active, roles, email)
VALUES ('admin', '$2a$10$2wCMEnzE7o0gc2.1dfqgduui0R7V4ZTQW4VPzrjbZ4mZ4CuAUCWJK', 'Y', 'ROLE_USER,ROLE_ADMIN', 'admin@onet.pl');

INSERT INTO users (username, password, active, roles, email)
VALUES ('golembiewski', '$2a$10$ZG0vJbwtK4hSoD88nvmeBe9yoSX32IBEABYZ8ifDKbgHuQ8Z52RRO', 'Y', 'ROLE_OWNER', 'golembiewski@o2.pl');

INSERT INTO users (username, password, active, roles, email)
VALUES ('hilton', '$2a$10$b8o275GROLtU3DmAkMkRT.UB.OxQ0rmIVBx3NWSOV3HITOGVx.nHy', 'Y', 'ROLE_OWNER', 'hilton@o2.pl');



-- Kraje
INSERT INTO countries  (name, abbreviation, dialingcode)
VALUES ('Polska', 'PL', 0048);

INSERT INTO countries  (name, abbreviation, dialingcode)
VALUES ('Niemcy', 'GER', 0049);

INSERT INTO countries  (name, abbreviation, dialingcode)
VALUES ('Hiszpania', 'HIS', 0034);

INSERT INTO countries  (name, abbreviation, dialingcode)
VALUES ('Włochy', 'ITA', 0039);


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
    SELECT 'al. Jerozolimskie', 65, 'Warszawa', '00-697',  id
    FROM regions
    WHERE regions.name = 'Mazowieckie';
    

INSERT INTO hotels (name, tel, description, addresses_id, users_id)
    SELECT 'Warsaw Marriott Hotel', 562157259, 'legendarny hotel w wieżowcu obok dworca centralnego',addresses.id, users.id
    FROM addresses, users
    WHERE addresses.street = 'al. Jerozolimskie' AND addresses."number" = 65 AND users.username = 'hilton';

INSERT INTO rooms (capacity, "number", price, hotels_id)
    SELECT 2, 1, 250, hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';

INSERT INTO rooms (capacity, "number", price, hotels_id)
    SELECT 2, 2, 250, hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';
   

INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';

INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';
    
INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Warsaw Marriott Hotel';

UPDATE hotels
    SET main_photo_id = 1
    WHERE hotels.name = 'Warsaw Marriott Hotel'; 



INSERT INTO addresses (street, "number", city, postalcode, regions_id)
    SELECT 'Karkonoska', 14, 'Karpacz', '58-540',  id
    FROM regions
    WHERE regions.name = 'Dolnośląskie';
    

INSERT INTO hotels (name, tel, description, addresses_id, users_id)
    SELECT 'Gołębiewski', 624048550, 'Hotel Gołebiewski w karpaczu z aquaparkiem i trzema restauracjami', addresses.id, users.id
    FROM addresses, users
    WHERE addresses.street = 'Karkonoska' AND addresses."number" = 14 AND users.username='golembiewski';
    

INSERT INTO rooms (capacity, "number", price, hotels_id)
    SELECT 2, 1, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
        
INSERT INTO rooms (capacity, "number", price, hotels_id)
    SELECT 2, 2, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
        
INSERT INTO rooms (capacity, "number", price, hotels_id)
    SELECT 2, 3, 200, hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
    
INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';

INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';
    
INSERT INTO photos (hotels_id)
    SELECT hotels.id
    FROM hotels
    WHERE hotels.name = 'Gołębiewski';

UPDATE hotels
    SET main_photo_id = 4
    WHERE hotels.name = 'Gołębiewski'; 

-- reservations
INSERT INTO reservations(startdate, finishdate, users_id, rooms_id)
VALUES ('2021-04-05', '2021-05-03', 1, 1 );
INSERT INTO reservations(startdate, finishdate, users_id, rooms_id)
VALUES ('2021-05-07', '2021-05-10', 1, 1 );
INSERT INTO reservations(startdate, finishdate, users_id, rooms_id)
VALUES ('2021-04-15', '2021-06-4', 1, 1 );
INSERT INTO reservations(startdate, finishdate, users_id, rooms_id)
VALUES ('2021-07-15', '2021-07-20', 1, 2 );

INSERT INTO comments (text, rating, users_id, hotels_id) 
VALUES ('Great', 5, (SELECT id FROM users WHERE username='dummy'), (SELECT id FROM hotels WHERE name='Warsaw Marriott Hotel'));

DELETE FROM reservations
WHERE id = 1;
DELETE FROM reservations
WHERE id = 2;
DELETE FROM reservations
WHERE id = 3;
DELETE FROM reservations
WHERE id = 4;

INSERT INTO reservations(startdate, finishdate, users_id, rooms_id)
VALUES (TO_DATE('2021-05-30', 'YYYY-MM-DD'), TO_DATE('2021-07-30', 'YYYY-MM-DD'), 1, 1 );
SELECT LAST_DAY(TO_DATE('2021-5-01', 'YYYY-MM-DD')) FROM DUAL;
SELECT SYSDATE FROM DUAL;
