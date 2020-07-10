package com.example.ident;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ident.Frag.HomeFrag;
import com.example.ident.Frag.MatchFrag;
import com.example.ident.Frag.RankFrag;
import com.example.ident.Frag.UserFrag;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Manactivity extends AppCompatActivity {
    BottomNavigationView nav;
    FrameLayout frm;
    private HomeFrag fhom;
    private MatchFrag fmatch;
    private RankFrag frank;
    public UserFrag fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        nav=findViewById(R.id.bottomNavigationView);
        frm=findViewById(R.id.framelayout);

        fhom=new HomeFrag();
        fmatch=new MatchFrag();
        frank=new RankFrag();
        fuser=new UserFrag();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.add(R.id.framelayout,fhom);
        ft.commit();

        fhom=new HomeFrag();
        fmatch=new MatchFrag();
        frank=new RankFrag();
        fuser=new UserFrag();

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.nav_home :
                    initfragment(fhom);
                    return true;
                case R.id.nav_match:
                    initfragment(fmatch);
                    return true;
                case R.id.nav_rank:
                    initfragment(frank);
                    return true;
                case R.id.nav_user :
                    initfragment(fuser);
                    return true;
            }
            return false;
        }});
    }

    public void initfragment(Fragment gm){
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayout,gm);
        ft.commit();
    }
}
