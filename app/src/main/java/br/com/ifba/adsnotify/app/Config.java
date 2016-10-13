package br.com.ifba.adsnotify.app;

/**
 * Esta classe contém informações de configuração do app relacionado com GCM.
 * @Author Robson Coutinho
 * @version 1.0
 * @since 10/05/2016.
 */

public class Config {
    // Flag para identificar se mostra uma única linha
    // Ou múltiplas linhas de texto na bandeja de notificação push
    public static boolean appendNotificationMessages = true;


    public static final String ROOT = "http://www.muitootimo.com.br/";

    public static final String CARREGA_AVISOS = ROOT+"api/avisos";
    public static final String URL_DISCIPLINAS = ROOT+"api/disciplinas";
    public static final String URL_PROFESSORES = ROOT+"api/professores";
    public static final String URL_DOCS = ROOT+"api/documentos";
    public static final String URL_QUESTIONARIO = ROOT+"api/questionarios";
    public static final String REQUEST_LOGIN = ROOT+"api/login";
    public static final String DISC_CURSADAS = ROOT+"api/disciplinasCursadas";
    public static final String INFO_USER = ROOT+"api/informacaoUser";
    public static final String RESPOSTAS_ARRAY = ROOT+"api/respostaQuestionario";
    public static final String TOKEN_GCM = ROOT+"api/token";

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

