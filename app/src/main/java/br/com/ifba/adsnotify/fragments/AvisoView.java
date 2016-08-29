package br.com.ifba.adsnotify.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.ListAvisoAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.gcm.NotificationUtils;
import br.com.ifba.adsnotify.helper.MyPreferenceManager;
import br.com.ifba.adsnotify.model.Mensagem;
import se.emilsjolander.flipview.FlipView;

public class AvisoView extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = AvisoView.class.getSimpleName();
    private Mensagem mensagem;
    private List<Mensagem> list;
    private LayoutInflater inflater;
    private View rootView;
    private ListAvisoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


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

        FlipView flipView = (FlipView)rootView.findViewById(R.id.flip_view);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        adapter = new ListAvisoAdapter(getActivity().getApplicationContext(), list);
        flipView.setAdapter(adapter);

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
         swipeRefreshLayout.setRefreshing(true);

        JsonArrayRequest req = new JsonArrayRequest(Config.CARREGA_AVISOS,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response.length() > 0){
                for (int i = 0; i < response.length(); i++) {
                    JSONObject msgJSON = null;
                    try {
                        msgJSON = (JSONObject) response.get(i);
                        String titulo = msgJSON.getString("titulo");
                        String mensg = msgJSON.getString("mensagem");
                        iniciaLista(titulo, mensg);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                   adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(getActivity(),"Sem Avisos",Toast.LENGTH_LONG).show();
                }

                  swipeRefreshLayout.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
              //  Toast.makeText(getActivity(), "Sem conexão!", Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(req);

    }



    public void iniciaLista(String titulo, String corpo){
        mensagem = new Mensagem();
        mensagem.setAvisoTitle(titulo);
        mensagem.setAvisoBody(corpo);
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
