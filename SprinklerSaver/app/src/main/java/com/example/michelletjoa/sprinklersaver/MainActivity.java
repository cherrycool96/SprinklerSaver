package com.example.michelletjoa.sprinklersaver;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener{

    HttpActivity httpActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch switcher = (Switch) findViewById(R.id.switchOnOff);
        switcher.setOnCheckedChangeListener(this);
        HttpActivity httpActivity = new HttpActivity();
        TextView txtConnect = new TextView(this);
        txtConnect = (TextView) findViewById(R.id.txtConnected);
        try {
            httpActivity.run("Off");
            txtConnect.setText("Connected");
        }
        catch (IOException e) {
            txtConnect.setText("Can Not Connect");
            e.printStackTrace();
        } catch (Exception e) {
            txtConnect.setText("Can Not Connect");
            e.printStackTrace();
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

    public void onToggleClicked(View view) {
        // When the switch is clicked, then we check if its on the on or off part of the switch
        boolean on = ((Switch) view).isChecked();
        changeTextStat(on);
    }

    public void changeTextStat(boolean tOrF){
        //This takes the current boolean if sprinkler is on or off, and then changes the text
        TextView txtStat = new TextView(this);
        txtStat = (TextView) findViewById(R.id.txtSprinklerStat);

        if (tOrF) {
            txtStat.setText("Sprinklers are on");

        } else {
            txtStat.setText("Sprinklers are off");
        }
    }

    public boolean getStat(){
        //for the edison to know that the USER want to change the stat
        Switch switcher = (Switch) findViewById(R.id.switchOnOff);
        return switcher.isChecked();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //for when the USER slides the switcher
        changeTextStat(isChecked);
        HttpActivity httpActivity = new HttpActivity();
        try {
            httpActivity.run(isChecked ? "On" : "Off");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}