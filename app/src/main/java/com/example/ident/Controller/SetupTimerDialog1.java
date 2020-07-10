package com.example.ident.Controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.ident.Frag.MatchFrag;
import com.example.ident.Model.Challenge;
import com.example.ident.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SetupTimerDialog1 extends DialogFragment
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
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.setup_timer1, (ViewGroup) getView(), false);

        final DatePicker datePicker=viewInflated.findViewById(R.id.datePicker1);

        builder.setView(viewInflated)
                .setPositiveButton("Suivant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String date =getDateFromDatePicker(datePicker);
                        challenge.setTime(date);

                        SetupTimerDialog2 std2=new SetupTimerDialog2();
                        std2.setMatchFrag(matchFrag);
                        std2.setChallenge(challenge);
                        std2.show(matchFrag.getActivity().getSupportFragmentManager(),"Dialog");

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        SetupTimerDialog1.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format( calendar.getTime() );
    }
}
