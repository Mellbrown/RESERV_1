package com.comma.cw01272.reservation.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.comma.cw01272.reservation.adapter.ExpandListAdapter;
import com.comma.cw01272.reservation.bean.NoticeChild;
import com.comma.cw01272.reservation.bean.NoticeGroup;
import com.comma.cw01272.reservation.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeFragment extends Fragment {


//    private ListView noticeListView;
//    private NoticeListAdapter listadapter;
//    private List<Notice> noticeList;
//    private HashMap<String, List<String>> listDataChild;
    String url ="http://cw01272.dothome.co.kr/NoticeList.php";
    private ExpandListAdapter ExpAdapter;
    private ExpandableListView ExpandList;

    Context mContext;
    ArrayList<NoticeGroup> list;
    ArrayList<NoticeChild> ch_list;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NoticeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_notice, container, false);
        mContext = container.getContext();

        ExpandList = (ExpandableListView) v.findViewById(R.id.exp_list);

        Display newDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        ExpandList.setIndicatorBounds(width-50, width);

        list = new ArrayList<NoticeGroup>();
        ch_list = new ArrayList<NoticeChild>();
        new BackgroundTask().execute();

        return v;



    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static NoticeFragment newInstance(String param1, String param2) {
        NoticeFragment fragment = new NoticeFragment();
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

        class BackgroundTask extends AsyncTask<Void, Void, String>
        {


            String target;
            @Override
            protected void onPreExecute() {
                target = "http://cw01272.dothome.co.kr/NoticeList.php";
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

                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    NoticeGroup gru = new NoticeGroup();
                    gru.setnoticeName(object.getString("noticeName"));
                    gru.setDate(object.getString("noticeDate"));

                    ch_list = new ArrayList<NoticeChild>();
                    JSONArray ja = jsonObject.getJSONArray("response");
                    JSONObject jo = ja.getJSONObject(count);
                    NoticeChild ch = new NoticeChild();
                    ch.setnoticeContent(jo.getString("noticeContent"));
                    ch_list.add(ch);
                    gru.setItems(ch_list);
                    list.add(gru);
                    count++;
         }
                ExpAdapter = new ExpandListAdapter(
                        getActivity().getApplication(), list);
                ExpandList.setAdapter(ExpAdapter);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
