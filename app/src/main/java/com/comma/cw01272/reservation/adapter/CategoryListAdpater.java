package com.comma.cw01272.reservation.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comma.cw01272.reservation.R;

import java.util.ArrayList;

public class CategoryListAdpater extends RecyclerView.Adapter<CategoryListAdpater.CategoryViewHolder> {

    public ArrayList<String> datalist = new ArrayList<>();
    OnclickListener onclickListener;

    public CategoryListAdpater(OnclickListener onclickListener){
        this.onclickListener = onclickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        final CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        categoryViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int layoutPosition = categoryViewHolder.getLayoutPosition();
                onclickListener.onclick(datalist.get(layoutPosition));
            }
        });
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.button.setText(datalist.get(position));
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public interface OnclickListener {
        void onclick(String s);
    }
    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public AppCompatButton button;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            button = ((AppCompatButton) itemView.findViewById(R.id.button));
        }
    }
}
