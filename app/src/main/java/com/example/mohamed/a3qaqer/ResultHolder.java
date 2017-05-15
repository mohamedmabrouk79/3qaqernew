package com.example.mohamed.a3qaqer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by mohamed on 14/05/2017.
 */

public class ResultHolder extends RecyclerView.ViewHolder {
    public ImageView drugImage;
    private TextView drugName,drugType,drugTarkez,PharmacyName,PhamacyPhone,PahrmacyLocation;
    private Context mContext;

    public ResultHolder(View itemView, Context context) {
        super(itemView);
        mContext=context;
        drugImage= (ImageView) itemView.findViewById(R.id.drug_image);
        drugName= (TextView) itemView.findViewById(R.id.drug_name);
        drugTarkez= (TextView) itemView.findViewById(R.id.drug_tarkez);
        PahrmacyLocation= (TextView) itemView.findViewById(R.id.location);
        PhamacyPhone= (TextView) itemView.findViewById(R.id.phone);
        PharmacyName= (TextView) itemView.findViewById(R.id.name);
    }

    public void bind(Drug drug){
        try {
            Picasso.with(mContext).load(Uri.parse(drug.getImageUrl())).into(drugImage);
        }catch (Exception e){

        }
        final Pharmacy dPharmacy=drug.getPharmacy();
        drugName.setText(drug.getDrugName());
        drugTarkez.setText(drug.getTarkez());
        PharmacyName.setText(dPharmacy.getName());
        PhamacyPhone.setText(dPharmacy.getPhone());
        PahrmacyLocation.setText(dPharmacy.getLoction());
        PahrmacyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInMap(dPharmacy.getLoction());
            }
        });
    }

    private void showInMap(String loc) {
        String[] splits = loc.split("-");
        if(!loc.equals("none")){
            Intent intent_to_maps = new Intent(mContext, MapsActivity.class);
            intent_to_maps.putExtra("lati", splits[0]);
            intent_to_maps.putExtra("longi", splits[1]);
            Log.d("longi = ", splits[1]);
            intent_to_maps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent_to_maps);
        }
    }
}
