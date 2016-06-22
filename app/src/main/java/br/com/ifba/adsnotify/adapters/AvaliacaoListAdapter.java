package br.com.ifba.adsnotify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.Avaliacao;
import br.com.ifba.adsnotify.model.Disciplina;
import br.com.ifba.adsnotify.model.Pergunta;

/**
 * Created by Robson on 27/05/2016.
 */
public class AvaliacaoListAdapter extends BaseAdapter {
    private static final String TAG = AvaliacaoListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Pergunta> perguntas;
    private LayoutInflater inflater;




    public AvaliacaoListAdapter(Activity activity, List<Pergunta> perguntas){
        this.activity = activity;
        this.perguntas = perguntas;
    }

    @Override
    public int getCount() {
        return perguntas.size();
    }

    @Override
    public Object getItem(int position) {
        return perguntas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            convertView = inflater.inflate(R.layout.list_row_avaliacao, null);

            final Context context = convertView.getContext();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
