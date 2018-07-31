package cl.zcloud.www.asignapaquetes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cl.zcloud.www.asignapaquetes.MainActivity;
import cl.zcloud.www.asignapaquetes.R;
import cl.zcloud.www.asignapaquetes.clases.CentroCosto;
import cl.zcloud.www.asignapaquetes.clases.GSON.RespuestaCentroCosto;
import cl.zcloud.www.asignapaquetes.clases.VerificarInternet;
import cl.zcloud.www.asignapaquetes.clases.paquetesGuardados;
import cl.zcloud.www.asignapaquetes.retrofit.APIService;
import cl.zcloud.www.asignapaquetes.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment {
    private Button btnPick, btnSearch, btnWatch, btnGuardar;
    private Spinner spCC, spEstado;
    private ArrayList<String> nombreCC;
    private ArrayList<Integer> idCC;
    private ArrayAdapter<String> spinner_adp;
    private ArrayAdapter<String> spinner_adp_estado;
    private EditText cont_paquete;
    private int idCCSave;
    private String descCCSave, etiqueta;


    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";


//    private CameraSurface cameraSurface;


//    private SurfaceView cameraView;
//    private CameraSource cameraSource;
//    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
//    private String token = "";
//    private String tokenanterior = "";

    private static final int REQUEST_GET_ACCOUNT = 112;
    private static final int PERMISSION_REQUEST_CODE = 200;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        btnPick = (Button) view.findViewById(R.id.btn_choice_cc);
        btnSearch = (Button) view.findViewById(R.id.btn_find_package);
        btnWatch = (Button) view.findViewById(R.id.watch_all);
        spCC = (Spinner) view.findViewById(R.id.spinner_cc);
        spEstado = (Spinner) view.findViewById(R.id.consumo_produccion);
        cont_paquete = (EditText) view.findViewById(R.id.cont_paquete);
        btnGuardar = (Button) view.findViewById(R.id.guardar);
//        cameraView = (SurfaceView) view.findViewById(R.id.camera_view);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(Objects.requireNonNull(getActivity()), "Permiso consedido", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }
//        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("config_tel", MODE_PRIVATE);

//        btnPick.setEnabled(sharedPref.getBoolean("remember_button_check", true));

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        cargarSpinner();

        spCC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("config_tel", MODE_PRIVATE);
                int pos = sharedPref.getInt("remember_position_cc", 0);
                if (pos == spCC.getSelectedItemPosition()) {
                    btnPick.setEnabled(false);
                } else {
                    btnPick.setEnabled(true);
                }

                idCCSave = idCC.get(i);
                descCCSave = nombreCC.get(i);

                btnPick.setText("SELECCIONAR : " + descCCSave);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });


        cargarSpinnerEstado();


//        cameraView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initQR();
//            }
//        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scannerQR(v);
//                initQR();
//                initQR2();
                readBarCode();
            }

        });
//        initQR();


        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPick.setEnabled(false);
            }
        });

        btnWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarFragment(ListaFragment.class);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPick.isEnabled() && TextUtils.isEmpty(cont_paquete.getText())) {
                    Toast.makeText(Objects.requireNonNull(getActivity()), "Debe seleccionar un CC y una etiqueta ", Toast.LENGTH_SHORT).show();
                } else {
                    guardarTodo();
                }

            }
        });

        return view;
    }

    public void readBarCode(){
        Intent intent = new Intent(getActivity(), BarcodeCaptureFragment.class);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureFragment.BarcodeObject);

                    int type = barcode.valueFormat;


                    switch (type) {
                        case Barcode.CONTACT_INFO:
                            Barcode.Email[] set = barcode.contactInfo.emails;
                            List<String> answers = new ArrayList<String>();
                            for (Barcode.Email aSet : set) {
                                answers.add(aSet.address);
                                Toast.makeText(getActivity(), "emails " + answers, Toast.LENGTH_LONG).show();
                            }
                            Barcode.PersonName name = barcode.contactInfo.name;
                            String contactName = name.first + " " + name.last;
                            cont_paquete.setText(contactName);
                            etiqueta = barcode.email.address;
                            break;
                        case Barcode.EMAIL:
                            cont_paquete.setText(barcode.email.address);
                            etiqueta = barcode.email.address;
                            break;
                        case Barcode.ISBN:
                            cont_paquete.setText(barcode.rawValue);
                            etiqueta = barcode.rawValue;
                            break;
                        case Barcode.PHONE:
                            cont_paquete.setText(barcode.phone.number);
                            etiqueta = barcode.phone.number;
                            break;
                        case Barcode.PRODUCT:
                            cont_paquete.setText(barcode.rawValue);
                            etiqueta = barcode.rawValue;
                            break;
                        case Barcode.SMS:
                            cont_paquete.setText(barcode.sms.message);
                            etiqueta = barcode.sms.message;
                            break;
                        case Barcode.TEXT:
                            cont_paquete.setText(barcode.rawValue);
                            etiqueta = barcode.rawValue;
                            break;
                        case Barcode.URL:
                            cont_paquete.setText(barcode.url.url);
                            etiqueta = barcode.url.url;
                            break;
                        case Barcode.WIFI:
                            cont_paquete.setText(barcode.wifi.ssid);
                            etiqueta = barcode.wifi.ssid;
                            break;
                        case Barcode.GEO:
                            cont_paquete.setText(barcode.geoPoint.lat + ":" + barcode.geoPoint.lng);
                            etiqueta = barcode.geoPoint.lat + ":" + barcode.geoPoint.lng;
                            break;
                        case Barcode.CALENDAR_EVENT:
                            cont_paquete.setText(barcode.calendarEvent.description);
                            etiqueta = barcode.calendarEvent.description;
                            break;
                        case Barcode.DRIVER_LICENSE:
                            cont_paquete.setText(barcode.driverLicense.licenseNumber);
                            etiqueta = barcode.driverLicense.licenseNumber;
                            break;
                        default:
                            cont_paquete.setText(barcode.rawValue);
                            etiqueta = barcode.rawValue;
                            break;
                    }
                    Toast.makeText(Objects.requireNonNull(getActivity()), R.string.barcode_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Objects.requireNonNull(getActivity()), R.string.barcode_failure, Toast.LENGTH_SHORT).show();
                }
            } else {
                System.out.println("SOUT SEIS " + String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.upload:
                return true;
            case R.id.download:
                VerificarInternet tarea = new VerificarInternet(getActivity(), new VerificarInternet.EntoncesHacer() {
                    @Override
                    public void cuandoHayInternet() {
                        new bajarDatos().execute();
                    }

                    @Override
                    public void cuandoNOHayInternet() {
                        noInternet("Problemas", "No tiene acceso a internet");
                    }
                });
                tarea.execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void guardarTodo(){
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date da = new Date();
        final String fechaFinalDispo = df.format(da);


        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("config_tel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("remember_position_cc",spCC.getSelectedItemPosition());
        editor.putInt("remember_position_prod",spEstado.getSelectedItemPosition());
//        editor.putBoolean("remember_button_check",false);
        editor.apply();


        String estadoP;
        if (spEstado.getSelectedItem().equals("Producción")){
            estadoP = "P";
        }else{
            estadoP = "C";
        }

        paquetesGuardados pq = new paquetesGuardados();
        pq.setEtiquetaGuardado(cont_paquete.getText().toString());
        pq.setDescCCGuardado(descCCSave);
        pq.setIdCCGuardado(idCCSave);
        pq.setProdCons(estadoP);
        pq.setFechaCaptura(fechaFinalDispo);
        pq.setEstadoGuardado(0);
        try{
            long id = MainActivity.myAppDB.myDao().setPaquetes(pq);
            if (id > 0){
                Toast.makeText(Objects.requireNonNull(getActivity()), "Se guardo el paquete ", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e ){
            e.printStackTrace();
        }

    }

    public void scannerQR(View view){
        IntentIntegrator intent = new IntentIntegrator(((Activity)getActivity()));
        intent.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES );
        intent.setOrientationLocked(false);
        intent.setPrompt("Escaneando...");
        intent.setCameraId(0);
        intent.setBeepEnabled(true);
        intent.setBarcodeImageEnabled(true);
        intent.forSupportFragment(MainFragment.this).initiateScan();

    }



/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(Objects.requireNonNull(getActivity()), "Cancelaste el escaneo", Toast.LENGTH_LONG).show();
            }else{
                cont_paquete.setText(result.getContents());
                etiqueta = result.getContents();
            }
        } else{
            Toast.makeText(Objects.requireNonNull(getActivity()), "null ", Toast.LENGTH_LONG).show();

        }
    }*/

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), GET_ACCOUNTS);
        int result1 = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{GET_ACCOUNTS, CAMERA}, REQUEST_GET_ACCOUNT);
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        Toast.makeText(Objects.requireNonNull(getActivity()), "Permission Granted, Now you can access location data and camera", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(Objects.requireNonNull(getActivity()), "Permission Denied, You cannot access location data and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void cargarSpinnerEstado(){
        ArrayList<String> arrEstados = new ArrayList<>();
        arrEstados.add("Producción");
        arrEstados.add("Consumo");

        SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("config_tel", MODE_PRIVATE);
        spinner_adp_estado = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, arrEstados);
        spEstado.setSelection(sharedPref.getInt("remember_position_cc", 0));
        spEstado.setAdapter(spinner_adp_estado);

    }

    public void cargarSpinner(){
        nombreCC = new ArrayList<>();
        idCC = new ArrayList<>();
        final List<CentroCosto> ccList = MainActivity.myAppDB.myDao().getCentroCosto();
        if (ccList.size() > 0){
            for (CentroCosto cc : ccList){
                nombreCC.add(cc.getDescCC());
                idCC.add(cc.getIdCC());
            }
            SharedPreferences sharedPref = Objects.requireNonNull(getActivity()).getSharedPreferences("config_tel", MODE_PRIVATE);
            spinner_adp = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, nombreCC);
            spCC.setSelection(sharedPref.getInt("remember_position_cc", 0));
            spCC.setAdapter(spinner_adp);
        }else{
            nombreCC.add("Ningun centro de costo ");
            idCC.add(0);

            spinner_adp = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, nombreCC);
            spCC.setAdapter(spinner_adp);
        }
        spinner_adp.notifyDataSetChanged();
    }


   private class bajarDatos extends AsyncTask<Void, Integer, Boolean>{
        private ProgressDialog dialogo;

       @Override
       protected void onPreExecute() {
           dialogo = new ProgressDialog(Objects.requireNonNull(getActivity()));
           dialogo.setMessage("Obteniendo centros de costo ...");
           dialogo.setCancelable(false);
           dialogo.show();
       }

       @Override
       protected Boolean doInBackground(Void... voids) {
           APIService apiService = RetrofitClient.getClient().create(APIService.class);
           Call<RespuestaCentroCosto> call = apiService.getCC();
           call.enqueue(new Callback<RespuestaCentroCosto>() {
               @Override
               public void onResponse(Call<RespuestaCentroCosto> call, Response<RespuestaCentroCosto> response) {
                    RespuestaCentroCosto resp = response.body();
                    if (resp != null){
                        if (resp.getEstado() == 1){
                            try{
                                    MainActivity.myAppDB.myDao().deleteCC();
                                    MainActivity.myAppDB.myDao().setCC(resp.getCentroCosto());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else if (resp.getEstado() == 2){
                            System.out.println("error " + resp.getCentroCosto());
                        }

                    }else{
                        System.out.println("error resp null");
                    }
               }

               @Override
               public void onFailure(Call<RespuestaCentroCosto> call, Throwable t) {
                   System.out.println("error 504" + t.getCause());
               }
           });
           return true;
       }

       @Override
       protected void onPostExecute(Boolean aBoolean) {
           super.onPostExecute(aBoolean);
           dialogo.dismiss();
           cargarSpinner();
           noInternet("Listo", "Se descargaron los centros de costo");
       }
   }





    private void noInternet(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        @SuppressLint("InflateParams") View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.empty_alert,null);
        builder.setView(viewInfalted);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cargarSpinner();
            }
        });
        builder.create().show();
    }

    public void cambiarFragment(Class fragmentClass){
        Fragment fragment = null;
        try{
            fragment = (Fragment) fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor_fragment, fragment).addToBackStack(null).commit();

    }
}
