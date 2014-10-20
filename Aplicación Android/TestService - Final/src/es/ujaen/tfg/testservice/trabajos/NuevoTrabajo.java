package es.ujaen.tfg.testservice.trabajos;

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
 * Clase que maneja la actividad de un Nuevo Trabajo
 * 
 * @author Vicente
 *
 */
public class NuevoTrabajo extends Activity {
	private Button bInsertar;
	private EditText tVidTrabajo;
	private EditText tVidSupervisor;
	private EditText tVnumEmpleado;
	private EditText tVidPosicionGPS;
	private EditText tVprovincia;
	private EditText tVdescripcion;
	private EditText tVestado;

	AlertDialog actions;

	private String estadoInsercion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_trabajo);

		bInsertar = (Button) findViewById(R.id.button_nuevo_trabajo_insertar_datos);

		tVidTrabajo = (EditText) findViewById(R.id.text_nuevo_trabajo_id_trabajo_data);
		tVidSupervisor = (EditText) findViewById(R.id.text_nuevo_trabajo_id_supervisor_data);
		tVnumEmpleado = (EditText) findViewById(R.id.text_nuevo_trabajo_num_empleado_data);
		tVidPosicionGPS = (EditText) findViewById(R.id.text_nuevo_trabajo_id_posiciongps_data);
		tVprovincia = (EditText) findViewById(R.id.text_nuevo_trabajo_provincia_data);
		tVdescripcion = (EditText) findViewById(R.id.text_nuevo_trabajo_descripcion_data);
		tVestado = (EditText) findViewById(R.id.text_nuevo_trabajo_estado_data);

		bInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoInsercion = insertarTrabajo(
						Integer.parseInt(tVidTrabajo.getText().toString()),
						Integer.parseInt(tVidSupervisor.getText().toString()),
						Integer.parseInt(tVnumEmpleado.getText().toString()),
						Integer.parseInt(tVidPosicionGPS.getText().toString()),
						tVprovincia.getText().toString(),
						tVdescripcion.getText().toString(),
						Integer.parseInt(tVestado.getText().toString()));
				Toast.makeText(getApplicationContext(), estadoInsercion,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Inserta un trabajo en la base de datos
	 * 
	 * @param idTrabajo
	 * @param idSupervisor
	 * @param numEmpleado
	 * @param idPosicionGPS
	 * @param provincia
	 * @param descripcion
	 * @param estado
	 * 
	 * @return string
	 */
	private String insertarTrabajo(int idTrabajo, int idSupervisor, int numEmpleado,
			int idPosicionGPS, String provincia, String descripcion, int estado) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/nuevoTrabajo";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("idTrabajo="
					+ (URLEncoder.encode(String.valueOf(idTrabajo), "UTF-8")));
			writer.write("&idSupervisor="
					+ (URLEncoder.encode(String.valueOf(idSupervisor), "UTF-8")));
			writer.write("&numEmpleado="
					+ (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
			writer.write("&posicionGPS="
					+ (URLEncoder.encode(String.valueOf(idPosicionGPS), "UTF-8")));
			writer.write("&provincia="
					+ (URLEncoder.encode(String.valueOf(provincia), "UTF-8")));
			writer.write("&descripcion="
					+ (URLEncoder.encode(String.valueOf(descripcion), "UTF-8")));
			writer.write("&estado="
					+ (URLEncoder.encode(String.valueOf(estado), "UTF-8")));			
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
				Log.e("Nuevo Trabajo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Nuevo Trabajo", e.getMessage(), e);
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
					Log.e("Nuevo Trabajo", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}

