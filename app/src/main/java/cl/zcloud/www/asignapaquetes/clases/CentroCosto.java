package cl.zcloud.www.asignapaquetes.clases;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "centro_costo")
public class CentroCosto {

    @SerializedName("id_centro_costo")
    @ColumnInfo(name = "id_centro_costo")
    @PrimaryKey
    private int idCC;


    @SerializedName("nombre")
    @ColumnInfo(name = "nombre")
    private String descCC;

    public int getIdCC() {
        return idCC;
    }
    public void setIdCC(int idCC) {
        this.idCC = idCC;
    }

    public String getDescCC() {
        return descCC;
    }
    public void setDescCC(String descCC) {
        this.descCC = descCC;
    }
}
