package xpresswebsolutionz.com.daybook.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xpresswebsolutionz.com.daybook.Bean.BeanDepartment;
import xpresswebsolutionz.com.daybook.R;
import xpresswebsolutionz.com.daybook.Utils.Util;

public class DepartmentAdapter extends ArrayAdapter<BeanDepartment> {
    Context context;
    ArrayList<BeanDepartment> deptList;
    int resource;
    RequestQueue requestQueue;

    public DepartmentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BeanDepartment> objects) {
        super(context, resource, objects);
        this.context = context;
        deptList = objects;
        this.resource=resource;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource,parent,false);
        final BeanDepartment beanDepartment=getItem(position);
        TextView textView=view.findViewById(R.id.key_name);
        textView.setText(beanDepartment.getDeptName());
        LinearLayout linearLayout=view.findViewById(R.id.bottom_wrapper);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDept(beanDepartment.getId(),position);
            }
        });
        return view;
    }

    void deleteDept(final int id,final int i){
        StringRequest request=new StringRequest(Request.Method.POST, Util.deleteDeptUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    String message=object.getString("message");
                    if (message.contains("Deleted sucessfully")){
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        deptList.remove(i);
                        notifyDataSetChanged();
                    }else {

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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Id", String.valueOf(id));
                return map;
            }
        };
        requestQueue.add(request);
    }
}
