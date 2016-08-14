package br.com.ifba.adsnotify.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.OperationCanceledException;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.AvaliacaoListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Avaliacao;
import br.com.ifba.adsnotify.model.Disciplina;
import br.com.ifba.adsnotify.model.Pergunta;
import br.com.ifba.adsnotify.model.OpcaoResposta;
import br.com.ifba.adsnotify.model.Resposta;
import br.com.ifba.adsnotify.model.User;


/**
 * Created by Robson on 18/05/2016.
 */
public class AvaliacaoActivity extends AppCompatActivity{
    private static String TAG = AvaliacaoActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private JSONArray jsonArrayPerguntas;
    private  Avaliacao avaliacao;
    private Pergunta pergunta;
    private OpcaoResposta opcaoResposta;
    private List<OpcaoResposta> listOpcaoRespostas  = new ArrayList<>();
    private List<Pergunta> perguntas = new ArrayList<>();
    private HashMap<String, String> paramsn;

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

    private AccountManager mAccountManager;
    private User user;
    private Disciplina disciplina;
    private List<Disciplina> disciplinasList = new ArrayList<>();
    private String emailuser;
    private boolean dado;
    private int contador = 0;
    private List<Resposta> respostasList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.avaliacao_layout);

        user = ((MyApplication) getApplication()).getUser();
        mAccountManager = AccountManager.get(AvaliacaoActivity.this);
        getAccounts(null);

        final Account[] accounts = mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE);
        String[] name = new String[accounts.length];

        paramsn = new HashMap<String,String>();

        for(int i = 0; i < accounts.length; i++) {
            name[i] = accounts[i].name;
            emailuser = accounts[i].name;
            Log.d("Nome Conta: ", name[i].toString());
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAvaliacao);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if(actionBar!=null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Avaliações");
            actionBar.show();
        }
        carregaDisciplinaCursada(emailuser);

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



    public void getAccounts(View view){
        mAccountManager.getAuthTokenByFeatures(Config.ACCOUNT_TYPE,
                Config.ACCOUNT_TOKEN_TYPE,
                null,
                AvaliacaoActivity.this,
                null,
                null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            Log.i("Script", "MainActivity.getAccounts()");
                            Log.i("Script", "MainActivity.getAccounts() : AccountType = " + bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            Log.i("Script", "MainActivity.getAccounts() : AccountName = " + bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            Log.i("Script", "MainActivity.getAccounts() : Token = " + bundle.getString(AccountManager.KEY_AUTHTOKEN));

                            user.setAccountType(bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            user.setAccountName(bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            user.setToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));


                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (android.accounts.OperationCanceledException e) {
                            e.printStackTrace();
                        }
                    }
                },
                null);
    }

    public void carregaDisciplinaCursada(String email){
        pDialog = new ProgressDialog(this);

        Log.d("EMAIL PARAMETRO", email);
        paramsn.put("email", email);

        pDialog.setMessage("Buscando Disciplinas cursadas...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST,
                Config.DISC_CURSADAS,
                new JSONObject(paramsn),
                new Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "Success DISCIPLINA: " + response.toString());

                        for(int i =0; i<response.length(); i++){
                            try {
                                disciplina = new Disciplina();
                                JSONObject discJSON = (JSONObject)response.get(i);
                                disciplina.setCodigo(discJSON.getString("codigo"));
                                disciplina.setNomeDisciplina(discJSON.getString("nome"));
                                disciplinasList.add(disciplina);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        hidePDialog();
                        carregarAvaliacao();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erro DISCIPLINA", error.toString());
                        hidePDialog();
                    }
                }
        ) ;

        MyApplication.getInstance().addToRequestQueue(req);

    }

    public void carregarAvaliacao(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Redirecionando para Avaliação...");
        pDialog.setCancelable(false);
        pDialog.show();

        avaliacao = new Avaliacao();

        JsonObjectRequest discReq = new JsonObjectRequest (Config.URL_QUESTIONARIO,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();

                        try {
                            avaliacao.setIdAvaliacao(response.getInt("id"));
                            avaliacao.setInicio(response.getString("inicio"));
                            avaliacao.setTermino(response.getString("termino"));

                            String perguntasJSON = response.getString("perguntas");
                            jsonArrayPerguntas = new JSONArray(perguntasJSON);

                            JSONObject objPerguntas;
                            JSONObject jsonOpcaoParse;
                            int pergunta_fechada = 1;

                            if(jsonArrayPerguntas != null) {
                                for(int i = 0; i < jsonArrayPerguntas.length(); i++){
                                    objPerguntas = jsonArrayPerguntas.getJSONObject(i);
                                    pergunta = new Pergunta();
                                    pergunta.setIdPergunta(objPerguntas.getInt("id"));
                                    pergunta.setEnunciado(objPerguntas.getString("enunciado"));
                                    pergunta.setTipoPergunta(objPerguntas.getInt("pergunta_fechada"));
                                    perguntas.add(pergunta);

                                    JSONArray opcaoRespostaJson = objPerguntas.getJSONArray("opcoes_resposta");


                                    if(pergunta_fechada == pergunta.getTipoPergunta()){
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
                                avaliando(0);

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


    public void avaliando(final int posicaoDisciplina) {

        Log.d("Passando aqui", String.valueOf(posicaoDisciplina));

        if (posicaoDisciplina < disciplinasList.size()) {

                listview = (ListView) findViewById(R.id.list);
                btn_prev = (Button) findViewById(R.id.prev);
                btn_next = (Button) findViewById(R.id.next);
                title = (TextView) findViewById(R.id.title);

                btn_prev.setEnabled(false);
                perguntaListVolley = new ArrayList<Pergunta>();


                int val = TOTAL_LIST_ITEMS % NUM_ITEMS_PAGE;
                val = val == 0 ? 0 : 1;
                pageCount = TOTAL_LIST_ITEMS / NUM_ITEMS_PAGE + val;

                for (int i = 0; i < TOTAL_LIST_ITEMS; i++) {
                    perguntaListVolley.add(perguntas.get(i));
                }

                carregaLista(0, posicaoDisciplina);

                btn_next.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if(dado == true){
                            increment++;
                            carregaLista(increment, posicaoDisciplina);
                            checkAtivo(posicaoDisciplina);
                            contador = increment;
                        }else{
                            Toast.makeText(AvaliacaoActivity.this, "Por favor, informe sua resposta!", Toast.LENGTH_SHORT).show();
                            if(increment == 0){
                                carregaLista(increment, posicaoDisciplina);
                            }else if(contador == increment){
                                carregaLista(contador, posicaoDisciplina);
                            }else{
                                carregaLista(increment-1, posicaoDisciplina);
                            }


                        }
                    }
                });

             /*   btn_prev.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        increment--;
                        carregaLista(increment, posicaoDisciplina);
                        checkAtivo(posicaoDisciplina);
                    }
                });*/

        }else{
            btn_next.setText("Finalizar");
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONArray jsonArray = new JSONArray();

                    for(int h = 0; h< respostasList.size(); h++){

                        try {
                            JSONObject json = new JSONObject();
                            json.put("id_resposta", String.valueOf(respostasList.get(h).getIdPerguntaRespondida()));
                            json.put("email", respostasList.get(h).getEmailUsurAvaliador());
                            json.put("campo_resposta", respostasList.get(h).getRespostaUsuário());
                            json.put("id_avaliacao", String.valueOf(respostasList.get(h).getIdAvaliacao()));
                            json.put("id_disciplina", String.valueOf(respostasList.get(h).getIdDiscplinaAvaliada()));
                            jsonArray.put(json);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        enviarAvaliacao(jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    }

            });
        }
    }


    private void carregaLista(int numero, int dPosicao){
        Log.d("Vem", "AQUI: " + dado );


        ArrayList<Pergunta> listPergunta = new ArrayList<>();
        ArrayList<OpcaoResposta> listOpcao = new ArrayList<>();


        title.setText(disciplinasList.get(dPosicao).getCodigo().toString()+ ": Pergunta "+(numero+1)+" de "+ pageCount);


        for(int i=0; i<listOpcaoRespostas.size();i++){
            listOpcao.add(listOpcaoRespostas.get(i));
        }

        int comece = numero * NUM_ITEMS_PAGE;
        for(int i=comece;i<(comece)+NUM_ITEMS_PAGE;i++){
            if(i<perguntaListVolley.size()){
                listPergunta.add(perguntaListVolley.get(i));
            }else{
                break;
            }
        }
        final int posicaoDisciplina = dPosicao;

        avaliacaoListAdapter = new AvaliacaoListAdapter(this,listPergunta,listOpcao){
            @Override
            public void setData(boolean data, Resposta resposta) {
                super.setData(data, resposta);
                dado = data;
                if(resposta != null) {
                    if(respostasList.isEmpty()) {
                        resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                        resposta.setEmailUsurAvaliador(emailuser);
                        resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                        respostasList.add(resposta);
                    }else{
                        if (!respostasList.contains(resposta)) {
                            resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                            resposta.setEmailUsurAvaliador(emailuser);
                            resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                            respostasList.add(resposta);
                            Log.d("Entrou no IF: ", resposta.getIdentificador());
                        } else {
                            Log.d("ANTES RESPOSTA",resposta.getRespostaUsuário());
                            Log.d("ANTES ID", resposta.getRespostaUsuário());
                            respostasList.remove(resposta);
                            resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                            resposta.setEmailUsurAvaliador(emailuser);
                            resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                            respostasList.add(resposta);

                            Log.d("DEPOIS RESPOSTA", resposta.getRespostaUsuário());
                            Log.d("DEPOIS ID", resposta.getIdentificador());

                        }
                    }
                }
            }
        };
        listview.setAdapter(avaliacaoListAdapter);
    }


    private void checkAtivo(int posicaoDisciplina){

        if(increment+1 == pageCount){
            int tamanhoDisc = disciplinasList.size();
            if(posicaoDisciplina < tamanhoDisc) {
                posicaoDisciplina++;

                btn_next.setEnabled(true);
                final int finalPosicaoDisciplina = posicaoDisciplina;
                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        avaliando(finalPosicaoDisciplina);
                        increment = 0;
                        btn_next.setEnabled(true);
                        TOTAL_LIST_ITEMS = perguntas.size();

                    }


                });
            }
            btn_prev.setEnabled(false);
        }
        else if(increment == 0){
            btn_prev.setEnabled(false);
        }
        else{
            btn_prev.setEnabled(false);
            btn_next.setEnabled(true);

        }
    }


    private void enviarAvaliacao(final JSONArray jsonArray) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray array = new JSONArray();
        json.putOpt("email",emailuser);
        json.putOpt("respostas", jsonArray);
        array.put(json);
        Log.d("JSONArray Respostas", array.toString());
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando Avaliações...");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.POST,
                Config.RESPOSTAS_ARRAY,
                array,
                new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("PERGUNTAS :"," ENVIADAS COM SUCESSO");
                    pDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("PERGUNTAS :"," ERRO AO ENVIAR");
                    pDialog.dismiss();
                }
            });
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }

}



