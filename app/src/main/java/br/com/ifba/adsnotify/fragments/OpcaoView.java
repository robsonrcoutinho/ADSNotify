package br.com.ifba.adsnotify.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.RecyclerViewAdapter;
import br.com.ifba.adsnotify.model.ItemObject;


/**
 * Created by Robson on 22/04/2016.
 */

public class OpcaoView extends Fragment {
    private GridLayoutManager lLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.opcao_layout, container, false);


        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getActivity().getApplicationContext(), 4);

        RecyclerView rView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), rowListItem);
        rView.setAdapter(rcAdapter);



        return rootView;
    }
    private List<ItemObject> getAllItemList(){

        List<ItemObject> allItems = new ArrayList<>();
        allItems.add(new ItemObject("Avaliações", R.drawable.um));
        allItems.add(new ItemObject("Avisos", R.drawable.dois));
        allItems.add(new ItemObject("Disciplinas", R.drawable.tres));
        allItems.add(new ItemObject("Documentos", R.drawable.cinco));
        allItems.add(new ItemObject("Professores", R.drawable.quatro));

        return allItems;
    }
}

