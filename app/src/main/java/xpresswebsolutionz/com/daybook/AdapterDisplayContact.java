package xpresswebsolutionz.com.daybook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterDisplayContact extends ArrayAdapter<BeanPerson>{
    Context context;
    ArrayList<BeanPerson> personList;
    int resource;

    public AdapterDisplayContact(@NonNull Context context, int resource, @NonNull ArrayList<BeanPerson> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        personList = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView textView_Name = view.findViewById(R.id.textView_adapter_display_contact_Name);
        TextView textView_Phone = view.findViewById(R.id.textView_adapter_display_contact_Phone);
        BeanPerson beanPerson = getItem(position);

        textView_Name.setText(beanPerson.getName());
        textView_Phone.setText(beanPerson.getPhone());

        return view;
    }
}
