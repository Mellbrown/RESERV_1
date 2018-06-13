package com.comma.cw01272.reservation.fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.comma.cw01272.reservation.request.CancelReservationRequest;
import com.comma.cw01272.reservation.bean.CheckReservation;
import com.comma.cw01272.reservation.request.CheckReservationRequest;
import com.comma.cw01272.reservation.adapter.CheckReserveListAdapter;
import com.comma.cw01272.reservation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IFCOMM-10 on 2017-11-27.
 */

public class CheckReserveFragment extends Fragment implements CheckReserveListAdapter.Callback {



    private ListView checkReserveListView;
    private CheckReserveListAdapter adapter;
    private List<CheckReservation> checkReserveList;
    private String UserID = null;
    //private AlertDialog dialog;
    private PopupWindow mPopupWindow;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CheckReserveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment findSeatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckReserveFragment newInstance(String param1, String param2) {
        CheckReserveFragment fragment = new CheckReserveFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_check_reserve, container, false);

        checkReserveListView = (ListView) v.findViewById(R.id.checkreserveListView);
        //checkReserveList = new ArrayList<CheckReservation>();

        //adapter = new CheckReserveListAdapter(getActivity().getApplicationContext(), checkReserveList, this);
        //checkReserveListView.setAdapter(adapter);

        SharedPreferences sf = this.getActivity().getSharedPreferences("UserINFO", 0);
        UserID = sf.getString("UserID", ""); // 키값으로 꺼냄

        getlist();


        return v;
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

    @Override
    public void onClickCancel(int itemPosition) {

        String cancelreservationSeq = checkReserveList.get(itemPosition).reserSeq;
        String comSeq = checkReserveList.get(itemPosition).comSeq;

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
                        mTitleTxt.setText("예약취소를 성공했습니다.");
                        Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                        mCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        dialog = builder.setMessage("예약취소를 성공했습니다.")
//                                .setPositiveButton("확인", null)
//                                .create();
//                        dialog.show();
                    }

                    else {
                        View popupView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                        mPopupWindow = new PopupWindow(popupView,
                                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        TextView mTitleTxt = (TextView)popupView.findViewById(R.id.popup_title);
                        mTitleTxt.setText("예약취소에 실패했습니다.");
                        Button mCloseButton = (Button)popupView.findViewById(R.id.popup_button);
                        mCloseButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        dialog = builder.setMessage("예약취소에 실패했습니다.")
//                                .setNegativeButton("확인", null)
//                                .create();
//                        dialog.show();
                    }

                    getlist();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        CancelReservationRequest cancelReservationRequest = new CancelReservationRequest(cancelreservationSeq, comSeq,responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(cancelReservationRequest);




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

    public void getlist(){

        checkReserveList = new ArrayList<CheckReservation>();
        adapter = new CheckReserveListAdapter(getActivity().getApplicationContext(), checkReserveList, this);
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("response");
                    int count = 0;
                    String  reserSeq, comSeq,comName, cominfo, comVailableNum,comTotalNum,comImg;
                    while(count < jsonArray.length())
                    {
                        JSONObject object = jsonArray.getJSONObject(count);
                        reserSeq = object.getString("reserSeq");
                        comSeq = object.getString("comSeq");
                        comName = object.getString("com_name");
                        comVailableNum = object.getString("vailableNum");
                        comTotalNum = object.getString("totalNum");
                        CheckReservation chkreservation = new CheckReservation(reserSeq, comSeq, comName, comVailableNum,comTotalNum);
                        checkReserveListView.setAdapter(adapter);
                        checkReserveList.add(chkreservation);
                        adapter.notifyDataSetChanged();
                        checkReserveListView.invalidateViews();
                        count++;

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };

        CheckReservationRequest checkReservationRequest = new CheckReservationRequest(UserID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(checkReservationRequest);



    }
 }
