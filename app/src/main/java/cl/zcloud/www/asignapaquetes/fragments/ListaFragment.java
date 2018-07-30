package cl.zcloud.www.asignapaquetes.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.zcloud.www.asignapaquetes.MainActivity;
import cl.zcloud.www.asignapaquetes.R;
import cl.zcloud.www.asignapaquetes.adapters.adaptadorListaTotal;
import cl.zcloud.www.asignapaquetes.clases.paquetesGuardados;

public class ListaFragment extends Fragment {
    private RecyclerView lista_items;
    private adaptadorListaTotal adaptadorListaTotal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lista_fragment, container, false);

        lista_items = (RecyclerView) view.findViewById(R.id.lista_items);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        obtenerLista();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.empty_menu, menu);
    }

    public void obtenerLista(){

        lista_items.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lista_items.setLayoutManager(layoutManager);

        List<paquetesGuardados> pq = MainActivity.myAppDB.myDao().getPaquetes();
        if (pq.size() > 0){
            adaptadorListaTotal = new adaptadorListaTotal(pq,Objects.requireNonNull(getActivity()), new adaptadorListaTotal.OnItemClickListener(){
                @Override
                public void OnItemClick(paquetesGuardados item) {
                    showAlertForBloqueo(item);
                }
            });
            lista_items.setAdapter(adaptadorListaTotal);
        }
    }



    private void showAlertForBloqueo(final paquetesGuardados paquetesGuardados){
        View viewInfalted = LayoutInflater.from(Objects.requireNonNull(getActivity())).inflate(R.layout.spinner_alert,null);
        final Spinner spiner = viewInfalted.findViewById(R.id.spinner_alert);
        ArrayList<String> arrEstadosAlert = new ArrayList<>();
        arrEstadosAlert.add("Producción");
        arrEstadosAlert.add("Consumo");

        ArrayAdapter<String> spinner_adp_estado = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, arrEstadosAlert);
        if (paquetesGuardados.getProdCons().equals("P")){
            spiner.setSelection(0);
        }else{
            spiner.setSelection(1);
        }
        spiner.setAdapter(spinner_adp_estado);

        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle("Edicion")
                .setPositiveButton("Editar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton("cancelar",null).create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        paquetesGuardados paq = new paquetesGuardados();
                        paq.setEstadoGuardado(0);
                        paq.setIdCCGuardado(paquetesGuardados.getIdCCGuardado());
                        paq.setDescCCGuardado(paquetesGuardados.getDescCCGuardado());
                        paq.setEtiquetaGuardado(paquetesGuardados.getEtiquetaGuardado());
                        paq.setIdPaqueteGuardado(paquetesGuardados.getIdPaqueteGuardado());
                        paq.setFechaCaptura(paquetesGuardados.getFechaCaptura());

                        if (spiner.getSelectedItem().equals("Producción")){
                            paq.setProdCons("P");
                        }else{
                            paq.setProdCons("C");
                        }
                        try{
                          int id =   MainActivity.myAppDB.myDao().updatePaquetes(paq);
                          if (id > 0){
                              Toast.makeText(Objects.requireNonNull(getActivity()), "Actualizado con exito", Toast.LENGTH_SHORT).show();
                              obtenerLista();
                          }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
