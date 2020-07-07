package com.example.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.Fragment;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.AdapterView.*;

//import androidx.fragment.app.Fragment;

public class Casesfragment extends Fragment{

    View view;
    Button btn;
    Spinner spinner;
    BarChart barChart;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cases, container, false);
        final List<String> locations = new ArrayList<>();
        locations.add("Montreal");
        locations.add("Canada");
        locations.add("WorldWide");
        spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(),android.R.layout.simple_spinner_item,locations);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                databaseselect(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        barChart = view.findViewById(R.id.bargraph);
        btn = view.findViewById(R.id.profile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changefragment();
            }
        });
        return view;
    }
    public void changefragment(){
        Fragment fg = new Profile();
        FragmentChange fc = (FragmentChange)getActivity();
        fc.replacefragment(fg);
    }
    public void databaseselect(int pos){
        final int position=pos;
        Log.i("databaseselected","position "+pos);
        DatabaseReference databaseReference = database.getReference("covid").child("bargraphs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String xml = null;
                if(position == 0){
                    xml = (String) dataSnapshot.child("montreal").getValue();
                    displaygraph(xml);
                }else if (position == 1){
                    String xmld = (String) dataSnapshot.child("Canada").child("days").getValue();
                    xml = (String) dataSnapshot.child("Canada").child("count").getValue();
                    displaygraphCanadaorWorld(xmld,xml);
                }else if (position == 2){
                    String xmld = (String) dataSnapshot.child("worldwide").child("days").getValue();
                    xml = (String) dataSnapshot.child("worldwide").child("count").getValue();
                    displaygraphCanadaorWorld(xmld,xml);
                }

                Log.i("database xml","bardata "+position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void displaygraphCanadaorWorld(String xmld,String xml){
        String XMLD = xmld;
        String XML = xml;
        Log.i("printing string","data"+xmld);
        if (XMLD != null){

            String regex = ",";
            String regex2 = ",";
            String[] pairs = xml.split(regex);
            String[] pairs2 = xmld.split(regex2);
            ArrayList<String> days = new ArrayList<String>();
            ArrayList<String> count = new ArrayList<String>();
            for (String a:pairs){
                count.add(a);
            }
            for (String b:pairs2) {
                days.add(b);
            }
            String[] daymonth = new String[days.size()];
            for (int i = 0; i < days.size(); i++) {
                daymonth[i] = days.get(i);
            }
            List<BarEntry> cases = new ArrayList<BarEntry>();
            for (int i = 0; i < count.size(); i++) {
                cases.add(new BarEntry(i, Integer.parseInt(count.get(i))));
            }
            BarDataSet dataSet = null;
            dataSet = new BarDataSet(cases, "count");
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(daymonth);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);
            ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
            sets.add(dataSet);
            BarData barData = new BarData(sets);
            barChart.setData(barData);
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.invalidate();
            barChart.refreshDrawableState();
        }
    }
    public void displaygraph(String xml){

        String XML = xml;
        if (XML != null) {
            String regex = "</array><array>";
            String[] pairs = XML.split(regex);
            ArrayList<String> data = new ArrayList<String>();
            ArrayList<String> days = new ArrayList<String>();
            ArrayList<String> count = new ArrayList<String>();
            for (String a : pairs) {
                data.add(a);
            }
            for (int i = 0; i < data.size(); i += 2) {
                days.add(data.get(i));
            }

            for (int i = 1; i <= data.size(); i += 2) {
                count.add(data.get(i));
            }
            String[] daymonth = new String[data.size() / 2];
            for (int i = 0; i < days.size(); i++) {
                daymonth[i] = days.get(i);
            }
            List<BarEntry> cases = new ArrayList<BarEntry>();
            for (int i = 0; i < count.size(); i++) {
                cases.add(new BarEntry(i, Integer.parseInt(count.get(i))));
            }

            BarDataSet dataSet = null;
            dataSet = new BarDataSet(cases, "count");
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(daymonth);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);
            ArrayList<IBarDataSet> sets = new ArrayList<IBarDataSet>();
            sets.add(dataSet);
            BarData barData = new BarData(sets);
            barChart.setData(barData);
            barChart.setTouchEnabled(true);
            barChart.setDragEnabled(true);
            barChart.setScaleEnabled(true);
            barChart.invalidate();
            barChart.refreshDrawableState();
        }

    }
}
