package es.ujaen.tfg.modelos;

/**
 * Clase que define el modelo de tipo de datos para Trabajo
 * 
 * @author Vicente
 *
 */
public class Trabajo {
	private int 		idTrabajo;
	private int			idSupervisor;
	private int 		numEmpleado;	
	private int 		idPosicionGPS;
	private String 		provincia;
	private String 		descripcion;
	private int			estado; // 0 - pendiente  1 - terminado
	
	public Trabajo() {}
	
	public Trabajo(int idTrabajo, int idSupervisor, int numEmpleado, int idPosicionGPS, 
			String provincia, String descripcion, int estado) {
		this.idTrabajo = idTrabajo;
		this.idSupervisor = idSupervisor;
		this.numEmpleado = numEmpleado;
		this.idPosicionGPS = idPosicionGPS;
		this.provincia = provincia;
		this.descripcion = descripcion;
		this.estado = estado;
	}

	public int getIdSupervisor() {
		return idSupervisor;
	}

	public void setIdSupervisor(int idSupervisor) {
		this.idSupervisor = idSupervisor;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public int getIdTrabajo() {
		return idTrabajo;
	}

	public void setIdTrabajo(int idTrabajo) {
		this.idTrabajo = idTrabajo;
	}

	public int getPosicionGPS() {
		return idPosicionGPS;
	}

	public void setPosicionGPS(int idPosicionGPS) {
		this.idPosicionGPS = idPosicionGPS;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getNumEmpleado() {
		return numEmpleado;
	}

	public void setNumEmpleado(int numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

}
