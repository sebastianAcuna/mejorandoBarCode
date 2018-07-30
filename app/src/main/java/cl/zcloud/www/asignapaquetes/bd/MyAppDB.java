package cl.zcloud.www.asignapaquetes.bd;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import cl.zcloud.www.asignapaquetes.clases.CentroCosto;
import cl.zcloud.www.asignapaquetes.clases.paquetesGuardados;

@Database(entities = {CentroCosto.class, paquetesGuardados.class}, version = 1)
public abstract class MyAppDB extends RoomDatabase {
    public  abstract  MyDao myDao();
}
