package com.example.ukol;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;



public class MainActivity extends AppCompatActivity {
    Button btn_playerCount;
   // EditText et_dataInput;
    Button btn_history;
    Spinner sp_GameChoice;
    //ListView lv_playerCount;
    TextView textv_playerCount;


    HashMap<String, String> mapGames = new HashMap<String, String>();

    final static String appFileName = "appFile.txt";
    final static String appDirName = "/appDir/";
    final static String appDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + appDirName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapGames.put("Team_Fortres_2","440");
        mapGames.put("CounterStrike_Global_Offensive","730");
        mapGames.put("Guild_Wars_2","1284210");
        mapGames.put("The_Elder_Scrolls_Online","306130");

        btn_playerCount=findViewById(R.id.btn_GetPlayerCount);
        sp_GameChoice = findViewById(R.id.GameSpinner);
        textv_playerCount = findViewById(R.id.textv_playerCount);
        btn_history = findViewById(R.id.btn_ChangeToHistory);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_GameChoice.setAdapter(myAdapter);

        btn_playerCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url = "https://api.steampowered.com/ISteamUserStats/GetNumberOfCurrentPlayers/v1/?appid=" + mapGames.get(sp_GameChoice.getSelectedItem().toString());


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String playerCountString = "";
                                try {
                                    JSONObject responseOfWeb = response.getJSONObject("response");
                                    playerCountString = responseOfWeb.getString("player_count");
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                String data = "The game: "+ sp_GameChoice.getSelectedItem().toString()  + " with the id of: " + mapGames.get(sp_GameChoice.getSelectedItem().toString()) + "\n" +
                                        "Has " + playerCountString.toString() + " currently active players on steam. \n\n";

                               // Toast.makeText(MainActivity.this, "number of current players: " + playerCountString.toString() + " From game: " + sp_GameChoice.getSelectedItem().toString() +" with id of:" +mapGames.get(sp_GameChoice.getSelectedItem().toString()), Toast.LENGTH_LONG).show();
                                textv_playerCount.setText(data);
                                writeToExternalMemory(data);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                );


                queue.add(request);


            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });



    }

    public void openActivity2() {
        Intent intent = new Intent(this, Activity2.class);
        startActivity(intent);
    }

    private boolean writeToExternalMemory(String dataToWrite)
    {
        try
        {
            File appDir = new File(appDirPath);
            if(!appDir.exists())
            {
                appDir.mkdir();
                FileOutputStream fos = new FileOutputStream(appDirPath + appFileName,true);
                fos.write(dataToWrite.getBytes());
                fos.flush();
                fos.close();
            } else {
                FileOutputStream fos = new FileOutputStream(appDirPath + appFileName,true);
                fos.write(dataToWrite.getBytes());
                fos.flush();
                fos.close();
            }

            Log.d("TAG", "ZAPIS JE OK.");
            return true;
        }
        catch (Exception e)
        {
            Log.d("TAG", "Chyba zapisu: " + e.toString());
            return false;
        }
    }



}