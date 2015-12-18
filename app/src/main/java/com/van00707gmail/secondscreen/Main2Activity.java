package com.van00707gmail.secondscreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView mTxtVw = (TextView) findViewById(R.id.textView);
        String buf = getIntent().getExtras().getString("info");
        if (buf == "401")
        {
            mTxtVw.setText("Error - 401 Unauthorized");
            return;
        }
        String out = "";

        ArrayList<String> param = new ArrayList<>();
        param.add("name");
        param.add("link");
        param.add("account");
        param.add("created_time");
        mTxtVw.setText(buf);
        for (String i : param)
        {
            int j = buf.indexOf(i) + i.length() + 3;
            i+=" - ";
            while (buf.charAt(j) != '\"')
            {
                i+= buf.charAt(j);
                j++;
            }
            i+="\n";

            out+=i;
        }

        mTxtVw.setText(out);
        // name length + 3  , link, location, created_time, account
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
