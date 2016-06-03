package br.com.ifba.adsnotify.activity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.ifba.adsnotify.R;
import br.com.ifba.adsnotify.app.Config;
import br.com.ifba.adsnotify.app.MyApplication;
import br.com.ifba.adsnotify.fragments.AvisoView;
import br.com.ifba.adsnotify.fragments.OpcaoView;
import br.com.ifba.adsnotify.fragments.UsuarioView;
import br.com.ifba.adsnotify.gcm.GcmIntentService;
import br.com.ifba.adsnotify.model.User;

/**
 * Created by Robson on 22/04/2016.
 */
public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private User user;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        user = ((MyApplication) getApplication()).getUser();
        mAccountManager = AccountManager.get(MainActivity.this);
        getAccounts(null);
        if(mAccountManager.getAccountsByType(Config.ACCOUNT_TYPE).length == 0){
            finish();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Verificação do tipo intent filters
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // GCM registrado com êxito
                    // Agora inscreve-se a para receber  notificações
                    String token = intent.getStringExtra("token");

                    Toast.makeText(getApplicationContext(), "Token de registro GCM: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // ID de registro GCM é armazenado no Banco de dados do servidor

                    Toast.makeText(getApplicationContext(), "GCM token de registro é armazenado no servidor!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // Nova notificação push é recebido
                    Toast.makeText(getApplicationContext(), "Notificação de envio é recebido!", Toast.LENGTH_LONG).show();
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
}


    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.tab_home,
                R.drawable.tab_opcoes,
                R.drawable.tab_perfil
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AvisoView(), "AvisoView");
        adapter.addFrag(new OpcaoView(), "OpcaoView");
        adapter.addFrag(new UsuarioView(), "UsuarioView");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }


/*GCM PlayService configuração*/
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }


    /*
    * Método é usado para verificar a disponibilidade dos serviços do Google Play. Se os serviços do Google Play
    * não estão disponíveis, vamos simplesmente fechar o aplicativo.
    * */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "\n" +
                        "Este dispositivo não é suportado. Serviços do Google Play não foi instalado!");
                Toast.makeText(getApplicationContext(), "Este dispositivo não é suportado. Serviços do Google Play não foi instalado!",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /*
    *  Registrar broadcast receiver no método onResume()  para ambos REGISTRATION_COMPLETE e PUSH_NOTIFICATION intent filters.
    * */
    @Override
    protected void onResume() {
        super.onResume();

        // Registrar registro GCM receptor receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // Registra novo receiver de mensagens automáticas
        // Ao fazer isso, a atividade será notificada sempre que uma nova mensagem chega
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    public void getAccounts(View view){
        mAccountManager.getAuthTokenByFeatures(Config.ACCOUNT_TYPE,
                Config.ACCOUNT_TOKEN_TYPE,
                null,
                MainActivity.this,
                null,
                null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            Log.i(TAG, ".getAccounts()");
                            Log.i(TAG, ".getAccounts() : AccountType = " + bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            Log.i(TAG, ".getAccounts() : AccountName = " + bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            Log.i(TAG, ".getAccounts() : Token = " + bundle.getString(AccountManager.KEY_AUTHTOKEN));

                            user.setAccountType(bundle.getString(AccountManager.KEY_ACCOUNT_TYPE));
                            user.setAccountName(bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            user.setToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));


                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                null);
    }

}