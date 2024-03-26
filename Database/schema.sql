--
-- File generated with SQLiteStudio v3.4.4 on Tue Mar 26 19:02:00 2024
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table: Aircraft
DROP TABLE IF EXISTS Aircraft;
CREATE TABLE IF NOT EXISTS Aircraft (aircraftID INTEGER PRIMARY KEY, airportID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, reserved boolean);

-- Table: Airline
DROP TABLE IF EXISTS Airline;
CREATE TABLE IF NOT EXISTS Airline(
    name        TEXT PRIMARY KEY
);

-- Table: Airport
DROP TABLE IF EXISTS Airport;
CREATE TABLE IF NOT EXISTS Airport (letterCode char (3) PRIMARY KEY, locationID TEXT REFERENCES City (name) ON UPDATE SET DEFAULT, name TEXT);

-- Table: City
DROP TABLE IF EXISTS City;
CREATE TABLE IF NOT EXISTS City (name TEXT PRIMARY KEY, country TEXT, temperature REAL, metric TEXT);


-- Table: Fleet
DROP TABLE IF EXISTS Fleet;
CREATE TABLE IF NOT EXISTS Fleet (aircraftID integer PRIMARY KEY REFERENCES Aircraft (aircraftID), airlineName TEXT REFERENCES Airline (name));

-- Table: Flight
DROP TABLE IF EXISTS Flight;
CREATE TABLE IF NOT EXISTS Flight (flightNumber TEXT PRIMARY KEY, sourceID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, destinationID char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, aircraftID INTEGER REFERENCES Aircraft (aircraftID), airlineOperating TEXT REFERENCES Airline (name) ON UPDATE SET DEFAULT, airportOperating char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, recordWritter TEXT REFERENCES User (login) ON UPDATE SET DEFAULT, flightDiscriminator INTEGER, scheduledDepart DATETIME, scheduledArriv DATETIME, actualDepart DATETIME, actualArriv DATETIME);

-- Table: User
DROP TABLE IF EXISTS User;
CREATE TABLE IF NOT EXISTS User (login TEXT PRIMARY KEY, pass TEXT, userDiscriminator integer, employedByAirport char (3) REFERENCES Airport (letterCode) ON UPDATE SET DEFAULT, employedByAirline TEXT REFERENCES Airline (name) ON UPDATE SET DEFAULT);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
