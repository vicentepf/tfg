package es.ujaen.tfg.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.tfg.modelos.Empleado;

/**
 * Clase para interaccion con el servicio Web y Empleados
 * 
 * @author Vicente
 *
 */
public class EmpleadoDAO {
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
			Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE,
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
			Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
	}

	/**
	 * Inserta un nuevo Empleado
	 * 
	 * @param e
	 * @return
	 */
	public static boolean insertaEmpleado(Empleado e) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "INSERT INTO empleado (numEmpleado,pass,nombre,apellidos,direccion,provincia,email,tlf,dni,nivel) VALUES (?,?,?,?,?,?,?,?,?,?)";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, e.getNumEmpleado());
						stmn.setString(2, e.getPass());
						stmn.setString(3, e.getNombre());
						stmn.setString(4, e.getApellidos());
						stmn.setString(5, e.getDireccion());
						stmn.setString(6, e.getProvincia());
						stmn.setString(7, e.getEmail());
						stmn.setInt(8, e.getTlf());
						stmn.setString(9, e.getDni());
						stmn.setInt(10, e.getNivel());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(EmpleadoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
	
	/**
	 * Actualiza los datos del Empleado
	 * 
	 * @param e
	 * @return
	 */
	public static boolean actualizaEmpleado(Empleado e) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "UPDATE empleado SET numEmpleado=?,pass=?,nombre=?,apellidos=?,direccion=?,provincia=?,email=?,tlf=?,dni=?,nivel=? WHERE numEmpleado=?;";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, e.getNumEmpleado());
						stmn.setString(2, e.getPass());
						stmn.setString(3, e.getNombre());
						stmn.setString(4, e.getApellidos());
						stmn.setString(5, e.getDireccion());
						stmn.setString(6, e.getProvincia());
						stmn.setString(7, e.getEmail());
						stmn.setInt(8, e.getTlf());
						stmn.setString(9, e.getDni());
						stmn.setInt(10, e.getNivel());
						stmn.setInt(11, e.getNumEmpleado());

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(EmpleadoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}
	
	/**
	 * Elimina un Empleado
	 * 
	 * @param e
	 * @return Devuelve true o false dependiendo de si ha tenido exito
	 */
	public static boolean eliminaEmpleado(int numEmpleado) {
		boolean salida = false;

		try {
			Connection cx = openConexion();
			if (cx != null) {
				try {
					String qry = "DELETE FROM empleado WHERE numEmpleado=?";

					try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
						stmn.setInt(1, numEmpleado);

						if (stmn.executeUpdate() > 0) {
							salida = true;
						}
					}
					closeConexion();
				} catch (SQLException ex) {
					Logger.getLogger(EmpleadoDAO.class.getName()).log(
							Level.SEVERE, ex.getMessage(), ex);
				}
			}
			return salida;
		} catch (NumberFormatException ex) {
			Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE,
					ex.getMessage(), ex);
		}
		return salida;
	}

	/**
     * Recupera los atributos despues de una lectura en BD
     *
     * PARA 1 EMPLEADO
     *
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve un Empleado
     */
    public static Empleado recuperaDatosEmpleado(ResultSet rs) {
    	Empleado e = new Empleado();

        try {
        	int numEmpleado = Integer.parseInt(rs.getString("numEmpleado"));
        	String pass = rs.getString("pass");
        	String nombre = rs.getString("nombre");
        	String direccion = rs.getString("direccion");
        	String apellidos = rs.getString("apellidos");
        	String provincia = rs.getString("provincia");
        	String email = rs.getString("email");
        	int tlf = Integer.parseInt(rs.getString("tlf"));
        	String dni = rs.getString("dni");
        	int nivel = Integer.parseInt(rs.getString("nivel"));

            e = new Empleado(numEmpleado,pass,nombre,direccion,apellidos,provincia,email,tlf,dni,nivel);
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return e;
    }

    /**
     * Recuperamos una lectura de 1 Empleado
     *
     * @param numEmpleado
     * @return Devuelve 1 Empleado
     */
    public static Empleado lecturaEmpleadoBD(int numEmpleado) {
        Empleado e = null;
        if (openConexion() != null && numEmpleado > 0 && numEmpleado < 2000000) {
            try {
                String qry = "SELECT * FROM empleado WHERE numEmpleado=?";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                    stmn.setInt(1, numEmpleado);
                    try (ResultSet rs = stmn.executeQuery()) {
                        if(rs.next()) {
                        	e = recuperaDatosEmpleado(rs);
                        }
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return e;
    }
    
    /**
     * Recupera los atributos despues de una lectura en BD
     *
     * PARA VARIOS EMPLEADOS
     *
     * @param rs Se le pasa el resultado de la consulta
     * @return Devuelve un vector con todos los Empleados
     */
    public static Vector<Empleado> recuperaDatosEmpleados(ResultSet rs) {
    	Vector<Empleado> vE = new Vector<Empleado>();
    	Empleado e = null;

        try {
        	while (rs.next()) {
	        	int numEmpleado = Integer.parseInt(rs.getString("numEmpleado"));
	        	String pass = rs.getString("pass");
	        	String nombre = rs.getString("nombre");
	        	String direccion = rs.getString("direccion");
	        	String apellidos = rs.getString("apellidos");
	        	String provincia = rs.getString("provincia");
	        	String email = rs.getString("email");
	        	int tlf = Integer.parseInt(rs.getString("tlf"));
	        	String dni = rs.getString("dni");
	        	int nivel = Integer.parseInt(rs.getString("nivel"));
	
	            e = new Empleado(numEmpleado,pass,nombre,direccion,apellidos,provincia,email,tlf,dni,nivel);
	            
	            vE.add(e);
        	}
        } catch (NumberFormatException | SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vE;
    }

    /**
     * Recuperamos una lectura de VARIOS Empleados
     *
     * @param numEmpelado
     * @return Devuelve un vector de Empleados
     */
    public static Vector<Empleado> lecturaEmpleadosBD() {
        Vector<Empleado> vE = new Vector<Empleado>();
        if (openConexion() != null) {
            try {
                String qry = "SELECT * FROM empleado";
                try (PreparedStatement stmn = cnx.prepareStatement(qry)) {
                    try (ResultSet rs = stmn.executeQuery()) {
                        vE = recuperaDatosEmpleados(rs);
                    }
                }
                closeConexion();
            } catch (SQLException ex) {
                Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return vE;
    }
}
