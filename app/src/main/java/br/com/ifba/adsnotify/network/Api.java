package br.com.ifba.adsnotify.network;

import java.util.List;

import br.com.ifba.adsnotify.model.Mensagem;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Robson on 24/04/2016.
 */
public interface Api {


    @GET("/bins/35vca")
    public void getData(Callback<List<Mensagem>> response);

}

