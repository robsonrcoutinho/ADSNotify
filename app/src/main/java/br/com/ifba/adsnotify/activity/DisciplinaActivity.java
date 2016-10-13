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
import java.util.Collections;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.DisciplinaListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Disciplina;

/**
 * Classe usada para buscar e mostrar visualização de todas as disciplinas do curso
 * @Author Robson Coutinho
 * @version 1.0
 * @since 19/05/2016.
 */
public class DisciplinaActivity extends AppCompatActivity {
    private static final String TAG = DisciplinaActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Disciplina> discList = new ArrayList<Disciplina>();
    private ListView listView;
    private DisciplinaListAdapter adapter;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.disciplina_layout);

        image = (ImageView) findViewById(R.id.falhaLoginDisciplina);
        image.setVisibility(View.GONE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDisciplina);

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Disciplinas");
            actionBar.show();
        }

        listView = (ListView) findViewById(R.id.listDisciplinas);
        adapter = new DisciplinaListAdapter(this, discList);
        listView.setAdapter(adapter);

        carregaDisciplina();

        if(discList.size() == 0 || discList== null){
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    image.setVisibility(View.GONE);
                    carregaDisciplina();
                    Log.d("Disciplina", "onTouch image");
                    return false;
                }
            });

        }
    }


    /*
    * Metodo usado para fazer a solicicação de todas as disciplinas
    * */
    public void carregaDisciplina(){
    image.setVisibility(View.GONE);

    pDialog = new ProgressDialog(this);
    pDialog.setMessage("Carregando Disciplinas...");
    pDialog.setCancelable(false);
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
                            disc.setEmenta(Config.ROOT+obj.getString("ementa"));

                            discList.add(disc);
                            Collections.sort(discList,disc);

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
                Toast.makeText(DisciplinaActivity.this,
                        "Erro ao tentar conectar! Verifique sua conexão",
                        Toast.LENGTH_LONG).show();
                image.setVisibility(View.VISIBLE);

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










