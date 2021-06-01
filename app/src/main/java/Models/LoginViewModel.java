package Models;

import java.util.List;

public class LoginViewModel {
    String token;
    List<Datos> datos;
    Boolean Valido;
    String Respuesta;
    List<Response> response;

    public String gettoken() {
        return token;
    }

    public void settoken(String token) {
        this.token = token;
    }

    public List<Datos> getDatos() {
        return datos;
    }

    public void setDatos(List<Datos> datos) {
        this.datos = datos;
    }

    public Boolean getValido() {
        return Valido;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(String Respuesta) {
        Respuesta = Respuesta;
    }

    public void setValido(Boolean Valido) {
        Valido = Valido;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
}

class Datos {
    int traN_TIPO_USUARIO;
    int traN_ID_TRABAJADOR;
    int empN_ID_EMPRESA;
    String usersystem;
    String empN_ID_EMPRESA_TXT;
    String empC_LOGO;

    public int gettraN_TIPO_USUARIO() {
        return traN_TIPO_USUARIO;
    }

    public void settraN_TIPO_USUARIO(int traN_TIPO_USUARIO) {
        this.traN_TIPO_USUARIO = traN_TIPO_USUARIO;
    }

    public int gettraN_ID_TRABAJADOR() {
        return traN_ID_TRABAJADOR;
    }

    public void settraN_ID_TRABAJADOR(int traN_ID_TRABAJADOR) {
        this.traN_ID_TRABAJADOR = traN_ID_TRABAJADOR;
    }

    public int getempN_ID_EMPRESA() {
        return empN_ID_EMPRESA;
    }

    public void setempN_ID_EMPRESA(int empN_ID_EMPRESA) {
        this.empN_ID_EMPRESA = empN_ID_EMPRESA;
    }

    public String getusersystem() {
        return usersystem;
    }

    public void setusersystem(String usersystem) {
        this.usersystem = usersystem;
    }

    public String getempN_ID_EMPRESA_TXT() {
        return empN_ID_EMPRESA_TXT;
    }

    public void setempN_ID_EMPRESA_TXT(String empN_ID_EMPRESA_TXT) {
        this.empN_ID_EMPRESA_TXT = empN_ID_EMPRESA_TXT;
    }

    public String getempC_LOGO() {
        return empC_LOGO;
    }

    public void setempC_LOGO(String empC_LOGO) {
        this.empC_LOGO = empC_LOGO;
    }
}

class Response{
    Boolean valido;
    String respuesta;
    String error;

    public Boolean getvalido() {
        return valido;
    }

    public void setvalido(Boolean valido) {
        valido = valido;
    }

    public String getrespuesta() {
        return respuesta;
    }

    public void setrespuesta(String respuesta) {
        respuesta = respuesta;
    }

    public String geterror() {
        return error;
    }

    public void seterror(String error) {
        error = error;
    }
}