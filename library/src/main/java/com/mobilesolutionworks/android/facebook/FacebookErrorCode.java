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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunarta on 7/9/14.
 */
public enum FacebookErrorCode {

    INVALID_SESSION_FACEBOOK_ERROR_CODE(190),

    // error
    API_SESSION_EXPIRED(102),
    API_UNKNOWN(1),
    API_SERVICE(2),
    API_TOO_MANY_CALLS(4),
    API_USER_TOO_MANY_CALLS(17),
    API_PERMISSION_DENIED(10),
    APPLICATION_LIMIT_REACHED(341),
    DUPLICATE_POST(506),
    ERROR_POSTING_LINK(1609005),

    // sub error
    APP_NOT_INSTALLED(458),
    USER_CHECKPOINTED(459),
    PASSWORD_CHANGED(460),
    EXPIRED(463),
    UNCONFIRMED_USER(464),
    INVALID_ACCESS_TOKEN(467),
    UNKNOWN(0xffff);

    public final int code;

    FacebookErrorCode(int code) {
        this.code = code;
    }

    public static FacebookErrorCode get(int code) {
        FacebookErrorCode errorCode = sMap.get(code);
        return errorCode == null ? UNKNOWN : errorCode;
    }

    static Map<Integer, FacebookErrorCode> sMap;

    static {
        sMap = new HashMap<Integer, FacebookErrorCode>();
        for (FacebookErrorCode codes : FacebookErrorCode.values()) {
            sMap.put(codes.code, codes);
        }
    }
}
