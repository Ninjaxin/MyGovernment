package com.example.sukritgulati.mygovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sukritgulati on 4/9/17.
 */

public class OfficerAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<Officer> officerList;
    private MainActivity ma;

    public OfficerAdapter(ArrayList<Officer> officerList, MainActivity ma){
        this.officerList = officerList;
        this.ma = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_content,parent,false);
        myView.setOnClickListener(ma);
        myView.setOnLongClickListener(ma);
        return new MyViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(officerList.get(position).getTitle());

            String party = officerList.get(position).getParty();
            if(party.contentEquals("Unknown") || party == "") {
                holder.name.setText(officerList.get(position).getName());
            } else {
                holder.name.setText(officerList.get(position).getName() + " (" + party + ")");
            }

    }

    @Override
    public int getItemCount() {
        return officerList.size();
    }
}
