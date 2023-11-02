package com.example.fetchcodingexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.*;

public class MainActivity extends AppCompatActivity {

    private TextView output;
    private Button getItems;
    private RecyclerView output2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.outputTextView);
        output2 = (RecyclerView) findViewById(R.id.recyclerView);
        getItems = (Button) findViewById(R.id.getItemsButton);
        //make textview scrollable
        output.setMovementMethod(new ScrollingMovementMethod());

        String s1[] = new String[0];
        try {
            s1 = new String[]{deserializeItem()};
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        recyclerAdapter recyclerAdapter = new recyclerAdapter(this, s1);

        getItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String itemText = deserializeItem();
                    output.setText(itemText);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private String deserializeItem() throws IOException, JSONException {
        String itemText = "";
        String jsonUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        Gson gson = new Gson();

        //Reads data from JSON File and converts it to a string
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

        //Sorts by list ID, then sorts naturally by name
        Comparator<Item> comparator
                = Comparator.comparing(Item::getListId)
                .thenComparing(value -> {
                    int indexOf = value.name.indexOf(' ');
                    return Integer.parseInt(value.name.substring(indexOf + 1));
                });
        Collections.sort(itemList, comparator);

        //converts items to a string to be return and displayed.
        for (int i=0; i<itemList.size(); i++)
        {
            itemText += itemList.get(i).toString();
        }
        return itemText;
    }


}