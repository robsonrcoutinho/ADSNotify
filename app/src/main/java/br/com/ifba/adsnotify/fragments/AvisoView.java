package br.com.ifba.adsnotify.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aphidmobile.flip.FlipViewController;
import com.aphidmobile.utils.UI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.adapters.ListAvisoAdapter;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.model.Mensagem;
import br.com.ifba.adsnotify.network.DataCallBack;

public class AvisoView extends Fragment{
    private static final String TAG = AvisoView.class.getSimpleName();
    private FlipViewController flipView;
    private Mensagem mensagem;
    private List<Mensagem> list =  new ArrayList<>();
    private ImageView image;
    private ProgressDialog pDialog;
    private LayoutInflater inflater;
    private View rootView;
    private int op;

    public AvisoView() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;

        flipView = new FlipViewController(getActivity().getApplicationContext(), FlipViewController.VERTICAL);

        rootView = inflater.inflate(R.layout.aviso_layout, container, false);
        image = (ImageView) rootView.findViewById(R.id.falhaLogin);
        image.setVisibility(View.GONE);

        carregaAviso();

        if(op == 0){
            image.setVisibility(View.VISIBLE);
            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    carregaAviso();
                    Log.d("FLIP", "onTouch image");
                    return false;
                }
            });
            return rootView;
        }else {
            return flipView;
        }
       }

    public void carregaAviso(){
        image.setVisibility(View.GONE);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Carregando Avisos...");
        pDialog.show();


        publicaAviso(new DataCallBack() {
            @Override
            public void onSuccess(JSONArray response) {
                hidePDialog();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject msgJSON = (JSONObject) response.get(i);

                        Log.d(TAG, "Recebido JSON:" + msgJSON.toString());

                        String titulo = msgJSON.getString("titulo");
                        String mensg = msgJSON.getString("mensagem");
                        iniciaLista(titulo, mensg);

                    }
                        op = 1;
                        flipView.setAdapter(new ListAvisoAdapter(getActivity().getApplicationContext(), list));

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });
    }

    public void publicaAviso(final DataCallBack callback) {

           JsonArrayRequest req = new JsonArrayRequest(Config.CARREGA_AVISOS,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hidePDialog();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                op = 0;
                rootView = inflater.inflate(R.layout.aviso_layout, null);
                Toast.makeText(getActivity().getApplicationContext(),
                        "Erro ao tentar conectar! Verifique sua conexão",
                        Toast.LENGTH_LONG).show();



            }
        });
        req.setTag(TAG);
        int socketTimeout = 1000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);
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
        flipView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        flipView.onPause();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
