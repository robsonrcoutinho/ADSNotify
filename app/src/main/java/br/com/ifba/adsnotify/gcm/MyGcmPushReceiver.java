package br.com.ifba.adsnotify.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.gcm.GcmListenerService;

import br.com.ifba.adsnotify.activity.MainActivity;
import br.com.ifba.adsnotify.app.Config;
/**
 * Created by Robson on 10/05/2016.
 */

/*
*  Esta é uma classe receiver em que o método onMessageReceived() será acionado sempre que dispositivo recebe nova notificação push.
* */
public class MyGcmPushReceiver extends GcmListenerService {
    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    /**
     *  Chamado quando mensagem é recebida.
     *
     * @param from   SenderID do remetente.
     * @param bundle bundle contendo dados de mensagem como pares de chave/valor.
     *
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String image = bundle.getString("image");
        String timestamp = bundle.getString("created_at");
        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Title: " + title);
        Log.e(TAG, "message: " + message);
        Log.e(TAG, "image: " + image);
        Log.e(TAG, "timestamp: " + timestamp);

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

            // pp está em primeiro plano, transmitir a mensagem de envio
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // Ligar som de notificação
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        } else {

            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);

            if (TextUtils.isEmpty(image)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, image);
            }
        }
    }

    /**
     *Mostrando notificação com apenas texto
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Mostrando notificação com texto e imagem
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}