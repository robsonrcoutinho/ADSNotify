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
import br.com.ifba.adsnotify.model.Professor;

/**
 * Classe adapter usada para mostragem de Professores na view
 * @Author Robson Coutinho
 * @version 1.0
 * @since 27/05/2016.
 */
public class ProfessorListAdapter extends BaseAdapter {
    private static final String TAG = ProfessorListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Professor> professores;
    private LayoutInflater inflater;
    private Intent intent;


    public ProfessorListAdapter(Activity activity, List<Professor> professores){
        this.activity = activity;
        this.professores = professores;
    }

    @Override
    public int getCount() {
        return professores.size();
    }

    @Override
    public Object getItem(int position) {
        return professores.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_professor, null);
        final Context context = convertView.getContext();

        TextView nomeProfessor = (TextView) convertView.findViewById(R.id.idNomeProfessor);
        TextView matricula = (TextView) convertView.findViewById(R.id.idMatricula);

        Professor prof = professores.get(position);

        matricula.setText(prof.getMatricula());
        nomeProfessor.setText(prof.getNome());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URLcurriculo = professores.get(position).getCurriculo();
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(URLcurriculo),
                        "text/html");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}

