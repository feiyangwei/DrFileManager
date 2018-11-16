package com.drxx.drfilemanager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.drxx.drfilemanager.adapter.DataAdapter;
import com.drxx.drfilemanager.adapter.HomeAdapter;
import com.drxx.drfilemanager.model.FileInfo;
import com.drxx.drfilemanager.utils.FileUtils;
import com.drxx.drfilemanager.utils.PermissionsUtil;
import com.drxx.drfilemanager.view.OperationPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/15
 * 邮箱：cugb_feiyang@163.com
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_left)
    RecyclerView rvLeft;
    @BindView(R.id.rv_right)
    RecyclerView rvRight;
    @BindView(R.id.tv_path)
    TextView tvPath;
    @BindView(R.id.rv_path)
    RecyclerView rvPath;
    private Context mContext;
    private List<FileInfo> homeList = new ArrayList<>();
    private List<FileInfo> dataList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private DataAdapter dataAdapter;
    private OperationPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        PermissionsUtil.checkAndRequestPermissions(this, new PermissionsUtil.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted() {

            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms) {

            }
        });
    }

    private void initPathRv() {
        homeAdapter = new HomeAdapter(mContext, homeList, new HomeAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int position) {
                tvPath.setText(homeList.get(position).getFilePath());
                dataList.clear();
                dataList.addAll(FileUtils.getFile(homeList.get(position)));
                dataAdapter.notifyDataSetChanged();


            }
        });
        rvPath.setLayoutManager(new GridLayoutManager(mContext, 1));
        rvPath.setAdapter(homeAdapter);
    }

    private void initLeftRv() {
        homeAdapter = new HomeAdapter(mContext, homeList, new HomeAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int position) {
                tvPath.setText(homeList.get(position).getFilePath());
                dataList.clear();
                dataList.addAll(FileUtils.getFile(homeList.get(position)));
                dataAdapter.notifyDataSetChanged();
            }
        });
        rvLeft.setLayoutManager(new LinearLayoutManager(mContext));
        rvLeft.setAdapter(homeAdapter);
    }

    private void initRightRv() {
        dataAdapter = new DataAdapter(mContext, dataList, new DataAdapter.OnItemClickListener() {
            @Override
            public void itemClick(View v, int position) {
                if (Constants.FILE_TYPE_DIR.equals(dataList.get(position).getFileType())) {//文件夹
                    tvPath.setText(dataList.get(position).getFilePath());
                    List<FileInfo> list = FileUtils.getFile(dataList.get(position));
                    dataList.clear();
                    dataList.addAll(list);
                    dataAdapter.notifyDataSetChanged();
                } else {
                    // TODO: 2018/11/15 打开文件
                    FileUtils.openFile(mContext, new File(dataList.get(position).getFilePath()));
                }
            }

            @Override
            public void itemLongClick(View v, int position) {
                // TODO: 2018/11/15 弹窗 重命名、删除、复制、剪切
                if (null == popupWindow) {
                    popupWindow = new OperationPopupWindow(mContext);
                }
                popupWindow.showAsDropDown(v);


            }
        });
        rvRight.setLayoutManager(new LinearLayoutManager(mContext));
        rvRight.setAdapter(dataAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        homeList = FileUtils.getStoragePath(mContext);

        if (homeList.size() > 0) {
            dataList = FileUtils.getFile(homeList.get(0));
            tvPath.setText(homeList.get(0).getFilePath());
        }
        initLeftRv();
        initRightRv();

        Log.e("BGA", "getFilesDir = " + mContext.getFilesDir());
        Log.e("BGA", "getCacheDir = " + mContext.getCacheDir());
        Log.e("BGA", "getExternalStorageDirectory = " + Environment.getExternalStorageDirectory());
        Log.e("BGA", "getExternalCacheDirs = " + mContext.getExternalCacheDirs());
    }
}
