--
-- Base de datos: `serweb_tfg`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE IF NOT EXISTS `empleado` (
  `numEmpleado` int(11) NOT NULL,
  `pass` varchar(40) NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `apellidos` varchar(60) NOT NULL,
  `direccion` varchar(100) NOT NULL,
  `provincia` varchar(40) NOT NULL,
  `email` varchar(50) NOT NULL,
  `tlf` int(9) NOT NULL,
  `dni` varchar(9) NOT NULL,
  `nivel` int(1) NOT NULL,
  PRIMARY KEY (`numEmpleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
