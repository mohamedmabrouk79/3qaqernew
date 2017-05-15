package com.example.mohamed.a3qaqer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mohamed on 15/05/2017.
 */

public class PharmacyProfileActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferenceDrugs;
    private static  FirebaseUser mFirebaseUser;
    private List<Pharmacy>  mPharmacies=new ArrayList<>();
    public static Intent newIntent(Context context, FirebaseUser firebaseUser){
        Intent intent=new Intent(context,PharmacyProfileActivity.class);
        mFirebaseUser=firebaseUser;
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_view);
        mRecyclerView= (RecyclerView) findViewById(R.id.user_profile_posts);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("pharmacies");
        mDatabaseReferenceDrugs=FirebaseDatabase.getInstance().getReference().child("drugs");
        FloatingActionsMenu rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions_left);
        FloatingActionButton addpost = new FloatingActionButton(this);
        addpost.setTitle("Added once");
        rightLabels.addButton(addpost);
        addpost.setIcon(R.drawable.ic_plus_white_24dp);
        FloatingActionButton edit = new FloatingActionButton(this);
        edit.setTitle("Added twice");
        rightLabels.addButton(edit);
        edit.setIcon(R.drawable.ic_tooltip_edit_white_24dp);

        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PharmacyProfileActivity.this, "Add Post", Toast.LENGTH_SHORT).show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PharmacyProfileActivity.this, "Edit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDta();
    }

    private void getDta(){
        mPharmacies.clear();
        final DatabaseReference mReference=mDatabaseReference.child(mFirebaseUser.getUid());
      mReference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {

              final Map<String,String> map= (Map<String, String>) dataSnapshot.getValue();
              DatabaseReference reference=mReference.child("Drugs");
              Toast.makeText(PharmacyProfileActivity.this, dataSnapshot.getKey()+"", Toast.LENGTH_SHORT).show();
              reference.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
//                      List<String> list=new ArrayList<String>();
//                      list = (List<String>) dataSnapshot.getValue();
                      Map<String,String> list= (Map<String, String>) dataSnapshot.getValue();
                      for (String s:list.values()) {
                        DatabaseReference reference1=mDatabaseReferenceDrugs.child(s);
                          reference1.addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(DataSnapshot dataSnapshot) {
                                  Map<String,String> map1= (Map<String, String>) dataSnapshot.getValue();
                                Pharmacy pharmacy=new Pharmacy();
                                pharmacy.setPhone(map.get("phone"));
                                pharmacy.setLoction(map.get("location"));
                                pharmacy.setName(map.get("name"));
                                  Toast.makeText(PharmacyProfileActivity.this, pharmacy.getName()+"", Toast.LENGTH_SHORT).show();
                                Drug drug=new Drug();
                                drug.setType(map1.get("type"));
                                drug.setDrugName(map1.get("name"));
                                drug.setTarkez(map1.get("tarkez"));
                                pharmacy.setDrug(drug);
                                mPharmacies.add(pharmacy);
                                 update(mPharmacies,pharmacy);
                              }

                              @Override
                              public void onCancelled(DatabaseError databaseError) {

                              }
                          });
                      }

                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              });
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
    }

    private void update(List<Pharmacy> mPharmacie,Pharmacy pharmacy){
        mRecyclerView.setAdapter(new UsrProfileAdapter(pharmacy,mPharmacie));
    }

}
