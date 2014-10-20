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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.testservice.R;

/**
 * Clase encargada del cambio de contraseña del empleado
 * 
 * @author Vicente
 *
 */
public class CambiarPassEmpleado extends Activity {
	private Button bCambiar;
	private EditText eTpass1;
	private EditText eTpass2;

	private int numEmpleado;
	private String pass1 = "", pass2 = "";

	Empleado empleado = new Empleado();

	AlertDialog actions;

	private String estadoCambio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cambiar_pass_empleado);

		// Recojo la información Extra recibida
		Bundle extras = getIntent().getExtras();
		numEmpleado = extras.getInt("numEmpleado");

		bCambiar = (Button) findViewById(R.id.button_cambiar_pass_empleado);

		eTpass1 = (EditText) findViewById(R.id.text_cambiar_pass_empleado_pass1_data);
		eTpass2 = (EditText) findViewById(R.id.text_cambiar_pass_empleado_pass2_data);

		empleado = consultarEmpleado(numEmpleado);

		

		bCambiar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				pass1 = eTpass1.getText().toString();
				pass2 = eTpass2.getText().toString();
				
				if (pass1.equals(pass2)) {
					estadoCambio = actualizarEmpleado(
							empleado.getNumEmpleado(), pass1,
							empleado.getNombre(), empleado.getApellidos(),
							empleado.getDireccion(), empleado.getProvincia(),
							empleado.getEmail(), empleado.getTlf(),
							empleado.getDni(), empleado.getNivel());
					Toast.makeText(getApplicationContext(), estadoCambio,
							Toast.LENGTH_SHORT).show();
					CambiarPassEmpleado.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), "La Pass no coincide", Toast.LENGTH_SHORT).show();
				}
			}
		});
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
				Log.e("Menu Empleado", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Menu Empleado", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Actualiza los datos de un empleado
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
	 * Manejador de respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;

		/**
		 * Recupera el empleado consultado
		 * 
		 * @return Empleado
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
		 * Recupera la respuesta del servicio Web por la actualizacion
		 * 
		 * @return string
		 */
		public String getActualizacion() {
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
					Log.e("Nuevo Equipo", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
