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
import br.com.ifba.adsnotify.adapters.DisciplinaListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Disciplina;

/**
 * Created by Robson on 19/05/2016.
 */
public class DisciplinaActivity extends AppCompatActivity {
    private static final String TAG = DisciplinaActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Disciplina> discList = new ArrayList<Disciplina>();
    private ListView listView;
    private DisciplinaListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.disciplina_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDisciplina);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Disciplinas");
            actionBar.show();
        }


        listView = (ListView) findViewById(R.id.listDisciplinas);
        adapter = new DisciplinaListAdapter(this, discList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Carregando Disciplinas...");
        pDialog.show();

        JsonArrayRequest discReq = new JsonArrayRequest(Config.URL_DISCIPLINAS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Disciplina disc = new Disciplina();
                                disc.setNomeDisciplina(obj.getString("nome"));
                                disc.setCargaHoraria(obj.getString("carga_horaria") + " Horas");
                                disc.setCodigo(obj.getString("codigo"));
                                disc.setEmenta(obj.getString("ementa"));

                                discList.add(disc);

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

            }
        });


        MyApplication.getInstance().addToRequestQueue(discReq);
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










