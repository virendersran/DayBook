package xpresswebsolutionz.com.daybook;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    TextView textView_Date;
    EditText editText_Amount,editText_Purpose;
    Spinner spn_name,spn_department,spn_payment,spn_category,spn_freq,spn_status;
    ArrayList<String> namesList,deptList,payList,catgrylist,freqList,statusList;
    Button btn_Save;
    String k="\u20B9";
    String dateTime="";

    ArrayAdapter<String> sAdapterName,sAdapterDept,sAdapterPay,sAdapterCatgry,sAdapterFreq,sAdapterStatus;

    String date="",addName="",addDept="",addAmount="",addPurpose="",addPayment="",addcategory="",addFrequency="",addStatus="";

    RequestQueue requestQueue;
    ArrayList<String> contactsList;
    SharedPreferences sharedPreferences;
    ProgressDialog pd;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_add, container, false);
       date = DateFormat.getDateInstance().format(new Date());
       dateTime=DateFormat.getDateTimeInstance().format(new Date());

       textView_Date = view.findViewById(R.id.text_date);
       editText_Amount = view.findViewById(R.id.editTextamount);
       editText_Purpose = view.findViewById(R.id.editTextpurpose);
      // editText_Amount.setText(" "+k);
       textView_Date.setText(date);

       spn_name = view.findViewById(R.id.spinner_add_name);
       spn_department = view.findViewById(R.id.spinner_add_Department);
       spn_payment = view.findViewById(R.id.spinner_add_payType);
       spn_category = view.findViewById(R.id.spinner_add_category);
       spn_freq = view.findViewById(R.id.spinner_add_frequency);
       spn_status = view.findViewById(R.id.spinner_add_status);

       btn_Save = view.findViewById(R.id.button_Add_Save);

       requestQueue = Volley.newRequestQueue(getActivity());
       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pd=new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Adding");

       getContactsList();
       getDepartList();

       initLists();

       btn_Save.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               addAmount = editText_Amount.getText().toString().trim();
               addPurpose = editText_Purpose.getText().toString().trim();


               Log.d("Name",addName);
               Log.d("Dept",addDept);
               Log.d("Amount",addAmount);
               Log.d("Purpose",addPurpose);
               Log.d("Pay",addPayment);
               Log.d("cat",addcategory);
               Log.d("Freq",addFrequency);
               Log.d("Status",addStatus);

               addDayBook();


           }
       });


       return view;
    }

    void initLists(){

        namesList = new ArrayList<>();
//        deptList = new ArrayList<>();
        payList = new ArrayList<>();
        catgrylist = new ArrayList<>();
        freqList = new ArrayList<>();
        statusList = new ArrayList<>();

        namesList.add("Select Name");
        namesList.add("Parteek");
        namesList.add("Virender");

//        deptList.add("Select Department");
//        deptList.add("Accounts");
//        deptList.add("Ecommerce");
//        deptList.add("Sceience");

        payList.add("Payment Type");
        payList.add("Recieve");
        payList.add("Pay");
        payList.add("Loan Recieve");
        payList.add("Loan pay");

        catgrylist.add("Select Category");
        catgrylist.add("Refundable");
        catgrylist.add("Non Refundable");

        freqList.add("Select Frequency");
        freqList.add("Daliy");
        freqList.add("Weekly");
        freqList.add("Monthly");
        freqList.add("Yearly");

        statusList.add("Select Status");
        statusList.add("officially");
        statusList.add("unofficially");

        sAdapterName = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,contactsList);
        sAdapterDept = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,deptList);
        sAdapterPay = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,payList);
        sAdapterCatgry = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,catgrylist);
        sAdapterFreq = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,freqList);
        sAdapterStatus = new ArrayAdapter<>(getActivity(),R.layout.spinner_item,statusList);

        spn_name.setAdapter(sAdapterName);
        spn_department.setAdapter(sAdapterDept);
        spn_payment.setAdapter(sAdapterPay);
        spn_category.setAdapter(sAdapterCatgry);
        spn_freq.setAdapter(sAdapterFreq);
        spn_status.setAdapter(sAdapterStatus);

        spn_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ){

                    addName = contactsList.get(position);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spn_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {
                    addDept = deptList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {
                    addPayment = payList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {
                    addcategory = catgrylist.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spn_freq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {
                    addFrequency = freqList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0 ) {
                    addStatus = statusList.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    void getContactsList(){

        contactsList = new ArrayList<>();
        contactsList.add("Select Name");

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

                            contactsList.add(object1.getString("Name"));

                        }



                    }else {

                        // Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Error",error.toString());

            }
        });
        requestQueue.add(request);

    }

    void getDepartList(){
        deptList = new ArrayList<>();
        deptList.add("Select Department");

        StringRequest request = new StringRequest(Request.Method.POST, Util.getDeptUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("res",response);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("departments");
                    String message=object.getString("message");
                    Log.e("array",array.toString());
                    if (message.contains("Records Retrieved sucessfully")){
                        for (int i=0;i<array.length();i++){
                            JSONObject object1=array.getJSONObject(i);
                            String name=object1.getString("Name");
                            deptList.add(name);
                        }

                        Log.e("size", String.valueOf(array.length()));

                    }else {

                        // Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    void addDayBook(){

        pd.show();

        StringRequest request = new StringRequest(Request.Method.POST, Util.addDayBook, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int message=object.getInt("success");

                    if (message == 1){

                        spn_name.setSelection(0);
                        spn_department.setSelection(0);
                        spn_category.setSelection(0);
                        spn_freq.setSelection(0);
                        spn_payment.setSelection(0);
                        spn_status.setSelection(0);
                        editText_Amount.setText("");
                        editText_Purpose.setText("");

                        pd.dismiss();
                        Toast.makeText(getActivity(), "Added SuccessFully", Toast.LENGTH_SHORT).show();


                    }else {

                        pd.dismiss();
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

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
                map.put("Date",date);
                map.put("PersonName",addName);
                map.put("DeptName",addDept);
                map.put("Amount",addAmount);
                map.put("Purpose",addPurpose);
                map.put("PaymentType",addPayment);
                map.put("Category",addcategory);
                map.put("Frequancy",addFrequency);
                map.put("Status",addStatus);
                map.put("UserId", String.valueOf(sharedPreferences.getInt("userID",0)));
                map.put("CreatedDate",dateTime);
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

}
