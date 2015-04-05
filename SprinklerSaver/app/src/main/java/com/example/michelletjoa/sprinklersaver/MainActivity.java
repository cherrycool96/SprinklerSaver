package com.example.michelletjoa.sprinklersaver;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.io.IOException;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener{

    HttpActivity httpActivity;
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;

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

        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_analytics),
//                        new PrimaryDrawerItem().withName(R.string.drawer_item_mod),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            toolbar.setTitle(((Nameable) drawerItem).getNameRes());
                            switchView(((Nameable) drawerItem).getNameRes());
                        }
                    }
                })
                .build();



        XYPlot plot = (XYPlot) findViewById(R.id.wateringData);

        // Create a couple arrays of y-values to plot:
        Number[] series1Numbers = {0, 0, 30, 0, 0, 0, 0, 0, 0, 10, 0};

        // Turn the above arrays into XYSeries':
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "");                             // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_plf1);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);
    }

    private void switchView (int view) {
        switch (view) {
            case R.string.drawer_item_home:
                viewFlipper.setDisplayedChild(0);
                break;
            case R.string.drawer_item_analytics:
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
        HttpActivity httpActivity = new HttpActivity();
        try {
            httpActivity.run(isChecked ? "On" : "Off");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}