-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 26, 2017 at 05:08 PM
-- Server version: 5.5.41
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lhoug001_bitchat`
--

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `admin_id` int(10) NOT NULL,
  `admin_email` varchar(255) NOT NULL,
  `admin_pass` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`admin_id`, `admin_email`, `admin_pass`) VALUES
(1, 'test', 'test');

-- --------------------------------------------------------

--
-- Table structure for table `markers`
--

CREATE TABLE IF NOT EXISTS `markers` (
  `id` int(11) NOT NULL,
  `name` varchar(60) NOT NULL,
  `marker_con` text NOT NULL,
  `lat` float(10,6) NOT NULL,
  `lng` float(10,6) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `markers`
--

INSERT INTO `markers` (`id`, `name`, `marker_con`, `lat`, `lng`) VALUES
(42, 'PSH', 'This is the PSH chatroom!', 51.472675, -0.037050),
(43, 'DOC', 'This is the DOC chatroom!', 51.473728, -0.038573),
(41, 'GREEN', 'This is the GREEN chatroom!', 0.000000, 0.000000),
(40, 'LIB', 'This is the LIB chatroom!', 51.474987, -0.035977),
(39, 'IGLT', 'This is the IGLT chatroom!', 51.473698, -0.037050),
(38, 'RHB', 'This is the RHB chatroom!', 51.474075, -0.035966);

-- --------------------------------------------------------

--
-- Table structure for table `messages`
--

CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` int(11) NOT NULL,
  `message_content` text NOT NULL,
  `message_loc` text NOT NULL,
  `message_long` decimal(9,6) NOT NULL,
  `message_lat` decimal(9,6) NOT NULL,
  `message_sender` text NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=477 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `messages`
--

INSERT INTO `messages` (`message_id`, `message_content`, `message_loc`, `message_long`, `message_lat`, `message_sender`, `time`) VALUES
(471, 'Hey you guys!!!', 'RHB', '-0.038573', '51.473728', 'Luke Houghton', '2017-04-26 05:40:26'),
(472, 'Hello this is a test', 'DOC', '-0.038573', '51.473728', 'Luke Houghton', '2017-04-26 05:41:15'),
(473, 'The green  ', 'GREEN', '-0.036459', '51.473293', 'Luke Houghton', '2017-04-26 05:42:06'),
(474, 'where is everyone', 'PSH', '-0.035966', '51.474077', 'Luke Houghton', '2017-04-26 05:42:46'),
(475, 'where is everyone', 'PSH', '-0.035966', '51.474077', 'Luke Houghton', '2017-04-26 05:42:50'),
(476, 'Library message', 'LIB', '-0.035977', '51.474986', 'Luke Houghton', '2017-04-26 05:43:28');

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE IF NOT EXISTS `reports` (
  `report_id` int(11) NOT NULL,
  `report_post` int(11) NOT NULL,
  `report_content` varchar(255) NOT NULL,
  `report_loc` varchar(255) NOT NULL,
  `report_sender` varchar(255) NOT NULL,
  `report_reporter` varchar(255) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `reports`
--

INSERT INTO `reports` (`report_id`, `report_post`, `report_content`, `report_loc`, `report_sender`, `report_reporter`) VALUES
(1, 41, 'hello !', 'PSH', 'Rohan', 'Luke'),
(3, 48, 'Hi all', 'DOC', 'Luke', 'Tim'),
(4, 52, 'Hey ', 'Cinema', 'Nauman', 'Jordan');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL,
  `username` varchar(60) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `user_img` text NOT NULL,
  `course` text NOT NULL,
  `Activated` tinyint(1) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `user_img`, `course`, `Activated`) VALUES
(2, 'Luke Houghton', 'Bizibit1515', 'lhoug001@gold.ac.uk', '5.png', '', 1),
(7, 'Chenda', 'Chenda123', 'c@gold.ac.uk', '6.png', '', 1),
(9, 'Jordan', 'Jordan123', 'j@gold.ac.uk', '2.png', '', 1),
(177, 'cbunk001', '123456', 'cbunk001@gold.ac.uk', '', '', 1),
(178, 'adenn001', 'bob', 'adenn001@gold.ac.uk', '', '', 1),
(181, 'ajaun001', 'IndustrialScale##9800', 'ajaun001@gold.ac.uk', '', '', 1),
(182, 'achar051', 'tesy', 'achar051@gold.ac.uk', '', '', 1),
(185, 'athay002', 'Goldsmiths12', 'athay002@gold.ac.uk', '', '', 1),
(190, 'tchri001', 'ttt123', 'tchri001@gold.ac.uk', '', '', 1),
(197, 'achar@go', 'thisis', 'achar@gold.ac.uk', '', '', 0),
(205, 'niqba002', 'naum81', 'niqba002@gold.ac.uk', '', '', 1),
(206, 'qwerty@g', 'qwertyuiop', 'qwerty@gold.ac.uk', '', '', 0),
(210, 'rcorb001', 'password', 'rcorb001@gold.ac.uk', '', '', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`admin_id`);

--
-- Indexes for table `markers`
--
ALTER TABLE `markers`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`message_id`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`report_id`),
  ADD UNIQUE KEY `report_content` (`report_content`),
  ADD UNIQUE KEY `report_loc` (`report_loc`),
  ADD UNIQUE KEY `report_loc_2` (`report_loc`),
  ADD UNIQUE KEY `report_loc_3` (`report_loc`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `admin`
--
ALTER TABLE `admin`
  MODIFY `admin_id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT for table `markers`
--
ALTER TABLE `markers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=44;
--
-- AUTO_INCREMENT for table `messages`
--
ALTER TABLE `messages`
  MODIFY `message_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=477;
--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=211;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
