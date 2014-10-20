--
-- Base de datos: `serweb_tfg`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `posiciongps`
--

CREATE TABLE IF NOT EXISTS `posiciongps` (
  `idPosicionGPS` int(11) NOT NULL,
  `latitud` float NOT NULL,
  `longitud` float NOT NULL,
  `descripcion` varchar(200) NOT NULL,
  PRIMARY KEY (`idPosicionGPS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
