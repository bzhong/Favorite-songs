package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct the data source
        readItems();
        // Create the adapter to convert the array to views
        itemsAdapter = new TodoItemAdapter(this, items);
        // Attach the adapter to a ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        String itemText = etNewItem.getText().toString();

        writeNewItem(itemText);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.get(pos).delete();
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("itemText", items.get(pos).text);
                i.putExtra("itemIndex", pos);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int position = data.getExtras().getInt("itemIndex");
            if (position != -1) {
                String updatedText = data.getExtras().getString("itemText");
                updateItem(position, updatedText);
            }
        }
    }

    private void readItems() {
        items = (ArrayList) SQLite.select().from(TodoItem.class).queryList();
    }

    private void writeNewItem(String itemText) {
        TodoItem newItem = new TodoItem();
        newItem.id = maxItemId() + 1;
        newItem.text = itemText;
        newItem.priority = "High";
        itemsAdapter.add(newItem);
        newItem.save();
    }

    private void updateItem(int pos, String text) {
        TodoItem oldItem = items.get(pos);
        oldItem.text = text;
        oldItem.save();
        items.set(pos, oldItem);
        itemsAdapter.notifyDataSetChanged();
    }

    private int maxItemId() {
        int maxId = -1;
        for (TodoItem item: items) {
            if (item.id > maxId) {
                maxId = item.id;
            }
        }
        return maxId;
    }
}
