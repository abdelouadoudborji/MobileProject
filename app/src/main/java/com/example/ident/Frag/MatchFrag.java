package com.example.ident.Frag;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ident.Controller.ListInvAdapter;
import com.example.ident.Login;
import com.example.ident.Model.Challenge;
import com.example.ident.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchFrag extends Fragment {

    private TextView txt4,txt,txt2,txt3;

    private RecyclerView listInvReceived;
    private RecyclerView listInvAccepted;
    private RecyclerView listInvConfig;
    private RecyclerView listInvSet;

    private FirebaseFirestore db;
    private ListInvAdapter adapter0;
    private ListInvAdapter adapter1;
    private ListInvAdapter adapter2;
    private ListInvAdapter adapter3;

    private List<Challenge> Challenges0;
    private List<Challenge> Challenges1;
    private List<Challenge> Challenges2;
    private List<Challenge> Challenges3;
    View root;
    public MatchFrag()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_match, container, false);

        db = FirebaseFirestore.getInstance();

        txt4=root.findViewById(R.id.textView4);
        txt=root.findViewById(R.id.textView);
        txt2=root.findViewById(R.id.textView2);
        txt3=root.findViewById(R.id.textView3);
        txt.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);

        listInvReceived= root.findViewById(R.id.listReceived);
        listInvAccepted =root.findViewById(R.id.listAccepted);
        listInvConfig=root.findViewById(R.id.listConfig);
        listInvSet=root.findViewById(R.id.listSet);

        listInvReceived.setNestedScrollingEnabled(false);
        listInvAccepted.setNestedScrollingEnabled(false);
        listInvConfig.setNestedScrollingEnabled(false);
        listInvSet.setNestedScrollingEnabled(false);

        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        listInvReceived.setLayoutManager(layoutManager1);
        listInvAccepted.setLayoutManager(layoutManager2);
        listInvConfig.setLayoutManager(layoutManager3);
        listInvSet.setLayoutManager(layoutManager4);

        loadData();
        return root;
    }

    public void loadData() {
        Challenges0 =new ArrayList<>();
        Challenges1 =new ArrayList<>();
        Challenges2 =new ArrayList<>();
        Challenges3 =new ArrayList<>();

        db.collection("users/"+ Login.getTeam().getId() +"/challenges").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult())
                        {
                            final Challenge challenge = new Challenge();
                            challenge.setId(doc.getId());
                            challenge.setFrom(doc.getString("from"));
                            challenge.setFrom_name(doc.getString("from_name"));
                            challenge.setTo(doc.getString("to"));
                            challenge.setTo_name(doc.getString("to_name"));
                            challenge.setLocation(doc.getString("location"));
                            challenge.setTime(doc.getString("time"));
                            challenge.setStatus(doc.getLong("status"));
                            challenge.setComment(doc.getString("comment"));
                            challenge.setReport(doc.getString("report"));

                            Long status = challenge.getStatus();
                            if (status != null)
                            {
                                if (status == 0) {
                                    Challenges0.add(challenge);
                                } else if (status == 1) {
                                    Challenges1.add(challenge);
                                } else if (status == 2) {
                                    Challenges2.add(challenge);
                                } else if (status == 3) {
                                    Challenges3.add(challenge);
                                }
                            }
                        }
                        adapter0 = new ListInvAdapter(MatchFrag.this, Challenges0, 0);
                        adapter1 = new ListInvAdapter(MatchFrag.this, Challenges1, 1);
                        adapter2 = new ListInvAdapter(MatchFrag.this, Challenges2, 2);
                        adapter3 = new ListInvAdapter(MatchFrag.this, Challenges3, 3);
                        updateText();

                        listInvReceived.setAdapter(adapter0);
                        listInvAccepted.setAdapter(adapter1);
                        listInvConfig.setAdapter(adapter2);
                        listInvSet.setAdapter(adapter3);
                    }
                });
    }

    public void updateChallenge(final Challenge ch)
    {
        final Map<String,Object> challenge=new HashMap<>();

        challenge.put("from",ch.getFrom());
        challenge.put("from_name",ch.getFrom_name());
        challenge.put("to",ch.getTo());
        challenge.put("to_name",ch.getTo_name());
        challenge.put("status",ch.getStatus());
        challenge.put("time",ch.getTime());
        challenge.put("location",null);
        challenge.put("comment",ch.getComment());

        db.collection("users/"+ch.getTo()+"/challenges").document(ch.getId()).update(challenge).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid)
            {
                db.collection("users/"+ch.getFrom()+"/challenges").document(ch.getId()).update(challenge).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(root.getContext(),"Sent.", Toast.LENGTH_LONG).show();
                        loadData();
                        updateText();
                    }
                });
            }
        });
    }

    private void updateText()
    {
        if(Challenges0.size()==0)
        {
            txt.setText("Aucune nouvelle invitation");
            txt.setVisibility(View.VISIBLE);
        }
        else
        {
            txt.setVisibility(View.GONE);
        }
        if(Challenges1.size()==0)
        {
            txt2.setText("Pas encore d'invitation acceptée");
            txt2.setVisibility(View.VISIBLE);
        }
        else
        {
            txt2.setVisibility(View.GONE);
        }
        if(Challenges2.size()==0)
        {
            txt3.setText("Aucune Matche n'est en cours de planification");
            txt3.setVisibility(View.VISIBLE);
        }
        else
        {
            txt3.setVisibility(View.GONE);
        }
        if(Challenges3.size()==0)
        {
            txt4.setText("Aucun matche n'a été planifié");
            txt4.setVisibility(View.VISIBLE);
        }
        else
        {
            txt4.setVisibility(View.GONE);
        }
    }
}