package es.ujaen.tfg.modelos;

/**
 * Clase para el modelo de datos de Equipo(PosicionGPS)
 * 
 * @author Vicente
 *
 */
public class PosicionGPS {
	private int				idPosicionGPS;
	private final float 	latitud;
	private final float 	longitud;
	private String			descripcion;
	
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

	public float getLongitud() {
		return longitud;
	}

}
