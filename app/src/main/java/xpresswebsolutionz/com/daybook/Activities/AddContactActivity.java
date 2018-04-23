package xpresswebsolutionz.com.daybook.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;
import xpresswebsolutionz.com.daybook.Utils.VolleySingleton;

public class AddContactActivity extends AppCompatActivity {


    EditText editText_Name,editText_Phone,editText_Email,editText_Address1,editText_Address2,editText_Note;
    Button btn_Save;
    String personName,personPhone,personEmail,personAddress1,personAddress2,personNote;

    ProgressDialog pd;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Adding Contact");

        editText_Name = findViewById(R.id.editText_addContact_Name);
        editText_Phone = findViewById(R.id.editText_addContact_Phone);
        editText_Email = findViewById(R.id.editText_addContact_Email);
        editText_Address1 = findViewById(R.id.editText_addContact_Address1);
        editText_Address2 = findViewById(R.id.editText_addContact_Address2);
        editText_Note = findViewById(R.id.editText_addContact_Note);

        btn_Save = findViewById(R.id.button_addContact);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                personName = editText_Name.getText().toString().trim();
                personPhone = editText_Phone.getText().toString().trim();
                personEmail = editText_Email.getText().toString().trim();
                personAddress1 = editText_Address1.getText().toString().trim();
                personAddress2 = editText_Address2.getText().toString().trim();
                personNote = editText_Note.getText().toString().trim();
                if (isValid()) {
                    addContact();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, ContactActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
        finish();
    }

    void addContact(){

        pd.show();
        StringRequest request=new StringRequest(Request.Method.POST, Util.addContactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int message=object.getInt("success");

                    if (message == 1){

                        pd.dismiss();
                        Toast.makeText(AddContactActivity.this, "Contact Added SuccessFully", Toast.LENGTH_SHORT).show();
                        clearFields();



                    }else {

                        Toast.makeText(AddContactActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                map.put("Name",personName);
                map.put("Address",personAddress1+" "+ personAddress2);
                map.put("PhoneNumber",personPhone);
                map.put("Email",personEmail);
                map.put("Notes",personNote);
                map.put("UserId", String.valueOf(sharedPreferences.getInt(Util.Key_UserID,0)));
                return map;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    public boolean isValid(){
        boolean isChecked=true;
        if (editText_Name.getText().toString().isEmpty()){
            isChecked=false;
            editText_Name.setError("Please Enter Name");
            Log.e("valid","False");
        }

        if (editText_Phone.toString().isEmpty()){
            isChecked=false;
            editText_Phone.setError("Please Enter Phone");
        }else if (!(editText_Phone.getText().toString().length()==10)){
            isChecked=false;
            editText_Phone.setError("Please Enter Valid Phone");
        }

        if (editText_Email.getText().toString().isEmpty()){
            isChecked=false;
            editText_Email.setError("Please Enter Email");
        }else if (!personEmail.contains("@")){
            isChecked=false;
            editText_Email.setError("Please Enter Valid Email");
        }

        if (editText_Address1.getText().toString().isEmpty()){
            isChecked=false;
            editText_Address1.setError("Please Enter Address");
        }

        if (editText_Note.getText().toString().isEmpty()){
            isChecked=false;
            editText_Note.setError("Please Enter a Note");
        }

        return isChecked;
    }


    void clearFields(){

        editText_Name.setText("");
        editText_Phone.setText("");
        editText_Email.setText("");
        editText_Address1.setText("");
        editText_Address2.setText("");
        editText_Note.setText("");
    }


}
