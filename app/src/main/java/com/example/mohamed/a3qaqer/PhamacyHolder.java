package com.example.mohamed.a3qaqer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mohamed on 15/05/2017.
 */

public class PhamacyHolder extends RecyclerView.ViewHolder {
    private ImageView imageView,delete;
    private TextView drugName,drugTarkez,drugType;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseReference;
    public PhamacyHolder(View itemView , FirebaseUser user) {
        super(itemView);
        firebaseUser=user;
        drugName= (TextView) itemView.findViewById(R.id.dru_name_post);
        drugTarkez= (TextView) itemView.findViewById(R.id.tarkez);
        drugType= (TextView) itemView.findViewById(R.id.type);

        imageView= (ImageView) itemView.findViewById(R.id.drug_image_post);
        delete= (ImageView) itemView.findViewById(R.id.delete_post);
        mDatabaseReference= FirebaseDatabase.getInstance().getReference().child("pharmacies").child(firebaseUser.getUid()).child("Drugs");
       }

    public void bind(final Drug drug){
        drugName.setText(drug.getDrugName());
        drugType.setText(drug.getType());
        drugTarkez.setText(drug.getTarkez());
        mDatabaseReference.child(drug.getDrugName());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mReference=FirebaseDatabase.getInstance().getReference().child("drugs").child(drug.getDrugName()).child("pharmacy");
                mReference.child(firebaseUser.getUid()).removeValue();
                mDatabaseReference.child(drug.getDrugName()).removeValue();
            }
        });

    }


}
