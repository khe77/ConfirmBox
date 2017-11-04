package org.khe77.confirmbox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText en, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        en = (EditText)findViewById(R.id.en);
        pw = (EditText)findViewById(R.id.pw);
    }

    public void onLogin(View view){
        String device_token = FirebaseInstanceId.getInstance().getToken();
        new Login().execute("http://172.16.2.5:3000/login", en.getText().toString(), pw.getText().toString(), device_token);

        //handler.sendEmptyMessage(0);

    }

    class Login extends AsyncTask<String, String, String> {
        //ProgressDialog dialog = new ProgressDialog(UserActivity.this);
        @Override
        protected String doInBackground(String... params) {
            StringBuilder output = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                if(conn != null){
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("POST");
                    //conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write( "en="+params[1]+"&pw="+params[2]+"&device_token="+params[3] );
                    wr.flush();
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
            //dialog.dismiss();
            //JSON 파싱 --> ListView에 출력
            try {
                JSONObject object = new JSONObject(s);
                if(object.length() > 0 ){
                    Log.i("SH", object.getString("en"));
                    Log.i("SH", object.getString("name"));
                    //sharedpreferences
                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                    intent.putExtra("en",object.getString("en"));
                    intent.putExtra("name",object.getString("name"));
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("SH", "empty");
                    Toast.makeText(LoginActivity.this, "사번 또는 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
