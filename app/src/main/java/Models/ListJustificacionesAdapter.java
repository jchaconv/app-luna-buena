package Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.asistencia.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListJustificacionesAdapter extends ArrayAdapter<JustificacionesResponse> {

    private static final String TAG = "ListJustificacionesAdapter";

    private Context mContext;
    int mResource;

    public ListJustificacionesAdapter(Context context, int resource, ArrayList<JustificacionesResponse> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String tituloJustificacion = getItem(position).getTituloJustificacion();
        String motivoJustificacion = getItem(position).getMotivoJustificacion();
        String fechaJustificacion = getItem(position).getFechaJustificacion();
        String descEstadoJustificacion = getItem(position).getDescEstadoJustificacion();

        /*JustificacionesResponse justificacionesResponse = new JustificacionesResponse(tituloJustificacion,
                motivoJustificacion, fechaJustificacion, descEstadoJustificacion);*/

        LayoutInflater inflater = LayoutInflater.from(mContext);
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.textView2);
        TextView tvMotivo = (TextView) convertView.findViewById(R.id.textView3);
        TextView tvFecha = (TextView) convertView.findViewById(R.id.textView4);
        TextView tvDescEstado = (TextView) convertView.findViewById(R.id.textView5);

        tvTitulo.setText(tituloJustificacion);
        tvMotivo.setText(motivoJustificacion);
        tvFecha.setText(fechaJustificacion);
        tvDescEstado.setText(descEstadoJustificacion);

        //return super.getView(position, convertView, parent);
        return convertView;
    }
}
