package com.example.ukol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Environment;
import android.util.Log;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Activity2 extends AppCompatActivity {

    private static final String TAG = "MMMMM";
    final static String appFileName = "appFile.txt";
    final static String appDirName = "/appDir/";
    final static String appDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + appDirName;
    String oneLine = "";
    Button btn_back;

    TextView tv_scrollable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        btn_back = findViewById(R.id.btn_back);
        tv_scrollable = findViewById(R.id.tv_Scrollable);
        tv_scrollable.setMovementMethod(new ScrollingMovementMethod());

        readFromExternalMemory();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });
    }



    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private boolean readFromExternalMemory()
    {
        try
        {
            File appFile = new File(appDirPath + appFileName);
            if(appFile.exists())
            {
                FileInputStream fis = new FileInputStream (new File(appDirPath + appFileName));
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);

                String readResult = "";

                while ( (oneLine = bufferedReader.readLine()) != null )
                {
                    readResult += oneLine + System.getProperty("line.separator");
                }

                bufferedReader.close();
                isr.close();
                fis.close();

                tv_scrollable.setText(readResult);
            }

            return true;
        }
        catch (Exception e)
        {
            tv_scrollable.setText("Chyba cteni");
            Log.d(TAG, "Chyba cteni \n" + e.toString());
            return false;
        }
    }
}