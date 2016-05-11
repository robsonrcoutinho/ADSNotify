package br.com.ifba.adsnotify.app;

/**
 * Created by Robson on 10/05/2016.
 */

/*
*  Aqui nós declaramos as urls terminal REST API
* */
public class EndPoints {

    public static final String BASE_URL = "http://192.168.0.100:80/gcm_chat";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";

}
