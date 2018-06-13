package com.comma.cw01272.reservation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.activity.AddCompany;
import com.comma.cw01272.reservation.bean.Reservation;
import com.comma.cw01272.reservation.adapter.ReserveListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class findSeatFragment extends Fragment {

    private FloatingActionButton btnAdd;
    private ListView ReserveListView;
    private ReserveListAdapter adapter;
    private List<Reservation> ReserveList;

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_find_seat, container, false);
        ReserveListView = (ListView) v.findViewById(R.id.reserveListView);
        btnAdd = (FloatingActionButton) v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddCompany.class));
            }
        });
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new BackgroundTask().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute() {
            target = "http://cw01272.dothome.co.kr/CompanyList.php";
            ReserveList = new ArrayList<Reservation>();
            adapter = new ReserveListAdapter(getActivity().getApplicationContext(), ReserveList);
            ReserveListView.setAdapter(adapter);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();


            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        public void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate();

        }

        @Override
        public void onPostExecute(String result) {

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String  comSeq, comName, cominfo, comVailableNum,comTotalNum,comImg;
                while(count < jsonArray.length())
                {
                    JSONObject object = jsonArray.getJSONObject(count);
                    comSeq = object.getString("seq");
                    comName = object.getString("name");
                    cominfo = object.getString("info");
                    comVailableNum = object.getString("vailableNum");
                    comTotalNum = object.getString("TotalNum");
                    comImg = object.getString("file_name");
                    Reservation reservation = new Reservation(comSeq, comName, cominfo, comVailableNum,comTotalNum,comImg);
                    ReserveListView.setAdapter(adapter);
                    ReserveList.add(reservation);
                    adapter.notifyDataSetChanged();
                    count++;

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
