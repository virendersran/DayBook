package xpresswebsolutionz.com.daybook;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayContactsFragment extends Fragment {

    ListView listView;
    ArrayList<BeanPerson> contactsList;
    ProgressDialog pd;

    AdapterDisplayContact adp_displayContact;

    public DisplayContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_display_contacts, container, false);
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Loading Contacts...");

        listView = view.findViewById(R.id.listView_DisplayContact);

        getContactsList();

        return view;

    }


    void getContactsList(){

        contactsList = new ArrayList<>();
        pd.show();

        StringRequest request=new StringRequest(Request.Method.GET, Util.getContactUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("persons");
                    String message=object.getString("message");

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

                        adp_displayContact = new AdapterDisplayContact(getActivity(),R.layout.adapter_display_contacts,contactsList);
                        listView.setAdapter(adp_displayContact);



                    }else {

                        // Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());
                pd.dismiss();

            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

}
