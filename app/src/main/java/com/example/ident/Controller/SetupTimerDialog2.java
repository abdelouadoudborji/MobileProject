package com.example.ident.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.ident.Frag.MatchFrag;
import com.example.ident.Model.Challenge;
import com.example.ident.R;

import java.util.Date;

public class SetupTimerDialog2 extends DialogFragment
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
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.setup_timer2, (ViewGroup) getView(), false);

        final TimePicker timePicker=viewInflated.findViewById(R.id.timePicker1);

        builder.setView(viewInflated)
                .setPositiveButton("Ajouter un commentaire", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String time =getTimeFromTimePicker(timePicker);
                        challenge.setTime(challenge.getTime()+", "+time);

                        SetupTimerDialog3 std3=new SetupTimerDialog3();
                        std3.setMatchFrag(matchFrag);
                        std3.setChallenge(challenge);
                        std3.show(matchFrag.getActivity().getSupportFragmentManager(),"Dialog");

                    }
                })
                .setNegativeButton("Envoyer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {

                        String time =getTimeFromTimePicker(timePicker);
                        challenge.setTime(challenge.getTime()+", "+time);
                        challenge.setStatus((long) 2);
                        matchFrag.updateChallenge(challenge);
                        SetupTimerDialog2.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private String getTimeFromTimePicker(TimePicker timePicker)
    {
        return timePicker.getHour()+":"+timePicker.getMinute();
    }
}
