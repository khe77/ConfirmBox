package org.khe77.confirmbox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView listView = null;
    class Item {
        String en;
        String task_id;
        String cfm_seq;
        String cfm_title;
        String cfm_text;
        String cfm_name;
        boolean checked;
        Item(String en, String task_id, String cfm_seq, String cfm_title, String cfm_text, String cfm_name) {
            this.en = en;
            this.task_id = task_id;
            this.cfm_seq = cfm_seq;
            this.cfm_title = cfm_title;
            this.cfm_text = cfm_text;
            this.cfm_name = cfm_name;
            this.checked = false;

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
            TextView enView = (TextView)convertView.findViewById(R.id.en);
            TextView task_idView = (TextView)convertView.findViewById(R.id.task_id);
            TextView cfm_seqView = (TextView)convertView.findViewById(R.id.cfm_seq);
            TextView cfm_titleView = (TextView)convertView.findViewById(R.id.cfm_title);
            TextView cfm_textView = (TextView)convertView.findViewById(R.id.cfm_text);
            TextView cfm_nameView = (TextView)convertView.findViewById(R.id.cfm_name);

            final Item item = itemList.get(position);
            enView.setText(item.en);
            task_idView.setText(item.task_id);
            cfm_seqView.setText(item.cfm_seq);
            cfm_titleView.setText(item.cfm_title);
            cfm_textView.setText(item.cfm_text);
            cfm_nameView.setText(item.cfm_name);

            CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.check);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        item.checked = true;
                    } else {
                        item.checked = false;
                    }
                }
            });
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
        Intent intent = getIntent();
        String en = intent.getExtras().getString("en");
        new LoadConfirmList().execute("http://172.16.2.5:3000/list?en="+en);
    }

    class LoadConfirmList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("결재 목록 로딩 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            dialog.dismiss();
            try {//JSON 파싱 --> ListView에 출력
                JSONArray array = new JSONArray(s);
                ArrayList<String> strings = new ArrayList<String>();
                listView = (ListView)findViewById(R.id.listview);
                for (int i = 0; i < array.length(); i++) {//JSON배열에서 이름 추출
                    JSONObject obj = array.getJSONObject(i);
                    //strings.add(obj.getString("cfm_title"));
                    itemList.add(new Item(obj.getString("en"), obj.getString("task_id"), obj.getString("cfm_seq"), obj.getString("cfm_title"), obj.getString("cfm_text"), obj.getString("cfm_name")));

                }
                /*
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        ListActivity.this, android.R.layout.simple_list_item_1,strings);
                */
                itemAdpater = new ItemAdapter(ListActivity.this, R.layout.list_item,
                        itemList);
                listView.setAdapter(itemAdpater);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");
                    //conn.setDoInput(true); conn.setDoOutput(true);
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }
    }

    //결재
    public void confirm(View view) {
        JSONArray jsonArray = new JSONArray();
        if(itemList.size() > 0) {
            for (int i = 0; i < itemList.size(); i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("en", ((Item)itemList.get(i)).en);
                    obj.put("task_id", ((Item)itemList.get(i)).task_id);
                    obj.put("cfm_seq", ((Item)itemList.get(i)).cfm_seq);
                } catch (JSONException e) {

                }
                jsonArray.put(obj);
            }
            new ConfirmList().execute("http://172.16.2.5:3000/confirms", jsonArray.toString());
        }
    }
    //반려
    public void reject(View view) {
    }

    class ConfirmList extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(ListActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("PUT");
                    conn.setDoInput(true); conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write( "confirmData="+params[1]);
                    writer.flush();
                    writer.close();
                    os.close();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) break;
                        output.append(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            } catch (Exception e) { e.printStackTrace(); }
            return output.toString();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("결재 처리 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공
                    Intent intent = getIntent();
                    String en = intent.getExtras().getString("en");
                    new LoadConfirmList().execute("http://172.16.2.5:3000/list?en="+en);
                } else {//로그인 실패
                    Toast.makeText(ListActivity.this,
                            "결재 처리중 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}
