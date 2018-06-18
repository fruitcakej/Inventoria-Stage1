package com.example.android.inventoriastage1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoriastage1.data.Contract;
import com.example.android.inventoriastage1.data.DbHelper;

public class MainActivity extends AppCompatActivity {

    private DbHelper shipDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup the fabulous fab to enter dummy data
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        shipDbHelper = new DbHelper(this);
        readDisplayData();
    }

    private void readDisplayData() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = shipDbHelper.getReadableDatabase();

        // Define projection to query the DB
        String[] projection = {
                BaseColumns._ID, Contract.Entry.COLUMN_SHIP_NAME, Contract.Entry.COLUMN_SHIP_PRICE,
                        Contract.Entry.COLUMN_SHIP_QUANTITY, Contract.Entry.COLUMN_SHIP_SUPPLIER,
                        Contract.Entry.COLUMN_SHIP_PHONE
        };
        Cursor cursor = db.query(
                Contract.Entry.TABLE_NAME,
                projection,
                null, null, null, null, null);

        TextView displayInfo = findViewById(R.id.display);

        try {

            displayInfo.setText("This Spacestation contains " + cursor.getCount() + " starships.\n\n");
            displayInfo.append(Contract.Entry._ID + " - " +
                    Contract.Entry.COLUMN_SHIP_NAME + " - " +
                    Contract.Entry.COLUMN_SHIP_PRICE + " - " +
                    Contract.Entry.COLUMN_SHIP_QUANTITY + " - " +
                    Contract.Entry.COLUMN_SHIP_SUPPLIER + " - " +
                    Contract.Entry.COLUMN_SHIP_PHONE + "\n");

            // Figure out the index of the columns
            int idColumnIndex = cursor.getColumnIndex(Contract.Entry._ID);
            int nameColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_SHIP_NAME);
            int priceColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_SHIP_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_SHIP_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_SHIP_SUPPLIER);
            int phoneColumnIndex = cursor.getColumnIndex(Contract.Entry.COLUMN_SHIP_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {

                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.

                int currentID = cursor.getInt(idColumnIndex);
                int shipName = cursor.getInt(nameColumnIndex);
                int price = cursor.getInt(priceColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);
                String supplier = cursor.getString(supplierColumnIndex);
                String phone = cursor.getString(phoneColumnIndex);

                // Display the values of all the rows
                displayInfo.append("\n" + currentID + " - " +
                        shipName + " - " +
                        price + " - " +
                        quantity + " - " +
                        supplier + " - " +
                        phone);
            }

        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert dummy data into table
     */

    private void insertData() {

        SQLiteDatabase db = shipDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are keys;
        ContentValues values = new ContentValues();
        values.put(Contract.Entry.COLUMN_SHIP_NAME, Contract.Entry.STARSHIP_ROMULAN_WARBIRD);
        values.put(Contract.Entry.COLUMN_SHIP_PRICE, 65431);
        values.put(Contract.Entry.COLUMN_SHIP_QUANTITY, 2);
        values.put(Contract.Entry.COLUMN_SHIP_SUPPLIER, "Marquis");
        values.put(Contract.Entry.COLUMN_SHIP_PHONE, "099656527386797");

        // Insert the row, returning the primary key of the new row
        long newRowId = db.insert(Contract.Entry.TABLE_NAME, Contract.Entry.NULL, values);
        Log.v("MainActivity", "New Row ID" + newRowId);

        // Update view on screen
        readDisplayData();
    }
}
