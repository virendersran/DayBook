package xpresswebsolutionz.com.daybook.Fragments;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.Bean.BeanDepartment;
import xpresswebsolutionz.com.daybook.Bean.BeanPerson;
import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;
import xpresswebsolutionz.com.daybook.Utils.VolleySingleton;


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
    ArrayList<BeanPerson> contactIDList;
    ArrayList<BeanDepartment> deptIDList;
    SharedPreferences sharedPreferences;
    ProgressDialog pd;

    Calendar myCalendar;

    int contactID,deptID,payID,catID,freID,statusID;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_add, container, false);
       date = DateFormat.getDateInstance().format(new Date());
       dateTime=DateFormat.getDateTimeInstance().format(new Date());
        myCalendar = Calendar.getInstance();

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

               if (isValid()) {

                   Log.d("cID", String.valueOf(contactID));
                   Log.d("Dept", String.valueOf(deptID));
                   Log.d("Amount", addAmount);
                   Log.d("Purpose", addPurpose);
                   Log.d("Pay", String.valueOf(payID));
                   Log.d("cat", String.valueOf(catID));
                   Log.d("Freq", String.valueOf(freID));
                   Log.d("Status", String.valueOf(statusID));

                   addDayBook();
               }





           }
       });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        textView_Date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });




       return view;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textView_Date.setText(dateFormat(sdf.format(myCalendar.getTime())));
//        dateFormat(sdf.format(myCalendar.getTime()));
    }

    public String dateFormat(String date) {

        String date1 = "";

        String s[] = date.split("-");

        int year = Integer.parseInt(s[0]);
        int month = Integer.parseInt(s[1]);
        String s1 = s[2];
        Log.e("time",String.valueOf(year)+" "+String.valueOf(month)+" "+s1);
//        String[] s2 = s1.split("T");
//        int day = Integer.parseInt(s2[0]);
//
        switch (month) {
            case 1:
                date1 = "Jan " +s1+","+year;
                break;
            case 2:
                date1 = "Feb " +s1+","+year;
                break;
            case 3:
                date1 = "Mar " +s1+","+year;
                break;
            case 4:
                date1 = "Apr " +s1+","+year;
                break;
            case 5:
                date1 = "May " +s1+","+year;
                break;
            case 6:
                date1 = "June " +s1+","+year;
                break;
            case 7:
                date1 = "July " +s1+","+year;
                break;
            case 8:
                date1 = "Aug " +s1+","+year;
                break;
            case 9:
                date1 = "Sep " +s1+","+year;
                break;
            case 10:
                date1 = "Oct " +s1+","+year;
                break;
            case 11:
                date1 = "Nov " +s1+","+year;
                break;
            case 12:
                date1 = "Dec " +s1+","+year;
                break;

            default:date1 = "";
                break;
        }


        return date1;
    }

    public boolean isValid(){
        boolean isChecked = true;

        if (editText_Amount.getText().toString().trim().isEmpty()){
            isChecked = false;
            editText_Amount.setError("Please Enter Amount");
        }
        if (editText_Purpose.getText().toString().trim().isEmpty()){
            isChecked = false;
            editText_Purpose.setError("Please Enter Any Purpose");
        }
        if (contactID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Name", Toast.LENGTH_SHORT).show();

        }
        if (deptID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Department", Toast.LENGTH_SHORT).show();

        }
        if (payID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Payment Type", Toast.LENGTH_SHORT).show();

        }
        if (catID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Payment category", Toast.LENGTH_SHORT).show();

        }
        if (freID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Frequency", Toast.LENGTH_SHORT).show();

        }
        if (statusID == 0){
            isChecked = false;
            Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();

        }

        return isChecked;
    }

    void initLists(){

        payList = new ArrayList<>();
        catgrylist = new ArrayList<>();
        freqList = new ArrayList<>();
        statusList = new ArrayList<>();

        payList.add("Payment Type");
        payList.add("Pay");
        payList.add("Receive");
        payList.add("Loan Pay");
        payList.add("Loan Receive");


        catgrylist.add("Select Category");
        catgrylist.add("Refundable");
        catgrylist.add("Non Refundable");

        freqList.add("Select Frequency");
        freqList.add("Routine");
        freqList.add("Monthly");
        freqList.add("Once in a while");
        freqList.add("Yearly");

        statusList.add("Select Status");
        statusList.add("Officially");
        statusList.add("Unofficially");

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
                    for (int k=0;k<contactIDList.size();k++){
                        BeanPerson beanPerson=contactIDList.get(k);
                        if (addName.equals(beanPerson.getName())){
                            contactID=beanPerson.getPersonID();
                        }
                    }
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

                    for (int k=0;k<deptIDList.size();k++){
                        BeanDepartment department = deptIDList.get(k);
                        if (addDept.equals(department.getDeptName())){
                            deptID = department.getId();
                        }
                    }
                   // Toast.makeText(getActivity(), String.valueOf(deptID), Toast.LENGTH_SHORT).show();
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
                    payID = position;
                    Log.e("payID",String.valueOf(payID));

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
                    catID = position;
                    Log.e("CatId",String.valueOf(catID));

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
                    freID = position;
                    Log.e("FreID",String.valueOf(freID));

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
                    statusID = position;
                   Log.e("statusID",String.valueOf(statusID));

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

        contactIDList = new ArrayList<>();

        StringRequest request=new StringRequest(Request.Method.POST, Util.getContactUrl, new Response.Listener<String>() {
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
                            contactIDList.add(new BeanPerson(object1.getInt("Id"),object1.getString("Name")));

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
        })
        {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("UserId", String.valueOf(sharedPreferences.getInt(Util.Key_UserID, 0)));
                return map;
            }

        };
        requestQueue.add(request);

    }

    void getDepartList(){
        deptList = new ArrayList<>();
        deptList.add("Select Department");
        deptIDList = new ArrayList<>();

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
                            int id = object1.getInt("Id");
                            String name=object1.getString("Name");
                            deptList.add(name);
                            deptIDList.add(new BeanDepartment(id,name));
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
        })
        {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("UserId", String.valueOf(sharedPreferences.getInt(Util.Key_UserID, 0)));
                return map;
            }

        };

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
                Log.e("Date",textView_Date.getText().toString());
                map.put("Date",textView_Date.getText().toString());
                map.put("PersonName",addName);
                map.put("DeptName",addDept);
                map.put("Amount",addAmount);
                map.put("Purpose",addPurpose);
                map.put("PaymentType",addPayment);
                map.put("Category",addcategory);
                map.put("Frequancy",addFrequency);
                map.put("Status",addStatus);
                map.put("UserId", String.valueOf(sharedPreferences.getInt(Util.Key_UserID,0)));
                map.put("CreatedDate",dateTime);
                return map;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

}
