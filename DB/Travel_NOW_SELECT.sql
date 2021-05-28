SELECT comments.id as id, text, rating, postdate, hotels_id as hotelsid, username, email
FROM comments INNER JOIN users ON users.id = comments.users_id;
    
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
  
  

SELECT hotels.id, hotels.name as hname, tel, description, city, street, "number", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating
FROM hotels, addresses, regions, countries, (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo
WHERE hotels.addresses_id = addresses.id AND
     addresses.regions_id = regions.id AND
     regions.countries_id = countries.id AND
     hotels.id = roominfo.hotels_id AND
     city='Warszawa';


SELECT id, capacity, price, hotels_id as hotelsid FROM rooms WHERE hotels_id=1;

SELECT comments.id as id, text, rating, postdate, hotels_id as hotelsid, username as authorUserName, email
FROM comments INNER JOIN users ON users.id = comments.users_id;

SELECT id FROM photos WHERE hotels_id = 1;

SELECT id, capacity, price, hotels_id as hotelsid 
FROM rooms 
WHERE hotels_id=1 AND
      id NOT IN (
        SELECT rooms_id
        FROM reservations
        WHERE (startdate > '2021-03-30' and startdate < '2021-03-31') OR
              (finishdate > '2021-03-30' and finishdate < '2021-03-31')
);

--INSERT INTO countries (name, dialingcode, abbreviation) VALUES ( 'dcsd', 0021, 'abcd');
    

--SELECT hotels.id as id, hotels.name as hname, tel, description, main_photo_id as mphoto, city, street, "number", POSTALCODE, regions.name as rname, countries.name as cname, numofrooms, avgprice, (SELECT AVG(rating) FROM comments WHERE hotels_id=hotels.id) as rating 
--FROM hotels, 
--     addresses, 
--     regions, 
--    countries, 
--     (SELECT hotels_id, SUM(1) as numofrooms, AVG(price/capacity) as avgprice FROM rooms GROUP BY hotels_id) roominfo 
--     WHERE hotels.addresses_id = addresses.id AND 
--          addresses.regions_id = regions.id AND 
--           regions.countries_id = countries.id AND 
--           hotels.id = roominfo.hotels_id AND 
--           users_id=3;

--SELECT * FROM hotel_info WHERE city='Warszawa';
--SELECT * FROM hotel_info WHERE id=1; -- hotel id
--SELECT * FROM hotel_info WHERE city='Warszawa' -- ;

/*
SELECT id, capacity, "number", price, hotels_id as hotelsid 
    FROM rooms 
    WHERE hotels_id=1 AND id NOT IN (
        SELECT rooms_id 
        FROM reservations 
        WHERE (startdate <= '2021-04-29' and finishdate > '2021-04-29') OR (startdate < '2021-04-30' and finishdate >= '2021-04-30')
    );
*/