package cl.zcloud.www.asignapaquetes.clases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "paquetes_guardados")
public class paquetesGuardados {
    @ColumnInfo(name = "id_paquete_guardado")
    @PrimaryKey(autoGenerate = true)
    private int idPaqueteGuardado;

    @Expose
    @SerializedName("id_cc_guardado")
    @ColumnInfo(name = "id_cc_guardado")
    private int idCCGuardado;

    @Expose
    @SerializedName("desc_cc_guardado")
    @ColumnInfo(name = "desc_cc_guardado")
    private String descCCGuardado;

    @Expose
    @SerializedName("etiqueta_guardado")
    @ColumnInfo(name = "etiqueta_guardado")
    private String etiquetaGuardado;

    @Expose
    @SerializedName("prod_cons")
    @ColumnInfo(name = "prod_cons")
    private String prodCons;

    @Expose
    @SerializedName("fecha_captura")
    @ColumnInfo(name = "fecha_captura")
    private String fechaCaptura;

    @ColumnInfo(name = "estado_guardado")
    private int estadoGuardado;


    public int getIdPaqueteGuardado() {
        return idPaqueteGuardado;
    }
    public void setIdPaqueteGuardado(int idPaqueteGuardado) {
        this.idPaqueteGuardado = idPaqueteGuardado;
    }

    public int getIdCCGuardado() {
        return idCCGuardado;
    }
    public void setIdCCGuardado(int idCCGuardado) {
        this.idCCGuardado = idCCGuardado;
    }

    public String getDescCCGuardado() {
        return descCCGuardado;
    }
    public void setDescCCGuardado(String descCCGuardado) {
        this.descCCGuardado = descCCGuardado;
    }

    public String getEtiquetaGuardado() {
        return etiquetaGuardado;
    }
    public void setEtiquetaGuardado(String etiquetaGuardado) {
        this.etiquetaGuardado = etiquetaGuardado;
    }

    public int getEstadoGuardado() {
        return estadoGuardado;
    }
    public void setEstadoGuardado(int estadoGuardado) {
        this.estadoGuardado = estadoGuardado;
    }

    public String getProdCons() {
        return prodCons;
    }
    public void setProdCons(String prodCons) {
        this.prodCons = prodCons;
    }

    public String getFechaCaptura() {
        return fechaCaptura;
    }
    public void setFechaCaptura(String fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }
}
