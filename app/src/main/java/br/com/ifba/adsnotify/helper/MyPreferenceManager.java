package br.com.ifba.adsnotify.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import br.com.ifba.adsnotify.model.User;

/**
 * Created by Robson on 10/05/2016.
 */

/**Esta classe armazena dados em SharedPreferences .
 * Aqui nós armazenamos temporariamente as notificações push não lidas
  */

public class MyPreferenceManager{
    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor para Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref modo
    int PRIVATE_MODE = 0;

    // Sharedpref nome
    private static final String PREF_NAME = "adsnotify_gcm";

    // Todas as chaves compartilhadas Shared Preferences
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_MATRC = "user_matrc";
    private static final String KEY_NOTIFICATIONS = "notifications";

    // Construtor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void addNotification(String notification) {

        // get velhas notificações
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getNome());
        editor.putString(KEY_USER_MATRC, user.getMatricula());
        editor.commit();

        Log.e(TAG, "Preferencias compartilhadas user: " + user.getNome() + ", " + user.getMatricula());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, matricula;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            matricula = pref.getString(KEY_USER_MATRC, null);

            User user = new User(id, name, matricula);
            return user;
        }
        return null;
    }

}