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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

/**
 * Created by yunarta on 7/9/14.
 */
public interface WorksFacebook {

    interface Callback {

        void onCancelled();

        void onSessionOpened(Session result);
    }

    interface ResponseCallback {

        void onCancelled();

        void onCompleted(Response response);
    }

    /**
     * Open facebook session.
     */
    void open(WorksFacebook.Callback callback);

    /**
     * Validate current session.
     */
    void validate(WorksFacebook.Callback callback);

    void request(Request request, WorksFacebook.ResponseCallback callback);

    void requestMe(WorksFacebook.ResponseCallback callback);

    void close();
}
