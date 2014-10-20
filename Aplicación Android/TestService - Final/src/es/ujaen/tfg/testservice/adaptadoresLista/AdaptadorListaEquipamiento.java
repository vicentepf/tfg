package es.ujaen.tfg.testservice.adaptadoresLista;

import java.util.Vector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.ujaen.tfg.modelos.PosicionGPS;
import es.ujaen.tfg.testservice.R;

/**
 * Clase del adaptador de la lista de equipamiento
 * @author Vicente
 *
 */
public class AdaptadorListaEquipamiento extends BaseAdapter {
	private final Activity actividad;
	private final Vector<PosicionGPS> vE;
	
	public AdaptadorListaEquipamiento(Activity actividad, Vector<PosicionGPS> vE) {
		super();
		this.actividad = actividad;
		this.vE = vE;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = actividad.getLayoutInflater();
			convertView = inflater.inflate(R.layout.elemento_lista, null, true);
			
			TextView titulo = (TextView) convertView.findViewById(R.id.text_titulo);
			TextView subtitulo = (TextView) convertView.findViewById(R.id.text_subtitulo);
			titulo.setText("Id Posicion: " + String.valueOf(vE.elementAt(position).getIdPosicionGPS()));
			subtitulo.setText("Descripcion: " + vE.elementAt(position).getDescripcion());
			
			ImageView imagen = (ImageView) convertView.findViewById(R.id.text_icono);
			imagen.setImageResource(R.drawable.equipo32);		
		}
		
		return convertView;
	}
	
	public int getCount() {
		return vE.size();
	}
	
	public Object getItem(int arg0) {
		return vE.get(arg0);
	}
	
	public long getItemId(int position) {
		return position;
	}
}
