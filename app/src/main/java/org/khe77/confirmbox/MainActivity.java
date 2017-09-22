package org.khe77.confirmbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        handler.sendEmptyMessageDelayed(0, 1000*1);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if( msg.what == 0 ) {
                // 최초 체크!!
                progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                // 앱종료
                finish();
            }
        }
    };
}
