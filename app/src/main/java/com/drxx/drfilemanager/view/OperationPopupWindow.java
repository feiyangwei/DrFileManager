package com.drxx.drfilemanager.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.drxx.drfilemanager.R;
import com.drxx.drfilemanager.fragment.RenameFragment;
import com.drxx.drfilemanager.model.FileInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类描述：
 * 创建人：wfy
 * 创建时间：2018/11/16
 * 邮箱：cugb_feiyang@163.com
 */
public class OperationPopupWindow extends PopupWindow {
    @BindView(R.id.tv_rename)
    Button tvRename;
    @BindView(R.id.tv_copy)
    Button tvCopy;
    @BindView(R.id.tv_cut)
    Button tvCut;
    @BindView(R.id.tv_delete)
    Button tvDelete;

    private Context mContext;
    private View root;


    public OperationPopupWindow(Context mContext) {
        this.mContext = mContext;
        init();
    }

    private void init() {
        root = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popu_operation, null);
        setContentView(root);
        ButterKnife.bind(this, root);
        //自定义基础，设置我们显示控件的宽，高，焦点，点击外部关闭PopupWindow操作
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置背景
        ColorDrawable colorDrawable = new ColorDrawable(0000000000);
        this.setBackgroundDrawable(colorDrawable);
    }


    @OnClick({R.id.tv_rename, R.id.tv_copy, R.id.tv_cut, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_rename:
                dismiss();
                renameDocument();
                break;
            case R.id.tv_copy:
                dismiss();
                break;
            case R.id.tv_cut:
                dismiss();
                break;
            case R.id.tv_delete:
                dismiss();
                break;
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            Rect rect = new Rect();
//            anchor.getGlobalVisibleRect(rect);
//            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            setHeight(h);
//        }
        super.showAsDropDown(anchor);
    }

    /**
     * 删除
     */
    private void deleteDocument() {

    }

    /**
     * 移动
     */
    private void moveDocument() {

    }

    /**
     * 重命名
     */
    private void renameDocument() {
        RenameFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), new FileInfo("111"));
    }


}
