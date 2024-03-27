--
-- File generated with SQLiteStudio v3.4.4 on Wed Mar 27 06:15:09 2024
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Aircraft
DROP TABLE IF EXISTS Aircraft;
CREATE TABLE IF NOT EXISTS Aircraft (aircraftID INTEGER PRIMARY KEY, airportID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, reserved boolean);
INSERT INTO Aircraft (aircraftID, airportID, reserved) VALUES (0, 'YYZ', 0);
INSERT INTO Aircraft (aircraftID, airportID, reserved) VALUES (1, 'YYZ', 0);
INSERT INTO Aircraft (aircraftID, airportID, reserved) VALUES (2, 'YYZ', 0);
INSERT INTO Aircraft (aircraftID, airportID, reserved) VALUES (3, 'AUN', 0);
INSERT INTO Aircraft (aircraftID, airportID, reserved) VALUES (4, 'PTY', 0);

-- Table: Airline
DROP TABLE IF EXISTS Airline;
CREATE TABLE IF NOT EXISTS Airline(
    name        TEXT PRIMARY KEY
);
INSERT INTO Airline (name) VALUES ('Sunwing');
INSERT INTO Airline (name) VALUES ('JetTravel');
INSERT INTO Airline (name) VALUES ('CopaAirline');

-- Table: Airport
DROP TABLE IF EXISTS Airport;
CREATE TABLE IF NOT EXISTS Airport (letterCode char (3) PRIMARY KEY, locationID TEXT REFERENCES City (name) ON UPDATE SET DEFAULT, name TEXT);
INSERT INTO Airport (letterCode, locationID, name) VALUES ('AUN', 'Antigua', 'Antigua Airport');
INSERT INTO Airport (letterCode, locationID, name) VALUES ('YYZ', 'Toronto', 'Toronto Airport');
INSERT INTO Airport (letterCode, locationID, name) VALUES ('PTY', 'Panama City', 'Panama Airport');

-- Table: City
DROP TABLE IF EXISTS City;
CREATE TABLE IF NOT EXISTS City (name TEXT PRIMARY KEY, country TEXT, temperature REAL, metric TEXT);
INSERT INTO City (name, country, temperature, metric) VALUES ('Toronto', 'Canada', 18.02, 'Celcius');
INSERT INTO City (name, country, temperature, metric) VALUES ('Montreal', 'Canada', 32.1, 'Celcius');
INSERT INTO City (name, country, temperature, metric) VALUES ('Winnipeg', 'Canada', 14.9, 'Celcius');
INSERT INTO City (name, country, temperature, metric) VALUES ('Antigua', 'Guatemala', 25.0, 'Celcius');
INSERT INTO City (name, country, temperature, metric) VALUES ('Panama City', 'Panama', 35.0, 'Celcius');

-- Table: Fleet
DROP TABLE IF EXISTS Fleet;
CREATE TABLE IF NOT EXISTS Fleet (aircraftID integer PRIMARY KEY REFERENCES Aircraft (aircraftID) ON UPDATE SET DEFAULT, airlineName TEXT REFERENCES Airline (name) ON UPDATE SET DEFAULT);
INSERT INTO Fleet (aircraftID, airlineName) VALUES (0, 'Sunwing');
INSERT INTO Fleet (aircraftID, airlineName) VALUES (1, 'Sunwing');
INSERT INTO Fleet (aircraftID, airlineName) VALUES (2, 'Sunwing');
INSERT INTO Fleet (aircraftID, airlineName) VALUES (3, 'JetTravel');
INSERT INTO Fleet (aircraftID, airlineName) VALUES (4, 'CopaAirline');

-- Table: Flight
DROP TABLE IF EXISTS Flight;
CREATE TABLE IF NOT EXISTS Flight (flightNumber TEXT PRIMARY KEY, sourceID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, destinationID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, aircraftID INTEGER REFERENCES Aircraft (aircraftID), airlineOperating TEXT REFERENCES Airline (name) ON UPDATE SET DEFAULT, airportOperating char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, recordWritter TEXT REFERENCES User (login) ON UPDATE SET DEFAULT, flightDiscriminator INTEGER, scheduledDepart DATETIME, scheduledArriv DATETIME, actualDepart DATETIME, actualArriv DATETIME);
INSERT INTO Flight (flightNumber, sourceID, destinationID, aircraftID, airlineOperating, airportOperating, recordWritter, flightDiscriminator, scheduledDepart, scheduledArriv, actualDepart, actualArriv) VALUES ('WG117', 'YYZ', 'ANU', 0, NULL, 'YYZ', 'AirportAdmin', 1, '2024-03-30T12:30', '2024-03-30T18:30', '2024-03-30T12:30', '2024-03-30T18:30');
INSERT INTO Flight (flightNumber, sourceID, destinationID, aircraftID, airlineOperating, airportOperating, recordWritter, flightDiscriminator, scheduledDepart, scheduledArriv, actualDepart, actualArriv) VALUES ('WG120', 'ANU', 'YYZ', 0, 'Sunwing', NULL, 'AirlineAdmin', 3, '2024-04-30T12:30', '2024-04-30T18:30', '2024-04-30T12:30', '2024-04-30T18:30');
INSERT INTO Flight (flightNumber, sourceID, destinationID, aircraftID, airlineOperating, airportOperating, recordWritter, flightDiscriminator, scheduledDepart, scheduledArriv, actualDepart, actualArriv) VALUES ('WG118', 'ANU', 'YYZ', 0, 'Sunwing', NULL, 'AirlineAdmin', 2, '2024-01-30T12:30', '2024-01-30T18:30', '2024-01-30T12:30', '2024-01-30T18:30');

-- Table: User
DROP TABLE IF EXISTS User;
CREATE TABLE IF NOT EXISTS User (login TEXT PRIMARY KEY, pass TEXT, userDiscriminator integer, employedByAirport char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, employedByAirline TEXT REFERENCES Airline (name) ON UPDATE SET DEFAULT);
INSERT INTO User (login, pass, userDiscriminator, employedByAirport, employedByAirline) VALUES ('RegularUser', 'pass', 1, NULL, NULL);
INSERT INTO User (login, pass, userDiscriminator, employedByAirport, employedByAirline) VALUES ('SystemUser', 'sysadmin', 2, NULL, NULL);
INSERT INTO User (login, pass, userDiscriminator, employedByAirport, employedByAirline) VALUES ('AirportAdmin', 'airportadmin', 3, 'YYZ', NULL);
INSERT INTO User (login, pass, userDiscriminator, employedByAirport, employedByAirline) VALUES ('AirlineAdmin', 'airlineadmin', 4, NULL, 'Sunwing');

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
