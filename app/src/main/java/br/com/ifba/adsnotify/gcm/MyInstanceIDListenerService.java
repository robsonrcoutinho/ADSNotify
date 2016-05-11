package br.com.ifba.adsnotify.gcm;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/*
* Este serviço invoca onTokenRefresh () método verifica se há uma mudança na GCM token de registro .
* */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = MyInstanceIDListenerService.class.getSimpleName();

    /**
     * Chama se InstanceID token é atualizado. Isto pode ocorrer se a segurança do
     *  token anterior for comprometido. Esta chamada é iniciada pela
     * provider InstanceID .
     *
     */
    @Override
    public void onTokenRefresh() {
        Log.e(TAG, "onTokenRefresh");
        // Buscar Instância ID token atualizados  e notificar o servidor do nosso aplicativo de quaisquer alterações (se aplicável) .
        Intent intent = new Intent(this, GcmIntentService.class);
        startService(intent);
    }
}