package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.DocumentoListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Documento;

/**
 * Classe usada para buscar e mostrar visualização de todos os documentos do curso
 * @Author Robson Coutinho
 * @version 1.0
 * @since 19/05/2016.
 */

public class DocumentoActivity extends AppCompatActivity {
    private static final String TAG = DocumentoActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Documento> docList = new ArrayList<Documento>();
    private ListView listView;
    private DocumentoListAdapter adapter;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.documento_layout);

        image = (ImageView) findViewById(R.id.falhaLoginDoc);
        image.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDocumento);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Documentos");
            actionBar.show();
        }

        listView = (ListView) findViewById(R.id.listDocumentos);
        adapter = new DocumentoListAdapter(this, docList);
        listView.setAdapter(adapter);
        carregaDocumento();

        if(docList.size() == 0 || docList== null){
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    image.setVisibility(View.GONE);
                    carregaDocumento();
                    Log.d("Disciplina", "onTouch image");
                    return false;
                }
            });

        }


    }
    /*
    * Metodo usado para solicitar lista de documetnos online
    * */
    public void carregaDocumento(){
        image.setVisibility(View.GONE);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Carregando Documentos...");
        pDialog.show();

        JsonArrayRequest docReq = new JsonArrayRequest(Config.URL_DOCS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Documento doc = new Documento();

                                doc.setTitulo(obj.getString("titulo"));
                                doc.setUrl(Config.ROOT+obj.getString("url"));
                                docList.add(doc);

                                Log.d(TAG, String.valueOf(i));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(DocumentoActivity.this,"Erro ao tentar conectar! Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
                image.setVisibility(View.VISIBLE);

            }
        });

        MyApplication.getInstance().addToRequestQueue(docReq);    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


}
