package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

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
 * Created by Robson on 19/05/2016.
 */
public class DocumentoActivity extends AppCompatActivity {
    private static final String TAG = DocumentoActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Documento> docList = new ArrayList<Documento>();
    private ListView listView;
    private DocumentoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.documento_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDocumento);
        setSupportActionBar(toolbar);
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

        pDialog = new ProgressDialog(this);
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
                                doc.setUrl(obj.getString("url"));
                                docList.add(doc);

                                Log.d(TAG, String.valueOf(i));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, docList.get(i).getTitulo());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

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
