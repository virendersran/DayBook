package xpresswebsolutionz.com.daybook.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPersonsFragment extends Fragment {

    EditText editText_Name,editText_Phone,editText_Email,editText_Address1,editText_Address2,editText_Note;
    Button btn_Save;
    String personName,personPhone,personEmail,personAddress1,personAddress2,personNote;

    RequestQueue requestQueue;
    ProgressDialog pd;
    SharedPreferences sharedPreferences;



    public AddPersonsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_persons, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pd=new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Adding Contact");

        editText_Name = view.findViewById(R.id.editText_addContact_Name);
        editText_Phone = view.findViewById(R.id.editText_addContact_Phone);
        editText_Email = view.findViewById(R.id.editText_addContact_Email);
        editText_Address1 = view.findViewById(R.id.editText_addContact_Address1);
        editText_Address2 = view.findViewById(R.id.editText_addContact_Address2);
        editText_Note = view.findViewById(R.id.editText_addContact_Note);

        btn_Save = view.findViewById(R.id.button_addContact);

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

        return view;
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
                       Toast.makeText(getActivity(), "Contact Added SuccessFully", Toast.LENGTH_SHORT).show();
                       clearFields();

                    }else {

                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
                map.put("UserId", String.valueOf(sharedPreferences.getInt("userID",0)));
                return map;
            }
        };
        requestQueue.add(request);

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
