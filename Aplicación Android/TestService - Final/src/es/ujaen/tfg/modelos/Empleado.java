package es.ujaen.tfg.modelos;

/**
 * Clase que define el modelo de datos para Empleado
 * @author Vicente
 *
 */
public class Empleado {
	private int 	numEmpleado;
	private String  pass;
	private String	nombre;
	private String 	apellidos;
	private String  direccion;
	private String	provincia;
	private String	email;
	private int		tlf;
	private String 	dni;
	private int		nivel; // Común-0 o supervisor-1
	
	
	public Empleado() {}
	
	public Empleado(int numEmpleado, String pass, String nombre, 
			String apellidos, String direccion, String provincia, String email, int tlf, String dni, int nivel) 
	{
		this.numEmpleado = numEmpleado;
		this.pass = pass;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.provincia = provincia;
		this.email = email;
		this.tlf = tlf;
		this.dni = dni;
		this.nivel = nivel;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getTlf() {
		return tlf;
	}

	public void setTlf(int tlf) {
		this.tlf = tlf;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public int getNumEmpleado() {
		return numEmpleado;
	}

	public void setNumEmpleado(int numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
}
