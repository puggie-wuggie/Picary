package com.puggerino.picary;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {

    DatePicker pickerdate;
    TextView info;
    Button selectButton;

    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = (TextView)findViewById(R.id.info);
        pickerdate = (DatePicker)findViewById(R.id.pickerdate);
        Calendar today = Calendar.getInstance();
        selectButton = (Button)findViewById(R.id.selectButton);

        selectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), datePickerResult.class);

                month = pickerdate.getMonth()+1;
                day = pickerdate.getDayOfMonth();
                year = pickerdate.getYear();

                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", day);

                startActivity(intent);
            }
        });

        pickerdate.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                new OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Toast.makeText(getApplicationContext(), "onDateChanged", Toast.LENGTH_SHORT).show();

                        info.setText(
                                "You picked " + year + " " + monthOfYear + " "+ dayOfMonth
                        );
                    }
                }
        );
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
