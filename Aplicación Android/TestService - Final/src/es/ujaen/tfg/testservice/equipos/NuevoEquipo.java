package es.ujaen.tfg.testservice.equipos;

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
 * Clase que maneja la actividad de Nuevo equipo
 * 
 * @author Vicente
 *
 */
public class NuevoEquipo extends Activity {
	private Button bInsertar;
	private EditText tVidPosicionGPS;
	private EditText tVlatitud;
	private EditText tVlongitud;
	private EditText tVdescripcion;

	AlertDialog actions;

	private String estadoInsercion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nuevo_equipo);

		bInsertar = (Button) findViewById(R.id.button_nuevo_equipo_insertar_datos);

		tVidPosicionGPS = (EditText) findViewById(R.id.text_nuevo_equipo_id_posiciongps_data);
		tVlatitud = (EditText) findViewById(R.id.text_nuevo_equipo_latitud_data);
		tVlongitud = (EditText) findViewById(R.id.text_nuevo_equipo_longitud_data);
		tVdescripcion = (EditText) findViewById(R.id.text_nuevo_equipo_descripcion_data);

		bInsertar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoInsercion = insertarEquipo(
						Integer.parseInt(tVidPosicionGPS.getText().toString()),
						Float.parseFloat(tVlatitud.getText().toString()),
						Float.parseFloat(tVlongitud.getText().toString()),
						tVdescripcion.getText().toString());
				Toast.makeText(getApplicationContext(), estadoInsercion,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Inserta un equipo en la base de datos
	 * 
	 * @param idPosicionGPS
	 * @param latitud
	 * @param longitud
	 * @param descripcion
	 * 
	 * @return string
	 */
	private String insertarEquipo(int idPosicionGPS, float latitud,
			float longitud, String descripcion) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/nuevaPosicionGPS";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("idPosicionGPS="
					+ (URLEncoder.encode(String.valueOf(idPosicionGPS), "UTF-8")));
			writer.write("&latitud="
					+ (URLEncoder.encode(String.valueOf(latitud), "UTF-8")));
			writer.write("&longitud="
					+ (URLEncoder.encode(String.valueOf(longitud), "UTF-8")));
			writer.write("&descripcion="
					+ (URLEncoder.encode(String.valueOf(descripcion), "UTF-8")));
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
				Log.e("Nuevo Equipo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Nuevo Equipo", e.getMessage(), e);
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
					Log.e("Nuevo Equipo", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
