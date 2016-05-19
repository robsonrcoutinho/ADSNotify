package br.com.ifba.adsnotify.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.aphidmobile.flip.FlipViewController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
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



    public AvisoView() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        flipView = new FlipViewController(getActivity().getApplicationContext(), FlipViewController.VERTICAL);
        carregaAviso();

        return flipView;
    }
    public void carregaAviso(){


        publicaAviso(new DataCallBack() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject msgJSON = (JSONObject) response.get(i);

                        Log.d(TAG, "Recebido JSON:" + msgJSON.toString());

                        String titulo = msgJSON.getString("titulo");
                        String mensg = msgJSON.getString("mensagem");
                        iniciaLista(titulo, mensg);

                    }
                    flipView.setAdapter(new ListAvisoAdapter(getActivity().getApplicationContext(), list));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Erro ao tentar conectar com servidor! Verifique sua conexão",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void publicaAviso(final DataCallBack callback) {

           JsonArrayRequest req = new JsonArrayRequest(Config.CARREGA_AVISOS,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity().getApplicationContext(),
                        "Erro ao tentar conectar com servidor! Verifique sua conexão",
                        Toast.LENGTH_LONG).show();
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
        flipView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        flipView.onPause();
    }

}
