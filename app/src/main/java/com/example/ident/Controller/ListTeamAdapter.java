package com.example.ident.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ident.Frag.HomeFrag;
import com.example.ident.Model.classTeam;
import com.example.ident.R;

import java.util.List;

class ListTeamHolder extends RecyclerView.ViewHolder
{
    TextView name;
    TextView members;
    ImageView image;
    Button challengeBtn;

    ListTeamHolder(View itemView) {
        super(itemView);
        name= itemView.findViewById(R.id.team);
        members=itemView.findViewById(R.id.members);
        image=itemView.findViewById(R.id.imageView);
        challengeBtn=itemView.findViewById(R.id.challengeBtn);
    }
}

public class ListTeamAdapter extends RecyclerView.Adapter<ListTeamHolder>
{
    private HomeFrag homeFrag;
    private List<classTeam> teamRanks;

    public ListTeamAdapter(HomeFrag homeFrag, List<classTeam>  teamRanks) {
        this.homeFrag = homeFrag;
        this.teamRanks = teamRanks;
    }

    @NonNull
    @Override
    public ListTeamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(homeFrag.getActivity().getBaseContext());
        View view = inflater.inflate(R.layout.list_team, parent, false);

        return new ListTeamHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListTeamHolder holder, final int position)
    {

        String m="";
        for(String str:teamRanks.get(position).getMembers())
        {
            m=m+" - "+str+"\n";
        }
        holder.members.setText(m);
        holder.name.setText(teamRanks.get(position).getUsername());
        holder.challengeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                homeFrag.SendChallenge(teamRanks.get(position).getId(),teamRanks.get(position).getUsername());
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamRanks.size();
    }
}
