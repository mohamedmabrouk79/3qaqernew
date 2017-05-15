package com.example.mohamed.a3qaqer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohamed on 14/05/2017.
 */

public class Adapter extends RecyclerView.Adapter<ResultHolder> {
    private List<Drug> mDrugs=new ArrayList<>();

    public Adapter(List<Drug> drugs){
      mDrugs=drugs;
    }
    @Override
    public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_item_view,parent,false);
        return new ResultHolder(view,parent.getContext());
    }

    @Override
    public void onBindViewHolder(ResultHolder holder, int position) {
      holder.bind(mDrugs.get(position));
    }

    @Override
    public int getItemCount() {
        return mDrugs.size();
    }
}
