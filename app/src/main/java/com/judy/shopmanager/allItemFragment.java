package com.judy.shopmanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class allItemFragment extends Fragment {

    DatabaseReference db;
    ListView listView;
    Spinner sortKey;
    EditText value;
    TextView total;
    Button sortBtn;
    String sortter,sortValue;
    Query q;
    int f=0,count;
    ArrayAdapter<String> spinAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_item, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sortBtn=(Button)getView().findViewById(R.id.sortBtn);
        sortKey=(Spinner)getView().findViewById(R.id.sortSpinner);
        value=(EditText)getView().findViewById(R.id.sortValue);
        total=(TextView)getView().findViewById(R.id.totalFilter);
        db= FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Items");
        FirebaseListAdapter<Item> firebaseListAdapter = new FirebaseListAdapter<Item>(getActivity(),Item.class,R.layout.rowitem,db) {
            @Override
            protected void populateView(View v, Item item, int position) {
                TextView text1=(TextView)v.findViewById(R.id.text1);
                TextView text2=(TextView)v.findViewById(R.id.text2);
                TextView text3=(TextView)v.findViewById(R.id.text3);
                TextView text4=(TextView)v.findViewById(R.id.text4);
                TextView text5=(TextView)v.findViewById(R.id.text5);
                TextView text6=(TextView)v.findViewById(R.id.text6);
                TextView text7=(TextView)v.findViewById(R.id.text7);
                text1.setText(item.getIdno());
                text2.setText(item.getName());
                text3.setText(item.getBrand());
                text4.setText(item.getCost());
                text5.setText(item.getDate());
                text6.setText(item.getStore());
                text7.setText(item.getType());
            }
        };
        listView = (ListView) getView().findViewById(R.id.listView);
        listView.setAdapter(firebaseListAdapter);
        String array[]={"Store","Name","Brand","Type"};
        spinAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,array);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortKey.setAdapter(spinAdapter);
        sortKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortter=parent.getItemAtPosition(position).toString().toLowerCase().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                f=1;
            }
        });
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sortValue=value.getText().toString().toLowerCase().trim();
                if(TextUtils.isEmpty(sortValue)||f==1){
                    value.setError(getString(R.string.required_field));
                }
                else {
                        q=db.orderByChild(sortter).equalTo(sortValue);
                        new loadItems().execute();

                }

                }
        });
    }
    public class loadItems extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            count=0;
        }

        @Override
        protected Void doInBackground(Void... params) {

            q.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    total.setText(String.valueOf(++count));
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
            FirebaseListAdapter<Item> firebaseAdapter = new FirebaseListAdapter<Item>(getActivity(),Item.class,R.layout.rowitem,q) {
                @Override
                protected void populateView(View v, Item item, int position) {
                    count++;
                    TextView text1=(TextView)v.findViewById(R.id.text1);
                    TextView text2=(TextView)v.findViewById(R.id.text2);
                    TextView text3=(TextView)v.findViewById(R.id.text3);
                    TextView text4=(TextView)v.findViewById(R.id.text4);
                    TextView text5=(TextView)v.findViewById(R.id.text5);
                    TextView text6=(TextView)v.findViewById(R.id.text6);
                    TextView text7=(TextView)v.findViewById(R.id.text7);
                    text1.setText(item.getIdno());
                    text2.setText(item.getName());
                    text3.setText(item.getBrand());
                    text4.setText(item.getCost());
                    text5.setText(item.getDate());
                    text6.setText(item.getStore());
                    text7.setText(item.getType());


                }
            };
            listView = (ListView) getView().findViewById(R.id.listView);
            listView.setAdapter(firebaseAdapter);

        }
    }
}

