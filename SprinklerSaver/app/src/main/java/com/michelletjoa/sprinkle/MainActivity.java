package com.michelletjoa.sprinkle;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener{

    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private TextView txtConnect;
    private TextView txtStatus;
    private ImageView imgStatus;
    private XYPlot wateringData;
    private XYSeries currentWateringDataSeries;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch switcher = (Switch) findViewById(R.id.switchOnOff);
        switcher.setOnCheckedChangeListener(this);
        txtConnect = (TextView) findViewById(R.id.txtConnected);
        txtStatus = (TextView) findViewById(R.id.txtSprinklerStat);
        imgStatus = (ImageView) findViewById(R.id.icon);

        txtConnect.setText("Disconnected");

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        // Create the AccountHeader
        AccountHeader.Result headerResult = new AccountHeader()
            .withActivity(this)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                    new ProfileDrawerItem().withName("Johnny Appleseed").withEmail("j.appleseed@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile))
            )
            .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                    return false;
                }
            }).build();
        //Now create your drawer and pass the AccountHeader.Result
        Drawer.Result result = new Drawer()
            .withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(headerResult)
            .addDrawerItems(
                    new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(R.drawable.ic_settings_remote_grey_600_18dp),
                    new PrimaryDrawerItem().withName(R.string.drawer_item_analytics).withIcon(R.drawable.ic_insert_chart_grey_600_18dp),
                    new DividerDrawerItem(),
                    new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_settings_grey_600_18dp)
            )
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                    switchView(((Nameable) drawerItem).getNameRes());
                }
            }).build();

        new DownloadWebpageTask().execute("Status");

        wateringData = (XYPlot) findViewById(R.id.wateringData);
        setWateringData(new Number[] {0, 0, 30, 0, 0, 0, 0, 0, 0, 10, 0});

        new DownloadWebpageTask().execute("Analytics");
    }

    private void switchView (int view) {
        switch (view) {
            case R.string.drawer_item_home:
                viewFlipper.setDisplayedChild(0);
                break;
            case R.string.drawer_item_analytics:
                new DownloadWebpageTask().execute("Analytics");
                viewFlipper.setDisplayedChild(1);
                break;
            case R.string.drawer_item_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        new DownloadWebpageTask().execute("Sprinklers" + (isChecked ? "On" : "Off"));
    }

    public class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
//            textView.setText(result);
            System.out.println(result);
            if (result.contains("Turning")) {
                txtConnect.setText("Connected");
                if (result.contains("on")) {
                    txtStatus.setText("Sprinklers are on");
                    imgStatus.setImageResource(R.drawable.watering);
                } else if (result.contains("off")) {
                    txtStatus.setText("Sprinklers are off");
                    if (result.contains("rain")) {
                        imgStatus.setImageResource(R.drawable.rain);
                    } else {
                        imgStatus.setImageResource(R.drawable.off);
                    }
                }
            } else if (result.contains("Status")) {
                txtConnect.setText("Connected");
                if (result.contains("on")) {
                    txtStatus.setText("Sprinklers are on");
                    imgStatus.setImageResource(R.drawable.watering);
                } else if (result.contains("off")) {
                    txtStatus.setText("Sprinklers are off");
                    if (result.contains("rain")) {
                        imgStatus.setImageResource(R.drawable.rain);
                    } else {
                        imgStatus.setImageResource(R.drawable.off);
                    }
                }
            } else if (result.contains("Analytics")) {
                txtConnect.setText("Connected");
                if (result.contains("watering data")) {
                    
                }
            }
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL("http://10.0.0.4:7655/" + myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            System.out.println("The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private void setWateringData (Number[] seriesNumbers) {
        if (currentWateringDataSeries != null)
            wateringData.removeSeries(currentWateringDataSeries);

        // Turn the above arrays into XYSeries':
        currentWateringDataSeries = new SimpleXYSeries(
                Arrays.asList(seriesNumbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "");                                    // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        wateringData.addSeries(currentWateringDataSeries, series1Format);

        // reduce the number of range labels
        wateringData.setTicksPerRangeLabel(3);
    }
}