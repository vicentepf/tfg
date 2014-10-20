INSERT INTO `serweb_tfg`.`empleado` (`numEmpleado`, `pass`, `nombre`, `apellidos`, `direccion`, `provincia`, `email`, `tlf`, `dni`, `nivel`) VALUES ('1', '1234', 'Vicente', 'Plaza Franco', 'Cincel 14', 'Jaén', 'vicenteplazafranco@gmail.com', '670342915', '77359173F', '1');
INSERT INTO `serweb_tfg`.`empleado` (`numEmpleado`, `pass`, `nombre`, `apellidos`, `direccion`, `provincia`, `email`, `tlf`, `dni`, `nivel`) VALUES ('2', '1234', 'Yolanda', 'Lopez Lopez', 'Castilla 3', 'Jaén', 'yolanda@algo.com', '666666666', '12345678G', '0');


INSERT INTO `serweb_tfg`.`posiciongps` (`idPosicionGPS`, `latitud`, `longitud`, `descripcion`) VALUES ('1', '36.280182', '-6.091452', 'Antena GPRS Conil');
INSERT INTO `serweb_tfg`.`posiciongps` (`idPosicionGPS`, `latitud`, `longitud`, `descripcion`) VALUES ('2', '37.785646', '-3.795013', 'Antena SMS Jaén');
INSERT INTO `serweb_tfg`.`posiciongps` (`idPosicionGPS`, `latitud`, `longitud`, `descripcion`) VALUES ('3', '37.791005', '-3.773298', 'Estación de servicio 254 Jaén');

INSERT INTO `serweb_tfg`.`trabajo` (`idTrabajo`, `idSupervisor`, `numEmpleado`, `posicionGPS`, `provincia`, `descripcion`, `estado`) VALUES ('1', '1', '2', '1', 'Cádiz', 'Avería alimentación', '0');
INSERT INTO `serweb_tfg`.`trabajo` (`idTrabajo`, `idSupervisor`, `numEmpleado`, `posicionGPS`, `provincia`, `descripcion`, `estado`) VALUES ('2', '1', '2', '2', 'Jaén', 'Sustitución ventilador', '0');
INSERT INTO `serweb_tfg`.`trabajo` (`idTrabajo`, `idSupervisor`, `numEmpleado`, `posicionGPS`, `provincia`, `descripcion`, `estado`) VALUES ('3', '1', '2', '3', 'Jaén', 'Sustituir cableado', '0');