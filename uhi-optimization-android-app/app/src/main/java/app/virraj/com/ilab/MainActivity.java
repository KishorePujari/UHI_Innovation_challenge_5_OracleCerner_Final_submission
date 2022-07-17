package app.virraj.com.ilab;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class WebAppInterface {
    Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}



public class MainActivity extends AppCompatActivity {
    private WebView mWebView;
    CustomException customException;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabasePath("/data/data/" + this.getPackageName() + "/databases/");
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");


        try{
            copyAsset("html");
        }
        catch(CustomException e){
            customException = new CustomException("html not copied");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            android.webkit.WebView.setWebContentsDebuggingEnabled(true);
        }

        String webViewPath = "file:///" + getExternalFilesDir(null) + "/html/index.html";
        mWebView.loadUrl(webViewPath);
    }
    /**
     * Copying the assets in the device so that the app can run in the offline mode i.e without internet connectivity.
     * @param path provided path to copy the html files
     * @throws CustomException when I/O error occurred
     */
    private void copyAsset(String path) throws CustomException {
        AssetManager manager = getAssets();

        try {
            String[] contents = manager.list(path);
            if (contents == null || contents.length == 0)
                throw new IOException();

            File dir = new File(getExternalFilesDir(null), path);
            if(!dir.mkdirs()){
                customException = new CustomException("mkdir returns false");
            }
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        }
        catch (IOException e) {
            copyFileAsset(path);
            customException = new CustomException("IOException "+e.getMessage());

        }
    }

    /**
     * Copying singe file at a time.
     * @param path copy the file present in this path.
     * @throws CustomException when any I/O error occurred.
     */
    private void copyFileAsset(String path) throws CustomException{
        File file = new File(getExternalFilesDir(null), path);
        try
        {
            InputStream in = getAssets().open(path);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
        }
        catch(IOException e){
            throw new CustomException("I/O error "+e.getMessage());
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
