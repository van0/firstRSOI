package com.van00707gmail.secondscreen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity {

    class Post extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... codes){
          //  WebView mWebView = (WebView) findViewById(R.id.webView);
            String codeReDir = codes[0];//mWebView.getUrl().substring(mWebView.getUrl().indexOf("code=")+5);
            ArrayList<String> val1 = new ArrayList<String>();
            val1.add("client_id");
            val1.add("c482b6fea7756662da51d4bd8ad5278ceb6d01a5");
            val1.add("client_secret");
            val1.add("7diOcKYyNj4b/S627aq3WaBaeGI0V2vzPMs2E39srFajYs5dChrn7HPZcyeFg7CKLz2HoaWIiNqev9WotPdDzd961mdnpkr7OKqgItJR3YCH8MjG3D80TWnqnPidzm0K");
            val1.add("grant_type");
            val1.add("authorization_code");
            val1.add("code");
            val1.add(codeReDir);
            val1.add("redirect_uri");
            val1.add("https://www.yandex.ru/");

            String urlparam ="";
            for (int i=0; i<val1.size(); i+=2){
                urlparam += val1.get(i) + "=" + val1.get(i+1);
                if (i+2 != val1.size())
                    urlparam +="&";
            }

            String inputLine = "NULL";
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL("https://api.vimeo.com/oauth/access_token");
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setFixedLengthStreamingMode(urlparam.getBytes().length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                writer.write(urlparam);
                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return  response.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return  response.toString();
        }
    }

    class Get extends AsyncTask<String,Void,String>
    {
        // gradov@bmstu.ru
        @Override
        protected String doInBackground(String... codes){
            String inputLine = "NULL";
            StringBuffer response = new StringBuffer();
            try {
                URL url = new URL(codes[0]);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.addRequestProperty("Authorization", "bearer" + " " + codes[1]);
                int responseCode = conn.getResponseCode();
                inputLine = Integer.toString(responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                inputLine = "";
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                return  response.toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return inputLine;
            }
            return  response.toString();
        }
    }

    public void onClickWT(View v) throws ExecutionException, InterruptedException {
        mBttnWT = (Button) findViewById(R.id.button2);
        Get task = new Get();
        task.execute("https://api.vimeo.com/me", "");
        mBttnWT.setText(task.get());

      //  Intent intent = new Intent(MainActivity.this,Main2Activity.class );
      //  intent.putExtra("info", mBttnWT.getText());
      //  startActivity(intent);
    }

    public void onClick(View v) throws IOException, InterruptedException, ExecutionException {

        String codeReDir;
        Get conn2 = new Get();
        conn2.execute("https://api.vimeo.com/me", tCode);

        codeReDir = conn2.get();

        Intent intent = new Intent(MainActivity.this,Main2Activity.class );
        intent.putExtra("info", codeReDir);
        startActivity(intent);


    }




    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            //int j = url.indexOf("state=free&code=");
            if (url.indexOf("https://www.yandex.ru/?state=free&code=")==0)
            {
                String codeReDir = url.substring(url.indexOf("code=")+5);

                Post conn = new Post();
                conn.execute(codeReDir);

                try {
                    codeReDir = conn.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                int i = 17;
                String buf = "";
                while (codeReDir.charAt(i) != '\"')
                {
                    buf += codeReDir.charAt(i);
                    i++;
                }
                tCode = buf;

                Get conn2 = new Get();
                conn2.execute("https://api.vimeo.com/me", tCode);

                try {
                    codeReDir = conn2.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this,Main2Activity.class );
                intent.putExtra("info", codeReDir);
                startActivity(intent);
            }
            view.loadUrl(url);
            return true;
        }
    }

    private  String tCode="";
    private WebView mWebView;
    private Button mBttn;
    private Button mBttnWT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webView);
       // mBttn = (Button) findViewById(R.id.button);

        mWebView.setWebViewClient(new MyWebViewClient());
       // mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://api.vimeo.com/oauth/authorize?response_type=code&client_id=c482b6fea7756662da51d4bd8ad5278ceb6d01a5&redirect_uri=https://www.yandex.ru/&access_token&state=free");
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
