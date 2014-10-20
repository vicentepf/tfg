--
-- Base de datos: `serweb_tfg`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `trabajo`
--

CREATE TABLE IF NOT EXISTS `trabajo` (
  `idTrabajo` int(11) NOT NULL,
  `idSupervisor` int(11) NOT NULL,
  `numEmpleado` int(11) NOT NULL,
  `posicionGPS` int(11) NOT NULL,  
  `provincia` varchar(40) NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  `estado` int(1) NOT NULL,  
  PRIMARY KEY (`idTrabajo`),
  FOREIGN KEY (`posicionGPS`) REFERENCES posiciongps(`idPosicionGPS`),
  FOREIGN KEY (`idSupervisor`) REFERENCES empleado(`numEmpleado`),
  FOREIGN KEY (`numEmpleado`) REFERENCES empleado(`numEmpleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

