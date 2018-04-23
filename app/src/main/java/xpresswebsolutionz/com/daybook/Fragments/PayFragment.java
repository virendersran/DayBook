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

import xpresswebsolutionz.com.daybook.Adapters.Adapter_DayBook;
import xpresswebsolutionz.com.daybook.Bean.BeanDayReport;
import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;
import xpresswebsolutionz.com.daybook.Utils.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayFragment extends Fragment {

    ArrayList<BeanDayReport> reportListPay;
    ProgressDialog pd;
    ListView listView;
    Adapter_DayBook adapter_dayBook;
    SharedPreferences sharedPreferences;

    public PayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        listView = view.findViewById(R.id.listView_DayBookPay);
        pd=new ProgressDialog(getContext());
        pd.setCancelable(false);
        pd.setMessage("Loading Report...");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        getDayReport();

        return view;
    }

    void getDayReport(){

        reportListPay = new ArrayList<>();
        pd.show();

        StringRequest request=new StringRequest(Request.Method.POST, Util.getDayReport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array=object.getJSONArray("report");
                    String message=object.getString("message");
                    if (message.contains("Records Retrieved sucessfully")){
                        for (int i=0;i<array.length();i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            String date = object1.getString("Date");
                            String name = object1.getString("PersonName");
                            String department = object1.getString("DeptName");
                            String purpose = object1.getString("Purpose");
                            String type = object1.getString("PaymentType");
                            String category = object1.getString("Category");
                            String frequency = object1.getString("Frequancy");
                            String status = object1.getString("Status");
                            double amount = object1.getDouble("Amount");
                            if (type.toLowerCase().contains("pay")) {
                                reportListPay.add(new BeanDayReport(date,name,department,purpose,type,category,frequency,status,amount));
                            }
                        }
                        pd.dismiss();

                            adapter_dayBook = new Adapter_DayBook(getActivity(), R.layout.raw_day_record_item, reportListPay);
                            listView.setAdapter(adapter_dayBook);




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

        }
                ;
        VolleySingleton.getInstance(getContext()).addToRequestQueue(request);

    }



}
