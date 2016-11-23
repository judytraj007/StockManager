package com.judy.shopmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


public class soldItemsFragment extends Fragment {

    ListView slistView;
    Spinner sSortKey;
    EditText Svalue;
    TextView countTxt;
    Button sSortBtn;
    String sSortter,sSortValue;
    Query q;
    DatabaseReference db;
    int f=0,count;
    ArrayAdapter<String> spinAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.fragment_sold, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        slistView = (ListView) getView().findViewById(R.id.soldView);
        sSortBtn = (Button) getView().findViewById(R.id.sortSoldBtn);
        sSortKey = (Spinner) getView().findViewById(R.id.sortSoldSpinner);
        Svalue = (EditText) getView().findViewById(R.id.sortSoldValue);
        countTxt = (TextView) getView().findViewById(R.id.totalTxt);
        db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Sold");
        FirebaseListAdapter<Item> listAdapter = new FirebaseListAdapter<Item>(getActivity(), Item.class, R.layout.rowitem, db) {
            @Override
            protected void populateView(View v, Item item, int position) {
                TextView text1 = (TextView) v.findViewById(R.id.text1);
                TextView text2 = (TextView) v.findViewById(R.id.text2);
                TextView text3 = (TextView) v.findViewById(R.id.text3);
                TextView text4 = (TextView) v.findViewById(R.id.text4);
                TextView text5 = (TextView) v.findViewById(R.id.text5);
                TextView text6 = (TextView) v.findViewById(R.id.text6);
                TextView text7 = (TextView) v.findViewById(R.id.text7);
                text1.setText(item.getIdno());
                text2.setText(item.getName());
                text3.setText(item.getBrand());
                text4.setText(item.getCost());
                text5.setText(item.getDate());
                text6.setText(item.getStore());
                text7.setText(item.getType());
            }
        };
        slistView.setAdapter(listAdapter);
        String array[] = {"Store", "Name", "Brand", "Type"};
        spinAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, array);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSortKey.setAdapter(spinAdapter);
        sSortKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSortter = parent.getItemAtPosition(position).toString().toLowerCase().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                f = 1;
            }
        });
        sSortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sSortValue = Svalue.getText().toString().toLowerCase().trim();
                if (TextUtils.isEmpty(sSortValue) || f == 1) {
                    Svalue.setError(getString(R.string.required_field));
                } else {
                    q = db.orderByChild(sSortter).equalTo(sSortValue);
                    new loadData().execute();

                }
            }
        });
    }
    public class loadData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            count=0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            q.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    countTxt.setText(String.valueOf(++count));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FirebaseListAdapter<Item> firebaseAdapter = new FirebaseListAdapter<Item>(getActivity(), Item.class, R.layout.rowitem, q) {
                @Override
                protected void populateView(View v, Item item, int position) {
                    count++;
                    TextView text1 = (TextView) v.findViewById(R.id.text1);
                    TextView text2 = (TextView) v.findViewById(R.id.text2);
                    TextView text3 = (TextView) v.findViewById(R.id.text3);
                    TextView text4 = (TextView) v.findViewById(R.id.text4);
                    TextView text5 = (TextView) v.findViewById(R.id.text5);
                    TextView text6 = (TextView) v.findViewById(R.id.text6);
                    TextView text7 = (TextView) v.findViewById(R.id.text7);
                    text1.setText(item.getIdno());
                    text2.setText(item.getName());
                    text3.setText(item.getBrand());
                    text4.setText(item.getCost());
                    text5.setText(item.getDate());
                    text6.setText(item.getStore());
                    text7.setText(item.getType());

                }
            };
            slistView = (ListView) getView().findViewById(R.id.soldView);
            slistView.setAdapter(firebaseAdapter);

        }
    }

}
