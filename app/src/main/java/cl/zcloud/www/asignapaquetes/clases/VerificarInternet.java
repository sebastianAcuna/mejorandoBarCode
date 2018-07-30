package cl.zcloud.www.asignapaquetes.clases;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

public class VerificarInternet extends AsyncTask<Void, Void, Boolean> {
    private ProgressDialog dialog;
    private Context context;
    private EntoncesHacer accion;

    public VerificarInternet(Context context, EntoncesHacer accion ) {
        this.context = context;
        this.accion = accion;
    }


    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Verificando conexión a Internet");
        dialog.setCancelable(false);
        dialog.show();
    }


    public interface EntoncesHacer {
        void cuandoHayInternet();
        void cuandoNOHayInternet();
    }
    @Override
    protected Boolean doInBackground(Void... arg0) {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("ping -c 2 -w 4  www.google.cl");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException | InterruptedException e){ e.printStackTrace(); }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean resultado) {
        if (resultado) {
            dialog.dismiss();
            dialog = null;
            accion.cuandoHayInternet();
        } else {
            dialog.dismiss();
            dialog = null;
            accion.cuandoNOHayInternet();
        }


    }
}