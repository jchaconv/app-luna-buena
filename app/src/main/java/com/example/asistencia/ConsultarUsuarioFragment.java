package com.example.asistencia;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Models.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConsultarUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarUsuarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView txtNombre, txtApellidoPaterno, txtApellidoMaterno, txtCorreo, txtCelular;
    private Button btnConsultarUsuario;

    private static RequestQueue queue;
    private static String ApiURL = "http://dongato-001-site1.ctempurl.com/api/Trabajador/SelectById?idTrabajador=5";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConsultarUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultarUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarUsuarioFragment newInstance(String param1, String param2) {
        ConsultarUsuarioFragment fragment = new ConsultarUsuarioFragment();
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

        View vista = inflater.inflate(R.layout.fragment_consultar_usuario, container, false);

        txtNombre = (TextView) vista.findViewById(R.id.txtNombre);
        txtApellidoPaterno = (TextView) vista.findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = (TextView) vista.findViewById(R.id.txtApellidoMaterno);
        txtCorreo = (TextView) vista.findViewById(R.id.txtCorreo);
        txtCelular = (TextView) vista.findViewById(R.id.txtCelular);
        btnConsultarUsuario = (Button) vista.findViewById(R.id.btnConsultarUsuario);

        btnConsultarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue = Volley.newRequestQueue(getContext());

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ApiURL, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            String nombre = response.getJSONObject(0).getString("traC_NOMBRES").toString();
                            String apellidoP = response.getJSONObject(0).getString("traC_APE_PATERNO").toString();
                            String apellidoM = response.getJSONObject(0).getString("traC_APE_MATERNO").toString();
                            String celular = response.getJSONObject(0).getString("traN_TELEFONO").toString();
                            String correo = response.getJSONObject(0).getString("traC_EMAIL").toString();


                            //Toast.makeText(getContext(), correo, Toast.LENGTH_SHORT).show();
                            txtNombre.setText("Nombre: " + nombre);
                            txtApellidoPaterno.setText("Apellido Paterno: " + apellidoP);
                            txtApellidoMaterno.setText("Apellido Materno: " + apellidoM);
                            txtCorreo.setText("Correo: " + correo);
                            txtCelular.setText("Celular: " + celular);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonArrayRequest);
            }
        });

        return vista;
    }
}