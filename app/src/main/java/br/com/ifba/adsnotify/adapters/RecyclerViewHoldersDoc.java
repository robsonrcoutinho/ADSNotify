package br.com.ifba.adsnotify.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.activity.AvaliacaoActivity;
import br.com.ifba.adsnotify.activity.DisciplinaActivity;
import br.com.ifba.adsnotify.activity.DocumentoActivity;
import br.com.ifba.adsnotify.activity.ProfessorActivity;
import br.com.ifba.adsnotify.docactivities.CalendarioAcademicoPDF;

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
                intent = new Intent(context,CalendarioAcademicoPDF.class);
                context.startActivity(intent);
                break;
            case 1:
                Toast.makeText(view.getContext(), "Posicao = " + position, Toast.LENGTH_SHORT).show();

                break;
            case 2:
                Toast.makeText(view.getContext(), "Posicao = " + position, Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(view.getContext(), "Posicao = " + position, Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(view.getContext(), "Posicao = " + position, Toast.LENGTH_SHORT).show();
                break;
        }


    }
}
