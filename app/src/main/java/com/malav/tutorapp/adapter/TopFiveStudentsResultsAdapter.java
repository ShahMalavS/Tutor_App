package com.malav.tutorapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.malav.tutorapp.R;
import com.malav.tutorapp.beans.OnlineResources;
import com.malav.tutorapp.beans.Results;

import java.util.ArrayList;

/**
 * Created by shahmalav on 09/03/17.
 */

public class TopFiveStudentsResultsAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    ArrayList<Results> listResults;
    Results result;
    private String TAG = "TopFiveStudentsResultsAdapter";

    public TopFiveStudentsResultsAdapter(Context context, ArrayList<Results> listResults) {
        this.listResults = listResults;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listResults.size();
    }

    @Override
    public Object getItem(int location) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.item_view_results_exam_results, null);

        result = listResults.get(position);

        TextView srNo = (TextView) convertView.findViewById(R.id.srNo);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView marks = (TextView) convertView.findViewById(R.id.marks);

        srNo.setText(result.getSrNo());
        name.setText(result.getName());
        marks.setText(result.getMarksObtained()+" / "+result.getMarksTotal());

        return convertView;
    }
}
