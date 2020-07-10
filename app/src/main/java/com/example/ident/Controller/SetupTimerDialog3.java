package com.example.ident.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.ident.Frag.MatchFrag;
import com.example.ident.Model.Challenge;
import com.example.ident.R;

public class SetupTimerDialog3 extends DialogFragment
{
    MatchFrag matchFrag;
    Challenge challenge;

    public void setMatchFrag(MatchFrag matchFrag)
    {
        this.matchFrag=matchFrag;
    }

    public void setChallenge(Challenge challenge)
    {
        this.challenge=challenge;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.setup_timer3, (ViewGroup) getView(), false);

        TextView textView=viewInflated.findViewById(R.id.textView5);
        final EditText editText=viewInflated.findViewById(R.id.editText);

        builder.setView(viewInflated)
                .setPositiveButton("Envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        challenge.setComment(editText.getText().toString());
                        challenge.setStatus((long) 2);
                        matchFrag.updateChallenge(challenge);
                        SetupTimerDialog3.this.getDialog().cancel();

                    }
                });


        return builder.create();
    }
}
