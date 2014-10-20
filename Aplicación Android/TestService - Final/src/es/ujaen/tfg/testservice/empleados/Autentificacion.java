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
import android.widget.EditText;
import android.widget.Toast;
import es.ujaen.tfg.testservice.AcercaDe;
import es.ujaen.tfg.testservice.R;

/**
 * Clase encargada de la actividad de inicio de sesion
 * 
 * @author Vicente
 *
 */
public class Autentificacion extends Activity {
	private EditText eTnumEmpleado;
	private EditText eTpass;
	private Button bEntrar;
	private Button bSalir;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autentificacion);
        
        // Para evitar que StrictMode nos impida acceder a la red
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        
        eTnumEmpleado = (EditText) findViewById(R.id.editText_autentificacion_numEmpleado);
        eTpass = (EditText) findViewById(R.id.editText_autentificacion_pass);
        bEntrar = (Button) findViewById(R.id.button_autentificacion_entrar);
        bSalir = (Button) findViewById(R.id.button_autentificacion_salir);
        
        bEntrar.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		int a = autentificacion(
        				Integer.parseInt(eTnumEmpleado.getText().toString()), 
        				eTpass.getText().toString()
        				);
        		Log.d("Autentificacion", "RETURN " + a);
        		// 0 - Empleado
        		if (a == 0) {
    		    	Intent i = new Intent(Autentificacion.this, MenuEmpleado.class);
    		    	// Añade contenido extra, el numEmpleado
    		    	i.putExtra("numEmpleado", Integer.parseInt(eTnumEmpleado.getText().toString()));
    		    	eTnumEmpleado.setText("");
        			eTpass.setText("");
    		    	startActivity(i);
    		    // 1 - Supervisor
        		} else if (a == 1) {
        			Intent i = new Intent(Autentificacion.this, MenuSupervisor.class);
    		    	// Añade contenido extra, el numEmpleado
    		    	i.putExtra("numEmpleado", Integer.parseInt(eTnumEmpleado.getText().toString()));
    		    	eTnumEmpleado.setText("");
        			eTpass.setText("");
    		    	startActivity(i);
        		// 2 - Error
        		} else {
        			eTnumEmpleado.setText("");
        			eTpass.setText("");
        			Toast.makeText(getApplicationContext(), "Datos Incorrectos", Toast.LENGTH_SHORT).show();
        		}
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
		inflater.inflate(R.menu.menu_opciones_autentificacion, menu);
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.acercaDe_autentificacion:
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
	 * Consulta en la base de datos si el inicio de sesion es correcto o no
	 * 
	 * @param numEmpleado
	 * @param pass
	 * 
	 * @return int
	 */
	private int autentificacion(int numEmpleado, String pass) {
		try {
    		String url = "http://kefren.ujaen.es:6901/t/SerWeb/services/SerWeb/autentificacion";
	    	HttpURLConnection conexion = (HttpURLConnection) ((new URL(url).openConnection()));
	    	
	    	conexion.setRequestMethod("POST");
	    	conexion.setDoOutput(true);
	    	
	    	OutputStream sal = conexion.getOutputStream();
	    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sal, "UTF-8"));
	    	
	    	writer.write("numEmpleado=" + (URLEncoder.encode(String.valueOf(numEmpleado), "UTF-8")));
	    	writer.write("&pass=" + (URLEncoder.encode(String.valueOf(pass), "UTF-8")));
	    	writer.close();
	    	
	    	if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    		SAXParserFactory fabrica = SAXParserFactory.newInstance();
	    		SAXParser parser = fabrica.newSAXParser();
	    		XMLReader lector = parser.getXMLReader();
	    		
	    		ManejadorSerWeb manejadorXML = new ManejadorSerWeb();
	    		lector.setContentHandler(manejadorXML);
	    		lector.parse(new InputSource(conexion.getInputStream()));
	    		
	    		return Integer.parseInt(manejadorXML.getAutentificacion());
	    	} else {
	    		Log.e("Autentificacion", conexion.getResponseMessage());
	    	}
    	} catch (Exception e) {
    		Log.e("Autentificacion", e.getMessage(), e);
    		return 2;
    	}
		
		return 2;
	}
	
	/**
	 * Maneja las respuestas del servicio Web
	 * 
	 * @author Vicente
	 *
	 */
	class ManejadorSerWeb extends DefaultHandler {
    	private StringBuilder cadena;    	
    	private Vector<String> lista;
    	
    	/**
    	 * Recupera la respuesta del servicio Web por la autentificacion
    	 * @return
    	 */
    	public String getAutentificacion() {
    		return lista.firstElement();
    	}
    	
    	public void startDocument() throws SAXException {
    		cadena = new StringBuilder();
    		lista = new Vector<String>();
    	}
    	
    	public void characters (char ch[], int comienzo, int longitud) {
    		cadena.append(ch, comienzo, longitud);
    	}
    	
    	public void endElement (String uri, String nombreLocal, String nombreCualif) throws SAXException {
    		if (nombreLocal.equals("return")) {
    			try {
    				lista.add(URLDecoder.decode(cadena.toString(),"UTF-8"));
    			} catch (UnsupportedEncodingException e) {
    				Log.e("TestService", e.getMessage(), e);
    			}
    		}
    		cadena.setLength(0);
    	}
    }
}
