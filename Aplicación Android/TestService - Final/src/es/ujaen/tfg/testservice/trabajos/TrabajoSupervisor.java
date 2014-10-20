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
import es.ujaen.tfg.modelos.Trabajo;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;
import es.ujaen.tfg.testservice.equipos.GoogleMapsActivity;

/**
 * Clase que maneja la actividad de un trabajo para el supervisor
 * 
 * @author Vicente
 *
 */
public class TrabajoSupervisor extends Activity {
	private Button bEstado;
	private Button bActualizar;
	private Button bPosicionGPS;
	private int idTrabajo;
	private EditText tVidTrabajo;
	private EditText tVidSupervisor;
	private EditText tVnumEmpleado;
	private EditText tVprovincia;
	private EditText tVdescripcion;
	private EditText tVposicionGPS;
	private TextView tVestado;

	private Trabajo trabajo;

	AlertDialog actionsEstado;
	AlertDialog actionsEliminar;

	private String estadoActualizacion;
	private String estadoEliminacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trabajo_supervisor);

		// Recojo la información Extra recibida
		Bundle extras = getIntent().getExtras();
		idTrabajo = extras.getInt("idTrabajo");

		bEstado = (Button) findViewById(R.id.button_trabajo_supervisor_estado);
		bPosicionGPS = (Button) findViewById(R.id.button_trabajo_supervisor_posiciongps);
		bActualizar = (Button) findViewById(R.id.button_trabajo_supervisor_actualizar_datos);

		tVidTrabajo = (EditText) findViewById(R.id.text_trabajo_supervisor_id_trabajo_data);
		tVidSupervisor = (EditText) findViewById(R.id.text_trabajo_supervisor_id_supervisor_data);
		tVnumEmpleado = (EditText) findViewById(R.id.text_trabajo_supervisor_num_empleado_data);
		tVposicionGPS = (EditText) findViewById(R.id.text_trabajo_supervisor_posiciongps_data);
		tVprovincia = (EditText) findViewById(R.id.text_trabajo_supervisor_provincia_data);
		tVdescripcion = (EditText) findViewById(R.id.text_trabajo_supervisor_descripcion_data);
		tVestado = (TextView) findViewById(R.id.text_trabajo_supervisor_estado_data);

		trabajo = consultarTrabajo(idTrabajo);

		tVidTrabajo.setText(String.valueOf(trabajo.getIdTrabajo()));
		tVidSupervisor.setText(String.valueOf(trabajo.getIdSupervisor()));
		tVnumEmpleado.setText(String.valueOf(trabajo.getNumEmpleado()));
		tVposicionGPS.setText(String.valueOf(trabajo.getPosicionGPS()));
		tVprovincia.setText(trabajo.getProvincia());
		tVdescripcion.setText(trabajo.getDescripcion());
		tVestado.setText(String.valueOf(trabajo.getEstado()));

		bEstado.setOnClickListener(buttonEstadoListener);

		bPosicionGPS.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(TrabajoSupervisor.this,
						GoogleMapsActivity.class);
				// Añade contenido extra, el numEmpleado
				i.putExtra("idPosicionGPS", trabajo.getPosicionGPS());
				startActivity(i);
			}
		});

		bActualizar.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				estadoActualizacion = actualizarTrabajo(Integer.parseInt(tVidTrabajo.getText().toString()),
						Integer.parseInt(tVidSupervisor.getText().toString()), Integer.parseInt(tVnumEmpleado.getText().toString()),
						Integer.parseInt(tVposicionGPS.getText().toString()), tVprovincia.getText().toString(),
						tVdescripcion.getText().toString(), Integer.parseInt(tVestado.getText().toString()));
				Toast.makeText(getApplicationContext(), estadoActualizacion,
						Toast.LENGTH_SHORT).show();
				TrabajoSupervisor.this.finish();
			}
		});

		AlertDialog.Builder builderEstado = new AlertDialog.Builder(this);
		builderEstado.setTitle("Elige una opción");
		String[] optionsEstado = { "Pendiente", "Finalizado" };
		builderEstado.setItems(optionsEstado, actionEstadoListener);
		builderEstado.setNegativeButton("Cancelar", null);
		actionsEstado = builderEstado.create();
		
		AlertDialog.Builder builderEliminar = new AlertDialog.Builder(this);
		builderEliminar.setTitle("¿Seguro que desea ELIMINAR el trabajo?");
		String[] optionsEliminar = { "Si", "No" };
		builderEliminar.setItems(optionsEliminar, actionEliminarListener);
		builderEliminar.setNegativeButton("Cancelar", null);
		actionsEliminar = builderEliminar.create();
	}

	DialogInterface.OnClickListener actionEstadoListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0: // Pendiente
				estadoActualizacion = actualizarTrabajo(trabajo.getIdTrabajo(),
						trabajo.getIdSupervisor(), trabajo.getNumEmpleado(),
						trabajo.getPosicionGPS(), trabajo.getProvincia(),
						trabajo.getDescripcion(), 0);
				Toast.makeText(getApplicationContext(), estadoActualizacion,
						Toast.LENGTH_SHORT).show();
				TrabajoSupervisor.this.finish();
				break;
			case 1: // Finalizado
				estadoActualizacion = actualizarTrabajo(trabajo.getIdTrabajo(),
						trabajo.getIdSupervisor(), trabajo.getNumEmpleado(),
						trabajo.getPosicionGPS(), trabajo.getProvincia(),
						trabajo.getDescripcion(), 1);
				Toast.makeText(getApplicationContext(), estadoActualizacion,
						Toast.LENGTH_SHORT).show();
				TrabajoSupervisor.this.finish();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * Actuador del boton eliminar
	 */
	DialogInterface.OnClickListener actionEliminarListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0: // SI
				estadoEliminacion = eliminarTrabajo(trabajo.getIdTrabajo());
				Toast.makeText(getApplicationContext(), estadoEliminacion,
						Toast.LENGTH_SHORT).show();
				TrabajoSupervisor.this.finish();
				break;
			case 1: // NO
				break;
			default:
				break;
			}
		}
	};
	
	View.OnClickListener buttonEstadoListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			actionsEstado.show();
		}
	};
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_trabajo_supervisor, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.eliminarTrabajo:
			lanzarEliminarTrabajo(null);
			break;
		case R.id.acercaDe_trabajo_supervisor:
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
	 * Muestra el Dialog de antes de eliminar
	 * 
	 * @param view
	 */
	private void lanzarEliminarTrabajo(View view) {		
		actionsEliminar.show();
	}

	/**
	 * Consulta un trabajo en la base de datos
	 * 
	 * @param idTrabajo
	 * 
	 * @return Trabajo
	 */
	private Trabajo consultarTrabajo(int idTrabajo) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarTrabajo";
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

				return manejadorXML.getTrabajo();
			} else {
				Log.e("Consultar Trabajo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Consultar Trabajo", e.getMessage(), e);
			return null;
		}

		return null;
	}

	/**
	 * Actualiza los datos de un trabajo en la base de datos
	 * 
	 * @param idTrabajo
	 * @param idSupervisor
	 * @param numEmpleado
	 * @param posicionGPS
	 * @param provincia
	 * @param descripcion
	 * @param estado
	 * 
	 * @return string
	 */
	private String actualizarTrabajo(int idTrabajo, int idSupervisor,
			int numEmpleado, int posicionGPS, String provincia,
			String descripcion, int estado) {
		try {
			String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/actualizarTrabajo";
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
					+ (URLEncoder.encode(String.valueOf(posicionGPS), "UTF-8")));
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

				return manejadorXML.getActualizacion();
			} else {
				Log.e("Actualizar Trabajo", conexion.getResponseMessage());
			}
		} catch (Exception e) {
			Log.e("Actualizar Trabajo", e.getMessage(), e);
			return null;
		}

		return null;
	}
	
	/**
	 * Eliminar un trabajo de la base de datos
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
	 * Manejador de respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;

		/**
		 * Recupera los datos de un trabajo
		 * @return
		 */
		public Trabajo getTrabajo() {
			Trabajo t = new Trabajo();

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
				j = j + 1;
			}

			return t;
		}

		/**
		 * Recupera la respuesta del servicio Web por la actualizacion
		 * 
		 * @return string
		 */
		public String getActualizacion() {
			return lista.firstElement();
		}
		
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
					Log.e("Trabajo Supervisor", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
