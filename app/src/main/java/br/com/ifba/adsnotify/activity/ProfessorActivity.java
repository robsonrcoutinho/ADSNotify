package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
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
import br.com.ifba.adsnotify.adapters.ProfessorListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Professor;

/**
 * Created by Robson on 19/05/2016.
 */
public class ProfessorActivity extends AppCompatActivity {
    private static final String TAG = ProfessorActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private List<Professor> profList = new ArrayList<Professor>();
    private ListView listView;
    private ProfessorListAdapter adapter;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.professor_layout);

        image = (ImageView) findViewById(R.id.falhaLoginProf);
        image.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProf);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Professores");
            actionBar.show();
        }

        listView = (ListView) findViewById(R.id.listProfs);
        adapter = new ProfessorListAdapter(this, profList);
        listView.setAdapter(adapter);

        carregaProfessor();

        if(profList.size() == 0 || profList== null){
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    carregaProfessor();
                    Log.d("Disciplina", "onTouch image");
                    return false;
                }
            });

        }


    }

    public void carregaProfessor(){
        image.setVisibility(View.GONE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Carregando Professores...");
        pDialog.show();


        JsonArrayRequest profReq = new JsonArrayRequest(Config.URL_PROFESSORES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG,response.toString());
                hidePDialog();

                for(int i = 0 ; i<= response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        Professor prof = new Professor();

                        prof.setMatricula(obj.getString("matricula"));
                        prof.setNome(obj.getString("nome"));
                        prof.setCurriculo(obj.getString("curriculo"));

                        profList.add(prof);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter.notifyDataSetChanged();
                }
            }
          }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
                Toast.makeText(ProfessorActivity.this, "Erro ao tentar conectar! Verifique sua conexão",
                        Toast.LENGTH_SHORT).show();
                image.setVisibility(View.VISIBLE);
            }
        });

        MyApplication.getInstance().addToRequestQueue(profReq);
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



