package br.com.ifba.adsnotify.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.model.ItemObjectMenu;

/**
 * Classe RecyclerView usada para criação do Menu
 * @Author Robson Coutinho
 * @version 1.0
 * @since  24/04/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<ItemObjectMenu> itemList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<ItemObjectMenu> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.itemNome.setText(itemList.get(position).getName());
        holder.ItemImage.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}