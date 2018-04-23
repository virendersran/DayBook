package xpresswebsolutionz.com.daybook.Adapters;

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

import xpresswebsolutionz.com.daybook.Bean.BeanDayReport;
import xpresswebsolutionz.com.daybook.R;

public class Adapter_DayBook extends ArrayAdapter<BeanDayReport> {

    Context context;
    int resource;
    ArrayList<BeanDayReport> objects;

    public Adapter_DayBook(@NonNull Context context, int resource, @NonNull ArrayList<BeanDayReport> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource,parent,false);
        TextView textView_Name = view.findViewById(R.id.textView_adapter_daybook_Name);
        TextView textView_Dept = view.findViewById(R.id.textView_adapter_display_department);
        TextView textView_payType = view.findViewById(R.id.paymentType);
        TextView textView_Amount = view.findViewById(R.id.textView_amount);
        TextView textView_Freq = view.findViewById(R.id.textView_frequency);

        BeanDayReport dayReport = getItem(position);

        textView_Name.setText(dayReport.getName());
        textView_Dept.setText(dayReport.getDept());
        textView_payType.setText(dayReport.getPayType());
        textView_Amount.setText(String.valueOf(dayReport.getAmount()));
        textView_Freq.setText(dayReport.getFrequency());


        return view;
    }
}
