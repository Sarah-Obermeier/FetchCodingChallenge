package com.example.fetchcodingexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.*;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        button = (Button) findViewById(R.id.getItemsButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DeserializeItem di = new DeserializeItem();
                    di.execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");

                } catch (Exception e) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("WARNING");
                    dialog.setMessage("Something went wrong");
                    dialog.setNeutralButton("Ok", null);
                    dialog.create().show();
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
                InputStream is = new URL(urls[0]).openStream();
                String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
                is.close();

                //code used to test other input variations
                /*InputStream is = getAssets().open("fetchJSONFile.json");
                String jsonTxt = IOUtils.toString(is, "UTF-8");
                is.close();*/

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
                cancel(true);
                return null;
            }
        }

        //Sorts the data to be displayed
        List<Item> sortList(List<Item> itemList) {
            try {
                Comparator<Item> comparator
                        = Comparator.comparing(Item::getListId)
                        .thenComparing(value -> {
                            if (android.text.TextUtils.isDigitsOnly(value.name.substring(5)))
                                return Integer.parseInt(value.name.substring(5));
                            else
                                return Integer.MAX_VALUE;
                        });
                Collections.sort(itemList, comparator);
                return itemList;
            } catch (Exception e) {
                cancel(true);
            }
            return itemList;
        }

        public void alertUser(Context context){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("WARNING");
            dialog.setMessage("Something went wrong");
            dialog.setNeutralButton("Ok", null);
            dialog.create().show();
        }

        @Override
        protected void onPostExecute(List<Item> itemList)
        {
            putDataIntoRecyclerView(itemList);
        }

        //occurs when an error happens while deserializing the json file
        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            alertUser(MainActivity.this);
        }

        void putDataIntoRecyclerView(List<Item> itemList)
        {
            RecyclerAdapter recyclerAdapter = new RecyclerAdapter(MainActivity.this, itemList);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }
}