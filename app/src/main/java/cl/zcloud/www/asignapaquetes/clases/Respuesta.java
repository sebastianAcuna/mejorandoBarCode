package cl.zcloud.www.asignapaquetes.clases;

import java.util.HashMap;
import java.util.Map;

public class Respuesta {

    private Integer estado;
    private String mensaje;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getEstado() {
        return estado;
    }
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
