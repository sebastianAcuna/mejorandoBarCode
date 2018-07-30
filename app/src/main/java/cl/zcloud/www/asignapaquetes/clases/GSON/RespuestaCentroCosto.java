package cl.zcloud.www.asignapaquetes.clases.GSON;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.zcloud.www.asignapaquetes.clases.CentroCosto;

public class RespuestaCentroCosto {

    @SerializedName("estado")
    @Expose
    private Integer estado;

    @SerializedName("centro_costo")
    @Expose
    private List<CentroCosto> centroCosto = null;

    public Integer getEstado() {
        return estado;
    }
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<CentroCosto> getCentroCosto() {
        return centroCosto;
    }
    public void setCentroCosto(List<CentroCosto> centroCosto) {
        this.centroCosto = centroCosto;
    }
}
