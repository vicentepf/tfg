package es.ujaen.tfg.testservice.adaptadoresLista;

import java.util.Vector;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.ujaen.tfg.modelos.Trabajo;
import es.ujaen.tfg.testservice.R;

/**
 * Clase del adaptador de la lista de trabajos
 * 
 * @author Vicente
 *
 */
public class AdaptadorListaTrabajos extends BaseAdapter {
	private final Activity actividad;
	private Vector<Trabajo> vT;
	
	public AdaptadorListaTrabajos(Activity actividad, Vector<Trabajo> vT) {
		super();
		this.actividad = actividad;
		this.vT = vT;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = actividad.getLayoutInflater();
			convertView = inflater.inflate(R.layout.elemento_lista, null, true);
			
			TextView titulo = (TextView) convertView.findViewById(R.id.text_titulo);
			TextView subtitulo = (TextView) convertView.findViewById(R.id.text_subtitulo);
			titulo.setText("Id Trabajo: " + String.valueOf(vT.elementAt(position).getIdTrabajo()));
			subtitulo.setText("Provincia: " + vT.elementAt(position).getProvincia());
			
			// Iconos internos: ic_delete, btn_check_buttonless_on

			ImageView imagen = (ImageView) convertView.findViewById(R.id.text_icono);
			if (vT.elementAt(position).getEstado() == 0) {
				imagen.setImageResource(R.drawable.checkoff32);
			} else if (vT.elementAt(position).getEstado() == 1) {
				imagen.setImageResource(R.drawable.checkon32);
			}

			
			for (int i=0; i<vT.size();++i) {
				Log.d("Vector " + i, vT.elementAt(i).getProvincia());
			}			
		}
		
		return convertView;
	}
	
	public int getCount() {
		return vT.size();
	}
	
	public Object getItem(int arg0) {
		return vT.get(arg0);
	}
	
	public long getItemId(int position) {
		return position;
	}
}
