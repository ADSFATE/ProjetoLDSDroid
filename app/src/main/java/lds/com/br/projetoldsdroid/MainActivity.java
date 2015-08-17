package lds.com.br.projetoldsdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {

    private WebView webview;
    private static final String TAG = "Main";
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("WebView Exemplo");

        this.webview = (WebView)findViewById(R.id.webView);

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        progressBar = ProgressDialog.show(MainActivity.this, "WebView Exemplo", "Carregando...");

        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                Toast.makeText(getApplicationContext(), "Ah não! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });

        webview.loadUrl("http://10.0.2.2:8080//Projeto_LDS/");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                teste();
            }
        };

        //  new Thread(r).start();

    }

    private void teste(){

        SoapObject soap = new SoapObject("http://model", "inserirUsuario");

        PropertyInfo pNome = new PropertyInfo();
        pNome.setName("nome");
        pNome.setValue("Joãozão");
        pNome.setType(String.class);

        PropertyInfo pIdade = new PropertyInfo();
        pIdade.setName("idade");
        pIdade.setValue(10);
        pIdade.setType(Integer.class);

        soap.addProperty(pNome);
        soap.addProperty(pIdade);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);
        Log.i("PWS", "Chamando WebService");
        String url = "http://10.0.2.2:8080/Primeiro_Web_Service/services/UsuarioDAO";
        HttpTransportSE httpTransport = new HttpTransportSE(url);

        try {

            httpTransport.call("", envelope);
            Object msg = envelope.getResponse();
            Log.d("PWS", "Teste: " + msg);

        } catch (Exception e) {

            Log.d("Faustão:"," Errouuuuu - " + e.getMessage());

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
