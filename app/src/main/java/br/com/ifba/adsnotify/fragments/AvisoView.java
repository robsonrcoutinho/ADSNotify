package br.com.ifba.adsnotify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.AvisoListAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.gcm.NotificationUtils;
import br.com.ifba.adsnotify.helper.MyPreferenceManager;
import br.com.ifba.adsnotify.model.Mensagem;

/**
 * Classe responsavel pela apresentação de avisos
 * @Author Robson Coutinho
 * @version 1.0
 * @since 10/05/2016.
 */

public class AvisoView extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Mensagem mensagem;
    private List<Mensagem> list;
    private LayoutInflater inflater;
    private View rootView;
    private AvisoListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    ImageView image;
    TextView textView;



    public AvisoView() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;

        /*Limpa notificações armazenadas em sharedPreferences*/
        MyPreferenceManager.clear();
        NotificationUtils.clearNotifications();

        list =  new ArrayList<>();

        rootView = inflater.inflate(R.layout.aviso_layout_swipe, container, false);

        image = (ImageView) rootView.findViewById(R.id.falhaAviso);
        textView = (TextView) rootView.findViewById(R.id.tituloAviso);
        image.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);


        listView = (ListView)rootView.findViewById(R.id.listAviso);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        adapter = new AvisoListAdapter(getActivity().getApplicationContext(), list);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        carregaAviso();
                                    }
                                }
        );
        return rootView;
       }

    public void carregaAviso(){
        MyPreferenceManager.clear();
        NotificationUtils.clearNotifications();
        swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest req = new JsonArrayRequest(Config.CARREGA_AVISOS,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                list.clear();
                adapter.notifyDataSetChanged();

                if(response.length() > 0){
                    for (int i = response.length(); i >= 0; i--){
                    JSONObject msgJSON = null;
                    try {

                        msgJSON = (JSONObject) response.get(i);
                        String titulo = msgJSON.getString("titulo");
                        String mensg = msgJSON.getString("mensagem");
                        String create = msgJSON.getString("updated_at");

                        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date dateObj = sdt.parse(create);

                        SimpleDateFormat posFormato = new SimpleDateFormat("dd MMM yy");
                        SimpleDateFormat posFormato2 = new SimpleDateFormat("HH:mm");
                        String as = "  às  ";
                        String data = posFormato.format(dateObj);
                        String hora = posFormato2.format(dateObj);

                        String dataFinal = data+as+hora+"h";

                        iniciaLista(titulo, mensg, dataFinal);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                   adapter.notifyDataSetChanged();
                }else{
                    //Toast.makeText(getActivity(),"Sem Novos Avisos!",Toast.LENGTH_LONG).show();
                    image.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Não há novos avisos! Clique na imagem para regarregar página.");
                    swipeRefreshLayout.setRefreshing(true);

                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            image.setVisibility(View.GONE);
                            textView.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(true);
                            carregaAviso();
                        }
                    });
                }

                swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
               // Toast.makeText(getActivity(),"Verifique sua conexão!",Toast.LENGTH_LONG).show();
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.icon_error_network);
                textView.setVisibility(View.VISIBLE);
                textView.setText("Erro conexão! Clique na imagem para regarregar página.");

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        image.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(true);
                        carregaAviso();
                    }
                });

            }
        });
        MyApplication.getInstance().addToRequestQueue(req);

    }

    public void iniciaLista(String titulo, String corpo, String data){
        mensagem = new Mensagem();
        mensagem.setAvisoTitle(titulo);
        mensagem.setAvisoBody(corpo);
        mensagem.setCreateAviso(data);
        list.add(mensagem);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        carregaAviso();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
