package com.example.ident.Controller;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ident.Model.classTeam;
import com.example.ident.R;
import com.example.ident.Frag.RankFrag;

import java.util.List;

class ListRankHolder extends RecyclerView.ViewHolder
{
    TextView name,matchplayed,rank;
    ImageView image;

    ListRankHolder(View itemView) {
        super(itemView);
        name= itemView.findViewById(R.id.ach_name);
        image=itemView.findViewById(R.id.ach_image);
        matchplayed=itemView.findViewById(R.id.ach_matchplayed);
        rank=itemView.findViewById(R.id.ach_rank);
    }
}
public class ListRankAdapter extends RecyclerView.Adapter<ListRankHolder>
{
    private RankFrag rankFrag;
    private List<classTeam> teamRanks;

    public ListRankAdapter(RankFrag rankFrag, List<classTeam>  teamRanks) {
    this.rankFrag = rankFrag;
    this.teamRanks = teamRanks;
    }

    @NonNull
    @Override
    public ListRankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(rankFrag.getActivity().getBaseContext());
        View view = inflater.inflate(R.layout.list_rank, parent, false);

        return new ListRankHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListRankHolder holder, final int position)
    {

        int rk= position+1;
        if(rk==1){
            holder.rank.setText("" + rk);
            holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cup_gold,0,0,0);
            holder.name.setCompoundDrawablePadding(10);
            holder.name.setText(teamRanks.get(position).getUsername());
            holder.matchplayed.setText("" + teamRanks.get(position).getScore());
        }else if(rk==2){
            holder.rank.setText("" + rk);
            holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cup_sliver,0,0,0);
            holder.name.setCompoundDrawablePadding(10);
            holder.matchplayed.setText("" + teamRanks.get(position).getScore());
            holder.name.setText(teamRanks.get(position).getUsername());
        }else if(rk==3) {
            holder.matchplayed.setText("" + teamRanks.get(position).getScore());
            holder.rank.setText("" + rk);
            holder.name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cup_bronze,0,0,0);
            holder.name.setCompoundDrawablePadding(10);
            holder.name.setText(teamRanks.get(position).getUsername());
        }else {
            holder.rank.setText("" + rk);
            holder.name.setText(teamRanks.get(position).getUsername());
            holder.matchplayed.setText("" + teamRanks.get(position).getScore());
        }
        }


    @Override
    public int getItemCount() {
        return teamRanks.size();
    }
}
