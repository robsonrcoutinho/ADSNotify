package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.AvaliacaoListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Avaliacao;
import br.com.ifba.adsnotify.model.Pergunta;
import br.com.ifba.adsnotify.model.OpcaoResposta;
import br.com.ifba.adsnotify.model.Questionario;


/**
 * Created by Robson on 18/05/2016.
 */
public class AvaliacaoActivity extends AppCompatActivity {
    private static String TAG = AvaliacaoActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private JSONArray jsonArrayPerguntas;
    private  Avaliacao avaliacao;
    private Pergunta pergunta;
    private OpcaoResposta opcaoResposta;
    private List<OpcaoResposta> listOpcaoRespostas  = new ArrayList<>();
    private List<Pergunta> perguntas = new ArrayList<>();
    private Questionario questionario = new Questionario();


    private ListView listview;
    private TextView title;
    private Button btn_prev;
    private Button btn_next;
    private ArrayList<Pergunta> perguntaListVolley;
    private AvaliacaoListAdapter  avaliacaoListAdapter;
    private int pageCount ;
    private int increment = 0;
    public int TOTAL_LIST_ITEMS = 0;
    public int NUM_ITEMS_PAGE   = 1;


    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.avaliacao_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAvaliacao);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Avaliações");
            actionBar.show();
        }

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Carregando Avaliação...");
        pDialog.setCancelable(false);
        pDialog.show();

        avaliacao = new Avaliacao();

        JsonObjectRequest discReq = new JsonObjectRequest (Config.URL_QUESTIONARIO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": RESPONSE", response.toString());
                        hidePDialog();

                        try {
                            avaliacao.setIdSemestre(response.getLong("id"));
                            avaliacao.setInicio(response.getString("inicio"));
                            avaliacao.setTermino(response.getString("termino"));

                            String perguntasJSON = response.getString("perguntas");
                            jsonArrayPerguntas = new JSONArray(perguntasJSON);

                            JSONObject objPerguntas;
                            JSONObject jsonOpcaoParse;
                            int pergunta_fechada = 1;
                            int pergunta_aberta = 0;



                            if(jsonArrayPerguntas != null) {
                                for(int i = 0; i < jsonArrayPerguntas.length(); i++){
                                    objPerguntas = jsonArrayPerguntas.getJSONObject(i);
                                    pergunta = new Pergunta();
                                    pergunta.setIdPergunta(objPerguntas.getInt("id"));
                                    pergunta.setEnunciado(objPerguntas.getString("enunciado"));
                                    pergunta.setTipoPergunta(objPerguntas.getInt("pergunta_fechada"));
                                    perguntas.add(pergunta);

                                    JSONArray opcaoRespostaJson = objPerguntas.getJSONArray("opcoes_resposta");
                                   // Log.d("OBJETO opcao_resposta",opcaoRespostaJson.toString());

                                    if(pergunta_fechada == pergunta.getTipoPergunta()){
                                       // Log.d("RECEBI DENTRO IF:",String.valueOf(pg));
                                        for(int j = 0 ; j< opcaoRespostaJson.length(); j++) {
                                            opcaoResposta = new OpcaoResposta();
                                            jsonOpcaoParse = opcaoRespostaJson.getJSONObject(j);
                                            opcaoResposta.setIdOpcao(jsonOpcaoParse.getLong("id"));
                                            opcaoResposta.setResposta(jsonOpcaoParse.getString("resposta_opcao"));
                                            opcaoResposta.setIdPergunta(jsonOpcaoParse.getInt("pergunta_id"));
                                            listOpcaoRespostas.add(opcaoResposta);
                                        }

                                    }

                                }
                                TOTAL_LIST_ITEMS = perguntas.size();

                                listview = (ListView)findViewById(R.id.list);
                                btn_prev     = (Button)findViewById(R.id.prev);
                                btn_next     = (Button)findViewById(R.id.next);
                                title    = (TextView)findViewById(R.id.title);

                                btn_prev.setEnabled(false);
                                perguntaListVolley = new ArrayList<Pergunta>();

                                int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
                                val = val==0?0:1;
                                pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;

                                for(int i=0;i<TOTAL_LIST_ITEMS;i++){
                                    perguntaListVolley.add(perguntas.get(i));
                                }

                                carregaLista(0);

                                btn_next.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {

                                        increment++;
                                        carregaLista(increment);
                                        checkAtivo();
                                    }
                                });

                                btn_prev.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        increment--;
                                        carregaLista(increment);
                                        checkAtivo();
                                    }
                                });

                            }else {
                                Toast.makeText(AvaliacaoActivity.this, "Não há avaliações disponiveis no momento...",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ERROR JSON",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error Avaliacao: " + error.getMessage());
                Toast.makeText(AvaliacaoActivity.this, "Erro ao buscar servidor...",Toast.LENGTH_SHORT).show();
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


    private void checkAtivo()
    {
        if(increment+1 == pageCount)
        {
            btn_next.setEnabled(false);
        }
        else if(increment == 0)
        {
            btn_prev.setEnabled(false);
        }
        else
        {
            btn_prev.setEnabled(true);
            btn_next.setEnabled(true);
        }
    }


    private void carregaLista(int numero){

        ArrayList<Pergunta> listPergunta = new ArrayList<>();
        ArrayList<OpcaoResposta> listOpcao = new ArrayList<>();
        title.setText("Pergunta "+(numero+1)+" de "+ pageCount);

        for(int i=0; i<listOpcaoRespostas.size();i++){
            listOpcao.add(listOpcaoRespostas.get(i));
        }

        int comece = numero * NUM_ITEMS_PAGE;
        for(int i=comece;i<(comece)+NUM_ITEMS_PAGE;i++){
            if(i<perguntaListVolley.size()){
                listPergunta.add(perguntaListVolley.get(i));
            }
            else
            {
                break;
            }
        }
        avaliacaoListAdapter = new AvaliacaoListAdapter(this,listPergunta,listOpcao);
        listview.setAdapter(avaliacaoListAdapter);
    }

}



