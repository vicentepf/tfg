package es.ujaen.tfg.servicios;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.modelos.PosicionGPS;
import es.ujaen.tfg.modelos.Trabajo;

public class SerWeb {

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// EMPLEADO
	// ///////////////////////////////////////////////////////////////////////////////////////////

	public int autentificacion(int numEmpleado, String pass) {
		Empleado e = es.ujaen.tfg.daos.EmpleadoDAO
				.lecturaEmpleadoBD(numEmpleado);

		if (e.getPass().equals(pass) && e.getNivel() == 0) {
			return 0;
		} else if (e.getPass().equals(pass) && e.getNivel() == 1) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * Añade un nuevo empleado
	 * 
	 * @param numEmpleado
	 * @param pass
	 * @param nombre
	 * @param apellidos
	 * @param direccion
	 * @param provincia
	 * @param email
	 * @param tlf
	 * @param dni
	 * @param nivel
	 * @return
	 */
	public String nuevoEmpleado(int numEmpleado, String pass, String nombre,
			String apellidos, String direccion, String provincia, String email,
			int tlf, String dni, int nivel) {
		Empleado e = new Empleado(numEmpleado, pass, nombre, apellidos,
				direccion, provincia, email, tlf, dni, nivel);

		if (es.ujaen.tfg.daos.EmpleadoDAO.insertaEmpleado(e)) {
			return "OK";
		}
		return "ERROR AL INSERTAR";
	}

	/**
	 * Actualiza datos de un Empleado
	 * 
	 * @param numEmpleado
	 * @param pass
	 * @param nombre
	 * @param apellidos
	 * @param direccion
	 * @param provincia
	 * @param email
	 * @param tlf
	 * @param dni
	 * @param nivel
	 * 
	 * @return "OK" o "ERROR EN ACTUALIZACION"
	 */
	public String actualizarEmpleado(int numEmpleado, String pass,
			String nombre, String apellidos, String direccion,
			String provincia, String email, int tlf, String dni, int nivel) {
		Empleado e = new Empleado(numEmpleado, pass, nombre, apellidos,
				direccion, provincia, email, tlf, dni, nivel);

		if (es.ujaen.tfg.daos.EmpleadoDAO.actualizaEmpleado(e))
			return "OK";
		return "ERROR EN ACTUALIZACION";
	}

	/**
	 * Eliminar un Empleado
	 * 
	 * @param numEmpleado
	 * @return "OK" o "ERROR EN ELIMINACION"
	 */
	public String eliminarEmpleado(int numEmpleado) {
		if (es.ujaen.tfg.daos.EmpleadoDAO.eliminaEmpleado(numEmpleado))
			return "OK";
		return "ERROR EN ELIMINACION";
	}

	/**
	 * Consulta un empleado por su numero de empleado
	 * 
	 * @param numEmpleado
	 * @return
	 */
	public List<String> consultarEmpleado(int numEmpleado) {
		Empleado e = es.ujaen.tfg.daos.EmpleadoDAO
				.lecturaEmpleadoBD(numEmpleado);
		List<String> lista = new Vector<String>();

		lista.add(Integer.toString(e.getNumEmpleado()));
		lista.add(e.getPass());
		lista.add(e.getNombre());
		lista.add(e.getApellidos());
		lista.add(e.getDireccion());
		lista.add(e.getProvincia());
		lista.add(e.getEmail());
		lista.add(Integer.toString(e.getTlf()));
		lista.add(e.getDni());
		lista.add(Integer.toString(e.getNivel()));
		
		return lista;
	}

	/**
	 * Conculta todos los Empleados
	 * 
	 * @return
	 */
	public List<String> consultarEmpleados() {
		Vector<Empleado> vE = es.ujaen.tfg.daos.EmpleadoDAO
				.lecturaEmpleadosBD();
		Iterator<Empleado> i = vE.iterator();
		Empleado e = new Empleado();
		List<String> lista = new Vector<String>();

		if (!vE.isEmpty()) {
			while (i.hasNext()) {
				e = i.next();
				lista.add(Integer.toString(e.getNumEmpleado()));
				lista.add(e.getPass());
				lista.add(e.getNombre());
				lista.add(e.getApellidos());
				lista.add(e.getDireccion());
				lista.add(e.getProvincia());
				lista.add(e.getEmail());
				lista.add(Integer.toString(e.getTlf()));
				lista.add(e.getDni());
				lista.add(Integer.toString(e.getNivel()));
			}
		}
		return lista;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// POSICION GPS -- EQUIPAMIENTO
	// ///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Inserta la localización de un nuevo equipo
	 * 
	 * @param idPosicionGPS
	 * @param latitud
	 * @param longitud
	 * @param descripcion
	 * 
	 * @return "OK" o "ERROR AL INSERTAR POSICION GPS"
	 */
	public String nuevaPosicionGPS(int idPosicionGPS, float latitud,
			float longitud, String descripcion) {
		PosicionGPS pGPS = new PosicionGPS(idPosicionGPS, latitud, longitud,
				descripcion);

		if (es.ujaen.tfg.daos.PosicionGPSDAO.insertaPosicionGPS(pGPS)) {
			return "OK";
		}
		return "ERROR AL INSERTAR";
	}

	/**
	 * Actualiza datos de una Posicion GPS
	 * 
	 * @param idPosicionGPS
	 * @param latitud
	 * @param longitud
	 * @param descripcion
	 * 
	 * @return "OK" o "ERROR EN ACTUALIZACION"
	 */
	public String actualizarPosicionGPS(int idPosicionGPS, float latitud,
			float longitud, String descripcion) {
		PosicionGPS pGPS = new PosicionGPS(idPosicionGPS, latitud, longitud,
				descripcion);

		if (es.ujaen.tfg.daos.PosicionGPSDAO.actualizaPosicionGPS(pGPS))
			return "OK";
		return "ERROR EN ACTUALIZACION";
	}

	/**
	 * Eliminar una Posicion GPS
	 * 
	 * @param numEmpleado
	 * 
	 * @return "OK" o "ERROR EN ELIMINACION"
	 */
	public String eliminarPosicionGPS(int idPosicionGPS) {
		if (es.ujaen.tfg.daos.PosicionGPSDAO.eliminaPosicionGPS(idPosicionGPS))
			return "OK";
		return "ERROR EN ELIMINACION";
	}

	/**
	 * Consulta una Posicion GPS
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return
	 */
	public List<String> consultarPosicionGPS(int idPosicionGPS) {
		PosicionGPS pGPS = es.ujaen.tfg.daos.PosicionGPSDAO
				.lecturaPosicionGPSBD(idPosicionGPS);
		List<String> lista = new Vector<String>();

		lista.add(Integer.toString(pGPS.getIdPosicionGPS()));
		lista.add(Float.toString(pGPS.getLatitud()));
		lista.add(Float.toString(pGPS.getLongitud()));
		lista.add(pGPS.getDescripcion());

		return lista;
	}
	
	public List<String> consultarPosicionesGPS() {
		Vector<PosicionGPS> vE = es.ujaen.tfg.daos.PosicionGPSDAO.lecturaPosicionesGPSBD();
		Iterator<PosicionGPS> i = vE.iterator();
		PosicionGPS e = new PosicionGPS();
		List<String> lista = new Vector<String>();

		if (!vE.isEmpty()) {
			while (i.hasNext()) {
				e = i.next();
				lista.add(Integer.toString(e.getIdPosicionGPS()));
				lista.add(Float.toString(e.getLatitud()));
				lista.add(Float.toString(e.getLongitud()));
				lista.add(e.getDescripcion());
			}
		}
		return lista;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////
	// TRABAJO
	// ///////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Inserta un nuevo Trabajo
	 * 
	 * @param idTrabajo
	 * @param idSupervisor
	 * @param numEmpleado
	 * @param posicionGPS
	 * @param provincia
	 * @param descripcion
	 * @param estado
	 * 
	 * @return "OK" o "ERROR AL INSERTAR"
	 */
	public String nuevoTrabajo(int idTrabajo, int idSupervisor,
			int numEmpleado, int posicionGPS, String provincia,
			String descripcion, int estado) {
		Trabajo t = new Trabajo(idTrabajo, idSupervisor, numEmpleado,
				posicionGPS, provincia, descripcion, estado);

		if (es.ujaen.tfg.daos.TrabajoDAO.insertaTrabajo(t))
			return "OK";
		return "ERROR AL INSERTAR";
	}

	/**
	 * Actualiza los datos de un Trabajo
	 * 
	 * @param idTrabajo
	 * @param idSupervisor
	 * @param numEmpleado
	 * @param posicionGPS
	 * @param provincia
	 * @param descripcion
	 * @param estado
	 * 
	 * @return "OK" o "ERROR EN ACTUALIZACION"
	 */
	public String actualizarTrabajo(int idTrabajo, int idSupervisor,
			int numEmpleado, int posicionGPS, String provincia,
			String descripcion, int estado) {
		Trabajo t = new Trabajo(idTrabajo, idSupervisor, numEmpleado,
				posicionGPS, provincia, descripcion, estado);

		if (es.ujaen.tfg.daos.TrabajoDAO.actualizaTrabajo(t))
			return "OK";
		return "ERROR EN ACTUALIZACION";
	}

	/**
	 * Elimina un Trabajo
	 * 
	 * @param idTrabajo
	 * 
	 * @return "OK" o "ERROR EN ELIMINACION"
	 */
	public String eliminarTrabajo(int idTrabajo) {
		if (es.ujaen.tfg.daos.TrabajoDAO.eliminaTrabajo(idTrabajo))
			return "OK";
		return "ERROR EN ELIMINACION";
	}

	/**
	 * Consulta un Trabajo
	 * 
	 * @param idTrabajo
	 * 
	 * @return "OK" o "NO EXISTE"
	 */
	public List<String> consultarTrabajo(int idTrabajo) {
		Trabajo t = es.ujaen.tfg.daos.TrabajoDAO.lecturaTrabajoBD(idTrabajo);
		List<String> lista = new Vector<String>();

		lista.add(Integer.toString(t.getIdTrabajo()));
		lista.add(Integer.toString(t.getIdSupervisor()));
		lista.add(Integer.toString(t.getNumEmpleado()));
		lista.add(Integer.toString(t.getPosicionGPS()));
		lista.add(t.getProvincia());
		lista.add(t.getDescripcion());
		lista.add(Integer.toString(t.getEstado()));

		return lista;
	}

	/**
	 * Consulta todos los Trabajos de un Empleado
	 * 
	 * @param numEmpleado
	 * 
	 * @return Lista con todos los trabajos creados por un Empleado
	 */
	public List<String> consultarTrabajosEmpleado(int numEmpleado) {
		Vector<Trabajo> vT = es.ujaen.tfg.daos.TrabajoDAO
				.lecturaTrabajosEmpleadoBD(numEmpleado);
		Iterator<Trabajo> i = vT.iterator();
		Trabajo t = new Trabajo();
		List<String> lista = new Vector<String>();

		if (!vT.isEmpty()) {
			while (i.hasNext()) {
				t = i.next();
				lista.add(Integer.toString(t.getIdTrabajo()));
				lista.add(Integer.toString(t.getIdSupervisor()));
				lista.add(Integer.toString(t.getNumEmpleado()));
				lista.add(Integer.toString(t.getPosicionGPS()));
				lista.add(t.getProvincia());
				lista.add(t.getDescripcion());
				lista.add(Integer.toString(t.getEstado()));
			}
		}
		return lista;
	}

	/**
	 * Consulta todos los Trabajos de un Supervisor
	 * 
	 * @param idSupervisor
	 * 
	 * @return Lista con todos los trabajos creados por un Supervisor
	 */
	public List<String> consultarTrabajosSupervisor(int idSupervisor) {
		Vector<Trabajo> vT = es.ujaen.tfg.daos.TrabajoDAO
				.lecturaTrabajosSupervisorBD(idSupervisor);
		Iterator<Trabajo> i = vT.iterator();
		Trabajo t = new Trabajo();
		List<String> lista = new Vector<String>();

		if (!vT.isEmpty()) {
			while (i.hasNext()) {
				t = i.next();
				lista.add(Integer.toString(t.getIdTrabajo()));
				lista.add(Integer.toString(t.getIdSupervisor()));
				lista.add(Integer.toString(t.getNumEmpleado()));
				lista.add(Integer.toString(t.getPosicionGPS()));
				lista.add(t.getProvincia());
				lista.add(t.getDescripcion());
				lista.add(Integer.toString(t.getEstado()));
			}
		}
		return lista;
	}
	
	/**
	 * Consulta todos los Trabajos de una determinada Posición GPS
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return Lista con todos los trabajos de una misma Posición GPS
	 */
	public List<String> consultarTrabajosEquipo(int idPosicionGPS) {
		Vector<Trabajo> vT = es.ujaen.tfg.daos.TrabajoDAO
				.lecturaTrabajosEquipoBD(idPosicionGPS);
		Iterator<Trabajo> i = vT.iterator();
		Trabajo t = new Trabajo();
		List<String> lista = new Vector<String>();

		if (!vT.isEmpty()) {
			while (i.hasNext()) {
				t = i.next();
				lista.add(Integer.toString(t.getIdTrabajo()));
				lista.add(Integer.toString(t.getIdSupervisor()));
				lista.add(Integer.toString(t.getNumEmpleado()));
				lista.add(Integer.toString(t.getPosicionGPS()));
				lista.add(t.getProvincia());
				lista.add(t.getDescripcion());
				lista.add(Integer.toString(t.getEstado()));
			}
		}
		return lista;
	}
}
