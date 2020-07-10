package com.example.ident.Frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ident.Controller.ListRankAdapter;
import com.example.ident.Model.classTeam;
import com.example.ident.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RankFrag extends Fragment {


    private RecyclerView listTeams;
    private FirebaseFirestore db;
    private ListRankAdapter adapter;

    private List<classTeam> teamRanks = new ArrayList<>();

    public RankFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_rank, container, false);

        db = FirebaseFirestore.getInstance();
        listTeams= root.findViewById(R.id.listAch);

        listTeams = root.findViewById(R.id.listAch);
        listTeams.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listTeams.setLayoutManager(layoutManager);

        loadData();
        return root;
    }

    private void loadData() {
        teamRanks=new ArrayList<>();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult())
                        {
                            final classTeam team = new classTeam();
                            final String name=doc.getString("nom");
                            final long score=doc.getLong("score");
                            team.setUsername(name);
                            team.setScore(score);
                            teamRanks.add(team);
                        }
                        Collections.sort(teamRanks);
                        adapter = new ListRankAdapter(RankFrag.this, teamRanks);
                        listTeams.setAdapter(adapter);
                    }
                });
    }
}
