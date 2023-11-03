package com.example.fetchcodingexample;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
                    di.execute("temp");

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public class DeserializeItem extends AsyncTask<String, String, List<Item>> {
        //private Context context;

        @Override
        protected List<Item> doInBackground(String... urls) {
            try {
                String itemText = "";
                String jsonUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
                Gson gson = new Gson();

                //Reads data from JSON File and converts it to a string
                InputStream is = new URL(jsonUrl).openStream();
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
                sortList(itemList);
                return itemList;

            }
            catch(IOException e){
                throw new RuntimeException(e);
            }
        }

        List<Item> sortList(List<Item> itemList)
        {
            try {
                Comparator<Item> comparator
                        = Comparator.comparing(Item::getListId)
                        .thenComparing(value -> {
                            int indexOf = value.name.indexOf(' ');
                            return Integer.parseInt(value.name.substring(indexOf + 1));
                        });
                Collections.sort(itemList, comparator);
                return itemList;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(List<Item> itemList)
        {
            int duration = Toast.LENGTH_SHORT;
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