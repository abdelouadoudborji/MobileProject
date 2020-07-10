package com.example.ident.Controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ident.Frag.MatchFrag;
import com.example.ident.Login;
import com.example.ident.Model.Challenge;
import com.example.ident.Model.classTeam;
import com.example.ident.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ListInvHolder extends RecyclerView.ViewHolder
{
    TextView desc;
    Button btn1;
    Button btn2;
    ImageView image;

    ListInvHolder(View itemView) {
        super(itemView);
        desc= itemView.findViewById(R.id.inv_desc);
        btn1= itemView.findViewById(R.id.inv_button);
        btn2= itemView.findViewById(R.id.inv_button2);
        image=itemView.findViewById(R.id.team_image);
    }
}

public class ListInvAdapter extends RecyclerView.Adapter<ListInvHolder>
{
    private FirebaseFirestore db;

    private String m_Text = "";

    Long sc1;
    Long sc2;

    private MatchFrag matchFrag;
    private List<Challenge> Challenges;
    private int status;

    public ListInvAdapter(MatchFrag matchFrag, List<Challenge> Challenges, int status) {
        db= FirebaseFirestore.getInstance();
        this.matchFrag = matchFrag;
        this.Challenges = Challenges;
        this.status=status;
    }

    @NonNull
    @Override
    public ListInvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(matchFrag.getActivity().getBaseContext());
        View view = inflater.inflate(R.layout.list_invitation, parent, false);

        return new ListInvHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListInvHolder holder, final int position)
    {
        final classTeam team=Login.getTeam();
        final Challenge challenge=Challenges.get(position);

        if(status==0)
        {
            holder.desc.setText("L'équipe "+ challenge.getFrom_name() +" vous défie");

            holder.btn1.setText("Accepter");
            holder.btn2.setText("Refuser");

            holder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Challenges.get(position).setStatus((long) 1);
                    db.collection("users/"+team.getId()+"/challenges").document(Challenges.get(position).getId()).update("status",1);
                    db.collection("users/"+challenge.getFrom()+"/challenges").document(Challenges.get(position).getId()).set(Challenges.get(position));

                    matchFrag.loadData();
                }
            });
            holder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    db.collection("users/"+team.getId()+"/challenges").document(Challenges.get(position).getId()).update("status",-1);
                    matchFrag.loadData();
                }
            });
        }
        else if (status==1)
        {
            if(challenge.getFrom().equals(team.getId()))
            {
                if(challenge.getReport()!=null)
                {
                    holder.desc.setText("L'équipe "+ challenge.getTo_name() +" n'a pas accepté la date que vous avez proposée ("+challenge.getTime()+")et suggérée :" + challenge.getReport());
                }
                else
                {
                    holder.desc.setText("L'équipe "+ challenge.getTo_name() +" accepte votre défi");
                }

                holder.btn1.setText("configurer l'heure et le lieu");
                holder.btn2.setVisibility(View.GONE);

                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        SetupTimerDialog1 std1=new SetupTimerDialog1();
                        std1.setMatchFrag(matchFrag);
                        std1.setChallenge(challenge);
                        std1.show(matchFrag.getActivity().getSupportFragmentManager(),"Dialog");
                    }
                });
            }
            else
            {
                holder.desc.setText("Vous avez accepté le défi de "+challenge.getFrom_name());
                holder.btn1.setVisibility(View.GONE);
                holder.btn2.setVisibility(View.GONE);
            }
        }
        else if (status==2)
        {
            if(challenge.getFrom().equals(team.getId()))
            {
                holder.desc.setText("En attente d'une réponse de l'équipe "+challenge.getTo_name());
                holder.btn1.setVisibility(View.GONE);
                holder.btn2.setVisibility(View.GONE);
            }
            else
            {
                String msg="L'équipe "+challenge.getFrom_name()+" propose "+challenge.getTime()+".";
                if(challenge.getComment()!=null)
                {
                    msg+="Et ils ont laissé ce commentaire : "+challenge.getComment();
                }
                holder.desc.setText(msg);
                holder.btn1.setText("Accepter");
                holder.btn2.setText("Refuser et Proposer une date.");

                holder.btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Challenges.get(position).setStatus((long) 1);
                        db.collection("users/"+challenge.getTo()+"/challenges").document(Challenges.get(position).getId()).update("status",3);
                        db.collection("users/"+challenge.getFrom()+"/challenges").document(Challenges.get(position).getId()).update("status",3);

                        db.collection("users").document(challenge.getFrom()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        sc1=document.getLong("score");
                                        Log.d("Score1", String.valueOf(sc1));
                                        db.collection("users").document(challenge.getFrom()).update("score",sc1+2);
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });
                        db.collection("users").document(challenge.getTo()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        sc2=document.getLong("score");
                                        Log.d("Score2", String.valueOf(sc2));
                                        db.collection("users").document(challenge.getTo()).update("score",sc2+2);
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });
                        matchFrag.loadData();
                    }
                });
                holder.btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        m_Text="";
                        AlertDialog.Builder builder = new AlertDialog.Builder(matchFrag.getContext());
                        builder.setTitle("Proposer une nouvelle Date : ");
                        final EditText input = new EditText(matchFrag.getContext());
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_Text = input.getText().toString();
                                db.collection("users/"+challenge.getTo()+"/challenges").document(Challenges.get(position).getId()).update("status",1);
                                db.collection("users/"+challenge.getFrom()+"/challenges").document(Challenges.get(position).getId()).update("status",1);
                                db.collection("users/"+challenge.getTo()+"/challenges").document(Challenges.get(position).getId()).update("report",m_Text);
                                db.collection("users/"+challenge.getFrom()+"/challenges").document(Challenges.get(position).getId()).update("report",m_Text);

                                matchFrag.loadData();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });

            }
        }
        else if (status==3)
        {
            holder.desc.setText("Match prévu entre "+challenge.getFrom_name()+" et "+challenge.getTo_name()+" le "+challenge.getTime());
            holder.btn1.setVisibility(View.GONE);
            holder.btn2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return Challenges.size();
    }
}
