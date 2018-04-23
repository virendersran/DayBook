package xpresswebsolutionz.com.daybook.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.Adapters.AdapterDisplayContact;
import xpresswebsolutionz.com.daybook.Bean.BeanPerson;
import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;
import xpresswebsolutionz.com.daybook.Utils.VolleySingleton;

public class ContactActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<BeanPerson> contactsList;
    ProgressDialog pd;

    AdapterDisplayContact adp_displayContact;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Contacts");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        listView = findViewById(R.id.listView_DisplayContact_Act);

        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading Contacts...");


        getContactsList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_contacts, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                finish();
                return true;
            case R.id.menu_item_add:
                Intent intent1 = new Intent(this,AddContactActivity.class);
                startActivity(intent1);
                finish();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    void getContactsList(){

        contactsList = new ArrayList<>();
        pd.show();

        StringRequest request=new StringRequest(Request.Method.POST, Util.getContactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("persons");
                    String message=object.getString("message");
                    Log.e("message",message);

                    if (message.contains("Records Retrieved sucessfully")){
                        for (int i=0;i<array.length();i++){
                            JSONObject object1=array.getJSONObject(i);

                            contactsList.add(new BeanPerson(object1.getInt("Id"),object1.getInt("UserId"),
                                    object1.getString("Name"),object1.getString("PhoneNumber"),
                                    object1.getString("Email"),object1.getString("Address"),
                                    object1.getString("Notes")));

                        }

                        pd.dismiss();
                        Log.e("contactList",String.valueOf(contactsList.size()));

                        adp_displayContact = new AdapterDisplayContact(ContactActivity.this,R.layout.adapter_display_contacts,contactsList);
                        listView.setAdapter(adp_displayContact);



                    }else {

                        // Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
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

                Log.e("Error",error.toString());
                pd.dismiss();

            }
        }){


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("UserId", String.valueOf(sharedPreferences.getInt(Util.Key_UserID, 0)));
                    return map;
                }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }
}
