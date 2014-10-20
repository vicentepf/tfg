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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import es.ujaen.tfg.modelos.PosicionGPS;
import es.ujaen.tfg.testservice.R;

/**
 * Clase encargada del mapa de Google Maps
 * 
 * @author Vicente
 *
 */
public class GoogleMapsActivity extends FragmentActivity {
	private GoogleMap map;
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */
	private PosicionGPS pGPS;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		// Recojo la información Extra recibida
		Bundle extras = getIntent().getExtras();
		int idPosicionGPS = extras.getInt("idPosicionGPS");

		pGPS = consultarPosicionGPS(idPosicionGPS);
		LatLng posicion = new LatLng(pGPS.getLatitud(), pGPS.getLongitud());
		
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		if (map != null) {
			Marker marcador = map.addMarker(new MarkerOptions()
					.position(posicion)
					.title(pGPS.getDescripcion()));
		}

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 15));

		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Consulta el Equipo(PosicionGPS)
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return PosicionGPS
	 */
	private PosicionGPS consultarPosicionGPS(int idPosicionGPS) {
		try {
			 String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarPosicionGPS";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("idPosicionGPS="
					+ (URLEncoder.encode(String.valueOf(idPosicionGPS), "UTF-8")));
			writer.close();

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getPosicionGPS();
			} else {
				Log.e("Trabajo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Trabajo", e.getMessage(), e);
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
		 * Recupera la PosicionGPS de la respuesta del servicio Web
		 * @return
		 */
		public PosicionGPS getPosicionGPS() {
			PosicionGPS pGPS = new PosicionGPS();

			for (int j = 0; j < lista.size(); ++j) {
				pGPS.setIdPosicionGPS(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				pGPS.setLatitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				pGPS.setLongitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				pGPS.setDescripcion(lista.elementAt(j));
				j = j + 1;
			}

			return pGPS;
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
					Log.e("Trabajo", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
