package br.com.ifba.adsnotify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.Disciplina;

/**
 * Classe adapter usada para mostragem de disciplinas na view
 * @Author Robson Coutinho
 * @version 1.0
 * @since 27/05/2016.
 */

public class DisciplinaListAdapter extends BaseAdapter {
    private static final String TAG = DisciplinaListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Disciplina> disciplinas;
    private LayoutInflater inflater;
    private Intent intent;


    public DisciplinaListAdapter(Activity activity, List<Disciplina> disciplinas){
        this.activity = activity;
        this.disciplinas = disciplinas;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Object getItem(int position) {
        return disciplinas.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_disciplina, null);
            final Context context = convertView.getContext();

        TextView nomeDisciplina = (TextView) convertView.findViewById(R.id.idNomeDisciplina);
        TextView cargaHoraria = (TextView) convertView.findViewById(R.id.idCargaHoraria);
        TextView codigo = (TextView) convertView.findViewById(R.id.idCodigoDisciplina);

        Disciplina disciplina = disciplinas.get(position);

        nomeDisciplina.setText(disciplina.getNomeDisciplina());
        cargaHoraria.setText(disciplina.getCargaHoraria());
        codigo.setText(disciplina.getCodigo());

        /*
        * URL da ementa é jogada em um evento de click, onde o browser do dispositivo mobile será chamado
        * */
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URLementa = disciplinas.get(position).getEmenta();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(URLementa),
                        "text/html");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
