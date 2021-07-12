package Models;

public class JustificacionesResponse {

    private String tituloJustificacion;
    private String motivoJustificacion;
    private String fechaJustificacion;
    private String descEstadoJustificacion;

    public JustificacionesResponse() {
    }

    public JustificacionesResponse(String tituloJustificacion, String motivoJustificacion, String fechaJustificacion, String descEstadoJustificacion) {
        this.tituloJustificacion = tituloJustificacion;
        this.motivoJustificacion = motivoJustificacion;
        this.fechaJustificacion = fechaJustificacion;
        this.descEstadoJustificacion = descEstadoJustificacion;
    }

    public String getTituloJustificacion() {
        return tituloJustificacion;
    }

    public void setTituloJustificacion(String tituloJustificacion) {
        this.tituloJustificacion = tituloJustificacion;
    }

    public String getMotivoJustificacion() {
        return motivoJustificacion;
    }

    public void setMotivoJustificacion(String motivoJustificacion) {
        this.motivoJustificacion = motivoJustificacion;
    }

    public String getFechaJustificacion() {
        return fechaJustificacion;
    }

    public void setFechaJustificacion(String fechaJustificacion) {
        this.fechaJustificacion = fechaJustificacion;
    }

    public String getDescEstadoJustificacion() {
        return descEstadoJustificacion;
    }

    public void setDescEstadoJustificacion(String descEstadoJustificacion) {
        this.descEstadoJustificacion = descEstadoJustificacion;
    }

}