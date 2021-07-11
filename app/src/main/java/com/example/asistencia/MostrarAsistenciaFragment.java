package com.example.asistencia;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import Models.Asistencia;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MostrarAsistenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostrarAsistenciaFragment extends Fragment {

    private TextView txtTrabajador, txtIpEntrada, txtIpSalida, txtHoraEntrada, txtHoraSalida;
    private MaterialButton btnGpsEntrada, btnGpsSalida;
    private RequestQueue mQueue;
    private CalendarView calendario;
    private String coordEntrada = "";
    private String coordSalida = "";
    Asistencia[] asistenciasArray;

    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MostrarAsistenciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MostrarAsistenciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MostrarAsistenciaFragment newInstance(String param1, String param2) {
        MostrarAsistenciaFragment fragment = new MostrarAsistenciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mostrar_asistencia, container, false);
        txtTrabajador = (TextView) view.findViewById(R.id.txtTrabajador);
        txtIpEntrada = (TextView) view.findViewById(R.id.txtIpEntrada);
        txtIpSalida = (TextView) view.findViewById(R.id.txtIpSalida);
        txtHoraEntrada = (TextView) view.findViewById(R.id.txtHoraEntrada);
        txtHoraSalida = (TextView) view.findViewById(R.id.txtHoraSalida);
        btnGpsEntrada = (MaterialButton) view.findViewById(R.id.txtGpsEntrada);
        btnGpsSalida = (MaterialButton) view.findViewById(R.id.txtGpsSalida);

        btnGpsEntrada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coordEntrada.equals("")) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("lat", coordEntrada.split(",")[0]);
                    intent.putExtra("lng", coordEntrada.split(",")[1]);
                    startActivity(intent);
                }
            }
        });

        btnGpsSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coordSalida.equals("")) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                    intent.putExtra("lat", coordSalida.split(",")[0]);
                    intent.putExtra("lng", coordSalida.split(",")[1]);
                    startActivity(intent);
                }
            }
        });

        mQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiURL + "Asistencia/SelectByTrabajador?idTrabajador="+Integer.parseInt(getFromSharedPreferences("idusuario")), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                try {
                    asistenciasArray = gson.fromJson(response, Asistencia[].class);
                } catch (Exception e) {
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof ServerError) {
                    Toast.makeText(getActivity(), "Error interno del servidor", Toast.LENGTH_SHORT).show();
                }
                else if(error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mQueue.add(stringRequest);

        //Inicializando Calendario
        calendario = (CalendarView) view.findViewById(R.id.calendarView);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String anio = year + "";
                month = month == 0 ? 12 : month+1;
                String mes = month < 10 ? "0" + month : month + "";
                String dia = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
                String seleccionado = anio + "-" + mes + "-" + dia;
                txtTrabajador.setText("");
                txtIpEntrada.setText("");
                txtIpSalida.setText("");
                txtHoraEntrada.setText("");
                txtHoraSalida.setText("");
                coordEntrada = "";
                coordSalida = "";
                btnGpsEntrada.setVisibility(view.INVISIBLE);
                btnGpsSalida.setVisibility(view.INVISIBLE);
                for (int i = 0; i<asistenciasArray.length; i++){
                    String fechaEnConsulta = asistenciasArray[i].getAsiD_FECHA().substring(0,10);
                    if(fechaEnConsulta.equals(seleccionado)){
                        txtTrabajador.setText(asistenciasArray[i].getTraN_ID_TRABAJADOR_TXT());
                        if(asistenciasArray[i].getAsiC_TIPO().equals("Entrada")) {
                            String _fechatmp = asistenciasArray[i].getAsiD_FECHA().substring(0, 10) + " " + asistenciasArray[i].getAsiC_HORA();
                            txtIpEntrada.setText(asistenciasArray[i].getAsiC_IP());
                            txtHoraEntrada.setText(_fechatmp);
                            coordEntrada = asistenciasArray[i].getAsiC_LATITUD()+","+asistenciasArray[i].getAsiC_LONGITUD();
                            btnGpsEntrada.setVisibility(view.VISIBLE);
                        }
                        if(asistenciasArray[i].getAsiC_TIPO().equals("Salida")){
                            String _fechatmp = asistenciasArray[i].getAsiD_FECHA().substring(0,10) + " " +asistenciasArray[i].getAsiC_HORA();
                            txtIpSalida.setText(asistenciasArray[i].getAsiC_IP());
                            txtHoraSalida.setText(_fechatmp);
                            coordSalida = asistenciasArray[i].getAsiC_LATITUD()+","+asistenciasArray[i].getAsiC_LONGITUD();
                            btnGpsSalida.setVisibility(view.VISIBLE);
                        }
                    }
                }
            }
        });

        return view;
    }

    private String getFromSharedPreferences(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }
}