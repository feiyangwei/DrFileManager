package com.drxx.drfilemanager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.drxx.drfilemanager.adapter.DataAdapter;
import com.drxx.drfilemanager.adapter.HomeAdapter;
import com.drxx.drfilemanager.fragment.CreateDirectoryFragment;
import com.drxx.drfilemanager.fragment.CreateFileFragment;
import com.drxx.drfilemanager.model.FileInfo;
import com.drxx.drfilemanager.model.MessageEvent;
import com.drxx.drfilemanager.utils.FileUtils;
import com.drxx.drfilemanager.utils.PermissionsUtil;
import com.drxx.drfilemanager.utils.ToastUtils;
import com.drxx.drfilemanager.view.OperationPopupWindow;
import com.drxx.drfilemanager.view.fab.FloatingActionButton;
import com.drxx.drfilemanager.view.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.fab_create_file)
    FloatingActionButton fabCreateFile;
    @BindView(R.id.fab_create_dir)
    FloatingActionButton fabCreateDir;
    @BindView(R.id.menu_fab)
    FloatingActionMenu menuFab;
    @BindView(R.id.tv_paste)
    TextView tvPaste;

    private Context mContext;
    private List<FileInfo> homeList = new ArrayList<>();
    private List<FileInfo> dataList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private DataAdapter dataAdapter;
    private int leftPosition = 0;
    private int rightPosition = 0;
    private String locationPath;//当前路径
    private String copyPath;//复制源路径
    private String messageFlag;//标示
    private OperationPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Log.e("BGA", "getFilesDir = " + mContext.getFilesDir());
        Log.e("BGA", "getCacheDir = " + mContext.getCacheDir());
        Log.e("BGA", "getObbDir = " + mContext.getObbDir());
        Log.e("BGA", "getExternalFilesDir = " + mContext.getExternalFilesDir(null));
        Log.e("BGA", "getExternalStorageDirectory = " + Environment.getExternalStorageDirectory());
        Log.e("BGA", "getRootDirectory = " + Environment.getRootDirectory());
        Log.e("BGA", "getDataDirectory = " + Environment.getDataDirectory());

        //getFilesDir = /data/user/0/com.drxx.drfilemanager/files
        //getCacheDir = /data/user/0/com.drxx.drfilemanager/cache
        //getExternalStorageDirectory = /storage/emulated/0
        //getExternalCacheDirs = [Ljava.io.File;@a905859
        PermissionsUtil.checkAndRequestPermissions(this, new PermissionsUtil.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted() {

            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                leftPosition = position;
                locationPath = homeList.get(position).getFilePath();
                tvPath.setText(locationPath);
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
                    locationPath = dataList.get(position).getFilePath();
                    tvPath.setText(locationPath);
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
            public void itemLongClick(View v, final int position) {
                rightPosition = position;
                // TODO: 2018/11/15 弹窗 重命名、删除、复制、剪切
                if (null == popupWindow) {
                    popupWindow = new OperationPopupWindow(mContext, new OperationPopupWindow.ResultCallBack() {
                        @Override
                        public void deleteResult(boolean result) {
                            if (result) {//删除成功
                                dataList.remove(position);
                                dataAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
                popupWindow.setPath(dataList.get(position).getFilePath());
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
            locationPath = homeList.get(0).getFilePath();
            tvPath.setText(locationPath);
        }
        initLeftRv();
        initRightRv();
    }

    @OnClick({R.id.fab_create_file, R.id.fab_create_dir, R.id.tv_paste})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_create_file:
                menuFab.close(true);
                CreateFileFragment.show(getSupportFragmentManager(), locationPath, "text/plain", "File");
                break;
            case R.id.fab_create_dir:
                menuFab.close(true);
                CreateDirectoryFragment.show(getSupportFragmentManager(), locationPath, "", "");
                break;
            case R.id.tv_paste:
                // TODO: 2018/11/26 还需要对刷新做判断
                if (FileUtils.copyFiles(copyPath, locationPath)) {
                    refreshData();
                    tvPaste.setVisibility(View.GONE);
                    if (messageFlag.equals(Constants.OPERATION_MOVE)) {//移动要删除
                        FileUtils.delete(copyPath);
                    }
                }
                break;
        }
    }

    private void refreshData() {
        if (null != dataAdapter) {
            dataList.clear();
            dataList.addAll(FileUtils.getFile(new FileInfo(locationPath)));
            dataAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent info) {
        messageFlag = info.getFlag();
        switch (messageFlag) {
            case Constants.OPERATION_DELETE:
            case Constants.OPERATION_RENAME:
            case Constants.OPERATION_CREATE_FILE:
            case Constants.OPERATION_CREATE_DIR:
                refreshData();
                ToastUtils.showLong(info.getResult());
                break;
            case Constants.OPERATION_COPY:
                tvPaste.setVisibility(View.VISIBLE);
                copyPath = info.getContent();
                break;
            case Constants.OPERATION_MOVE:
                tvPaste.setVisibility(View.VISIBLE);
                copyPath = info.getContent();
                break;
        }
    }

}
