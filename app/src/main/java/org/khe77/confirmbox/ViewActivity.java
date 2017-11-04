package org.khe77.confirmbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class ViewActivity extends AppCompatActivity {

    String en = "";
    String task_id = "";
    String cfm_seq = "";
    String title = "";
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent intent = getIntent();
        en = intent.getExtras().getString("en");
        task_id = intent.getExtras().getString("task_id");
        cfm_seq = intent.getExtras().getString("cfm_seq");
        title = intent.getExtras().getString("title");
        text = intent.getExtras().getString("text");
        TextView titleText = (TextView)findViewById(R.id.title);
        titleText.setText(title);
        TextView listText = (TextView)findViewById(R.id.text);
        listText.setText(text);

    }

    //결재
    public void confirm(View view) {
        EditText opinion = (EditText)findViewById(R.id.opinion);
        new Confirm().execute("http://172.16.2.5:3000/confirm", opinion.getText().toString());
    }

    //반려
    public void reject(View view) {
        EditText opinion = (EditText)findViewById(R.id.opinion);
        new Confirm().execute("http://172.16.2.5:3000/reject", opinion.getText().toString());
    }

    class Confirm extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(UserActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                JSONObject postDataParams = new JSONObject();
                EditText opinion = (EditText)findViewById(R.id.opinion);
                postDataParams.put("en", en);
                postDataParams.put("cfm_seq", cfm_seq);
                postDataParams.put("task_id", task_id);
                postDataParams.put("cfm_opinion", params[1]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("PUT");
                    conn.setDoInput(true); conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.toString();
        }

        @Override
        protected void onPreExecute() {
            //dialog.setMessage("사용자 목록 로딩 중...");
            //dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {//s-->서버에서 받은 JSON문자열
            try {
                JSONObject json = new JSONObject(s);
                if (json.getBoolean("result") == true) {//결재 성공
                    Intent intent = new Intent(ViewActivity.this, ListActivity.class);
                    intent.putExtra("en",en);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {//로그인 실패
                    Toast.makeText(ViewActivity.this,
                            "결재 처리중 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
