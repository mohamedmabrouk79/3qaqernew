package com.example.mohamed.a3qaqer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultSeachActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public static String QUARY="quary";
    private String quary;
    private ProgressBar mProgressBar;
    private static  DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private TextView error;

    public static Intent newIntent(Context context, String quary){
        Intent intent=new Intent( context,ResultSeachActivity.class);
        intent.putExtra(QUARY,quary);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_view);
        mProgressBar= (ProgressBar) findViewById(R.id.progress);
        mRecyclerView= (RecyclerView) findViewById(R.id.result_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quary=getIntent().getStringExtra(QUARY);
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("drugs");
        error= (TextView) findViewById(R.id.error);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem seach =menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) seach.getActionView();
        searchView.setQuery(quary,true);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                quary=query;

               // Toast.makeText(ResultSeachActivity.this, "jnjhj", Toast.LENGTH_SHORT).show();
                searchView.onActionViewCollapsed();
                searchView.setQuery("", false);
                mProgressBar.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getDrugs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    List<Drug> mDrugs=new ArrayList<>();
    public  void getDrugs(String quary){
        try {
            mDrugs.clear();
            final DatabaseReference Drugs = mDatabaseReference.child(quary);
            Drugs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Map<String, String> mainDrugA = (Map<String, String>) dataSnapshot.getValue();
                    final DatabaseReference mReference = Drugs.child("pharmacy");
                    mReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> list=new ArrayList<String>();
                             list = (List<String>) dataSnapshot.getValue();
                            if (list==null){
                                error.setVisibility(View.VISIBLE);
                                return;
                            }
                            list.remove(0);
                            for (String s : list) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("pharmacies").child(s);


                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mProgressBar.setVisibility(View.GONE);
                                        error.setVisibility(View.GONE);
                                        Drug drug = new Drug();
                                        Pharmacy pharmacy = new Pharmacy();
                                        drug.setDrugName(mainDrugA.get("name"));
                                        drug.setTarkez(mainDrugA.get("tarkez"));
                                        drug.setType(mainDrugA.get("type"));

                                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                        pharmacy.setLoction(map.get("location"));
                                        pharmacy.setName(map.get("name"));
                                        pharmacy.setPhone(map.get("phone"));
                                        drug.setPharmacy(pharmacy);
                                        mDrugs.add(drug);
                                        update(mDrugs);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(ResultSeachActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                           // Toast.makeText(ResultSeachActivity.this, list.size() + "", Toast.LENGTH_SHORT).show();
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
        }catch (Exception e){
        }
    }

    private void update(List<Drug> drugs){
        mRecyclerView.setAdapter(new Adapter(drugs));
    }


    @Override
    protected void onResume() {
        super.onResume();
         getDrugs(quary);

    }

}
