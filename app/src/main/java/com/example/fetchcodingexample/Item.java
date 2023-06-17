package com.example.fetchcodingexample;

public class Item {
    int id;
    int listId;
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getItemName() {
        return name;
    }

    public void setItemName(String itemName) {
        this.name = name;
    }

    @Override
    public String toString()
    {
        String idString = "";
        if (this.getId() < 10) {idString = "ID: " + this.getId() + "     ";}
        else if (this.getId() < 100) {idString = "ID: " + this.getId() + "  ";}
        else {idString = "ID: " + this.getId();}

        return idString + " | ListID: " + this.getListId() + " | Name: " + this.getItemName() + "\n";
    }

}
