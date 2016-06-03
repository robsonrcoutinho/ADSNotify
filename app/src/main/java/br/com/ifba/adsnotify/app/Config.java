package br.com.ifba.adsnotify.app;

/**
 * Created by Robson on 10/05/2016.
 */

/*
* Esta classe contém informações de configuração do app relacionado com GCM.
* */
public class Config {
    // Flag para identificar se mostra uma única linha
    // Ou múltiplas linhas de texto na bandeja de notificação push
    public static boolean appendNotificationMessages = true;

    // Tema global para receber aplicativos de notificações push
    public static final String TOPIC_GLOBAL = "global";


    public static final String CARREGA_AVISOS = "https://adsprojectifba.herokuapp.com/ws/avisos";
    public static final String URL_DISCIPLINAS = "http://adsprojectifba.herokuapp.com/ws/disciplinas";
    public static final String URL_PROFESSORES = "http://adsprojectifba.herokuapp.com/ws/professores";
    public static final String URL_DOCS = "http://adsprojectifba.herokuapp.com/ws/documentos";
    public static final String REQUEST_LOGIN = "";


    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";


    public static final String ACCOUNT_TOKEN_TYPE = "FULL";
    public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
    public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ACCOUNT_TYPE = "br.com.ifba.adsnotify.account";



   //Id para lidar com handle na tentativa de notificação
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}

