package br.com.ifba.adsnotify.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Robson on 02/06/2016.
 */

public class AuthenticationService extends Service {

    // Instancia do autenticador
    private AccountAuthenticator autenticador;

    @Override
    public void onCreate() {
        // Nova instancia do autenticador
        autenticador = new AccountAuthenticator(this);
    }

    /*
     * Ligando o servico ao servico do Android
     */
    @Override
    public IBinder onBind(Intent intent) {
        return autenticador.getIBinder();
    }
}