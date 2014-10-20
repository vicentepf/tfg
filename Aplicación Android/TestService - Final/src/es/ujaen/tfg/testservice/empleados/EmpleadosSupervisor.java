package es.ujaen.tfg.testservice.empleados;

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
import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;
import es.ujaen.tfg.testservice.adaptadoresLista.AdaptadorListaEmpleados;

/**
 * Clase que maneja la lista de empleados para el supervisor
 * 
 * @author Vicente
 *
 */
public class EmpleadosSupervisor extends ListActivity {
	private int numEmpleado;
	private Vector<Empleado> vE;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipamiento_supervisor);

		vE = new Vector<Empleado>();
		vE = consultarEmpleadosSupervisor();

		setListAdapter(new AdaptadorListaEmpleados(EmpleadosSupervisor.this, vE));
	}

	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		Object o = getListAdapter().getItemId(position);

		numEmpleado = vE.elementAt(Integer.parseInt(o.toString()))
				.getNumEmpleado();

		Intent i = new Intent(EmpleadosSupervisor.this,
				EmpleadoSupervisor.class);
		// Añade contenido extra, el numEmpleado
		i.putExtra("numEmpleado", numEmpleado);
		startActivity(i);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_empleados, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.nuevoEmpleado:
			lanzarNuevoEmpleado(null);
			break;
		case R.id.acercaDe_empleados:
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
	 * Inicia la actividad de Nuevo Empleado
	 * 
	 * @param view
	 */
	private void lanzarNuevoEmpleado(View view) {
		Intent i = new Intent(this, NuevoEmpleado.class);
		startActivity(i);
	}

	/**
	 * Consulta los empleados en la base de datos para el supervisor
	 * 
	 * @return Vector<Empleado>
	 */
	private Vector<Empleado> consultarEmpleadosSupervisor() {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarEmpleados";
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

				return manejadorXML.getEmpleados();
			} else {
				Log.e("Empleados Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Empleados Supervisor", e.getMessage(), e);
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
		 * Recupera los empleados de la respuesta del servicio Web
		 * 
		 * @return Vector<Empleado>
		 */
		public Vector<Empleado> getEmpleados() {
			Empleado e = new Empleado();
			Vector<Empleado> vE = new Vector<Empleado>();

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

				vE.add(e);
				e = new Empleado();
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
