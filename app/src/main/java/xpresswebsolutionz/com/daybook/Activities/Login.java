package xpresswebsolutionz.com.daybook.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;


public class Login extends AppCompatActivity {

    EditText editText_UserName,editText_Password;
    RelativeLayout relativeLayout_Login;
    Button button_Guest;

    String userName="",userPassword="";

    RequestQueue requestQueue;

    ProgressDialog pd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        editText_UserName = findViewById(R.id.editText_username);
        editText_Password = findViewById(R.id.editText_password);
        relativeLayout_Login = findViewById(R.id.relative_login);
        button_Guest = findViewById(R.id.button_Guest);

        requestQueue = Volley.newRequestQueue(Login.this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Signing In.....");


        relativeLayout_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = editText_UserName.getText().toString().trim();
                userPassword = editText_Password.getText().toString().trim();

                if (!userName.isEmpty() && !userPassword.isEmpty()){

                    userLogin();

                }else {
                    Toast.makeText(Login.this, "Please Enter UserName and Password", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button_Guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,MainActivity.class));
                finish();
            }
        });


    }


    void userLogin(){

        pd.show();

        StringRequest request=new StringRequest(Request.Method.POST, Util.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("login");
                    String message=object.getString("message");

                    if (message.contains("Login Sucessful")){
                        for (int i=0;i<array.length();i++){
                            JSONObject object1=array.getJSONObject(i);
                            int id=object1.getInt("Id");
                            editor.putInt(Util.Key_UserID,id);
                            editor.putString(Util.Key_UserName,object1.getString("UserName"));
                            editor.putBoolean(Util.Key_LoginStatus,true);
                            editor.commit();
                        }
                        pd.dismiss();
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();


                    }else {

                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pd.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e("Error",error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("UserName",userName);
                map.put("Passsword",userPassword);
                return map;
            }
        };
        requestQueue.add(request);

    }


}
