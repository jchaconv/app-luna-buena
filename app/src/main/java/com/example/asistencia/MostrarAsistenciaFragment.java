package com.example.asistencia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MostrarAsistenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostrarAsistenciaFragment extends Fragment {

    private TextView mTextViewResult;
    private RequestQueue mQueue;
    private Button btnMostrarAsistencia;

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

        mTextViewResult = (TextView) view.findViewById(R.id.text_view_result);
        btnMostrarAsistencia = (Button) view.findViewById(R.id.button_consultar_asistencia);

        mQueue = Volley.newRequestQueue(getContext());

        btnMostrarAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        return view;
    }

    private void jsonParse() {

        String url = "http://b3rs3rk3r-002-site2.htempurl.com/api/Asistencia/SelectByTrabajador?idTrabajador=5";

        /*
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        //JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            //public void onResponse(JSONArray response) {
                public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("");
                    for (int i = 0; i < jsonArray.length(); i++) {
                    //for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String fecha = jsonObject.getString("asiD_FECHA");
                        //JSONObject jsonObject = response.getJSONObject(i);
                        //String fecha = jsonObject.getString("asiD_FECHA");
                        mTextViewResult.append(fecha + "\n\n");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }); */

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());

                        //JSONArray array = null;
                        try {
                            //array = new JSONArray("");
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject row = response.getJSONObject(i);

                                //Se extrae solo la fecha sin la hora
                                String fecha = row.getString("asiD_FECHA").toString();
                                Log.d("Fecha", fecha);
                                String sFecha = fecha.substring(0, 10);

                                String hora = row.getString("asiC_HORA").toString();

                                //Tipo de asistencia
                                String tipo = row.getString("asiC_TIPO").toString();
                                if (tipo.equalsIgnoreCase("S")) {
                                    tipo = "Salida";
                                } else if (tipo.equalsIgnoreCase("E")) {
                                    tipo = "Entrada";
                                } else {
                                    tipo = "No especificado";
                                }

                                mTextViewResult.append("Fecha: " + sFecha + "\n" + "Hora: " + hora + "\n" + "Tipo: "
                                        + tipo + "\n" + "===============" + "\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        mQueue.add(getRequest);

    }

}