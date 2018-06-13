package com.comma.cw01272.reservation.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.request.ReservationRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

//import com.example.jizard.testapplication.adapter.ReplyListAdapter;
//import com.example.jizard.testapplication.datatype.ReplyData;
//import com.example.jizard.testapplication.parser.ReplyParser;

/**
 * Created by gold24park on 2016. 4. 5..
 */
public class CompanyActivity extends AppCompatActivity {

    private final String TAG_TYPE = "company";
    private String comSeq, comName,comInfo,comVailableNum,comTotalNum,comImg;
    private TextView tv_Name,tv_Info,tv_comVailableNum,tv_comTotal;
    private ImageView iv_img1;
    private ImageButton btnReservation;
    //private static ListView replyListView;
    //private static String cnum, id;
   // private static ReplyListAdapter replyListAdapter;
    private String UserID = null;
    private static Context context;
    private Toolbar mToolbar;
    //private AlertDialog dialog;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("예약하기");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        init();
        receiveIntentData();

//        try {
//            updateReplyList();
//            loadReplyFragment();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //뷰에 정보 세팅 & 출력
        tv_Name.setText(comName);
        tv_Info.setText(comInfo);
        tv_comVailableNum.setText(comVailableNum);
        tv_comTotal.setText(comTotalNum);
        loadImage1(comImg);

        btnReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonReponse = new JSONObject(response);
                            boolean success = jsonReponse.getBoolean("success");
                            if(success)
                            {

                                View popupView = getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("예약이 완료 되었습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                        //finish();

                                        Intent i = new Intent(CompanyActivity.this,MainActivity.class);
                                        i.putExtra("dataRE", "sucess");
                                        startActivity(i);

                                       // changeFragment(chkReserveFragment,FragmentsAvailable.HISTORY,false);

//                                        Intent i = new Intent(CompanyActivity.this,CheckReserveFragment.class);
//                                        startActivity(i);
//                                        CheckReserveFragment chkReserveFragment = new CheckReserveFragment();
//                                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                                        transaction.replace(R.id.contentContainer, chkReserveFragment).commit();
//                                        getSupportActionBar().setTitle("예약정보");

                                    }
                                });


                            }

                            else {
                                View popupView = getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("예약 실패했습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                    }
                                });
//                                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
//                                dialog = builder.setMessage("예약 실패했습니다.")
//                                        .setNegativeButton("확인", null)
//                                        .create();
//                                dialog.show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                ReservationRequest reserveRequest = new ReservationRequest(UserID, comSeq, comName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(CompanyActivity.this);
                queue.add(reserveRequest);

            }
        });

    }



    public void loadImage1(String url) {
        if(!url.isEmpty()) {
            Picasso.with(this).load(url).fit().into(iv_img1);
        }
    }


    public void init() {
        context = getApplicationContext();
        tv_Name = (TextView)findViewById(R.id.nametext);
        tv_Info = (TextView)findViewById(R.id.infotext);
        tv_comVailableNum = (TextView)findViewById(R.id.abletext);
        tv_comTotal = (TextView)findViewById(R.id.totaltext);
        iv_img1 = (ImageView)findViewById(R.id.iv_com);
        btnReservation = (ImageButton) findViewById(R.id.BTN_RESERVATION);
    }

    public void receiveIntentData() {
        Intent intent = getIntent();
        comSeq = intent.getStringExtra("seq");
        comName = intent.getStringExtra("name");
        comInfo = intent.getStringExtra("info");
        comVailableNum = intent.getStringExtra("vailableNum");
        comTotalNum = intent.getStringExtra("totalNum");
        comImg = intent.getStringExtra("comImg");
        SharedPreferences sf = getSharedPreferences("UserINFO", 0);
        UserID = sf.getString("UserID", ""); // 키값으로 꺼냄

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {


        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            super.onBackPressed();
            //finish();
        }


    }
    public enum FragmentsAvailable {
        HISTORY
    }
    private void changeFragment(Fragment newFragment, FragmentsAvailable newFragmentType, boolean withoutAnimation) {


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        try {
            getSupportFragmentManager().popBackStackImmediate(newFragmentType.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (java.lang.IllegalStateException e) {

        }

        transaction.addToBackStack(newFragmentType.toString());
        transaction.replace(R.id.contentContainer, newFragment);
        transaction.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();


    }
}
