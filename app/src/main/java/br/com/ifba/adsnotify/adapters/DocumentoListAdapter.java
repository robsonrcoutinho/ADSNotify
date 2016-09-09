package br.com.ifba.adsnotify.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.model.Documento;

/**
 * Classe adapter usada para mostragem de Documentos na view
 * @Author Robson Coutinho
 * @version 1.0
 * @since 27/05/2016.
 */
public class DocumentoListAdapter extends BaseAdapter {
    private static final String TAG = DocumentoListAdapter.class.getSimpleName();
    private Activity activity;
    private List<Documento> docs;
    private LayoutInflater inflater;
    private Intent intent;


    public DocumentoListAdapter(Activity activity, List<Documento> docs){
        this.activity = activity;
        this.docs = docs;
    }

    @Override
    public int getCount() {
        return docs.size();
    }

    @Override
    public Object getItem(int position) {
        return docs.get(position);
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
            convertView = inflater.inflate(R.layout.list_row_documento, null);
            final Context context = convertView.getContext();

        TextView tituto = (TextView) convertView.findViewById(R.id.idTituloDocumento);
        ImageView image =(ImageView) convertView.findViewById(R.id.thumbnail);

        Documento doc = docs.get(position);


        image.setImageResource(R.drawable.documento);


        tituto.setText(doc.getTitulo());



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String URLdoc = Config.ROOT_DOC;
                URLdoc+=docs.get(position).getUrl();

                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(URLdoc),
                        "text/html");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
