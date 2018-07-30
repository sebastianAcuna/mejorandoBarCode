package cl.zcloud.www.asignapaquetes.bd;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import cl.zcloud.www.asignapaquetes.clases.CentroCosto;
import cl.zcloud.www.asignapaquetes.clases.paquetesGuardados;

@Dao
public interface MyDao {
    @Insert
    public void setCC(List<CentroCosto> cc);

    @Query("SELECT * FROM centro_costo ORDER BY nombre ASC")
    public List<CentroCosto> getCentroCosto();

    @Query("DELETE FROM centro_costo")
    public void deleteCC();


    @Insert
    public long  setPaquetes(paquetesGuardados pg);

    @Query(" SELECT  * FROM paquetes_guardados")
    public List<paquetesGuardados> getPaquetes();


    @Update
    public int updatePaquetes(paquetesGuardados paquetesGuardados);
}
