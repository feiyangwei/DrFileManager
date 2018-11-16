package com.drxx.drfilemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drxx.drfilemanager.R;
import com.drxx.drfilemanager.model.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/15
 * 邮箱：cugb_feiyang@163.com
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private List<FileInfo> list;
    private OnItemClickListener listener;

    public HomeAdapter(Context mContext,List<FileInfo> list, OnItemClickListener listener) {
        this.mContext = mContext;
        if(null == list){
            list = new ArrayList<>();
        }
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FileInfo info = list.get(position);
        if (null != info) {
            //holder.tv.setText(info.getFileName());
            holder.tv.setText(info.getFilePath());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_show);
        }
    }

    public interface OnItemClickListener {
        public void itemClick(View v, int position);
    }
}
