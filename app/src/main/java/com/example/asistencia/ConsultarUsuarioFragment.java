package com.example.asistencia;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import Models.UpdateDatosModel;
import Models.Usuario;
import cn.pedant.SweetAlert.SweetAlertDialog;

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

    private TextView txtNombre, txtApellidoPaterno, txtCorreo, txtCelular, txtDireccion, txtTipoUsuario, txtPassword, txtEmpresa;

    private Button btnActualizarUsuario;
    private ImageView imagen;

    private static RequestQueue queue;
    private static final String ApiURL = "http://b3rs3rk3r-002-site2.htempurl.com/api/";

    private String url_imagen_perfil = "";

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
        txtCorreo = (TextView) vista.findViewById(R.id.txtCorreo);
        txtCelular = (TextView) vista.findViewById(R.id.txtCelular);
        txtDireccion = (TextView) vista.findViewById(R.id.txtDireccion);
        txtTipoUsuario = (TextView) vista.findViewById(R.id.txttipousuario);
        txtEmpresa = (TextView) vista.findViewById(R.id.txtEmpresa);
        txtPassword = (TextView) vista.findViewById(R.id.txtPassword);
        btnActualizarUsuario = (Button) vista.findViewById(R.id.btnActualizarUsuario);
        imagen = (ImageView) vista.findViewById(R.id.imagenId);

        queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ApiURL + "Trabajador/SelectById?idTrabajador="+Integer.parseInt(getFromSharedPreferences("idusuario")), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    String nombre = response.getJSONObject(0).getString("traC_NOMBRES").toString();
                    String apellidoP = response.getJSONObject(0).getString("traC_APE_PATERNO").toString();
                    String apellidoM = response.getJSONObject(0).getString("traC_APE_MATERNO").toString();
                    String celular = response.getJSONObject(0).getString("traN_TELEFONO").toString();
                    String correo = response.getJSONObject(0).getString("traC_EMAIL").toString();
                    String direccion = response.getJSONObject(0).getString("traC_DIRECCION").toString();
                    String tipoUsuario = response.getJSONObject(0).getString("traN_TIPO_USUARIO_TXT").toString();
                    String empresa = response.getJSONObject(0).getString("empN_ID_EMPRESA_TXT").toString();
                    String imagenUrl = response.getJSONObject(0).getString("traC_IMAGE_URL").toString();

                    txtNombre.setText(nombre);
                    txtApellidoPaterno.setText(apellidoP + " " + apellidoM);
                    txtCorreo.setText(correo);
                    txtCelular.setText(celular);
                    txtDireccion.setText(direccion);
                    txtTipoUsuario.setText(tipoUsuario);
                    txtEmpresa.setText(empresa);

                    //Llamando al método que trae la imagen
                    cargarImagen(imagenUrl);

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

        btnActualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _email = txtCorreo.getText().toString();
                Integer _telefono = Integer.parseInt(txtCelular.getText().toString());
                String _direccion = txtDireccion.getText().toString();
                String _clave = txtPassword.getText().toString();
                String _imageURL = url_imagen_perfil;
                String _userMod = "Android";
                Integer _userId = Integer.parseInt(getFromSharedPreferences("idusuario"));
                if(validarDatos(_email, _telefono, _direccion, _clave)){
                    queue = Volley.newRequestQueue(getActivity());
                    UpdateDatosModel modelo = new UpdateDatosModel();
                    modelo.setTraN_TELEFONO(_telefono);
                    modelo.setTraC_DIRECCION(_direccion);
                    modelo.setTraC_EMAIL(_email);
                    modelo.setTraC_IMAGE_URL(_imageURL);
                    modelo.setTraC_CLAVE(_clave);
                    modelo.setTraC_USU_MOD(_userMod);
                    modelo.setTraN_ID_TRABAJADOR(_userId);
                    Gson _gson = new Gson();
                    String json = _gson.toJson(modelo);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, ApiURL + "Trabajador/UpdateDatos", jsonObject, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Datos actualizados correctamente3").show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error instanceof ServerError) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Error interno del servidor").show();

                                } else if (error instanceof NoConnectionError) {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE).setTitleText("No hay conexión a internet").show();
                                } else {
                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("Datos actualizados correctamente").show();
                                }
                            }
                        });
                        queue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText(e.getMessage()).show();
                    }

                }
            }
        });

        return vista;
    }

    private boolean validarDatos(String par1, Integer par2, String par3, String par4) {
        if (par1 == "" || par2.toString() == "" || par3 == "" || par4 == "") {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText("Todos los campos son obligatorios").show();
            return false;
        }
        return true;
    }
    private String getFromSharedPreferences(String key) {
        SharedPreferences pref = getActivity().getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    //método que trae la imagen
    private void cargarImagen(String imagenUrl) {
        if(imagenUrl == "" || imagenUrl.equals("null")){
            imagenUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/1200px-No-Image-Placeholder.svg.png";
        }
        url_imagen_perfil = imagenUrl;
        ImageRequest imageRequest = new ImageRequest(imagenUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imagen.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error al cargar imagen", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(imageRequest);
    }
}