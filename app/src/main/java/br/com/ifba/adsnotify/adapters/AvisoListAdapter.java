package br.com.ifba.adsnotify.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.Mensagem;

/**
 * Classe adapter usada para mostragem de avisos na view
 * @Author Robson Coutinho
 * @version 1.0
 * @since 27/05/2016.
 */

public class AvisoListAdapter extends BaseAdapter {

    private Context context;
    private List<Mensagem> avisosList;
    private LayoutInflater inflater;
    private int repeatCount = 1;

    public AvisoListAdapter(Context context, List<Mensagem> avisos) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.avisosList = avisos;
    }

    @Override
    public int getCount() {
        return avisosList.size() * repeatCount;
    }


    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (convertView == null)
            layout = inflater.inflate(R.layout.feed, null);

        final Mensagem data = avisosList.get(position);
         Log.d("Valor Adapter::", data.getAvisoTitle().toString());

        ImageView image = (ImageView) layout.findViewById(R.id.imageAvisoList);
        image.setImageResource(R.drawable.aviso_icon_notification);

        TextView titulo = (TextView)layout.findViewById(R.id.tituloAvisoFeed);
        titulo.setText(data.getAvisoTitle());


        TextView dataAviso = (TextView)layout.findViewById(R.id.timestamp);
        dataAviso.setText(data.getCreateAviso());

        TextView corpoAviso = (TextView)layout.findViewById(R.id.idTextoAviso);
        corpoAviso.setText("     "  + data.getAvisoBody());
        return layout;
    }
}

