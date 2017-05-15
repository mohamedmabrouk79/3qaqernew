package com.example.mohamed.a3qaqer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mohamed on 15/05/2017.
 */

public class PhamacyHolder extends RecyclerView.ViewHolder {
    private ImageView imageView,delete;
    private TextView drugName,drugTarkez,drugType;
    public PhamacyHolder(View itemView) {
        super(itemView);
        drugName= (TextView) itemView.findViewById(R.id.dru_name_post);
        drugTarkez= (TextView) itemView.findViewById(R.id.tarkez);
        drugType= (TextView) itemView.findViewById(R.id.type);

        imageView= (ImageView) itemView.findViewById(R.id.drug_image_post);
        delete= (ImageView) itemView.findViewById(R.id.delete_post);

       }

    public void bind(Drug drug){
        drugName.setText(drug.getDrugName());
        drugType.setText(drug.getType());
        drugTarkez.setText(drug.getTarkez());

    }


}
