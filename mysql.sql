CREATE DATABASE `jdbc` /*!40100 DEFAULT CHARACTER SET utf8 */;
use jdbc;
CREATE TABLE `client` (
  `username` varchar(200) NOT NULL,
  `password` varchar(200) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
