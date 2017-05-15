package com.example.mohamed.a3qaqer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by mohamed on 4/24/17.
 */

public class UsrProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private FirebaseUser mUser;

    Pharmacy header;
    List<Pharmacy> listItems;


    public UsrProfileAdapter(Pharmacy mHeader, List<Pharmacy> mUserPosts, FirebaseUser user){
        this.header=mHeader;
        this.listItems= mUserPosts;
        mUser=user;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            Log.v("Type",TYPE_HEADER+"");
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.header_view_user,parent,false);
            Log.v("Type",view+"");
            return  new HeaderHolder(view);
        }else if (viewType==TYPE_ITEM){
            Log.v("Type",TYPE_ITEM+"");
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view,parent,false);
             return new PhamacyHolder(view,mUser);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
   if (holder instanceof HeaderHolder){
        HeaderHolder headerHolder= (HeaderHolder) holder;
       // Log.v("HEDAER",header.getName());
        headerHolder.bindData(header);
   }else if (holder instanceof PhamacyHolder){
       Pharmacy userPost =listItems.get(position-1);
       PhamacyHolder postHolder= (PhamacyHolder) holder;
       postHolder.bind(userPost.getDrug());

   }

    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return listItems.size()+1;
    }
}
