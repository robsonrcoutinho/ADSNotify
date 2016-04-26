package br.com.ifba.adsnotify.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;

import java.util.List;

import br.com.ifba.adsnotify.adapters.ListAvisoAdapter;
import br.com.ifba.adsnotify.model.Mensagem;
import br.com.ifba.adsnotify.network.Api;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AvisoView extends Fragment{
    private FlipViewController flipView;
    private List<Mensagem> msgLists;
    private ProgressDialog progressDialog;

    public AvisoView() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getActivity().getApplicationContext());

        flipView = new FlipViewController(getActivity().getApplicationContext(), FlipViewController.VERTICAL);
        final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint("https://api.myjson.com").build();
        Api msgApi =  restadapter.create(Api.class);

        msgApi.getData(new Callback<List<Mensagem>>() {
            @Override
            public void success(List<Mensagem> avisos, Response response) {
                msgLists = avisos;
                /*
                progressDialog = ProgressDialog.show(getActivity().getApplicationContext(), "Carregando Avisos", "Aguarde um momento...");
                progressDialog.show();
               */

                flipView.setAdapter(new ListAvisoAdapter(getActivity().getApplicationContext(), msgLists));
                Toast.makeText(getActivity().getApplicationContext(), "Avisos Carregados", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Falha ao carregar avisos", Toast.LENGTH_SHORT).show();
            }
        });

       return flipView;

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
