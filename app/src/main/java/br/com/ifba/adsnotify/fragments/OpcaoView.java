package br.com.ifba.adsnotify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.RecyclerViewAdapter;
import br.com.ifba.adsnotify.model.ItemObjectMenu;

/**
 * Classe responsavel pela geraçao das opções do menu
 * @Author Robson Coutinho
 * @version 1.0
 * @since 22/04/2016.
 */

public class OpcaoView extends Fragment {
    private GridLayoutManager lLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.opcao_layout, container, false);


        List<ItemObjectMenu> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getActivity().getApplicationContext(), 2);

        RecyclerView rView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), rowListItem);
        rView.setAdapter(rcAdapter);



        return rootView;
    }
    private List<ItemObjectMenu> getAllItemList(){

        List<ItemObjectMenu> allItems = new ArrayList<>();
        allItems.add(new ItemObjectMenu("Avaliações", R.drawable.avaliacao));
        allItems.add(new ItemObjectMenu("Disciplinas", R.drawable.disciplinas));
        allItems.add(new ItemObjectMenu("Documentos", R.drawable.doc));
        allItems.add(new ItemObjectMenu("Professores", R.drawable.prof));

        return allItems;
    }
}

