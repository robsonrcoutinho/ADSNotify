package br.com.ifba.adsnotify.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Robson on 02/06/2016.
 */
public class SyncService extends Service {

    // Instancia do sync adapter
    private static SyncAdapter syncAdapter = null;

    private static final Object lock = new Object();

    @Override
    public void onCreate() {
        synchronized (lock) {
            if (syncAdapter == null) {
                syncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    /**
     * Interface de comunicação retorna para o adaptador de sincronização(SyncAdapter) chamada de sistema
     */
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}