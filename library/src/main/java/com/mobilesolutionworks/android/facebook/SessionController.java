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

import android.content.Context;
import android.support.annotation.NonNull;
import com.facebook.Session;

/**
 * Created by yunarta on 7/9/14.
 */
public class SessionController {

    protected static SessionController sInstance;

    public static SessionController getInstance(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new SessionController(context);
        }

        return sInstance;
    }

    protected final Context mContext;

    protected SessionController(Context context) {
        mContext = context;
    }

    public Session getSession() {
        Session session = Session.getActiveSession();
        if (session == null) {
            session = Session.openActiveSessionFromCache(mContext);
            if (session == null) {
                session = new Session(mContext);
            }

            Session.setActiveSession(session);
        }

        return session;
    }
}
