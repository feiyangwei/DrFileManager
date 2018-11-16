/*
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

package com.drxx.drfilemanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MountReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && null != intent.getAction()) {
            String actionName = intent.getAction();
            switch (actionName) {
                case Intent.ACTION_MEDIA_MOUNTED://挂载
                    break;
                case Intent.ACTION_MEDIA_UNMOUNTED://卸载
                    break;

            }
            Log.e("MountReceiver", "getData = " + intent.getData() + "\n");
            Log.e("MountReceiver", "getAction = " + intent.getAction() + "\n");
            Log.e("MountReceiver", "getDataString = " + intent.getDataString() + "\n");
            Log.e("MountReceiver", "getPackage = " + intent.getPackage() + "\n");
            Log.e("MountReceiver", "getScheme = " + intent.getScheme() + "\n");
            Log.e("MountReceiver", "getType = " + intent.getType() + "\n");
/*
            getData = file:///storage/0000-0000
            getAction = android.intent.action.MEDIA_UNMOUNTED
            getDataString = file:///storage/0000-0000
            getPackage = null
            getScheme = file
            getType = null
*/
        }
    }
}
