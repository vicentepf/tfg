package es.ujaen.tfg.modelos;

/**
 * Clase que define el modelo de tipo de datos para PosicionGPS(Equipo)
 * 
 * @author Vicente
 *
 */
public class PosicionGPS {
	private int idPosicionGPS;
	private float latitud;
	private float longitud;
	private String descripcion;

	public PosicionGPS() {
		latitud = 0;
		longitud = 0;
	}

	public PosicionGPS(int idPosicionGPS, final float latitud,
			final float longitud, String descripcion) {
		this.idPosicionGPS = idPosicionGPS;
		this.latitud = latitud;
		this.longitud = longitud;
		this.descripcion = descripcion;
	}

	public int getIdPosicionGPS() {
		return idPosicionGPS;
	}

	public void setIdPosicionGPS(int idPosicionGPS) {
		this.idPosicionGPS = idPosicionGPS;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public float getLatitud() {
		return latitud;
	}

	public void setLatitud(float latitud) {
		this.latitud = latitud;
	}

	public void setLongitud(float longitud) {
		this.longitud = longitud;
	}

	public float getLongitud() {
		return longitud;
	}

}
