package cl.zcloud.www.asignapaquetes;

import android.arch.persistence.room.Room;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cl.zcloud.www.asignapaquetes.bd.MyAppDB;
import cl.zcloud.www.asignapaquetes.fragments.MainFragment;



public class MainActivity extends AppCompatActivity {
    public static MyAppDB myAppDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAppDB = Room.databaseBuilder(getApplicationContext(), MyAppDB.class, "llasaerp_asign_cc.db").allowMainThreadQueries().build();

        cambiarFragment(MainFragment.class);
    }


    public void cambiarFragment(Class fragmentClass){
        Fragment fragment = null;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor_fragment, fragment).commit();

    }
}
