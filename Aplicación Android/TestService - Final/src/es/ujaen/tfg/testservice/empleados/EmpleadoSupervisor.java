package es.ujaen.tfg.testservice.empleados;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.modelos.Trabajo;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;

/**
 * Clase encargada de la actividad de un empleado para el supervisor
 * 
 * @author Vicente
 *
 */
public class EmpleadoSupervisor extends Activity {
	private Button bActualizar;
	private int numEmpleado;
	private EditText tVnumEmpleado;
	private EditText tVpass;
	private EditText tVnombre;
	private EditText tVapellidos;
	private EditText tVdireccion;
	private EditText tVprovincia;
	private EditText tVemail;
	private TextView tVtlf;
	private EditText tVdni;
	private EditText tVnivel;

	private Empleado empleado;

	AlertDialog actions;

	AlertDialog actionsEliminar;

	private String estadoEliminacionEmpleado;
	private String estadoEliminacionTrabajo;
	private String estadoActualizacion;
	
	Vector<Trabajo> vT = new Vector<Trabajo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empleado_supervisor);

		// Recojo la información Extra recibida
		Bundle extras = getIntent().getExtras();
		numEmpleado = extras.getInt("numEmpleado");

		bActualizar = (Button) findViewById(R.id.button_empleado_supervisor_actualizar_datos);

		tVnumEmpleado = (EditText) findViewById(R.id.text_empleado_supervisor_num_empleado_data);
		tVpass = (EditText) findViewById(R.id.text_empleado_supervisor_pass_data);
		tVnombre = (EditText) findViewById(R.id.text_empleado_supervisor_nombre_data);
		tVapellidos = (EditText) findViewById(R.id.text_empleado_supervisor_apellidos_data);
		tVdireccion = (EditText) findViewById(R.id.text_empleado_supervisor_direccion_data);
		tVprovincia = (EditText) findViewById(R.id.text_empleado_supervisor_provincia_data);
		tVemail = (EditText) findViewById(R.id.text_empleado_supervisor_email_data);
		tVtlf = (EditText) findViewById(R.id.text_empleado_supervisor_tlf_data);
		tVdni = (EditText) findViewById(R.id.text_empleado_supervisor_dni_data);
		tVnivel = (EditText) findViewById(R.id.text_empleado_supervisor_nivel_data);

		empleado = consultarEmpleado(numEmpleado);

		tVnumEmpleado.setText(String.valueOf(empleado.getNumEmpleado()));
		tVpass.setText(empleado.getPass());
		tVnombre.setText(empleado.getNombre());
		tVapellidos.setText(empleado.getApellidos());
		tVdireccion.setText(empleado.getDireccion());
		tVprovincia.setText(empleado.getProvincia());
		tVemail.setText(empleado.getEmail());
		tVtlf.setText(String.valueOf(empleado.getTlf()));
		tVdni.setText(empleado.getDni());
		tVnivel.setText(String.valueOf(empleado.getNivel()));

		bActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoActualizacion = actualizarEmpleado(Integer
						.parseInt(tVnumEmpleado.getText().toString()), tVpass
						.getText().toString(), tVnombre.getText().toString(),
						tVapellidos.getText().toString(), tVdireccion.getText()
								.toString(), tVprovincia.getText().toString(),
						tVemail.getText().toString(), Integer.parseInt(tVtlf
								.getText().toString()), tVdni.getText()
								.toString(), Integer.parseInt(tVnivel.getText()
								.toString()));
				Toast.makeText(getApplicationContext(), estadoActualizacion,
						Toast.LENGTH_SHORT).show();
				EmpleadoSupervisor.this.finish();
			}
		});

		AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
		builderEliminar
				.setTitle("¿ELIMINAR el empleado? Puede acarrear borrado de Trabajos.");
		String[] optionsEliminar = { "SI", "NO" };
		builderEliminar.setItems(optionsEliminar, actionEliminarListener);
		actionsEliminar = builderEliminar.create();
	}

	/**
	 * Actuador del boton eliminar
	 */
	DialogInterface.OnClickListener actionEliminarListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0: // SI
				vT = consultarTrabajosEmpleado(empleado.getNumEmpleado());

				for (int i = 0; i < vT.size(); ++i) {
					estadoEliminacionTrabajo = eliminarTrabajo(vT.elementAt(i).getIdTrabajo());
					Log.d("Trabajo" + vT.elementAt(i).getIdTrabajo() + "eliminado por empleado ",
							estadoEliminacionTrabajo);
				}

				estadoEliminacionEmpleado = eliminarEmpleado(empleado.getNumEmpleado());
				
				Toast.makeText(getApplicationContext(),
						estadoEliminacionEmpleado, Toast.LENGTH_SHORT).show();
				EmpleadoSupervisor.this.finish();
				break;
			case 1: // NO
				break;
			default:
				break;
			}
		}
	};

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_empleado, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.eliminarEmpleado:
			lanzarEliminarEmpleado(null);
			break;
		case R.id.acercaDe_empleado:
			lanzarAcercaDe(null);
			break;
		}
		return true;
	}

	/**
	 * Inicia la actividad AcercaDe
	 * 
	 * @param view
	 */
	private void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	/**
	 * Lanza el Dialog antes de eliminar
	 * 
	 * @param view
	 */
	private void lanzarEliminarEmpleado(View view) {
		actionsEliminar.show();
	}

	/**
	 * Consulta un empleado en la base de datos
	 * 
	 * @param numEmpleado
	 * 
	 * @return Empleado
	 */
	private Empleado consultarEmpleado(int numEmpleado) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarEmpleado";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("numEmpleado="
					+ (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getEmpleado();
			} else {
				Log.e("Empleado Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Empleado Supervisor", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Actualiza los datos de un empleado en la base de datos
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
	 * @return string
	 */
	private String actualizarEmpleado(int numEmpleado, String pass,
			String nombre, String apellidos, String direccion,
			String provincia, String email, int tlf, String dni, int nivel) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/actualizarEmpleado";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("numEmpleado="
					+ (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
			writer.write("&pass="
					+ (URLEncoder.encode(String.valueOf(pass), "UTF-8")));
			writer.write("&nombre="
					+ (URLEncoder.encode(String.valueOf(nombre), "UTF-8")));
			writer.write("&apellidos="
					+ (URLEncoder.encode(String.valueOf(apellidos), "UTF-8")));
			writer.write("&direccion="
					+ (URLEncoder.encode(String.valueOf(direccion), "UTF-8")));
			writer.write("&provincia="
					+ (URLEncoder.encode(String.valueOf(provincia), "UTF-8")));
			writer.write("&email="
					+ (URLEncoder.encode(String.valueOf(email), "UTF-8")));
			writer.write("&tlf="
					+ (URLEncoder.encode(String.valueOf(tlf), "UTF-8")));
			writer.write("&dni="
					+ (URLEncoder.encode(String.valueOf(dni), "UTF-8")));
			writer.write("&nivel="
					+ (URLEncoder.encode(String.valueOf(nivel), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getActualizacion();
			} else {
				Log.e("Actualizar Empleado", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Actualizar Empleado", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Eliminar un empleado en la base de datos
	 * 
	 * @param numEmpleado
	 * 
	 * @return string
	 */
	private String eliminarEmpleado(int numEmpleado) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/eliminarEmpleado";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("numEmpleado="
					+ (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getEliminacion();
			} else {
				Log.e("Eliminar Empleado", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Eliminar Empleado", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Eliminar un trabajo en la base de datos
	 * 
	 * @param idTrabajo
	 * 
	 * @return string
	 */
	private String eliminarTrabajo(int idTrabajo) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/eliminarTrabajo";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("idTrabajo="
					+ (URLEncoder.encode(String.valueOf(idTrabajo), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getEliminacion();
			} else {
				Log.e("Eliminar Trabajo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Eliminar Trabajo", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Consulta los trabajos asignados a un empleado
	 * 
	 * @param numEmpleado
	 * 
	 * @return Vector<Trabajo>
	 */
	private Vector<Trabajo> consultarTrabajosEmpleado(int numEmpleado) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarTrabajosEmpleado";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("numEmpleado="
					+ (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getTrabajos();
			} else {
				Log.e("Empleado Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Empleado Supervisor", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Manejador de respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;

		/**
		 * Recupera un empleado de la respuesta del servicio Web
		 * @return
		 */
		public Empleado getEmpleado() {
			Empleado e = new Empleado();

			for (int j = 0; j < lista.size(); ++j) {
				e.setNumEmpleado(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				e.setPass(lista.elementAt(j));
				j = j + 1;
				e.setNombre(lista.elementAt(j));
				j = j + 1;
				e.setDireccion(lista.elementAt(j));
				j = j + 1;
				e.setApellidos(lista.elementAt(j));
				j = j + 1;
				e.setProvincia(lista.elementAt(j));
				j = j + 1;
				e.setEmail(lista.elementAt(j));
				j = j + 1;
				e.setTlf(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				e.setDni(lista.elementAt(j));
				j = j + 1;
				e.setNivel(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
			}

			return e;
		}

		/**
		 * Recupera un vector de trabajos de la respuesta del servicio Web
		 * 
		 * @return Vector<Trabajo>
		 */
		public Vector<Trabajo> getTrabajos() {
			Trabajo t = new Trabajo();
			Vector<Trabajo> vT = new Vector<Trabajo>();

			for (int j = 0; j < lista.size(); ++j) {
				// Log.d("valor ", lista.elementAt(j));
				t.setIdTrabajo(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				t.setIdSupervisor(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				t.setNumEmpleado(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				t.setPosicionGPS(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				t.setProvincia(lista.elementAt(j));
				j = j + 1;
				t.setDescripcion(lista.elementAt(j));
				j = j + 1;
				t.setEstado(Integer.parseInt(lista.elementAt(j)));

				vT.add(t);
				t = new Trabajo();
			}
			Log.d("Tam vector", String.valueOf(lista.size()));

			return vT;
		}

		/**
		 * Recupera la respuesta del servicio Web por la actualizacion
		 * @return
		 */
		public String getActualizacion() {
			return lista.firstElement();
		}

		/**
		 * Recupera la respuesta del servicio Web por la eliminacion
		 * @return
		 */
		public String getEliminacion() {
			return lista.firstElement();
		}

		public void startDocument() throws SAXException {
			cadena = new StringBuilder();
			lista = new Vector<String>();
		}

		public void characters(char ch[], int comienzo, int longitud) {
			cadena.append(ch, comienzo, longitud);
		}

		public void endElement(String uri, String nombreLocal,
				String nombreCualif) throws SAXException {
			if (nombreLocal.equals("return")) {
				try {
					Log.d("HANDLER ", cadena.toString());
					lista.add(URLDecoder.decode(cadena.toString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e("Empleado Supervisor", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
