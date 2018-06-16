package com.comma.cw01272.reservation.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.activity.AddCompany;
import com.comma.cw01272.reservation.adapter.CategoryListAdpater;
import com.comma.cw01272.reservation.bean.Reservation;
import com.comma.cw01272.reservation.adapter.ReserveListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class findSeatFragment extends Fragment implements ValueEventListener {

    private FloatingActionButton btnAdd;
    private ListView ReserveListView;
    private ReserveListAdapter adapter;
    private List<Reservation> ReserveList;
    private RecyclerView categorylist;
    private CategoryListAdpater categoryListAdpater;

    private OnFragmentInteractionListener mListener;

    private HashMap<String, Reservation> itemmap = new HashMap<>();
    private HashMap<String, List<String>> categorymap = new HashMap<>();
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

        categorylist = ((RecyclerView) v.findViewById(R.id.categorylist));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        categorylist.setLayoutManager(layoutManager);
        categoryListAdpater = new CategoryListAdpater(new CategoryListAdpater.OnclickListener(){
            @Override
            public void onclick(String s) {
                ReserveList.clear();
                List<String> list = categorymap.get(s);
                if(list != null){
                    for(String name : list){
                        Reservation item = itemmap.get(name);
                        if(item != null) ReserveList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        categorylist.setAdapter(categoryListAdpater);
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference("category").addValueEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseDatabase.getInstance().getReference("category").addValueEventListener(this);
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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        categoryListAdpater.datalist.clear();
        categorymap.clear();
        for(DataSnapshot data : dataSnapshot.getChildren()){
            String name = data.child("name").getValue(String.class);
            if(name == null) continue;
            categoryListAdpater.datalist.add(name);
            List<String> list = data.child("list").getValue(new GenericTypeIndicator<List<String>>(){});
            if(list == null)
                categorymap.put(name,new ArrayList<String>());
            else
                categorymap.put(name,list);
        }
        categoryListAdpater.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

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
                    ReserveList.add(reservation);
                    itemmap.put(comName, reservation);
                    count++;
                }
                adapter.notifyDataSetChanged();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
