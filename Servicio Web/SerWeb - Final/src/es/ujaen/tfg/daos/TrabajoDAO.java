package es.ujaen.tfg.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.tfg.modelos.Trabajo;

/**
 * Clase para interaccion con el servicio Web y Trabajos
 * 
 * @author Vicente
 *
 */
public class TrabajoDAO {
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
			Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE,
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
            Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Inserta un nuevo trabajo
     * 
     * @param t
     * @return
     */
    public static boolean insertaTrabajo(Trabajo t) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "INSERT INTO trabajo (idTrabajo,idSupervisor,numEmpleado,posicionGPS,provincia,descripcion,estado) VALUES (?,?,?,?,?,?,?)";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, t.getIdTrabajo());
						stmn.setInt(2, t.getIdSupervisor());
						stmn.setInt(3, t.getNumEmpleado());
						stmn.setInt(4, t.getPosicionGPS());
						stmn.setString(5, t.getProvincia());
						stmn.setString(6, t.getDescripcion());
						stmn.setInt(7, t.getEstado());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(TrabajoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Actualiza los datos de un trabajo
     * 
     * @param t
     * @return
     */
    public static boolean actualizaTrabajo(Trabajo t) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "UPDATE trabajo SET idTrabajo=?,idSupervisor=?,numEmpleado=?,posicionGPS=?,provincia=?,descripcion=?,estado=? WHERE idTrabajo=?;";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, t.getIdTrabajo());
						stmn.setInt(2, t.getIdSupervisor());
						stmn.setInt(3, t.getNumEmpleado());
						stmn.setInt(4, t.getPosicionGPS());
						stmn.setString(5, t.getProvincia());
						stmn.setString(6, t.getDescripcion());
						stmn.setInt(7, t.getEstado());
						stmn.setInt(8, t.getIdTrabajo());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(TrabajoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Elimina un trabajo
     * 
     * @param t
     * @return
     */
    public static boolean eliminaTrabajo(int idTrabajo) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "DELETE FROM trabajo WHERE idTrabajo=?";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, idTrabajo);

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(TrabajoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
    
    /**
     * Recupera los atributos despues de una lectura en BD
     *
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve un Trabajo
     */
    public static Trabajo recuperaDatosTrabajo(ResultSet rs) {
    	Trabajo t = new Trabajo();

        try {
        	int idTrabajo = Integer.parseInt(rs.getString("idTrabajo"));
        	int idSupervisor = Integer.parseInt(rs.getString("idSupervisor"));
        	int numEmpleado = Integer.parseInt(rs.getString("numEmpleado"));
        	int posicionGPS = Integer.parseInt(rs.getString("posicionGPS"));
        	String provincia = rs.getString("provincia");
        	String descripcion = rs.getString("descripcion");
        	int estado = Integer.parseInt(rs.getString("estado"));
        	
            t = new Trabajo(idTrabajo,idSupervisor,numEmpleado,posicionGPS,provincia,descripcion,estado);
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return t;
    }

    /**
     * Recuperamos una lectura de 1 Trabajo
     *
     * @param idTrabajo
     * @return Devuelve 1 Trabajo
     */
    public static Trabajo lecturaTrabajoBD(int idTrabajo) {
        Trabajo t = new Trabajo();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM trabajo WHERE idTrabajo=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                    stmn.setInt(1, idTrabajo);
                    try (ResultSet rs = stmn.executeQuery()) {
                        rs.next();
                        t = recuperaDatosTrabajo(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return t;
    }
    
    /**
     * Recupera los atributos despues de una lectura en BD
     *
     * PARA VARIOS TRABAJOS
     *
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve un vector con todos los Trabajos
     */
    public static Vector<Trabajo> recuperaDatosTrabajos(ResultSet rs) {
    	Vector<Trabajo> vT = new Vector<Trabajo>();
    	Trabajo t = new Trabajo();

        try {
        	while (rs.next()) {
        		int idTrabajo = Integer.parseInt(rs.getString("idTrabajo"));
            	int idSupervisor = Integer.parseInt(rs.getString("idSupervisor"));
            	int numEmpleado = Integer.parseInt(rs.getString("numEmpleado"));
            	int posicionGPS = Integer.parseInt(rs.getString("posicionGPS"));
            	String provincia = rs.getString("provincia");
            	String descripcion = rs.getString("descripcion");
            	int estado = Integer.parseInt(rs.getString("estado"));
	
            	t = new Trabajo(idTrabajo,idSupervisor,numEmpleado,posicionGPS,provincia,descripcion,estado);
	            
	            vT.add(t);
        	}
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vT;
    }

    /**
     * Recuperamos una lectura de VARIOS Trabajos para un Empleado
     *
     * @param numEmpleado
     * 
     * @return Devuelve un vector de Trabajos
     */
    public static Vector<Trabajo> lecturaTrabajosEmpleadoBD(int numEmpleado) {
        Vector<Trabajo> vT = new Vector<Trabajo>();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM trabajo WHERE numEmpleado=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                	stmn.setInt(1, numEmpleado);
                    try (ResultSet rs = stmn.executeQuery()) {
                        vT = recuperaDatosTrabajos(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return vT;
    }
    
    /**
     * Recuperamos una lectura de VARIOS Trabajos para un Supervisor
     *
     * @param idSupervisor
     * 
     * @return Devuelve un vector de Trabajos
     */
    public static Vector<Trabajo> lecturaTrabajosSupervisorBD(int idSupervisor) {
        Vector<Trabajo> vT = new Vector<Trabajo>();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM trabajo WHERE idSupervisor=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                	stmn.setInt(1, idSupervisor);
                    try (ResultSet rs = stmn.executeQuery()) {
                        vT = recuperaDatosTrabajos(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return vT;
    }
    
    /**
     * Recuperamos una lectura de VARIOS Trabajos para una determinada Posición GPS
     *
     * @param idPosicionGPS
     * 
     * @return Devuelve un vector de Trabajos
     */
    public static Vector<Trabajo> lecturaTrabajosEquipoBD(int idPosicionGPS) {
        Vector<Trabajo> vT = new Vector<Trabajo>();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM trabajo WHERE posicionGPS=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                	stmn.setInt(1, idPosicionGPS);
                    try (ResultSet rs = stmn.executeQuery()) {
                        vT = recuperaDatosTrabajos(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(TrabajoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return vT;
    }
}
