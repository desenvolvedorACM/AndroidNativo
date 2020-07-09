package com.concurseiro.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.concurseiro.Constants.Constants;
import com.concurseiro.R;

public class ObterCreditosActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obter_creditos);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Obter Créditos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.mViewHolder.wvObterCredito = (WebView) findViewById(R.id.wvObterCredito);
        this.mViewHolder.wvObterCredito.setWebViewClient(new Webclient());

        dialog = new ProgressDialog(ObterCreditosActivity.this);
        dialog.setMessage("Aguarde ...");
        dialog.setCancelable(false);
        dialog.show();

        this.mViewHolder.wvObterCredito.getSettings().setLoadsImagesAutomatically(true);
        this.mViewHolder.wvObterCredito.getSettings().setJavaScriptEnabled(true);
        this.mViewHolder.wvObterCredito.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        String Url = String.format("%s/id_usuario=/%s", Constants.URL_API_PAG, 205);
        this.mViewHolder.wvObterCredito.loadUrl(Url);

        dialog.dismiss();

    }

    private class ViewHolder {
        WebView wvObterCredito;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(Constants.TAG, "Voltar ");

                Intent intent = new Intent(ObterCreditosActivity.this, MenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private class Webclient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //super.shouldOverrideUrlLoading(view, url);
            view.loadUrl(url);
            dialog.dismiss();
            return true;
        }
    }
}
