package org.khe77.confirmbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView listView = null;
    class Item {
        String title;
        String name;
        Item(String title, String name) {
            this.title = title;
            this.name = name;
        }
    }
    ArrayList<Item> itemList = new ArrayList<Item>();
    class ItemAdapter extends ArrayAdapter {
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            }
            ImageView imageView = (ImageView)convertView.findViewById(R.id.image);
            TextView titleView = (TextView)convertView.findViewById(R.id.title);
            TextView nameView = (TextView)convertView.findViewById(R.id.name);
            Item item = itemList.get(position);
            titleView.setText(item.title);
            nameView.setText(item.name);
            return convertView;
        }

        public ItemAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
            super(context, resource, objects);
        }
    }
    ItemAdapter itemAdpater = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView)findViewById(R.id.listview);
        itemList.add(new Item("Title01", "name01"));
        itemList.add(new Item("Title02", "name02"));
        itemList.add(new Item("Title03", "name03"));
        itemList.add(new Item("Title04", "name04"));
        itemList.add(new Item("Title05", "name05"));
        itemList.add(new Item("Title06", "name06"));
        itemList.add(new Item("Title07", "name07"));
        itemList.add(new Item("Title08", "name08"));
        itemList.add(new Item("Title09", "name09"));
        itemList.add(new Item("Title10", "name10"));
        itemList.add(new Item("Title11", "name11"));
        itemList.add(new Item("Title12", "name12"));
        itemList.add(new Item("Title13", "name13"));
        itemList.add(new Item("Title14", "name14"));
        itemList.add(new Item("Title15", "name15"));
        itemList.add(new Item("Title16", "name16"));
        itemList.add(new Item("Title17", "name17"));
        itemList.add(new Item("Title18", "name18"));
        itemList.add(new Item("Title19", "name19"));
        itemList.add(new Item("Title20", "name20"));
        itemAdpater = new ItemAdapter(ListActivity.this, R.layout.list_item,
                itemList);
        listView.setAdapter(itemAdpater);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handler.sendEmptyMessage(0);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(ListActivity.this, ViewActivity.class);
            startActivity(intent);
            // 앱종료
            finish();
        }
    };
}
