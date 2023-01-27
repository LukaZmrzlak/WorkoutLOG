-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 27, 2023 at 05:37 PM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `workoutlog`
--

-- --------------------------------------------------------

--
-- Table structure for table `fitnes`
--

CREATE TABLE `fitnes` (
  `IDfitnes` int(11) NOT NULL,
  `vzdevek` varchar(20) NOT NULL,
  `datum` date NOT NULL,
  `vaja` varchar(30) NOT NULL,
  `seti` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `fitnes`
--

INSERT INTO `fitnes` (`IDfitnes`, `vzdevek`, `datum`, `vaja`, `seti`) VALUES
(6, 'testLogin', '2023-01-23', 'Bench press', '1: 65 kg 12 reps, 2: 70 kg 11 reps, 3: 75 kg 10 reps'),
(7, 'testLogin', '2023-01-22', 'Squat', '1: 80 kg 12 reps, 2: 85 kg 11 reps, 3: 90 kg 10 reps'),
(8, 'testLogin', '2023-01-22', 'Biceps curls', '4: 14 kg 10 reps, 5: 15 kg 9 reps, 6: 15 kg 8 reps'),
(9, 'testLogin', '2023-01-17', 'Bench press', '1: 14 kg 12 reps, 2: 14 kg 12 reps, 3: 14 kg 10 reps'),
(10, '12', '2023-01-22', 'Squat', '1: 80 kg 12 reps, 2: 85 kg 11 reps, 3: 90 kg 10 reps'),
(11, '12', '2023-01-22', 'Squat', '1: 80 kg 12 reps, 2: 85 kg 11 reps, 3: 90 kg 10 reps'),
(12, '12', '2023-01-22', 'Squat', '1: 80 kg 12 reps, 2: 85 kg 11 reps, 3: 90 kg 10 reps'),
(13, 'testLogin', '2023-01-23', 'Dumbell curls', '1: 14 kg 12 reps, 2: 15 kg 11 reps, 3: 15 kg 10 reps'),
(14, 'FinalT', '2023-01-27', 'Pull ups', '1: 75 kg 10 reps, 2: 75 kg 7 reps, 3: 75 kg 6 reps'),
(15, 'FinalT', '2023-01-27', 'Push ups', '4: 75 kg 30 reps, 5: 75 kg 30 reps, 6: 75 kg 30 reps'),
(16, 'testLogin', '2023-01-27', 'Bench press', '1: 70 kg 12 reps, 2: 75 kg 11 reps, 3: 75 kg 10 reps');

-- --------------------------------------------------------

--
-- Table structure for table `nastavitve`
--

CREATE TABLE `nastavitve` (
  `vzdevek` varchar(20) NOT NULL,
  `spol` varchar(10) NOT NULL,
  `starost` int(11) NOT NULL,
  `teza` float NOT NULL,
  `visina` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `nastavitve`
--

INSERT INTO `nastavitve` (`vzdevek`, `spol`, `starost`, `teza`, `visina`) VALUES
('1', 'zenski', 32, 60, 159),
('12', '12', 12, 12, 12),
('FinalT', 'Male', 25, 74, 179),
('STest', 'moski', 30, 87, 190),
('testLogin', 'Moski', 22, 81, 180);

-- --------------------------------------------------------

--
-- Table structure for table `teki`
--

CREATE TABLE `teki` (
  `IDteki` int(11) NOT NULL,
  `vzdevek` varchar(20) NOT NULL,
  `datum` date NOT NULL,
  `casovna_dolzina` time NOT NULL,
  `dolzina` text NOT NULL,
  `obcutek` varchar(20) NOT NULL,
  `vreme` varchar(20) NOT NULL,
  `zapiski` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `teki`
--

INSERT INTO `teki` (`IDteki`, `vzdevek`, `datum`, `casovna_dolzina`, `dolzina`, `obcutek`, `vreme`, `zapiski`) VALUES
(8, 'testLogin', '2023-01-23', '00:34:06', '5km 2m', 'Lahkotno', 'Soncno', 'Prvi tek po enem letu.'),
(9, 'testLogin', '2023-01-23', '00:34:06', '5km 2m', 'Lahkotno', 'Soncno', 'Prvi tek po enem letu.'),
(11, 'testLogin', '2023-01-16', '00:35:07', '5km 5m', '', '', ''),
(12, 'testLogin', '2023-01-16', '00:51:17', '8km 9m', '', '', ''),
(13, 'testLogin', '2022-01-12', '19:20:00', '3,89 km', 'Dobro', 'Sončno', '+1 workout'),
(14, 'testLogin', '2022-01-12', '19:20:00', '3,89 km', 'Dobro', 'Sončno', '+1 workout'),
(15, 'testLogin', '2022-01-12', '19:20:00', '3,89 km', 'Dobro', 'Sončno', '+1 workout'),
(16, 'testLogin', '2022-01-12', '19:20:00', '3,89 km', 'Dobro', 'Sončno', '+1 workout'),
(17, 'FinalT', '2023-01-27', '00:26:11', '5km 7m', 'Fine', 'Overcast', '/'),
(18, 'FinalT', '2023-01-27', '01:17:00', '16km 0m', 'Fine', 'Overcast', '/');

-- --------------------------------------------------------

--
-- Table structure for table `vpisi`
--

CREATE TABLE `vpisi` (
  `vzdevek` varchar(20) NOT NULL,
  `email` varchar(40) NOT NULL,
  `ime` varchar(20) NOT NULL,
  `priimek` varchar(20) NOT NULL,
  `geslo` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16;

--
-- Dumping data for table `vpisi`
--

INSERT INTO `vpisi` (`vzdevek`, `email`, `ime`, `priimek`, `geslo`) VALUES
('1', 'JK@gmail.com', 'Jan', 'Kočevar', '$2y$10$/Z.A7KouQwY5F3oXdOK2wuAJ7KMCo5A6Hbs87Fp8ihyQo4DK6Shqe'),
('12', '12', '12', '12', '$2y$10$AsliaMhZw/uwwIAxW5NY.e60/gVLBAfa9lLUM/j5HDYAC10H.uqdy'),
('123', '1234', '123', '123', '$2y$10$wLt/lpab7P2a5siYuHMwnufygCj6sZ11agBor0nh68ofbdY2AxkXK'),
('1234', '1234', '1234', '1234', '$2y$10$G7lRJ3.C7zc0zJnoZFm7f.GRWk1HXxjLYk2EKzjEZcBDhHEJzr/Se'),
('Ana', 'ana.han@gmail.com', 'Ana', 'Han', '$2y$10$KhZojMigj8rVcCyRv08DMeN339DYXUyaWEUSEd267H8B.w/2WRVhq'),
('FinalT', 'FinalTest@gmail.com', 'Final', 'Test', '$2y$10$bKA68iZgdtgStzaTbYcr/uYYNDw0ZJo7ycoZC0jr87mi64IJ/uDO2'),
('FTest', 'fulltest@gmail.com', 'Full', 'Test', '$2y$10$XIVdm701Nza9oO6fDBsLH.f//Vp0o28818T5ADQSwSZV4ROIP2oQi'),
('Luka', '3', '2', '3', '$2y$10$r6/orHsSODJjP5UeoIR1QubJVsSD78VJZQPqNaYBYYU1HMvXoouDa'),
('Luka Zmrzlak', 'zmrzlak.luka@gmail.com', 'Luka', 'Zmrzlak', '$2y$10$jO.ewiYQa2ZQzfD3Di2Hf.rz1C4Spck9r1FG.gmU1kPtpg27eIZUi'),
('STest', 'STest@gmail.com', 'S', 'Test', '$2y$10$XHJm0xIVOqyjVSD2vieOnunci6cmX2GfdRZnvbR2vGNBhbNzOAAQ6'),
('test', 'testtest@gmail.com', 'test', 'test', '$2y$10$HjDjoMyg.V571KwOU2CdUOuTerWfKJHe/uxxGMsR3kd7HkQUvSZCC'),
('Test2', 'zmrzlak.luka@gmail.com', 'Luka3', 'Zmrzlak', '$2y$10$76gUmGLRn4VlLC0y8RIZC.R903qso5bODWiqr0IRJ2wtxFcdENDeG'),
('Test235435', 'zmrzlak.luka@gmail.com', 'Luka3', 'Zmrzlak', '$2y$10$Ph3ZVJGvuJ48BTfwF8z9kOvTIBiGHYnQvSuiNFyItp/PsmG4jtvFS'),
('TEst4', 'asdads', 'asd', 'asddasd', '$2y$10$Ivp/xyNh9NjgS8QjbvA8veJPEb4aiR1/o3M7V.bt7iyj17/IzOucW'),
('TEst5', 'dassd', 'asdas', 'asdas', '$2y$10$y9hBCmELGkvhjFBAszL7Zu7RA4ZwAKAEOVWsp02z8eSYbrGWn/XPy'),
('testLogin', 'login@gmail.com', 'login', 'dssda', '$2y$10$L.nDgDgoCoSaoeAXq2nYl.63lu53vYxgDn5W8uXMsNuZVLMa8MZ5m');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `fitnes`
--
ALTER TABLE `fitnes`
  ADD PRIMARY KEY (`IDfitnes`),
  ADD KEY `vzdevek` (`vzdevek`);

--
-- Indexes for table `nastavitve`
--
ALTER TABLE `nastavitve`
  ADD PRIMARY KEY (`vzdevek`),
  ADD KEY `vzdevek` (`vzdevek`);

--
-- Indexes for table `teki`
--
ALTER TABLE `teki`
  ADD PRIMARY KEY (`IDteki`),
  ADD KEY `vzdevek` (`vzdevek`);

--
-- Indexes for table `vpisi`
--
ALTER TABLE `vpisi`
  ADD PRIMARY KEY (`vzdevek`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `fitnes`
--
ALTER TABLE `fitnes`
  MODIFY `IDfitnes` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `teki`
--
ALTER TABLE `teki`
  MODIFY `IDteki` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `teki`
--
ALTER TABLE `teki`
  ADD CONSTRAINT `teki_ibfk_1` FOREIGN KEY (`vzdevek`) REFERENCES `vpisi` (`vzdevek`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
