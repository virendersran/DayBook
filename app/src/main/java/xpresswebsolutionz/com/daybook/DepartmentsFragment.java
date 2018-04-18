package xpresswebsolutionz.com.daybook;


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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentsFragment extends Fragment {
    EditText editText_DeptName;
    Button btn_Save;
    String department_Name;

    RequestQueue requestQueue,requestQueue1;
    ProgressDialog pd;
    SharedPreferences sharedPreferences;
    ListView listView;
    DepartmentAdapter adapter;
    ArrayList<BeanDepartment> deptList;



    public DepartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_departments,container,false);

        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue1 = Volley.newRequestQueue(getActivity());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        pd=new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Adding Department");

        editText_DeptName = view.findViewById(R.id.editText_Dept_name);
        btn_Save = view.findViewById(R.id.button_Add_Dept);
        listView = view.findViewById(R.id.listView_Depts);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                department_Name = editText_DeptName.getText().toString().trim();
                if(!department_Name.isEmpty()){
                    addDept();

                }

            }
        });

        getDepartList();

        return view;
    }

    void addDept(){

        pd.show();
        StringRequest request=new StringRequest(Request.Method.POST, Util.addDeptUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    int message=object.getInt("success");

                    if (message >0){
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Department Added ", Toast.LENGTH_SHORT).show();
                        editText_DeptName.setText("");
                        getDepartList();

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
                map.put("Name",department_Name);
                map.put("UserId", String.valueOf(sharedPreferences.getInt("userID",0)));
                return map;
            }
        };
        requestQueue.add(request);

    }

    void getDepartList(){
        deptList = new ArrayList<>();

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
                            int id=object1.getInt("Id");
                            deptList.add(new BeanDepartment(id,name));
                        }
                        adapter=new DepartmentAdapter(getActivity(),R.layout.dept_list_item,deptList);
                        listView.setAdapter(adapter);
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

}
