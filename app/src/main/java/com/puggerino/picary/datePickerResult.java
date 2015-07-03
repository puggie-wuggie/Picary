package com.puggerino.picary;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.content.ContextWrapper;

public class datePickerResult extends Activity {

    int year;
    int month;
    int day;

    TextView textView1;

    Uri selectedImgUri;
    String selectedPath;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker_result);

        textView1 = (TextView)findViewById(R.id.textView1);

        Bundle extras = getIntent().getExtras();

            year = extras.getInt("year");
            month = extras.getInt("month");
            day = extras.getInt("day");

        textView1.setText(year+" "+month+" "+day);

        Button b = (Button)findViewById(R.id.bGallery);
        Button bCam = (Button)findViewById(R.id.bCamera);
        preview = (ImageView)findViewById(R.id.preview);

        bCam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 100);
            }
        });

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(10);
            }
        });
    }

    public void openGallery(int req_code){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(data.getData() != null){
                selectedImgUri = data.getData();
            }
            else {
                Log.d("selectedPath1: ", "Nothing is here");
                Toast.makeText(getApplicationContext(), "failed to get image", Toast.LENGTH_SHORT).show();
            }

            if(requestCode == 100 && resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedPath = getPath(selectedImgUri);
                preview.setImageURI(selectedImgUri);
                saveImageInternal(photo);
                Log.d("selectedPath1: ", selectedPath);
            }

            if(requestCode == 10){
                selectedPath = getPath(selectedImgUri);
                preview.setImageURI(selectedImgUri);
                Log.d("selectedPath1: ", selectedPath);

            }
        }
    }

    public String getPath(Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        String res = null;
       Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public String saveImageInternal(Bitmap bimage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(mypath);
            //Use compression on Bitmap object to write image to Outputstream
            bimage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public void loadImageInternal(String path){
        try{
            File f = new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            preview.setImageBitmap(b);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_picker_result, menu);
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
