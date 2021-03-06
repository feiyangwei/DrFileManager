/*
 * Copyright (C) 2014 Hari Krishna Dulipudi
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drxx.drfilemanager.fragment;

import android.app.Dialog;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.drxx.drfilemanager.Constants;
import com.drxx.drfilemanager.R;
import com.drxx.drfilemanager.model.FileInfo;
import com.drxx.drfilemanager.model.MessageEvent;
import com.drxx.drfilemanager.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Dialog to create a new directory.
 */
public class RenameFragment extends DialogFragment {
    private static final String TAG_RENAME = "rename";
    private static final String EXTRA_DOC = "document";
    private static final String EXTRA_PATH_OLD = "old_path";
    private static final String EXTRA_PATH_NEW = "new_path";
    private static final String EXTRA_FILE_NAME = "file_name";
    private boolean editExtension = true;
    private FileInfo doc;
    private String oldPath;
    private String newPath;

    public static void show(FragmentManager fm, String oldPath, String fileName) {
        final Bundle args = new Bundle();
        args.putString(EXTRA_PATH_OLD, oldPath);
        args.putString(EXTRA_FILE_NAME, fileName);
        final RenameFragment dialog = new RenameFragment();
        dialog.setArguments(args);
        dialog.show(fm, TAG_RENAME);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null != args) {
            doc = args.getParcelable(EXTRA_DOC);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();

        //final DocumentsActivity activity = (DocumentsActivity) getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater dialogInflater = LayoutInflater.from(builder.getContext());

        final View view = dialogInflater.inflate(R.layout.dialog_rename, null, false);
        final EditText text1 = (EditText) view.findViewById(R.id.tv_name);

        //String nameOnly = editExtension ? doc.displayName : FileUtils.removeExtension(doc.mimeType, doc.displayName);

        //text1.setText(nameOnly);
        //text1.setSelection(text1.getText().length());

        builder.setTitle(R.string.menu_rename);
        builder.setView(view);

        builder.setPositiveButton(R.string.menu_rename, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String displayName = text1.getText().toString();
                String fileName = getArguments().getString(EXTRA_FILE_NAME);
                oldPath = getArguments().getString(EXTRA_PATH_OLD);
                String result = FileUtils.renameFile(oldPath + fileName, oldPath + displayName);
                EventBus.getDefault().post(new MessageEvent(Constants.OPERATION_RENAME, result));//MainActivity

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}