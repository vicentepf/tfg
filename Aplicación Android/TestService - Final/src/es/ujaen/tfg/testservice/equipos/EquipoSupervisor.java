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
import android.widget.Toast;
import es.ujaen.tfg.modelos.PosicionGPS;
import es.ujaen.tfg.modelos.Trabajo;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;

/**
 * Clase que maneja la actividad de un Equipo para el supervisor
 * 
 * @author Vicente
 *
 */
public class EquipoSupervisor extends Activity {
	private Button bActualizar;
	private Button bPosicionGPS;
	private int idPosicionGPS;
	private EditText tVidPosicionGPS;
	private EditText tVlatitud;
	private EditText tVlongitud;
	private EditText tVdescripcion;

	private PosicionGPS pGPS;

	AlertDialog actionsEliminar;

	private String estadoEliminacionEquipo;
	private String estadoEliminacionTrabajo;
	private String estadoActualizacion;
	
	Vector<Trabajo> vT = new Vector<Trabajo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equipo_supervisor);

		// Recojo la información Extra recibida
		Bundle extras = getIntent().getExtras();
		idPosicionGPS = extras.getInt("idPosicionGPS");

		bPosicionGPS = (Button) findViewById(R.id.button_equipo_supervisor_posiciongps);
		bActualizar = (Button) findViewById(R.id.button_equipo_supervisor_actualizar_datos);

		tVidPosicionGPS = (EditText) findViewById(R.id.text_equipo_supervisor_id_posiciongps_data);
		tVlatitud = (EditText) findViewById(R.id.text_equipo_supervisor_latitud_data);
		tVlongitud = (EditText) findViewById(R.id.text_equipo_supervisor_longitud_data);
		tVdescripcion = (EditText) findViewById(R.id.text_equipo_supervisor_descripcion_data);

		pGPS = consultarEquipo(idPosicionGPS);

		tVidPosicionGPS.setText(String.valueOf(pGPS.getIdPosicionGPS()));
		tVlatitud.setText(String.valueOf(pGPS.getLatitud()));
		tVlongitud.setText(String.valueOf(pGPS.getLongitud()));
		tVdescripcion.setText(String.valueOf(pGPS.getDescripcion()));

		bPosicionGPS.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(EquipoSupervisor.this, GoogleMapsActivity.class);
				// Añade contenido extra, el numEmpleado
				i.putExtra("idPosicionGPS", pGPS.getIdPosicionGPS());
				startActivity(i);
			}
		});

		bActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoActualizacion = actualizarEquipo(
						Integer.parseInt(tVidPosicionGPS.getText().toString()),
						Float.parseFloat(tVlatitud.getText().toString()),
						Float.parseFloat(tVlongitud.getText().toString()),
						tVdescripcion.getText().toString());
				Toast.makeText(getApplicationContext(), estadoActualizacion,
						Toast.LENGTH_SHORT).show();
				EquipoSupervisor.this.finish();
			}
		});
		
		AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
		builderEliminar.setTitle("¿ELIMINAR el equipo? Puede acarrear el borrado de Trabajos.");
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
				vT = consultarTrabajosEquipo(pGPS.getIdPosicionGPS());
				
				for (int i=0; i<vT.size(); ++i) {
					estadoEliminacionTrabajo = eliminarTrabajo(vT.elementAt(i).getIdTrabajo());
					Log.d("Trabajo" + vT.elementAt(i).getIdTrabajo() + "eliminado por equipo ", estadoEliminacionTrabajo);
				}
				
				estadoEliminacionEquipo = eliminarEquipo(pGPS.getIdPosicionGPS());
				
				Toast.makeText(getApplicationContext(), estadoEliminacionEquipo,
						Toast.LENGTH_SHORT).show();
				EquipoSupervisor.this.finish();
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
		inflater.inflate(R.menu.menu_opciones_equipo, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.eliminarEquipo:
			lanzarEliminarEquipo(null);
			break;
		case R.id.acercaDe_equipo:
			lanzarAcercaDe(null);
			break;
		}
		return true;
	}

	/**
	 * Iniciar la actividad de Acerca De
	 * 
	 * @param view
	 */
	private void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	/**
	 * Muestra el Dialog de antes de eliminar
	 * 
	 * @param view
	 */
	private void lanzarEliminarEquipo(View view) {
		actionsEliminar.show();
	}
	
	/**
	 * Consultar los trabajos en los que esta un mismo equipo en la base de datos
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return Vector<Trabajo>
	 */
	private Vector<Trabajo> consultarTrabajosEquipo(int idPosicionGPS) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarTrabajosEquipo";
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

				return manejadorXML.getTrabajos();
			} else {
				Log.e("Equipo Supervisor", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Equipo Supervisor", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Consultar un equipo en la base de datos
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return PosicionGPSs
	 */
	private PosicionGPS consultarEquipo(int idPosicionGPS) {
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

				return manejadorXML.getEquipo();
			} else {
				Log.e("Consultar Equipo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Consultar Equipo", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Actualiza los datos de un equipo en la base de datos
	 * 
	 * @param idPosicionGPS
	 * @param latitud
	 * @param longitud
	 * @param descripcion
	 * 
	 * @return string
	 */
	private String actualizarEquipo(int idPosicionGPS, float latitud,
			float longitud, String descripcion) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/actualizarPosicionGPS";
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

				return manejadorXML.getActualizacion();
			} else {
				Log.e("Actualizar Equipo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Actualizar Equipo", e.getMessage(), e);
			return null;
		}

		return null;
	}
	
	/**
	 * Eliminar un equipo en la base de datos
	 * 
	 * @param idPosicionGPS
	 * 
	 * @return string
	 */
	private String eliminarEquipo(int idPosicionGPS) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/eliminarPosicionGPS";
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

				return manejadorXML.getEliminacion();
			} else {
				Log.e("Eliminar Equipo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Eliminar Equipo", e.getMessage(), e);
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
	 * Manejador de las respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;
		
		/**
		 * Recupera los trabajos de la respuesta en servicio Web
		 * 
		 * @return Vector<Trabajo>
		 */
		public Vector<Trabajo> getTrabajos() {
			Trabajo t = new Trabajo();
			Vector<Trabajo> vT = new Vector<Trabajo>();

			for (int j = 0; j < lista.size(); ++j) {
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

			return vT;
		}

		/**
		 * Recupera un equipo de la respuesta del servicio Web
		 * 
		 * @return PosicionGPS
		 */
		public PosicionGPS getEquipo() {
			PosicionGPS e = new PosicionGPS();

			for (int j = 0; j < lista.size(); ++j) {
				e.setIdPosicionGPS(Integer.parseInt(lista.elementAt(j)));
				j = j + 1;
				e.setLatitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				e.setLongitud(Float.parseFloat(lista.elementAt(j)));
				j = j + 1;
				e.setDescripcion(lista.elementAt(j));
			}
			Log.d("Tam vector", String.valueOf(lista.size()));

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
		
		/**
		 * Recupera la respuesta del servicio Web por la eliminacion
		 * 
		 * @return string
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
					lista.add(URLDecoder.decode(cadena.toString(), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					Log.e("Equipo Supervisor", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
