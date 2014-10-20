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
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;
import es.ujaen.tfg.testservice.trabajos.TrabajosEmpleado;

/**
 * Clase que maneja la actividad del menu principal del empleado
 * 
 * @author Vicente
 *
 */
public class MenuEmpleado extends Activity {
	private Button bTrabajos;
	private Button bSalir;
	private TextView tVbienvenida;
	private int numEmpleado;
	private Empleado empleado;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_empleado);
        
        // Recojo la informaci�n Extra recibida
        Bundle extras = getIntent().getExtras();
        numEmpleado = extras.getInt("numEmpleado");
        
        empleado = consultarEmpleado(numEmpleado);
        
        // Para evitar que StrictMode nos impida acceder a la red
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        bTrabajos = (Button) findViewById(R.id.button_menu_empleado_trabajos);
        bSalir = (Button) findViewById(R.id.button_menu_empleado_salir);
        tVbienvenida = (TextView) findViewById(R.id.text_menu_empleado_bienvenida);
        
        tVbienvenida.setText("Bienvenid@: " + empleado.getApellidos()
        		+ ", "
        		+ empleado.getNombre());
        
        bTrabajos.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		Intent i = new Intent(MenuEmpleado.this, TrabajosEmpleado.class);
		    	// A�ade contenido extra, el numEmpleado
		    	i.putExtra("numEmpleado", numEmpleado);
		    	startActivity(i);
        	}
        });
        
        bSalir.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		moveTaskToBack(true);
        	}
        });
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_opciones_menu_empleado, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cambiarPassEmpleado:
			lanzarCambiarPassEmpleado(null);
			break;
		case R.id.acercaDe_menu_empleado:
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
		// A�ade contenido extra, el numEmpleado
    	i.putExtra("numEmpleado", numEmpleado);
		startActivity(i);
	}

	/**
	 * Inicia la actividad de cambiar contrase�a del empleado
	 * 
	 * @param view
	 */
	private void lanzarCambiarPassEmpleado(View view) {
		Intent i = new Intent(MenuEmpleado.this,
				CambiarPassEmpleado.class);
		// A�ade contenido extra, el numEmpleado
		i.putExtra("numEmpleado", numEmpleado);
		startActivity(i);
	}
	
	/**
	 * Consultar un empleado en la base de datos
	 * 
	 * @param numEmpleado
	 * 
	 * @return Empleado
	 */
	private Empleado consultarEmpleado(int numEmpleado) {
		try {
    		String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/consultarEmpleado";
	    	HttpURLConnection conexion = (HttpURLConnection) ((new URL(url).openConnection()));
	    	
	    	conexion.setRequestMethod("POST");
	    	conexion.setDoOutput(true);
	    	
	    	OutputStream sal = conexion.getOutputStream();
	    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sal, "UTF-8"));
	    	
	    	writer.write("numEmpleado=" + (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
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
	 * Manejador de las respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
		private StringBuilder cadena;
		private Vector<String> lista;

		/**
		 * Recupera un empleado de la respuesta del servicio Web
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
					Log.e("Menu Empleado", e.getMessage(), e);
				}
			}
			cadena.setLength(0);
		}
	}
}
