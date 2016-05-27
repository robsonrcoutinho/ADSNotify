package br.com.ifba.adsnotify.docactivities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;

import br.com.ifba.adsnotify.R;

/**
 * Created by Robson on 20/05/2016.
 */

/*
* Classe não está sendo usada
* A chamada que seria feita nessa classe está sendo feita em adapters/RecyclerViewHoldersDoc.class
* */
public class CalendarioAcademicoPDF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.doc_calendario_pdf_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDocumentoCalendar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Calendário Acadêmico");
            actionBar.show();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + "http://www.bu.ufsc.br/ArtigoCientifico.pdf"),
                "text/html");
        startActivity(intent);
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

}
