package com.example.sukritgulati.mygovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sukritgulati on 4/9/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView name;

    public MyViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.positionTextView);
        name = (TextView) itemView.findViewById(R.id.nameTextView);
    }
}
