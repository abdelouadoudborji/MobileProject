package com.example.ident;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private final String TAG = "TAG";

    private EditText mNom,mEmail,mPassword;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNom = findViewById(R.id.nom);
        mEmail = findViewById(R.id.emailreg);
        mPassword = findViewById(R.id.password);
        Button mRegisterBtn = findViewById(R.id.register);
        TextView mLoginBtn = findViewById(R.id.loginhere);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String nom=mNom.getText().toString();

                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("email is required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("password is required");
                    return;
                }
                if(password.length()<2)
                {
                    mPassword.setError("password doit dépasser 2 caracteres");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "user created" ,Toast.LENGTH_SHORT).show();

                            userID=fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference=fStore.collection("users").document(userID);

                            Map<String,Object> user = new HashMap<>();
                            final String token_id = FirebaseInstanceId.getInstance().getToken();


                            user.put("email",email);
                            user.put("mot de passe",password);
                            user.put("nom",nom);
                            user.put("score",0);
                            user.put("token_id", token_id);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"user profile is created for "+userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Failure :"+e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),Login.class));
                        }
                        else
                        {
                            Toast.makeText(Register.this,"user not created, "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }
}
