package com.example.ident;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.example.ident.Model.classTeam;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginButton;
    TextView mRegisterButton;
    FirebaseAuth fAuth;

    private FirebaseFirestore db;

    private static classTeam team=new classTeam();

    public static classTeam getTeam() {
        return team;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.loginbtn);
        mRegisterButton = findViewById(R.id.registerbtn);
        fAuth=FirebaseAuth.getInstance();
        db= FirebaseFirestore.getInstance();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setEmail(mEmail.getText().toString().trim());
                String password = mPassword.getText().toString().trim();


                if(TextUtils.isEmpty(team.getEmail())){
                    mEmail.setError("email is required");
                    return  ;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("password is required");
                    return;
                }

                if(password.length()<2){
                    mPassword.setError("password doit dépasser 2 caracteres");
                    return;
                }

                fAuth.signInWithEmailAndPassword(team.getEmail(),password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            String token_id= FirebaseInstanceId.getInstance().getToken();
                            String current_id=fAuth.getCurrentUser().getUid();

                            Map<String , Object> tokenMap= new HashMap<>();
                            tokenMap.put("token_id", token_id);

                            db.collection("users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    db.collection("users").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (DocumentSnapshot doc : task.getResult())
                                                    {
                                                        if(doc.getString("email").equals(team.getEmail()))
                                                        {
                                                            team.setId(doc.getId());
                                                            team.setScore(doc.getLong("score"));
                                                            team.setUsername(doc.getString("nom"));
                                                            int i=0;
                                                            while(true)
                                                            {
                                                                final String m=doc.getString("membre"+i);
                                                                if(m==null) break;
                                                                else team.addMembers(m);
                                                                i++;
                                                            }
                                                        }
                                                    }
                                                    startActivity(new Intent(getApplicationContext(),Manactivity.class));
                                                    Toast.makeText(Login.this, "user logged in " ,Toast.LENGTH_SHORT).show();
                                                }
                                    });
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(Login.this,"user not logged in "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }

}
