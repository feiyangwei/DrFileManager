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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.drxx.drfilemanager.Constants;
import com.drxx.drfilemanager.R;
import com.drxx.drfilemanager.model.MessageEvent;
import com.drxx.drfilemanager.utils.FileUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Dialog to create a new directory.
 */
public class CreateDirectoryFragment extends DialogFragment {
    private static final String TAG_CREATE_DIRECTORY = "create_directory";
    private static final String EXTRA_PATH = "path";

    public static void show(FragmentManager fm, String path, String mimeType, String displayName) {
        final Bundle args = new Bundle();
        args.putString(EXTRA_PATH, path);
        final CreateDirectoryFragment dialog = new CreateDirectoryFragment();
        dialog.setArguments(args);
        dialog.show(fm, TAG_CREATE_DIRECTORY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final LayoutInflater dialogInflater = LayoutInflater.from(builder.getContext());

        final View view = dialogInflater.inflate(R.layout.dialog_create_dir, null, false);
        final EditText text1 = (EditText) view.findViewById(android.R.id.text1);
        //Utils.tintWidget(text1);

        builder.setTitle(R.string.menu_create_dir);
        builder.setView(view);
        final String path = getArguments().getString(EXTRA_PATH);
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String displayName = text1.getText().toString();

                String result = FileUtils.createDir(path + "/" + displayName);
                EventBus.getDefault().post(new MessageEvent(Constants.OPERATION_CREATE_DIR, result));//MainActivity
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        return builder.create();
    }
}
