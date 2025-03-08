

CREATE TABLE CustomerAccount(
    CustomerID INT AUTO_INCREMENT primary key NOT NULL,
    Name varchar(255) NOT NULL,
    Email varchar(255) NOT NULL UNIQUE,
    TelNumber BIGINT(14) NOT NULL,
    NumberOfSale INT  NULL
    CustomerType VARCHAR(10) NOT NULL DEFAULT 'Regular'
        CHECK(CustomerType IN ('Regular', 'Valued'))
);
CREATE TABLE TravelAdvisor(
    AdvisorID INT primary key AUTO_INCREMENT,
    Username varchar (255) NOT NULL UNIQUE,
    Name varchar(255) NOT NULL,
    Email varchar(255) NOT NULL,
    TelNumber BIGINT(14) NOT NULL,
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE Blank (
  BlankID INT AUTO_INCREMENT PRIMARY KEY,
  BlankNumber BIGINT(11) UNIQUE NOT NULL,
  AdvisorAssigned INT NULL,
  SellDate  DATE NULL,
  FOREIGN KEY (AdvisorAssigned) REFERENCES TravelAdvisor(AdvisorID)
);

CREATE TABLE OfficeManager(
    OfficeManagerID INT primary key AUTO_INCREMENT,
    Username varchar (255) NOT NULL UNIQUE,
    Name varchar(255) NOT NULL,
    Email varchar(255) NOT NULL,
    TelNumber BIGINT(14) NOT NULL,
    Password VARCHAR(255) NOT NULL
);
CREATE TABLE SystemAdministrator(
    SystemAdministratorID INT primary key AUTO_INCREMENT,
    Username varchar (255) NOT NULL UNIQUE,
    Name varchar(255) NOT NULL,
    Email varchar(255) NOT NULL,
    TelNumber BIGINT(14) NOT NULL,
    Password VARCHAR(255) NOT NULL
);
CREATE TABLE Sell (
  SaleID INT PRIMARY KEY AUTO_INCREMENT,
  Type VARCHAR(255),
  SellDate DATE,
  PaymentDate DATE NULL,
  AdvisorID INT,
  BlankNumber BIGINT,
  Departure VARCHAR(255),
  FirstLeg VARCHAR(255) NULL,
  SecondLeg VARCHAR(255) NULL,
  ThirdLeg VARCHAR(255) NULL;
  Destination VARCHAR(255),
  Currency VARCHAR(255),
  ExchangeRate FLOAT,
  Discount VARCHAR(255),
  TotalAmount FLOAT,
  PaymentType VARCHAR(255),
  CommissionRate VARCHAR(255),
  Refunded boolean NOT NULL DEFAULT 0,
  FOREIGN KEY (AdvisorID) REFERENCES TravelAdvisor(AdvisorID),
  FOREIGN KEY (BlankNumber) REFERENCES Blank(BlankNumber)
);


CREATE TABLE Sell (
  SaleID INT PRIMARY KEY AUTO_INCREMENT,
  Type VARCHAR(255),
  SellDate DATE,
  PaymentDate DATE NULL,
  AdvisorID INT,
  BlankNumber BIGINT,
  Departure VARCHAR(255),
  FirstLeg VARCHAR(255) NULL,
  SecondLeg VARCHAR(255) NULL,
  ThirdLeg VARCHAR(255) NULL,
  Destination VARCHAR(255),
  Currency VARCHAR(255),
  ExchangeRate FLOAT,
  Discount VARCHAR(255),
  TotalAmount FLOAT,
  PaymentType VARCHAR(255),
  CommissionRate VARCHAR(255),
  Refunded BOOLEAN NOT NULL DEFAULT 0,
  DepartureDate DATE NULL,
  PayLater BOOLEAN NOT NULL DEFAULT 0,
  CustomerEmail VARCHAR(255);
  FOREIGN KEY (AdvisorID) REFERENCES TravelAdvisor(AdvisorID),
  FOREIGN KEY (BlankNumber) REFERENCES Blank(BlankNumber)
);


CREATE TABLE Refund (
    RefundID INT AUTO_INCREMENT PRIMARY KEY,
    RefundAmount FLOAT,
    SaleID INT,
    AdvisorID INT,
    FOREIGN KEY (SaleID) REFERENCES Sell(SaleID),
    FOREIGN KEY (AdvisorID) REFERENCES TravelAdvisor(AdvisorID)
);

CREATE TABLE Commission (
    CommissionID INT PRIMARY KEY AUTO_INCREMENT,
    Type VARCHAR(255) NOT NULL,
    Percentage INT NOT NULL
);

CREATE TABLE Discount (
  DiscountID INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  Type VARCHAR(255) NOT NULL,
  Percentage INT NOT NULL
);

Arthur Daley Administrator LiesaLot 320


INSERT into SystemAdministrator (SystemAdministratorID, Username, Name, Email, TelNumber, Password) VALUES
( 320, 'arda','Arthur Daley', 'sjohnson@yahoo.com', '555555212', LiesaLot),
( 'davlee', 'David Lee', 'david.lee@gmail.com', '555777888', 182816),
('alirod', 'Alicia Rodriguez', 'alicia.rodriguez@gmail.com', '555123567', 182079),
('moah', 'Mohammed Ahmed', 'm.ahmed@gmail.com', '555555555', 656183);







INSERT INTO Blank (BlankNumber, AdvisorAssigned) VALUES
(42000000001,  1),
(44400000002, 1),
(44000000003, 1),
(42000000004, 1),
(44400000005, 2),
(10100000006, 2),
(44400000007, 3),
(44000000008, 2),
(10100000009,  1),
(10100000010,  1),
(10100000011, 1);


INSERT INTO Blank (BlankNumber) VALUES
(42000000020),
(44400000021),
(44000000022),
(42000000023),
(44400000024),
(10100000025),
(44400000026),
(44000000027),
(10100000028),
(10100000029),
(10100000030);

INSERT INTO Blank (BlankNumber) VALUES
(44400000030),
(44400000031),
(44400000032),
(44400000033),
(44400000034),
(44400000035),
(44400000036),
(44400000037),
(44400000038),
(44400000039),
(44400000040);


INSERT into TravelAdvisor (Username, Name, Email, TelNumber, Password) VALUES
('jackwash', 'Jack Washington', 'Jack.Washington@gmail.com', '738267921', 424890),
('fihill', 'Fiona Hill', 'Fiona.Hill@gmail.com' , '726721984', 782362),
('markal', 'Mark Alice', 'Mark@live.co.uk', '786826547', 633370),
('essmi', 'Esther Smith', 'Esther@gmail.com', '776826398', 380003),
('brown', 'Dmitiri Brown', 'Dmitiri.B@yahoo.com', '798145935', 218619),
('jara', 'Jahaad Rashidi', 'Jahaad@yahoo.co.uk', '203682692', 563940);


INSERT INTO OfficeManager (Username, Name, Email, TelNumber, Password) VALUES
('jesswill', 'Jessica Williams', 'jwilliams@gmail.com', '555555212', 106491),
('bran', 'Brandon Nguyen', 'bnguyen@gmail.com', '555666777', 716452);


INSERT INTO Sell (Type, SellDate, PaymentDate, AdvisorID, BlankNumber, Departure, FirstLeg, SecondLeg, ThirdLeg, Destination, Currency, ExchangeRate, Discount, TotalAmount, PaymentType, CommissionRate, Refunded) VALUES
('Domestic', '2023-02-15', NULL , 1, 10100000043, 'New York', 'London', NULL, NULL, 'Paris', 'USD', 1.2, '10%', 1000.00, 'Credit Card', '5%', 0),
('Domestic', '2022-02-20', NULL , 1, 10100000044, 'Las Vegas', NULL, NULL, NULL, 'Los Angeles', 'USD', 1.0, '20%', 500.00, 'Cash', '10%', 0),
('Domestic', '2022-04-10', NULL, 1, 10100000045, 'Chicago', NULL, NULL, NULL, 'Boston', 'USD', 1.1, '5%', 300.00, 'Debit Card', '3%', 1);






INSERT into SystemAdministrator (SystemAdministratorID, Username, Name, Email, TelNumber, Password) VALUES
( 320, 'arda','Arthur Daley', 'sjohnson@yahoo.com', '555555212', 'LiesaLot');


INSERT into OfficeManager (OfficeManagerID, Username, Name, Email, TelNumber, Password) VALUES
( 220, 'minnie','Minnie Minx', 'minnie@yahoo.com', '555555212', 'NotiGirl');


INSERT into TravelAdvisor (AdvisorID, Username, Name, Email, TelNumber, Password) VALUES
( 250, 'pene','Penelope Pitstop', 'pene@yahoo.com', '555555872', 'PinkMobile'),
( 211, 'den','Dennis Menace', 'den@yahoo.com', '555555872', 'Gnasher');