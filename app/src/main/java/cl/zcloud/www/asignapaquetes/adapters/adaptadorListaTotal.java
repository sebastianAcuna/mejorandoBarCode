package cl.zcloud.www.asignapaquetes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cl.zcloud.www.asignapaquetes.R;
import cl.zcloud.www.asignapaquetes.clases.paquetesGuardados;

public class adaptadorListaTotal extends RecyclerView.Adapter<adaptadorListaTotal.ViewHolder> {
    private List<paquetesGuardados> items;
    private OnItemClickListener itemClickListener;
    private Context context;

    public adaptadorListaTotal(List<paquetesGuardados> items, Context context, OnItemClickListener listener){
        this.itemClickListener = listener;
        this.items = items;
        this.context = context;
    }

    public interface  OnItemClickListener { void OnItemClick(paquetesGuardados item); }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_paquetes, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position), context,itemClickListener);

    }

    @Override
    public int getItemCount(){ return items.size(); }




    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView lbl_cc, lbl_et,prod_cons,fecha_captura;

        public ViewHolder(View itemView) {
            super(itemView);
            lbl_cc = itemView.findViewById(R.id.centro_costo_lb);
            lbl_et = itemView.findViewById(R.id.paquete_lb);
            prod_cons = itemView.findViewById(R.id.prod_cons);
            fecha_captura = itemView.findViewById(R.id.fecha_captura);

        }

        public void bind(final paquetesGuardados paquetesGuardados, Context context, final OnItemClickListener listener){
            if (paquetesGuardados.getProdCons().equals("P")){
                prod_cons.setTextColor(ContextCompat.getColor(context,R.color.colorUpload));
            }else{
                prod_cons.setTextColor(ContextCompat.getColor(context,R.color.colorDownload));
            }
            lbl_cc.setText(paquetesGuardados.getDescCCGuardado());
            lbl_et.setText(paquetesGuardados.getEtiquetaGuardado());
            prod_cons.setText(paquetesGuardados.getProdCons());
            fecha_captura.setText(paquetesGuardados.getFechaCaptura());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(paquetesGuardados);
                }
            });
        }
    }
}
