package com.judy.shopmanager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {


    DatabaseReference db, mdb;
    SimpleDateFormat formatter ;
    Date date;
    ArrayList<Item> items  = new ArrayList<Item>();
    ArrayList<String> brands=new ArrayList<String>();
    Button reportBtn;
    EditText fromDate;
    Query query, q;
    ListView listView;
    TextView t1, t2, t3;
    int itemCount = 0;
    int brandCount = 0;
    int i, j, totalArd = 0, totalChmd = 0;
    Item[] itemArray;
    int[] ard;
    int[] chmd;
    int[] brandTotal;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.reportList);
        t1 = (TextView) getView().findViewById(R.id.ardTotal);
        t2 = (TextView) getView().findViewById(R.id.chmdTotal);
        t3 = (TextView) getView().findViewById(R.id.total);
        fromDate=(EditText)getView().findViewById(R.id.fromDate);
        setDate s=new setDate(getActivity(),R.id.fromDate);
        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        mdb = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Brands");
        q=mdb.orderByChild("brandName");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                brands.add(dataSnapshot.getValue(Brand.class).getBrandName());

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
        db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Sold");
        query = db.orderByChild("date");

        reportBtn=(Button)getView().findViewById(R.id.reportBtn);
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(fromDate.getText().toString())){
                    fromDate.setError("Required Field!");
                }
                else {
                    try {
                        date = formatter.parse(fromDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                   new loadAllItemsFromFirebase().execute();
                }
            }
        });


    }

    public class loadAllItemsFromFirebase extends AsyncTask<Void,Void,ArrayList<Item>>{

        @Override
        protected ArrayList<Item> doInBackground(Void... params) {
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    items.add(dataSnapshot.getValue(Item.class));

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

            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> allItems) {
            for(int i=0;i<allItems.size();i++)
            try {
                if (formatter.parse(allItems.get(i).getDate()).compareTo(date)<0) {
                    allItems.remove(i);
                }
            }catch (ParseException e){
                e.printStackTrace();
            }
            brandCount=brands.size();
            ard = new int[brandCount];
            chmd = new int[brandCount];
            brandTotal = new int[brandCount];
            for (i = 0; i < brandCount; i++) {
                ard[i] = 0;
                chmd[i] = 0;
                brandTotal[i] = 0;
            }

            itemCount = allItems.size();
            itemArray = new Item[itemCount];
            for (i = 0; i < itemCount; i++)
                itemArray[i] = allItems.get(i);

            for (i = 0; i < itemCount; i++) {
                for (j = 0; j < brandCount; j++) {
                    if ((itemArray[i].getBrand().compareTo(brands.get(j))) == 0) {
                        brandTotal[j]++;
                        if (itemArray[i].getStore().compareTo("ard") == 0) {
                            ard[j]++;
                            totalArd++;
                        }
                        if (itemArray[i].getStore().compareTo("chmd") == 0) {
                            chmd[j]++;
                            totalChmd++;
                        }
                    }
                }
            }
            display();
        }
    }
    void display(){
        listView.setAdapter(new CustomAdapter(getActivity(),brands,ard,chmd,brandTotal));
        t1.setText(String.valueOf(totalArd));
        t2.setText(String.valueOf(totalChmd));
        t3.setText(String.valueOf(itemCount));

    }


}