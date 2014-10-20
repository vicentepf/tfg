package es.ujaen.tfg.testservice.equipos;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import es.ujaen.tfg.modelos.PosicionGPS;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;
import es.ujaen.tfg.testservice.adaptadoresLista.AdaptadorListaEquipamiento;

/**
 * Clase que maneja la lista de equipos para el supervisor
 * 
 * @author Vicente
 *
 */
public class EquipamientoSupervisor extends ListActivity {
	private int idPosicionGPS;
	private Vector<PosicionGPS> vE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipamiento_supervisor);

		vE = new Vector<PosicionGPS>();
		vE = consultarEquiposSupervisor();

		setListAdapter(new AdaptadorListaEquipamiento(EquipamientoSupervisor.this, vE));
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_equipamiento, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nuevoEquipo:
			lanzarNuevoEquipo(null);
			break;
		case R.id.acercaDe_equipamiento:
			lanzarAcercaDe(null);
			break;
		}
		return true;
	}

	/**
	 * Inicia la actividad de Acerca De
	 * 
	 * @param view
	 */
	private void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	/**
	 * Inicia la actividad de Nuevo Equipo
	 * 
	 * @param view
	 */
	private void lanzarNuevoEquipo(View view) {
		Intent i = new Intent(this, NuevoEquipo.class);
		startActivity(i);
	}

	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		Object o = getListAdapter().getItemId(position);

		idPosicionGPS = vE.elementAt(Integer.parseInt(o.toString())).getIdPosicionGPS();

		Intent i = new Intent(EquipamientoSupervisor.this, EquipoSupervisor.class);
		// Añade contenido extra, el numEmpleado
		i.putExtra("idPosicionGPS", idPosicionGPS);
		startActivity(i);
	}

	/**
	 * Consulta los equipos de la base de datos para el supervisor
	 * 
	 * @return Vector<PosicionGPS>
	 */
	private Vector<PosicionGPS> consultarEquiposSupervisor() {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarPosicionesGPS";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
				SAXParserFactory fabrica = SAXParserFactory.newInstance();
				SAXParser parser = fabrica.newSAXParser();
				XMLReader lector = parser.getXMLReader();

				ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
				lector.setContentHandler(manejadorXML);
				lector.parse(new InputSource(conexion.getInputStream()));

				return manejadorXML.getEquipos();
			} else {
				Log.e("Equipamiento Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Equipamiento Supervisor", e.getMessage(), e);
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
		 * Recupera los equipos de la respuesta del servicio Web
		 * 
		 * @return Vector<PosicionGPS>
		 */
		public Vector<PosicionGPS> getEquipos() {
			PosicionGPS e = new PosicionGPS();
			Vector<PosicionGPS> vE = new Vector<PosicionGPS>();
			
			for (int j = 0; j < lista.size(); ++j) {
				e.setIdPosicionGPS(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				e.setLatitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				e.setLongitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				e.setDescripcion(lista.elementAt(j));

				vE.add(e);
				e = new PosicionGPS();
			}

			return vE;
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
					Log.e("Equipamiento Supervisor", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
