package com.comma.cw01272.reservation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comma.cw01272.reservation.R;
import com.comma.cw01272.reservation.bean.Reservation;
import com.comma.cw01272.reservation.activity.CompanyActivity;

import java.util.List;

/**
 * Created by COMMA08 on 2017-11-06.
 */

public class ReserveListAdapter extends BaseAdapter{
    private Context context;
    private List<Reservation> reserveList;

    public ReserveListAdapter(Context context, List<Reservation> reserveList) {
        this.context = context;
        this.reserveList = reserveList;
    }

    @Override
    public int getCount() {
        return reserveList.size();
    }

    @Override
    public Object getItem(int i) {
        return reserveList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.reserve_item, null);
        final int pos = i;
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.llreserve);
        TextView comName = (TextView) v.findViewById(R.id.comName);
        //TextView comInfo = (TextView) v.findViewById();
        TextView comVailableNum = (TextView) v.findViewById(R.id.comableNum);
        TextView comTotalNum = (TextView) v.findViewById(R.id.comtotalNum);
        ImageView reservationIcon = (ImageView) v.findViewById(R.id.reservation_icon);

        comName.setText(reserveList.get(i).getComName());
       // comInfo.setText(reserveList.get(i).getComInfo());
        comVailableNum.setText(reserveList.get(i).getVailableNum());
        comTotalNum.setText(reserveList.get(i).getTotalNum());

        int intVailable = Integer.parseInt(reserveList.get(i).getVailableNum());
        int intTotal = Integer.parseInt(reserveList.get(i).getTotalNum());




        if (intVailable < intTotal){
            reservationIcon.setImageResource(R.drawable.a_reservation);
        }else{
            reservationIcon.setImageResource(R.drawable.not_reservation);
        }



        v.setTag(reserveList.get(i).getComName());
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CompanyActivity.class);
                i.putExtra("seq", reserveList.get(pos).comSeq);
                i.putExtra("name", reserveList.get(pos).comName);
                i.putExtra("info", reserveList.get(pos).cominfo);
                i.putExtra("vailableNum", reserveList.get(pos).comVailableNum);
                i.putExtra("totalNum", reserveList.get(pos).comTotalNum);
                i.putExtra("comImg", reserveList.get(pos).comImg);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        return v;
    }

}
