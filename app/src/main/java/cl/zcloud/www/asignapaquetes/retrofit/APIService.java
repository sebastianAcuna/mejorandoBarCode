package cl.zcloud.www.asignapaquetes.retrofit;

import cl.zcloud.www.asignapaquetes.clases.GSON.RespuestaCentroCosto;
import cl.zcloud.www.asignapaquetes.clases.Respuesta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {
    @POST("subir_asignaciones.php")
    Call<Respuesta> setAsign();


    @GET("obtener_cc.php")
    Call<RespuestaCentroCosto> getCC();
}
