package es.ujaen.tfg.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.tfg.modelos.PosicionGPS;

/**
 * Clase para interaccion con el servicio Web y Equipos(PosicionGPS)
 * 
 * @author Vicente
 *
 */
public class PosicionGPSDAO {
	private static Connection cnx;

    /**
     * Abre la conexion con la BD
     *
     * @return Devuelve la conexion
     */
	public static Connection openConexion() {
		cnx = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/serweb_tfg?user=root&password=sanchez");
		} catch (SQLException ex) {
			Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnx;
	}

    /**
     * Cierra la conexion abierta previamente
     */
    public static void closeConexion() {
        try {
            cnx.close();
        } catch (SQLException ex) {
            Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Inserta la localización de un nuevo equipo
     * 
     * @param pGPS
     * @return
     */
    public static boolean insertaPosicionGPS(PosicionGPS pGPS) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "INSERT INTO posiciongps (idPosicionGPS,latitud,longitud,descripcion) VALUES (?,?,?,?)";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, pGPS.getIdPosicionGPS());
						stmn.setFloat(2, pGPS.getLatitud());
						stmn.setFloat(3, pGPS.getLongitud());
						stmn.setString(4, pGPS.getDescripcion());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(PosicionGPSDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Actualiza los datos de una posición GPS
     * 
     * @param pGPS
     * @return
     */
    public static boolean actualizaPosicionGPS(PosicionGPS pGPS) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "UPDATE posiciongps SET idPosicionGPS=?, latitud=?, longitud=?, descripcion=? WHERE idPosicionGPS=?";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, pGPS.getIdPosicionGPS());
						stmn.setFloat(2, pGPS.getLatitud());
						stmn.setFloat(3, pGPS.getLongitud());
						stmn.setString(4, pGPS.getDescripcion());
						stmn.setInt(5, pGPS.getIdPosicionGPS());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(PosicionGPSDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Elimina una posición GPS
     * 
     * @param pGPS
     * @return
     */
    public static boolean eliminaPosicionGPS(int idPosicionGPS) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "DELETE FROM posiciongps WHERE idPosicionGPS=?";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, idPosicionGPS);

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(PosicionGPSDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Recupera los atributos despues de una lectura en BD
     *     
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve una Posicion GPS
     */
    public static PosicionGPS recuperaDatosPosicionGPS(ResultSet rs) {
    	PosicionGPS pGPS = new PosicionGPS();

        try {
        	int idPosicionGPS = Integer.parseInt(rs.getString("idPosicionGPS"));
        	float latitud = Float.parseFloat(rs.getString("latitud"));
        	float longitud = Float.parseFloat(rs.getString("longitud"));
        	String descripcion = rs.getString("descripcion");

        	pGPS = new PosicionGPS(idPosicionGPS,latitud,longitud,descripcion);
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return pGPS;
    }

    /**
     * Recuperamos una lectura de Posicion GPS
     *
     * @param idPosicion
     * @return Devuelve una Posicion GPS
     */
    public static PosicionGPS lecturaPosicionGPSBD(int idPosicionGPS) {
        PosicionGPS pGPS = null;
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM posiciongps WHERE idPosicionGPS=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                    stmn.setInt(1, idPosicionGPS);
                    try (ResultSet rs = stmn.executeQuery()) {
                        rs.next();
                        pGPS = recuperaDatosPosicionGPS(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return pGPS;
    }
    
    /**
     * Recupera los atributos despues de una lectura en BD
     *     
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve una Posicion GPS
     */
    public static Vector<PosicionGPS> recuperaDatosPosicionesGPS(ResultSet rs) {
    	Vector<PosicionGPS> vE = new Vector<PosicionGPS>();
    	PosicionGPS e = new PosicionGPS();

        try {
        	while (rs.next()) {
        		int idPosicionGPS = Integer.parseInt(rs.getString("idPosicionGPS"));
            	float latitud = Float.parseFloat(rs.getString("latitud"));
            	float longitud = Float.parseFloat(rs.getString("longitud"));
            	String descripcion = rs.getString("descripcion");
	
            	e = new PosicionGPS(idPosicionGPS, latitud, longitud, descripcion);
	            
	            vE.add(e);
        	}
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vE;
    }
    
    public static Vector<PosicionGPS> lecturaPosicionesGPSBD() {
        Vector<PosicionGPS> vE = new Vector<PosicionGPS>();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM posiciongps";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                    try (ResultSet rs = stmn.executeQuery()) {
                        vE = recuperaDatosPosicionesGPS(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(PosicionGPSDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return vE;
    }
}
