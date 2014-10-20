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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import es.ujaen.tfg.modelos.Trabajo;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;
import es.ujaen.tfg.testservice.adaptadoresLista.AdaptadorListaTrabajos;

/**
 * Clase que maneja la lista de trabajos del supervisor
 * 
 * @author Vicente
 *
 */
public class TrabajosSupervisor extends ListActivity {
	private int numEmpleado;
	private int idTrabajo;
	private Vector<Trabajo> vT;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trabajos_supervisor);

		// Recojo la informaci�n Extra recibida
		Bundle extras = getIntent().getExtras();
		numEmpleado = extras.getInt("numEmpleado");

		vT = new Vector<Trabajo>();
		vT = consultarTrabajosSupervisor(numEmpleado);

		setListAdapter(new AdaptadorListaTrabajos(TrabajosSupervisor.this, vT));
	}

	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		Object o = getListAdapter().getItemId(position);

		idTrabajo = vT.elementAt(Integer.parseInt(o.toString())).getIdTrabajo();

		Intent i = new Intent(TrabajosSupervisor.this, TrabajoSupervisor.class);
		// A�ade contenido extra, el numEmpleado
		i.putExtra("idTrabajo", idTrabajo);
		startActivity(i);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_trabajos_supervisor, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nuevoTrabajo:
			lanzarNuevoTrabajo(null);
			break;
		case R.id.acercaDe_trabajos_supervisor:
			lanzarAcercaDe(null);
			break;
		}
		return true;
	}

	/**
	 * Inicia la actividad Acerca De
	 * @param view
	 */
	private void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	/**
	 * Inicia la actividad de NuevoTrabajo
	 * @param view
	 */
	private void lanzarNuevoTrabajo(View view) {
		Intent i = new Intent(this, NuevoTrabajo.class);
		startActivity(i);
	}

	/**
	 * Consulta los trabajos creados por el Supervisor en la base de datos
	 * 
	 * @param idSupervisor
	 * 
	 * @return Vector<Trabajo>
	 */
	private Vector<Trabajo> consultarTrabajosSupervisor(int idSupervisor) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarTrabajosSupervisor";
			HttpURLConnection conexion = (HttpURLConnection) ((new URL(url)
					.openConnection()));

			conexion.setRequestMethod("POST");
			conexion.setDoOutput(true);

			OutputStream sal = conexion.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					sal, "UTF-8"));

			writer.write("idSupervisor="
					+ (URLEncoder.encode(String.valueOf(idSupervisor), "UTF-8")));
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
				Log.e("Trabajos Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Trabajos Supervisor", e.getMessage(), e);
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
		 * Recupera el vector de trabajos de la respuesta del servicio Web
		 * @return
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
					Log.e("Trabajos Supervisor", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
