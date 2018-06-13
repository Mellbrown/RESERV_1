package com.comma.cw01272.reservation.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.comma.cw01272.reservation.bean.NoticeChild;
import com.comma.cw01272.reservation.bean.NoticeGroup;
import com.comma.cw01272.reservation.R;

import java.util.ArrayList;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<NoticeGroup> groups;

    //ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();

    public ExpandListAdapter(Context context, ArrayList<NoticeGroup> groups) {
        this.context = context;
        this.groups = groups;
    }




    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<NoticeChild> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        NoticeChild child = (NoticeChild) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.notice_child_item, null);
        }

//        if (imageLoader == null)
//            imageLoader = MyApplication.getInstance().getImageLoader();

        TextView tv = (TextView) convertView.findViewById(R.id.notice_contents);
//        NetworkImageView iv = (NetworkImageView) convertView
//                .findViewById(R.id.flag);

        tv.setText(child.getnoticeContent().toString());
        //iv.setImageUrl(child.getImage(), imageLoader);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<NoticeChild> chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        NoticeGroup group = (NoticeGroup) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.notice_group_item, null);
        }
        TextView Titletv = (TextView) convertView.findViewById(R.id.group_title);
        TextView Datetv = (TextView) convertView.findViewById(R.id.group_date);
        Titletv.setText(group.getnoticeName());
        Datetv.setText(group.getDate());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}