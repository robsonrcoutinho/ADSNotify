package br.com.ifba.adsnotify.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.RecyclerViewAdapter;
import br.com.ifba.adsnotify.adapters.RecyclerViewAdapterDocumentos;
import br.com.ifba.adsnotify.model.ItemObject;

/**
 * Created by Robson on 19/05/2016.
 */
public class DocumentoActivity extends AppCompatActivity {
    private GridLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.documento_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDocumento);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Documentos");
            actionBar.show();
        }


        List<ItemObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(getApplicationContext(), 2);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recycler_view_documento);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapterDocumentos rcAdapter = new RecyclerViewAdapterDocumentos(getApplicationContext(), rowListItem);
        rView.setAdapter(rcAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<ItemObject> getAllItemList() {

        List<ItemObject> allItems = new ArrayList<>();
        allItems.add(new ItemObject("Calendário Acadêmico", R.drawable.calendario));
        allItems.add(new ItemObject("Ementa", R.drawable.ementa));
        allItems.add(new ItemObject("Grade Curricular", R.drawable.grade));
        allItems.add(new ItemObject("Horário Aulas ", R.drawable.horario));
        allItems.add(new ItemObject("Normas Acadêmicas", R.drawable.normas));
        allItems.add(new ItemObject("Projeto Pedagógico", R.drawable.projeto));


        return allItems;

    }
}
