package com.example.mohamed.a3qaqer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultSeachActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public static String QUARY="quary";
    private String quary;

    public static Intent newIntent(android.widget.SearchView.OnQueryTextListener context, String quary){
        Intent intent=new Intent(context,ResultSeachActivity.class);
        intent.putExtra(QUARY,quary);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_view);
        mRecyclerView= (RecyclerView) findViewById(R.id.result_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        quary=getIntent().getStringExtra(QUARY);
    }

    private void UpdateUi(){

    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateUi();
    }

    class ResultHolder extends RecyclerView.ViewHolder{
        private ImageView drugImage;
        private TextView drugName,drugType,drugTarkez,PharmacyName,PhamacyPhone,PahrmacyLocation;

        public ResultHolder(View itemView) {
            super(itemView);
            drugImage= (ImageView) itemView.findViewById(R.id.drug_image);
            drugName= (TextView) itemView.findViewById(R.id.drug_name);
            drugTarkez= (TextView) itemView.findViewById(R.id.drug_tarkez);
            PahrmacyLocation= (TextView) itemView.findViewById(R.id.location);
            PhamacyPhone= (TextView) itemView.findViewById(R.id.phone);
            PharmacyName= (TextView) itemView.findViewById(R.id.name);
        }
    }

    public void bind(Drug drug){

    }
}
