package es.ujaen.tfg.testservice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Clase que maneja la actividad de Acerca De
 * @author Vicente
 *
 */
public class AcercaDe extends Activity {
	TextView tVacercaDe;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca_de);
		
		tVacercaDe = (TextView) findViewById(R.id.text_acerca_de);
		
		tVacercaDe.setText("Esta aplicaci�n ha sido desarrollada como aplicaci�n cliente del Trabajo Fin de Grado:"
				 + System.getProperty ("line.separator") 
				 + System.getProperty ("line.separator")
				 + "Gesti�n y Acceso a los Servicios de una empresa de Telefon�a a trav�s de Aplicaci�n M�vil."
				 + System.getProperty ("line.separator")
				 + System.getProperty ("line.separator")
				 + "Vicente Plaza Franco,"
				 + System.getProperty ("line.separator")
				 + "Grado en Ingenier�a Inform�tica.");
	}
}
