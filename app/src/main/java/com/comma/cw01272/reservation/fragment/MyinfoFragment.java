package com.comma.cw01272.reservation.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.request.UserInfoRequest;
import com.comma.cw01272.reservation.request.UserModifyRequest;
import com.comma.cw01272.reservation.request.UserSecessionRequest;
import com.comma.cw01272.reservation.activity.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyinfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyinfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyinfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String UserID = null;
    private String userPASS = null;
    private String userEMAIL = null;
    String  user_id, user_pass, user_gender, usermajor, user_email;


    private EditText ed_email;
    private TextView txt_major;
    private TextView txt_userID;
    private EditText ed_pass = null;
    private Button btn_modify;
    private Button btn_secession;
    private PopupWindow mPopupWindow;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyinfoFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_myinfo , container, false);

        ed_email = (EditText) v.findViewById(R.id.input_email);
        txt_major = (TextView) v.findViewById(R.id.text_major);
        txt_userID = (TextView) v.findViewById(R.id.txt_userID);
        ed_pass = (EditText) v.findViewById(R.id.input_pass);
        btn_modify=(Button)v.findViewById(R.id.modifyButton);
        btn_secession=(Button)v.findViewById(R.id.secessionButton);



        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEMAIL = ed_email.getText().toString();
                userPASS = ed_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonReponse = new JSONObject(response);
                            boolean success = jsonReponse.getBoolean("success");
                            if(success)
                            {
                                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("회원정보 수정 되었습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                    }
                                });

                            }

                            else {
                                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("회원정보 수정을 실패했습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                    }
                                });

                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                UserModifyRequest userModifyRequest = new UserModifyRequest(UserID,userEMAIL,userPASS, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(userModifyRequest);

            }
        });

        btn_secession.setOnClickListener(new View.OnClickListener() {
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
                                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("회원탈퇴 되었습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(loginIntent);
                                    }
                                });



                            }

                            else {
                                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                                mPopupWindow = new PopupWindow(popupView,
                                        RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                                TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                                mTitleTxt.setText("회원탈퇴를 실패했습니다.");
                                Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                                mCloseButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mPopupWindow.dismiss();
                                    }
                                });

                            }


                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                UserSecessionRequest userSecessionRequest = new UserSecessionRequest(UserID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                queue.add(userSecessionRequest);




            }
        });



        SharedPreferences sf = this.getActivity().getSharedPreferences("UserINFO", 0);
        UserID = sf.getString("UserID", ""); // 키값으로 꺼냄

        getMyInfo();

        return v;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyinfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyinfoFragment newInstance(String param1, String param2) {
        MyinfoFragment fragment = new MyinfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getMyInfo(){


        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;

                    while(count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        user_id = object.getString("userID");
                        user_pass = object.getString("userPassword");
                        user_gender = object.getString("userGender");
                        usermajor = object.getString("userMajor");
                        user_email = object.getString("userEmail");
                        //CheckReservation chkreservation = new CheckReservation(reserSeq, comName, comVailableNum,comTotalNum);
                        count++;

                    }
                    txt_userID.setText(user_id);
                    ed_email.setText(user_email);
                    txt_major.setText(usermajor);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
        };

        UserInfoRequest userInfoRequest = new UserInfoRequest(UserID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(userInfoRequest);



    }


}
