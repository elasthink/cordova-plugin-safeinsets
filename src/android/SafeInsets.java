/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package com.elasthink.cordova.safeinsets;

import android.app.Activity;
import android.os.Build;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeInsets extends CordovaPlugin {

    private static final String TAG = "SafeInsets";

    private CallbackContext callbackContext;

    private final JSONObject insetsJSON = new JSONObject();

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        if (Build.VERSION.SDK_INT >= 20) {
            final Activity activity = cordova.getActivity();
            final Window window = activity.getWindow();
            final View rootView = window.getDecorView().getRootView();
            final float density = activity.getResources().getDisplayMetrics().density;

            rootView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                    view.onApplyWindowInsets(insets);

                    int top     = insets.getSystemWindowInsetTop(),
                        right   = insets.getSystemWindowInsetRight(),
                        bottom  = insets.getSystemWindowInsetBottom(),
                        left    = insets.getSystemWindowInsetLeft();

                    if (Build.VERSION.SDK_INT >= 28) {
                        DisplayCutout cutout = insets.getDisplayCutout();
                        if (cutout != null) {
                            top     = cutout.getSafeInsetTop();
                            right   = cutout.getSafeInsetRight();
                            bottom  = cutout.getSafeInsetBottom();
                            left    = cutout.getSafeInsetLeft();
                        }
                    }

                    try {
                        insetsJSON.put("top", top / density);
                        insetsJSON.put("bottom", bottom / density);
                        insetsJSON.put("left", left / density);
                        insetsJSON.put("right", right / density);
                    } catch (JSONException ex) {}

                    if (callbackContext != null) {
                        sendInsets();
                    }

                    return insets;
                }
            });
        } else {
            LOG.v(TAG, "Required API level 20");
        }
    }

    @Override
    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) {
        if ("check".equals(action)) {
            this.callbackContext = callbackContext;
            sendInsets();
            return true;
        }
        return false;
    }

    private void sendInsets() {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, insetsJSON);
        pluginResult.setKeepCallback(true);
        callbackContext.sendPluginResult(pluginResult);
    }
}