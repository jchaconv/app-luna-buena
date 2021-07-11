package Models;

public class Asistencia {

    private int asiN_ID_ASISTENCIA;
    private int traN_ID_TRABAJADOR;
    private String asiD_FECHA;
    private String asiC_HORA;
    private String asiC_TIPO;
    private String asiC_LATITUD;
    private String asiC_LONGITUD;
    private String asiC_IP;
    private String traN_ID_TRABAJADOR_TXT;

    public String getTraN_ID_TRABAJADOR_TXT() {
        return traN_ID_TRABAJADOR_TXT;
    }

    public void setTraN_ID_TRABAJADOR_TXT(String traN_ID_TRABAJADOR_TXT) {
        this.traN_ID_TRABAJADOR_TXT = traN_ID_TRABAJADOR_TXT;
    }

    public int getAsiN_ID_ASISTENCIA() {
        return asiN_ID_ASISTENCIA;
    }

    public void setAsiN_ID_ASISTENCIA(int asiN_ID_ASISTENCIA) {
        this.asiN_ID_ASISTENCIA = asiN_ID_ASISTENCIA;
    }

    public int getTraN_ID_TRABAJADOR() {
        return traN_ID_TRABAJADOR;
    }

    public void setTraN_ID_TRABAJADOR(int traN_ID_TRABAJADOR) {
        this.traN_ID_TRABAJADOR = traN_ID_TRABAJADOR;
    }

    public String getAsiD_FECHA() {
        return asiD_FECHA;
    }

    public void setAsiD_FECHA(String asiD_FECHA) {
        this.asiD_FECHA = asiD_FECHA;
    }

    public String getAsiC_HORA() {
        return asiC_HORA;
    }

    public void setAsiC_HORA(String asiC_HORA) {
        this.asiC_HORA = asiC_HORA;
    }

    public String getAsiC_TIPO() {
        return asiC_TIPO;
    }

    public void setAsiC_TIPO(String asiC_TIPO) {
        this.asiC_TIPO = asiC_TIPO;
    }

    public String getAsiC_LATITUD() {
        return asiC_LATITUD;
    }

    public void setAsiC_LATITUD(String asiC_LATITUD) {
        this.asiC_LATITUD = asiC_LATITUD;
    }

    public String getAsiC_LONGITUD() {
        return asiC_LONGITUD;
    }

    public void setAsiC_LONGITUD(String asiC_LONGITUD) {
        this.asiC_LONGITUD = asiC_LONGITUD;
    }

    public String getAsiC_IP() {
        return asiC_IP;
    }

    public void setAsiC_IP(String asiC_IP) {
        this.asiC_IP = asiC_IP;
    }
}
