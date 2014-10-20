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
import es.ujaen.tfg.testservice.R;

/**
 * Clase que maneja la actividad de Nuevo Empleado
 * 
 * @author Vicente
 *
 */
public class NuevoEmpleado extends Activity {
	private Button bInsertar;
	private EditText tVnumEmpleado;
	private EditText tVpass;
	private EditText tVnombre;
	private EditText tVapellidos;
	private EditText tVdireccion;
	private EditText tVprovincia;
	private EditText tVemail;
	private EditText tVtlf;
	private EditText tVdni;
	private EditText tVnivel;

	AlertDialog actions;

	private String estadoInsercion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_empleado);

		bInsertar = (Button) findViewById(R.id.button_nuevo_empleado_insertar_datos);

		tVnumEmpleado = (EditText) findViewById(R.id.text_nuevo_empleado_num_empleado_data);
		tVpass = (EditText) findViewById(R.id.text_nuevo_empleado_pass_data);
		tVnombre = (EditText) findViewById(R.id.text_nuevo_empleado_nombre_data);
		tVapellidos = (EditText) findViewById(R.id.text_nuevo_empleado_apellidos_data);
		tVdireccion = (EditText) findViewById(R.id.text_nuevo_empleado_direccion_data);
		tVprovincia = (EditText) findViewById(R.id.text_nuevo_empleado_provincia_data);
		tVemail = (EditText) findViewById(R.id.text_nuevo_empleado_email_data);
		tVtlf = (EditText) findViewById(R.id.text_nuevo_empleado_tlf_data);
		tVdni = (EditText) findViewById(R.id.text_nuevo_empleado_dni_data);
		tVnivel = (EditText) findViewById(R.id.text_nuevo_empleado_nivel_data);

		bInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoInsercion = insertarEmpleado(
						Integer.parseInt(tVnumEmpleado.getText().toString()),
						tVpass.getText().toString(),
						tVnombre.getText().toString(),
						tVapellidos.getText().toString(),
						tVdireccion.getText().toString(),
						tVprovincia.getText().toString(),
						tVemail.getText().toString(),
						Integer.parseInt(tVtlf.getText().toString()),
						tVdni.getText().toString(),
						Integer.parseInt(tVnivel.getText().toString()));
				Toast.makeText(getApplicationContext(), estadoInsercion,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Inserta un nuevo empleado en la base de datos
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
	private String insertarEmpleado(int numEmpleado, String pass, String nombre, String apellidos,
			String direccion, String provincia, String email, int tlf, String dni, int nivel) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/nuevoEmpleado";
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

				return manejadorXML.getInsercion();
			} else {
				Log.e("Nuevo Empleado", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Nuevo Empleado", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Manejador de las respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;

		/**
		 * Recupera la respuesta del servicio Web por la insercion
		 * 
		 * @return string
		 */
		public String getInsercion() {
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
					lista.add(URLDecoder.decode(cadena.toString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e("Nuevo Empleado", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
