package br.com.ifba.adsnotify.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.ifba.adsnotify.R;

/**
 * Created by Robson on 24/04/2016.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView itemNome;
    public ImageView ItemImage;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemNome = (TextView)itemView.findViewById(R.id.item_name);
        ItemImage = (ImageView)itemView.findViewById(R.id.item_photo);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clique posição = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
