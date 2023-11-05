package com.example.fetchcodingexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textView;
    private Button button;
    //private RecyclerView output2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        button = (Button) findViewById(R.id.getItemsButton);

        String s1[] = new String[0];

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String itemText = new DeserializeItem().toString();
                    DeserializeItem di = new DeserializeItem();
                    di.execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public class DeserializeItem extends AsyncTask<String, String, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... urls) {
            try {
                //String jsonUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
                Gson gson = new Gson();

                //Reads data from JSON URL and converts it to a string
                /*InputStream is = new URL(urls[0]).openStream();
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                is.close();*/

                //code used to test other input
                InputStream is = getAssets().open("fetchJSONFile.json");
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                is.close();

                //Convert json string to array of Item objects
                Item[] items = gson.fromJson(jsonTxt, Item[].class);
                List<Item> itemList = new ArrayList<Item>();

                //Eliminates objects with null and empty item names
                for (int i=0; i<items.length; i++)
                {
                    if (items[i].getItemName() == null) {continue;}
                    else if (items[i].getItemName().equals("")) {continue;}
                    else {itemList.add(items[i]);}
                }
                itemList = sortList(itemList);
                return itemList;

            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }

        //Sorts the data to be displayed
        List<Item> sortList(List<Item> itemList) {
            try {
                Comparator<Item> comparator
                        = Comparator.comparing(Item::getListId)
                        .thenComparing(value -> {
                            if (android.text.TextUtils.isDigitsOnly(value.name.substring(10)))
                                return Integer.parseInt(value.name.substring(5));
                            else
                                return Integer.MAX_VALUE;
                        });
                Collections.sort(itemList, comparator);
                return itemList;
            } catch (Exception e) {
                alertUser(MainActivity.this);
            }
            finally {return itemList;}
        }

        public void alertUser(Context context){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("WARNING");
            dialog.setMessage(this.toString());
            dialog.setNeutralButton("Ok", null);
            dialog.create().show();
        }

        @Override
        protected void onPostExecute(List<Item> itemList)
        {
            String[] itemText = new String[itemList.size()];

            //converts items to a string to be returned and displayed
            for (int i=0; i<itemList.size(); i++)
            {
                itemText[i] = itemList.get(i).toString();
            }
                putDataIntoRecyclerView(itemText);
        }

        void putDataIntoRecyclerView(String[] itemList)
        {
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, itemList);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}