-- MySQL dump 10.13  Distrib 8.0.27, for Linux (x86_64)
--
-- Host: localhost    Database: GH
-- ------------------------------------------------------
-- Server version	8.0.27-0ubuntu0.21.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Conta_esterno`
--

DROP TABLE IF EXISTS `Conta_esterno`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Conta_esterno` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Fusti` int NOT NULL,
  `Foglie` int NOT NULL,
  `Altezza_massima` int NOT NULL,
  `Temperatura` int NOT NULL,
  `Umidita` int NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id` (`Id`,`Data`),
  UNIQUE KEY `Id_2` (`Id`),
  UNIQUE KEY `Id_3` (`Id`),
  UNIQUE KEY `Data` (`Data`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Conta_esterno`
--

LOCK TABLES `Conta_esterno` WRITE;
/*!40000 ALTER TABLE `Conta_esterno` DISABLE KEYS */;
INSERT INTO `Conta_esterno` VALUES (1,'2022-01-01',5,5,4,3,4);
/*!40000 ALTER TABLE `Conta_esterno` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Conta_serra`
--

DROP TABLE IF EXISTS `Conta_serra`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Conta_serra` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Fusti` int NOT NULL,
  `Foglie` int NOT NULL,
  `Altezza_massima` int NOT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Id` (`Id`),
  UNIQUE KEY `Id_2` (`Id`),
  UNIQUE KEY `Id_3` (`Id`),
  UNIQUE KEY `Data` (`Data`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Conta_serra`
--

LOCK TABLES `Conta_serra` WRITE;
/*!40000 ALTER TABLE `Conta_serra` DISABLE KEYS */;
INSERT INTO `Conta_serra` VALUES (46,'2022-01-01',5,5,4);
/*!40000 ALTER TABLE `Conta_serra` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-01-14  7:35:46