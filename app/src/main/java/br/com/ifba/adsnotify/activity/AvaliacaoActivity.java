package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.AvaliacaoListAdapter;
import br.com.ifba.adsnotify.adapters.DisciplinaListAdapter;
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
                          int par = 0;
                          if(jsonArrayPerguntas != null) {
                          for(int i = 0; i < jsonArrayPerguntas.length(); i++){
                                  objPerguntas = jsonArrayPerguntas.getJSONObject(i);
                                  pergunta = new Pergunta();
                                  pergunta.setIdPergunta(objPerguntas.getInt("id"));
                                  pergunta.setEnunciado(objPerguntas.getString("enunciado"));
                                  pergunta.setTipoPergunta(objPerguntas.getInt("pergunta_fechada"));
                                  perguntas.add(pergunta);

                              JSONArray opcaoRespostaJson = objPerguntas.getJSONArray("opcoes_resposta");

                              if(par == 0){
                                  for(int j = 0 ; j< opcaoRespostaJson.length(); j++) {
                                      opcaoResposta = new OpcaoResposta();
                                      jsonOpcaoParse = opcaoRespostaJson.getJSONObject(j);
                                      opcaoResposta.setIdOpcao(jsonOpcaoParse.getLong("id"));
                                      opcaoResposta.setResposta(jsonOpcaoParse.getString("resposta_opcao"));
                                      opcaoResposta.setIdPergunta(jsonOpcaoParse.getLong("pergunta_id"));
                                      listOpcaoRespostas.add(opcaoResposta);

                                      Log.d("DENTRO OPCAO RESPOSTA:", listOpcaoRespostas.get(j).getResposta());
                                  }

                              }

                              }

                             /* listView = (ListView) findViewById(R.id.listAvaliacao);
                              adapter = new AvaliacaoListAdapter(AvaliacaoActivity.this, perguntas);
                              listView.setAdapter(adapter);*/

                              iniciaView(perguntas,listOpcaoRespostas);

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


   public void iniciaView( List<Pergunta> perguntasList, List<OpcaoResposta> respostaList){
       /*TextView tv1 = new TextView(this);
       tv1.setText(perguntasList.get(0).getEnunciado());
       tv1.setTop(5);
       tv1.setTextSize(15);
       tv1.setGravity(Gravity.NO_GRAVITY);

       TextView tv2 = new TextView(this);
       tv2.setTextSize(15);
       tv1.setTop(5);
       tv2.setGravity(Gravity.NO_GRAVITY);
       tv2.setText(perguntasList.get(1).getEnunciado());

       TextView tv3 = new TextView(this);
       tv3.setTextSize(15);
       tv1.setTop(5);
       tv3.setGravity(Gravity.NO_GRAVITY);
       tv3.setText(perguntasList.get(2).getEnunciado());

       LinearLayout ll = new LinearLayout(this);
       ll.setOrientation(LinearLayout.VERTICAL);
       ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
               LinearLayout.LayoutParams.MATCH_PARENT));
       ll.setGravity(Gravity.NO_GRAVITY);
       ll.addView(tv1);
       ll.addView(tv2);
       ll.addView(tv3);
       setContentView(ll);*/

        }



   }



