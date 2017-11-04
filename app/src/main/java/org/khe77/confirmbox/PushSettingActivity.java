package org.khe77.confirmbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
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

public class PushSettingActivity extends AppCompatActivity {

    Switch switch100 = null;
    Switch switch200 = null;
    Switch switch300 = null;
    Switch switch400 = null;
    Switch switch500 = null;
    Switch switch600 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_setting);
        Intent intent = getIntent();
        String en = intent.getExtras().getString("en");
        new LoadPushSet().execute("http://172.16.2.5:3000/pushset?en="+en);
    }

    class LoadPushSet extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(PushSettingActivity.this);
        @Override
        protected void onPreExecute() {
            dialog.setMessage("푸쉬설정 로딩 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            switch100 = (Switch)findViewById(R.id.switch100);
            switch200 = (Switch)findViewById(R.id.switch200);
            switch300 = (Switch)findViewById(R.id.switch300);
            switch400 = (Switch)findViewById(R.id.switch400);
            switch500 = (Switch)findViewById(R.id.switch500);
            switch600 = (Switch)findViewById(R.id.switch600);
            dialog.dismiss();
            try {//JSON 파싱 --> ListView에 출력
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    if(obj.getString("task_id").equals("100")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch100.setChecked(true);
                        } else {
                            switch100.setChecked(false);
                        }
                    } else if(obj.getString("task_id").equals("200")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch200.setChecked(true);
                        } else {
                            switch200.setChecked(false);
                        }
                    } else if(obj.getString("task_id").equals("300")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch300.setChecked(true);
                        } else {
                            switch300.setChecked(false);
                        }
                    } else if(obj.getString("task_id").equals("400")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch400.setChecked(true);
                        } else {
                            switch400.setChecked(false);
                        }
                    } else if(obj.getString("task_id").equals("500")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch500.setChecked(true);
                        } else {
                            switch500.setChecked(false);
                        }
                    } else if(obj.getString("task_id").equals("600")) {
                        if(obj.getString("push_yn").equals("Y")) {
                            switch600.setChecked(true);
                        } else {
                            switch600.setChecked(false);
                        }
                    }
                }
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
    public void save(View view) {
        switch100 = (Switch)findViewById(R.id.switch100);
        switch200 = (Switch)findViewById(R.id.switch200);
        switch300 = (Switch)findViewById(R.id.switch300);
        switch400 = (Switch)findViewById(R.id.switch400);
        switch500 = (Switch)findViewById(R.id.switch500);
        switch600 = (Switch)findViewById(R.id.switch600);

        JSONArray jsonArray = new JSONArray();
        Intent intent = getIntent();
        String en = intent.getExtras().getString("en");
        JSONObject obj = null;
        try {
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "100");
            obj.put("push_yn", switch100.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "200");
            obj.put("push_yn", switch200.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "300");
            obj.put("push_yn", switch300.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "400");
            obj.put("push_yn", switch400.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "500");
            obj.put("push_yn", switch500.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
            obj = new JSONObject();
            obj.put("en", en);
            obj.put("task_id", "600");
            obj.put("push_yn", switch600.isChecked() ? "Y" : "N");
            jsonArray.put(obj);
        } catch (JSONException e) {

        }
        new PushsetSave().execute("http://172.16.2.5:3000/pushset", jsonArray.toString());
    }

    class PushsetSave extends AsyncTask<String,String,String> {
        ProgressDialog dialog = new ProgressDialog(PushSettingActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("PUT");
                    conn.setDoInput(true); conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write( "pushsetData="+params[1]);
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
            dialog.setMessage("저장 중...");
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//로그인 성공
                    Toast.makeText(PushSettingActivity.this,
                            "저장 되었습니다.",
                            Toast.LENGTH_SHORT).show();
                } else {//로그인 실패
                    Toast.makeText(PushSettingActivity.this,
                            "결재 처리중 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    //푸쉬설정 이동
    public void confirm(View view) {
        finish();
    }

    //푸쉬설정 이동
    public void logout(View view) {
        handler.sendEmptyMessage(1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if( msg.what == 1 ) {
                Intent intent = new Intent(PushSettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    };
}
