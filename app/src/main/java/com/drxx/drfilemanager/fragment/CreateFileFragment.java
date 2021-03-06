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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.drxx.drfilemanager.Constants;
import com.drxx.drfilemanager.R;
import com.drxx.drfilemanager.model.MessageEvent;
import com.drxx.drfilemanager.utils.FileUtils;
import com.drxx.drfilemanager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;


/**
 * Dialog to create a new file.
 */
public class CreateFileFragment extends DialogFragment {
    private static final String TAG = "create_file";
    private static final String EXTRA_MIME_TYPE = "mime_type";
    private static final String EXTRA_DISPLAY_NAME = "display_name";
    private static final String EXTRA_PATH = "path";

    public static void show(FragmentManager fm, String path, String mimeType, String displayName) {
        final Bundle args = new Bundle();
        args.putString(EXTRA_MIME_TYPE, mimeType);
        args.putString(EXTRA_DISPLAY_NAME, displayName);
        args.putString(EXTRA_PATH, path);
        final CreateFileFragment dialog = new CreateFileFragment();
        dialog.setArguments(args);
        dialog.show(fm, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater dialogInflater = LayoutInflater.from(builder.getContext());

        final View view = dialogInflater.inflate(R.layout.dialog_create_dir, null, false);
        final EditText text1 = (EditText) view.findViewById(android.R.id.text1);
        //Utils.tintWidget(text1);

        String title = getArguments().getString(EXTRA_DISPLAY_NAME);
        if (!TextUtils.isEmpty(title)) {
            text1.setText(title);
            text1.setSelection(title.length());
        }
        builder.setTitle(R.string.menu_create_file);
        builder.setView(view);
        final String path = getArguments().getString(EXTRA_PATH);

        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String displayName = text1.getText().toString();
                final String mimeType = getArguments().getString(EXTRA_MIME_TYPE);
                String result = FileUtils.createFile(path, displayName);
                EventBus.getDefault().post(new MessageEvent(Constants.OPERATION_CREATE_FILE, result));//MainActivity
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}
