package br.com.ifba.adsnotify.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.ifba.adsnotify.R;


/**
 * Created by Robson on 24/04/2016.
 */
public class RecyclerViewHoldersDoc extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemNome;
    public ImageView ItemImage;
    Context context;


    public RecyclerViewHoldersDoc(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemNome = (TextView)itemView.findViewById(R.id.item_name_doc);
        ItemImage = (ImageView)itemView.findViewById(R.id.item_photo_doc);
    }

    @Override
    public void onClick(View view) {
        context = view.getContext();
        final Intent intent;
        int position = getAdapterPosition();

        switch (position){
            case 0:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);
                /*
                intent = new Intent(context,CalendarioAcademicoPDF.class);
                context.startActivity(intent);*/
                break;
            case 1:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);

                break;
            case 2:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);
                break;
            case 4:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);
                break;
            case 5:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("http://www.eunapolis.ifba.edu.br/objetos/Quadro_de_substituicao_de_horario.pdf"),
                        "text/html");
                context.startActivity(intent);
                break;
        }


    }
}
