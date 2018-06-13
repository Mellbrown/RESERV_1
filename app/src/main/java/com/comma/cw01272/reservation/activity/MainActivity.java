package com.comma.cw01272.reservation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.comma.cw01272.reservation.fragment.MyinfoFragment;
import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.fragment.CheckReserveFragment;
import com.comma.cw01272.reservation.fragment.NoticeFragment;
import com.comma.cw01272.reservation.fragment.findSeatFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private findSeatFragment findseatFragment;
    private NoticeFragment noticeFragment;
    private CheckReserveFragment chkReserveFragment;
    private MyinfoFragment myinfoFragment;
    private boolean mBackPressedToExitOnce = false;
    private String sueccesRe = null;
    BottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);


        findseatFragment = new findSeatFragment();
        noticeFragment = new NoticeFragment();
        chkReserveFragment = new CheckReserveFragment();
        myinfoFragment = new MyinfoFragment();


        initFragment();


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (tabId == R.id.tab_find_seat) {
                    transaction.replace(R.id.contentContainer, findseatFragment).commit();
                    getSupportActionBar().setTitle("예약하기");
                } else if (tabId == R.id.tab_notice) //공지사항
                {
                    transaction.replace(R.id.contentContainer, noticeFragment).commit();
                    getSupportActionBar().setTitle("공지사항");
                } else if (tabId == R.id.tab_my_reserve) {
                    transaction.replace(R.id.contentContainer, chkReserveFragment).commit();
                    getSupportActionBar().setTitle("예약정보");
                } else if (tabId == R.id.tab_my_info) {
                    transaction.replace(R.id.contentContainer, myinfoFragment).commit();
                    getSupportActionBar().setTitle("내정보");
                }
            }
        });

    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer, findseatFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {

        if (!mBackPressedToExitOnce) {
            mBackPressedToExitOnce = true;
            Toast.makeText(this,"한번 더 눌러 종료",Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPressedToExitOnce = false;
                }
            }, 3500);
        } else {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Intent intent = getIntent();
            sueccesRe = intent.getExtras().getString("dataRE");
            if(sueccesRe.toString().equals("sucess")){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.contentContainer, chkReserveFragment).commit();
                getSupportActionBar().setTitle("예약정보");
                bottomBar.setDefaultTabPosition(2);
            }
        }catch (Exception e){

        }


    }
}