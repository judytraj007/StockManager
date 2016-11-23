package com.judy.shopmanager;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BrandFragment extends Fragment {

    EditText brandName;
    Button addBrand;
    DatabaseReference mDatabase;
    String brand;
    Context con;
    Query q;
    ArrayList<String> allBrands=new ArrayList<>();

    public BrandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        con=getActivity();
        return inflater.inflate(R.layout.fragment_brand, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        brandName=(EditText)getView().findViewById(R.id.newBrand);
        addBrand=(Button)getView().findViewById(R.id.addBrand);
        mDatabase= FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Brands");
        q=mDatabase.orderByChild("brandName");
        q.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allBrands.add((dataSnapshot.getValue(Brand.class)).getBrandName());
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
        addBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int f=0;
                brand=brandName.getText().toString().toLowerCase().trim();
                for(int i=0;i<allBrands.size();i++)
                    if(brand.compareTo(allBrands.get(i))==0)
                        f=1;
                if(TextUtils.isEmpty(brand)){
                    brandName.setError("Required Field");
                }
                else if(f==1){
                    brandName.setError("Already Exists!");
                }
                else {
                    Brand b = new Brand(brandName.getText().toString().toLowerCase().trim());
                    mDatabase.push().setValue(b);
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            brandName.setText(" ");
                            AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                            builder.setMessage("New brand added succesfully!")
                                    .setTitle("Done!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                            builder.setMessage(databaseError.getMessage())
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }

            }
        });

    }
}

