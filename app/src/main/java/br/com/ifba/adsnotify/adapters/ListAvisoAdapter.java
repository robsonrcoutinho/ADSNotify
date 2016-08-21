package br.com.ifba.adsnotify.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.Mensagem;

/**
 * Created by Robson on 24/04/2016.
 */
public class ListAvisoAdapter extends BaseAdapter {

    private Context context;
    private List<Mensagem> avisosList;
    private LayoutInflater inflater;
    private int repeatCount = 1;

    public ListAvisoAdapter(Context context,List<Mensagem> avisos) {
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
            layout = inflater.inflate(R.layout.aviso_layout, null);

        final Mensagem data = avisosList.get(position);
         Log.d("Valor Adapter::", data.getAvisoTitle().toString());


        Typeface face = Typeface.createFromAsset(context.getAssets(), "Roboto-BlackItalic.ttf");
        TextView titulo = (TextView)layout.findViewById(R.id.title);
        titulo.setText(data.getAvisoTitle());
        titulo.setTypeface(face);

        Typeface face2 = Typeface.createFromAsset(context.getAssets(), "Roboto-MediumItalic.ttf");
        TextView descricao = (TextView)layout.findViewById(R.id.description);
        descricao.setText(data.getAvisoBody());
        descricao.setTypeface(face2);

        return layout;
    }
}

