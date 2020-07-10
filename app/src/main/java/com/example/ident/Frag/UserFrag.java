package com.example.ident.Frag;

import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ident.Login;
import com.example.ident.Model.classTeam;
import com.example.ident.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFrag extends Fragment
{
    View rootView;
    private String m_Text;
    TextView members;
    FirebaseFirestore db;
    classTeam team;
    String m;
    public UserFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_user, container, false);
        team= Login.getTeam();
        TextView name =rootView.findViewById(R.id.hometeam);
        members =rootView.findViewById(R.id.homemembers);
        Button btn=rootView.findViewById(R.id.button);

        db = FirebaseFirestore.getInstance();
        name.setText(team.getUsername());

        m="";
        for(String str:team.getMembers())
        {
            m=m+str+"\n";
        }
        members.setText(m);

        btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                m_Text="";
                AlertDialog.Builder builder = new AlertDialog.Builder(UserFrag.this.getContext());
                builder.setTitle("Ajouter un membre");
                final EditText input = new EditText(UserFrag.this.getContext());
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        Map<String , Object> mem= new HashMap<>();
                        mem.put("member"+team.getMembers().size(), m_Text);

                        db.collection("users").document(team.getId()).update("membre"+team.getMembers().size(),m_Text);
                        team.addMembers(m_Text);
                        m=m+m_Text+"\n";
                        members.setText(m);
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

        return rootView;
    }

}
