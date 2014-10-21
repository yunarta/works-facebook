/*
 * Copyright 2014-present Yunarta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobilesolutionworks.android.facebook;

import android.support.annotation.NonNull;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.widget.FacebookDialog;

/**
 * Created by yunarta on 7/9/14.
 */
public interface WorksFacebook {

    int FACEBOOK_DIALOG_REQUEST_MASK = 0xe000;

    void trackPendingDialogCall(FacebookDialog.PendingCall pendingCall, FacebookDialog.Callback callback);

    interface Callback {

        void onCancelled();

        void onSessionOpened(Session result);
    }

    interface ResponseCallback {

        void onCancelled();

        void onCompleted(Response response);
    }

    void open(@NonNull WorksFacebook.Callback callback);

    void validate(@NonNull WorksFacebook.Callback callback);

    void requestMe(@NonNull WorksFacebook.ResponseCallback callback);

    void readRequest(Request request, @NonNull WorksFacebook.ResponseCallback callback, String... newPermissions);

    void publishRequest(Request request, @NonNull WorksFacebook.ResponseCallback callback, String... newPermissions);

    void close();
}
