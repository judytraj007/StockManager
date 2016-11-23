package com.judy.shopmanager;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemFragment extends Fragment{

    String Idno;
    String Name;
    String Brand;
    String Cost;
    String Storeid;
    String Type;
    String formattedDate;
    DatabaseReference reference,itemRef;
    DatabaseReference brandRef;
    SimpleDateFormat df;
    ArrayList<String> allBrands = new ArrayList<>();
    ArrayList<String> allId = new ArrayList<>();
    String[] brands;
    Query q;
    EditText idno;
    EditText title;
    EditText cost;
    Spinner spinner,storeSpinner;
    Context con;
    ArrayAdapter<String> spinnerAdapter,storeSpinnerAdapter;
    public AddItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        con=getActivity();
        return inflater.inflate(R.layout.add_item_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton button=(ImageButton)getView().findViewById(R.id.img);
        brandRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Brands");
        q=brandRef.orderByChild("brandName");
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
        itemRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Items");
        Query query=itemRef.orderByChild("idno");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                allId.add(dataSnapshot.getValue(Item.class).getIdno().toLowerCase().trim());
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator.forSupportFragment(AddItemFragment.this).setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES ).initiateScan();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        idno=(EditText)getView().findViewById(R.id.IdNum);
        title=(EditText)getView().findViewById(R.id.title);
        cost=(EditText)getView().findViewById(R.id.price);
        spinner =(Spinner)getView().findViewById(R.id.brandSpinner);
        storeSpinner=(Spinner)getView().findViewById(R.id.storeSpinner);
        Calendar c = Calendar.getInstance();
        df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            idno.setText(scanContent);
        }else{
            Toast toast = Toast.makeText(getActivity(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
        final int count;
        count=allBrands.size();
        brands=new String[count];
        for(int i=0;i<allBrands.size();i++){
            brands[i]=allBrands.get(i);
        }
        String[] stores={"ARD","CHMD"};
        spinnerAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,brands);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        storeSpinnerAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,stores);
        storeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(storeSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Brand=parent.getItemAtPosition(position).toString().toLowerCase().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Storeid=parent.getItemAtPosition(position).toString().toLowerCase().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button ok=(Button)getView().findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b=new AlertDialog.Builder(con,R.style.MyAlertDialogStyle)
                        .setTitle("Save Data")
                        .setMessage("Are you sure you want to save this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Idno=idno.getText().toString().toLowerCase().trim();
                                Name=title.getText().toString().toLowerCase().trim();
                                Cost=cost.getText().toString().toLowerCase().trim();
                                int f=0;
                                RadioGroup radioGroup = (RadioGroup)getView(). findViewById(R.id.myRadioGroup);
                                RadioButton selectRadio = (RadioButton)getView().findViewById(radioGroup.getCheckedRadioButtonId());
                                for (int j=0;j<allId.size();j++){
                                    if(Idno.compareTo(allId.get(j))==0){
                                        f=2;
                                        idno.setError(getString(R.string.exits));
                                    }

                                }
                                if (radioGroup.getCheckedRadioButtonId() == -1){
                                    f=2;
                                }
                                else {
                                    Type = selectRadio.getText().toString().toLowerCase().trim();
                                }

                                if(TextUtils.isEmpty(Idno)){
                                    idno.setError("Invalid!");
                                    f=2;
                                }
                               if(TextUtils.isEmpty(Name)){
                                    title.setError("Invalid");
                                   f=2;
                                }
                                if(TextUtils.isEmpty(Cost)){
                                    cost.setError("Invalid!");
                                    f=2;
                                }
                                if(f==0) {
                                    createData();
                                    idno.setText(" ");
                                    title.setText(" ");
                                    cost.setText(" ");
                                }


                            }
                        });
                b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog d=b.create();
                d.show();

            }

        });

    }
    private void createData() {
        reference= FirebaseDatabase.getInstance().getReference("Items");
        Item item=new Item();
        item.setIdno(Idno);
        item.setName(Name);
        item.setBrand(Brand);
        item.setCost(Cost);
        item.setStore(Storeid);
        item.setType(Type);
        item.setDate(formattedDate);
        reference.push().setValue(item);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                new AlertDialog.Builder(con,R.style.MyAlertDialogStyle).setMessage("Item Added Successfully")
                        .setTitle("Success!")
                        .setPositiveButton(android.R.string.ok, null).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                new AlertDialog.Builder(con,R.style.MyAlertDialogStyle).setMessage(error.getMessage())
                        .setTitle("Error!")
                        .setPositiveButton(android.R.string.ok, null).show();
            }

        });



    }

}
