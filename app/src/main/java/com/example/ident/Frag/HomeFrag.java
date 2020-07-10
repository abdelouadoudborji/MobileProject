package com.example.ident.Frag;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ident.Controller.ListTeamAdapter;
import com.example.ident.Login;
import com.example.ident.Model.classTeam;
import com.example.ident.R;
import com.example.ident.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment
{
    FirebaseFirestore db;
    View rootView4;

    private RecyclerView listTeams;
    private ListTeamAdapter adapter;
    private List<classTeam> teamRanks = new ArrayList<>();

    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView4=inflater.inflate(R.layout.fragment_home, container, false);

        db=FirebaseFirestore.getInstance();
        listTeams= rootView4.findViewById(R.id.listTeams);

        listTeams.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listTeams.setLayoutManager(layoutManager);

        loadData();

        return rootView4;
    }

    private void loadData() {
        teamRanks = new ArrayList<>();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult())
                        {
                            if(doc.getString("email").equals(Login.getTeam().getEmail()))
                            {
                                continue;
                            }
                            final classTeam team = new classTeam();
                            final String name=doc.getString("nom");
                            final String teamId=doc.getId();
                            team.setId(teamId);
                            team.setUsername(name);
                            int i=0;

                            while(true)
                            {
                                final String m=doc.getString("membre"+i);
                                if(m==null) break;
                                else team.addMembers(m);
                                i++;
                            }

                            teamRanks.add(team);
                        }
                        adapter = new ListTeamAdapter(HomeFrag.this, teamRanks);
                        listTeams.setAdapter(adapter);
                    }
                });
    }

    public void SendChallenge(String idUser,String name)
    {
        Map<String,Object> challenge=new HashMap<>();

        challenge.put("from",Login.getTeam().getId());
        challenge.put("from_name",Login.getTeam().getUsername());
        challenge.put("to",idUser);
        challenge.put("to_name",name);
        challenge.put("status",0);
        challenge.put("time",null);
        challenge.put("location",null);
        challenge.put("comment",null);
        challenge.put("report",null);

        // TODO change
        db.collection("users/"+idUser+"/challenges").add(challenge).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(rootView4.getContext(),"Défi envoyé avec succès!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(rootView4.getContext(),"Erreur, veuillez réessayer.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
