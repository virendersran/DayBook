package xpresswebsolutionz.com.daybook.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isReg=sharedPreferences.contains(Util.Key_LoginStatus);
        if(isReg){
            handler.sendEmptyMessageDelayed(101,3000);
        }else{
            handler.sendEmptyMessageDelayed(102,3000);
        }
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==102){
                Intent i=new Intent(SplashActivity.this,Login.class);
                startActivity(i);
                finish();
            }else if(msg.what==101){
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };
}
