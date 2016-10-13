package br.com.ifba.adsnotify.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * @Author Robson Coutinho
 * @Email r.ramoscoutinho@gmail.com
 * @Since 12, Outubro, 2016
 */
public class RenderViewDocs extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final ProgressDialog  progressDialog = ProgressDialog.show(this, "Carregando Documento", "Aguarde um momento...");

        final WebView webview= new WebView(RenderViewDocs.this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(true);
        webview.setHorizontalScrollBarEnabled(true);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        String pagina = params.getString("page");

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView webView, String http) {
                webview.loadUrl(
                        "javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'");
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + pagina);
        setContentView(webview);

    }
    }
