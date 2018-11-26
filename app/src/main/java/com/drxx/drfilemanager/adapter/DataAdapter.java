package com.drxx.drfilemanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drxx.drfilemanager.Constants;
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
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private List<FileInfo> list;
    private OnItemClickListener listener;

    public DataAdapter(Context mContext, List<FileInfo> list, OnItemClickListener listener) {
        this.mContext = mContext;
        if (null == list) {
            list = new ArrayList<>();
        }
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FileInfo info = list.get(position);
        if (null != info) {
            if (Constants.FILE_TYPE_DIR.equals(info.getFileType())) {
                holder.iv.setImageResource(R.drawable.ic_doc_folder);
            } else {
                holder.iv.setImageResource(R.drawable.ic_doc_document);
            }
            holder.tv.setText(info.getFileName());
            //holder.tv.setText(info.getFilePath());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.itemClick(view, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.itemLongClick(view, position);
                    return true;
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
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_show);
            iv = itemView.findViewById(R.id.iv_type);
        }
    }

    public interface OnItemClickListener {
        public void itemClick(View v, int position);

        public void itemLongClick(View v, int position);
    }
}
