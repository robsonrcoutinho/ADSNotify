package br.com.ifba.adsnotify.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.OperationCanceledException;
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
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Classe para realização de avaliações através do aplicativo
 * @Author Robson Coutinho
 * @version 1.0
 * @since 018/05/2016.
 */

public class AvaliacaoActivity extends AppCompatActivity {
    private static String TAG = AvaliacaoActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private JSONArray jsonArrayPerguntas;
    private Avaliacao avaliacao;
    private Pergunta pergunta;
    private OpcaoResposta opcaoResposta;
    private List<OpcaoResposta> listOpcaoRespostas = new ArrayList<>();
    private List<Pergunta> perguntas = new ArrayList<>();
    private HashMap<String, String> paramsn;

    private ListView listview;
    private TextView title;
    private Button btn_next;
    private ArrayList<Pergunta> perguntaListVolley;
    private AvaliacaoListAdapter avaliacaoListAdapter;
    private int pageCount;
    private int increment = 0;
    public int TOTAL_LIST_ITEMS = 0;
    public int NUM_ITEMS_PAGE = 1;

    private AccountManager mAccountManager;
    private User user;
    private Disciplina disciplina;
    private List<Disciplina> disciplinasList = new ArrayList<>();
    private String emailuser;
    private boolean dado;
    private int contador = 0;
    private LinearLayout ll;
    private List<Resposta> respostasList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedinstanceState) {
        super.onCreate(savedinstanceState);
        setContentView(R.layout.avaliacao_layout);

        user = ((MyApplication) getApplication()).getUser();
        mAccountManager = AccountManager.get(AvaliacaoActivity.this);
        getAccounts(null);
        btn_next = (Button) findViewById(R.id.next);

        final Account[] accounts = mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE);
        String[] name = new String[accounts.length];

        paramsn = new HashMap<String, String>();

        ll = (LinearLayout) findViewById(R.id.llRelativeAvaliacao);

        for (int i = 0; i < accounts.length; i++) {
            name[i] = accounts[i].name;
            emailuser = accounts[i].name;
            Log.d("Nome Conta: ", name[i].toString());
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAvaliacao);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
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

    /*
    * Metodo usadado para obter uma conta do AccountManager Android
    * */
    public void getAccounts(View view) {
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

    /*
    * Metodo usado para buscar disciplinas cursadas pelo usuário
    * @param email String - consultar perfil e buscar disciplinas vinculadas
    * */
    public void carregaDisciplinaCursada(String email) {
        pDialog = new ProgressDialog(this);
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

                        if (response.length() > 0) {

                            try {
                            for (int i = 0; i < response.length(); i++) {
                                    disciplina = new Disciplina();
                                    JSONObject discJSON = (JSONObject) response.get(i);
                                    if(!discJSON.getString("codigo").isEmpty()){
                                        if(!discJSON.getString("nome").isEmpty()){
                                            disciplina.setCodigo(discJSON.getString("codigo"));
                                            disciplina.setNomeDisciplina(discJSON.getString("nome"));
                                            disciplinasList.add(disciplina);
                                        }
                                    }

                            }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            hidePDialog();
                            carregarAvaliacao();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast = Toast.makeText(AvaliacaoActivity.this, "Não há avaliações ou Perfil não Aluno!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        startActivity(new Intent(AvaliacaoActivity.this, MainActivity.class));
                        Log.d("Erro DISCIPLINA", error.toString());
                        hidePDialog();
                    }
                }
        );

        MyApplication.getInstance().addToRequestQueue(req);

    }

    /*
    * Metodo será chamado assim que @carregaDisciplinaCursada(String email) for processada
    * metodo carrega as perguntas cadastradas para usuários
    * */
    public void carregarAvaliacao() {
        ll.setVisibility(View.VISIBLE);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Redirecionando para Avaliação...");
        pDialog.setCancelable(false);
        pDialog.show();

        avaliacao = new Avaliacao();

        JsonObjectRequest discReq = new JsonObjectRequest(Config.URL_QUESTIONARIO,
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

                            if (jsonArrayPerguntas != null) {
                                for (int i = 0; i < jsonArrayPerguntas.length(); i++) {
                                    objPerguntas = jsonArrayPerguntas.getJSONObject(i);
                                    pergunta = new Pergunta();
                                    pergunta.setIdPergunta(objPerguntas.getInt("id"));
                                    pergunta.setEnunciado(objPerguntas.getString("enunciado"));
                                    pergunta.setTipoPergunta(objPerguntas.getInt("pergunta_fechada"));
                                    perguntas.add(pergunta);

                                    JSONArray opcaoRespostaJson = objPerguntas.getJSONArray("opcoes_resposta");


                                    if (pergunta_fechada == pergunta.getTipoPergunta()) {
                                        for (int j = 0; j < opcaoRespostaJson.length(); j++) {
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

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("ERROR JSON", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error Avaliacao: " + error.getMessage());
                Toast.makeText(AvaliacaoActivity.this, "Erro ao buscar servidor...", Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        });
        MyApplication.getInstance().addToRequestQueue(discReq);
    }

    /*
    *Metodo que faz uma paginação dinâmica das perguntas e disciplinas
    * Tendo um botão que permite que o usuario navegue por todas as perguntas cadastradas, respondendo-as
    * */
    public void avaliando(final int posicaoDisciplina) {

        if (posicaoDisciplina < disciplinasList.size()) {

            listview = (ListView) findViewById(R.id.list);

            title = (TextView) findViewById(R.id.titlee);
            Typeface face = Typeface.createFromAsset(getAssets(), "Roboto-BlackItalic.ttf");
            title.setTypeface(face);


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
                    if (dado == true) {
                        increment++;
                        carregaLista(increment, posicaoDisciplina);
                        checkAtivo(posicaoDisciplina);
                        contador = increment;
                    } else {
                        Toast.makeText(AvaliacaoActivity.this, "Por favor, informe sua resposta!", Toast.LENGTH_SHORT).show();
                        if (increment == 0) {
                            carregaLista(increment, posicaoDisciplina);
                        } else if (contador == increment) {
                            carregaLista(contador, posicaoDisciplina);
                        } else {
                            carregaLista(increment - 1, posicaoDisciplina);
                        }


                    }
                }
            });

        } else {
            btn_next.setText("Finalizar");
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        enviarAvaliacao();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
    /*
    * Metodo que carrega a lista de perguntas da avaliação e carrega na view
    * */
    private void carregaLista(int numero, int dPosicao) {
        ArrayList<Pergunta> listPergunta = new ArrayList<>();
        ArrayList<OpcaoResposta> listOpcao = new ArrayList<>();

        title.setText(disciplinasList.get(dPosicao).getCodigo().toString() + ": Pergunta " + (numero + 1) + " de " + pageCount);


        for (int i = 0; i < listOpcaoRespostas.size(); i++) {
            listOpcao.add(listOpcaoRespostas.get(i));
        }

        int comece = numero * NUM_ITEMS_PAGE;
        for (int i = comece; i < (comece) + NUM_ITEMS_PAGE; i++) {
            if (i < perguntaListVolley.size()) {
                listPergunta.add(perguntaListVolley.get(i));
            } else {
                break;
            }
        }
        final int posicaoDisciplina = dPosicao;

        /*
        * A lista de perguntas é passada para o adaptador e a resposta é repassada dinamicamente
        * Esse metodo tem como função, passar uma lista de Perguntas e pegar as resposta dessas perguntas
        * dinamicamente
        * */
        avaliacaoListAdapter = new AvaliacaoListAdapter(this, listPergunta, listOpcao) {
            @Override
            public void setData(boolean data, Resposta resposta) {
                super.setData(data, resposta);
                dado = data;
                if (resposta != null) {
                    if (respostasList.isEmpty()) {
                        resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                        resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                        respostasList.add(resposta);
                    } else {
                        if (!respostasList.contains(resposta)) {
                            resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                            resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                            respostasList.add(resposta);
                        } else {
                            respostasList.remove(resposta);
                            resposta.setIdDiscplinaAvaliada(posicaoDisciplina);
                            resposta.setIdAvaliacao(avaliacao.getIdAvaliacao());
                            respostasList.add(resposta);
                        }
                    }

                }
            }
        };
        listview.setAdapter(avaliacaoListAdapter);
    }

    /*
    * Metodo usado para fazer a checagem do numero de perguntas na lista
    * e mudar o numero de perguntas através de incremento
    * */
    private void checkAtivo(int posicaoDisciplina) {
        if (increment + 1 == pageCount) {
            int tamanhoDisc = disciplinasList.size();
            if (posicaoDisciplina < tamanhoDisc) {
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
        } else if (increment == 0) {

        } else {

            btn_next.setEnabled(true);

        }
    }

    /*
    * Metodo resposavel por pegar a lista de respostas dadas pelo usuário e enviar para servidor
    * */

    private void enviarAvaliacao() throws JSONException {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Enviando Avaliações...");
        pDialog.setCancelable(false);
        pDialog.show();
        final JSONArray jsonArray = new JSONArray();

        for (int h = 0; h < respostasList.size(); h++) {

            try {
                JSONObject json = new JSONObject();
                json.put("id_resposta", String.valueOf(respostasList.get(h).getIdPerguntaRespondida()));
                json.put("campo_resposta", respostasList.get(h).getRespostaUsuário());
                json.put("id_avaliacao", String.valueOf(respostasList.get(h).getIdAvaliacao()));
                json.put("id_disciplina", String.valueOf(respostasList.get(h).getIdDiscplinaAvaliada()));
                jsonArray.put(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.d("Respostas: ",jsonArray.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,  Config.RESPOSTAS_ARRAY,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        pDialog.dismiss();
                        if(response.equals("feita")){
                            Toast.makeText(AvaliacaoActivity.this,"Avaliação enviada com sucesso!", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(AvaliacaoActivity.this, MainActivity.class);
                            startActivityForResult(it,0);
                        }else  if(response.equals("sucesso")){
                            Toast.makeText(AvaliacaoActivity.this,"Avaliação enviada com sucesso!", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(AvaliacaoActivity.this, MainActivity.class);
                            startActivityForResult(it,0);
                        }else if(response.equals("erro")){
                            Toast.makeText(AvaliacaoActivity.this,"Erro! Tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(AvaliacaoActivity.this, MainActivity.class);
                            startActivityForResult(it,0);
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        pDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", emailuser);
                params.put("respostas",jsonArray.toString() );
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MyApplication.getInstance().addToRequestQueue(stringRequest);

    }


}



