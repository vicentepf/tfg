package es.ujaen.tfg.testservice.adaptadoresLista;

import java.util.Vector;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.ujaen.tfg.modelos.Empleado;
import es.ujaen.tfg.testservice.R;

/**
 * Clase para el adaptador de lista de empleados
 * 
 * @author Vicente
 *
 */
public class AdaptadorListaEmpleados extends BaseAdapter {
	private final Activity actividad;
	private final Vector<Empleado> vE;

	public AdaptadorListaEmpleados(Activity actividad, Vector<Empleado> vE) {
		super();
		this.actividad = actividad;
		this.vE = vE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = actividad.getLayoutInflater();
			convertView = inflater.inflate(R.layout.elemento_lista, null, true);

			TextView titulo = (TextView) convertView
					.findViewById(R.id.text_titulo);
			TextView subtitulo = (TextView) convertView
					.findViewById(R.id.text_subtitulo);
			titulo.setText("Num Empleado: "
					+ String.valueOf(vE.elementAt(position).getNumEmpleado()));
			subtitulo.setText("Nombre: "
					+ vE.elementAt(position).getNombre() + " "
					+ vE.elementAt(position).getApellidos());

			ImageView imagen = (ImageView) convertView
					.findViewById(R.id.text_icono);
			imagen.setImageResource(R.drawable.user);
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
