package br.com.ifba.adsnotify.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import br.com.ifba.adsnotify.activity.AuthenticatorActivity;
import br.com.ifba.adsnotify.app.Config;

/**
 * Created by Robson on 02/06/2016.
 */

/*
* Assistente Authenticator para aplicação
*/
public class AccountAuthenticator extends AbstractAccountAuthenticator {
    public Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }
    /*
    * Permite que altere dados da conta
    * */
    @Override
    public Bundle editProperties(
            AccountAuthenticatorResponse r, String s) {
        throw new UnsupportedOperationException();
    }
    /**
     * Metodo para adicionar uma nova conta na aplicação
     * **/
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse response,
            String accountType,String authTokenType,
            String[] requieredFeatures, Bundle options) throws NetworkErrorException {

        /**
         *  Disponibiliza para o usuario area que ele irá colocar senha e login para fazer autenticação no servidor
         * e retornar com o Token do usuario
         **/
        Intent it = new Intent(mContext, AuthenticatorActivity.class);
        // Type é o mesmo do xml/AccountType
        it.putExtra(Config.ARG_ACCOUNT_TYPE, accountType);
        // authTokenType é o "full de minha classe ConstantesLogin"
        it.putExtra(Config.ARG_AUTH_TYPE, authTokenType != null ? authTokenType : Config.ACCOUNT_TOKEN_TYPE );
        it.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, it);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(
            AccountAuthenticatorResponse r,
            Account account,
            String s,
            Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(
            AccountAuthenticatorResponse r,
            Account account,
            String s, Bundle bundle) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse r,
            Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}
