package com.judy.shopmanager;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseListAdapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SearchFragment extends android.support.v4.app.Fragment {
    String scanContent,key;
    TextView sell;
    Query q;
    String deleteKey;
    Item solditem=new Item();
    ListView sList;
    DatabaseReference dbRef,soldRef;
    EditText search;
    String cost;
    Context con;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        con=getActivity();
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton scanner = (ImageButton) getView().findViewById(R.id.imgbutton);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.forSupportFragment(SearchFragment.this).setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES).initiateScan();
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        Button imgbtn=(Button)getView().findViewById(R.id.search);
        search = (EditText) getView().findViewById(R.id.Key);
        sList=(ListView)getView().findViewById(R.id.sList);
        sell=(TextView)getView().findViewById(R.id.Sell);
        if (scanningResult != null) {
            scanContent = scanningResult.getContents();
            search.setText(scanContent);

        }else{
            Toast toast = Toast.makeText(getActivity(),"No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
        dbRef= FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Items");
        imgbtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          key = search.getText().toString().toLowerCase();
                                          q = dbRef.orderByChild("idno").equalTo(key);
                                          FirebaseListAdapter<Item> listAdapter =new FirebaseListAdapter<Item>(getActivity(),Item.class,R.layout.rowitem,q) {
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
                                                  solditem=item;
                                                  if((solditem.getName().toString())!=" ") {
                                                      sell.setText("Add this result to Sold Items List?");
                                                  }
                                              }
                                          };
                                      sList.setAdapter(listAdapter);
                                          sList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                  final Item obj=(Item) sList.getAdapter().getItem(position);
                                                  final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                  LayoutInflater inflater = getActivity().getLayoutInflater();
                                                  View dialogView= inflater.inflate(R.layout.edit_dialog, null);
                                                  builder.setView(dialogView);
                                                  final EditText mEditText =(EditText)dialogView.findViewById(R.id.sellCost);
                                                  mEditText.setText(solditem.getCost());
                                                  Button updateButton=(Button)dialogView.findViewById(R.id.updBtn);
                                                  builder.setTitle("Update Selling Price");
                                                  builder.setIcon(R.drawable.newu);
                                                  final AlertDialog dialog=builder.create();
                                                  dialog.show();
                                                  updateButton.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          cost=mEditText.getText().toString();
                                                          if(TextUtils.isEmpty(cost))mEditText.setError("Required Field!");
                                                          else {

                                                              dialog.cancel();
                                                          }
                                                      }
                                                  });

                                              }
                                          });
                                          q.addChildEventListener(new ChildEventListener() {
                                              @Override
                                              public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                Item item=dataSnapshot.getValue(Item.class);
                                                  deleteKey=dataSnapshot.getKey();
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
                                      }
                                  });


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            AlertDialog.Builder b= new AlertDialog.Builder(con,R.style.MyAlertDialogStyle)
                        .setTitle("Add To Sold?")
                        .setMessage("Are you sure you want to add this item to sold list?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbRef.child(deleteKey).removeValue();
                                soldRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://stockmanager-142503.firebaseio.com/Sold");
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                                String formattedDate = df.format(c.getTime());
                                if(!TextUtils.isEmpty(cost))solditem.setCost(cost);
                                solditem.setDate(formattedDate);
                                soldRef.push().setValue(solditem);
                                soldRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                        builder.setMessage("Item Added Successfully")
                                                .setTitle("Success!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(con,R.style.MyAlertDialogStyle);
                                        builder.setMessage(error.getMessage())
                                                .setTitle("Error!")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    }

                                });

                            }
                        });
               b.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog dialog=b.create();
                        dialog.show();

                sell.setText(" ");
                search.setText(" ");
        }
        });
    }

}

