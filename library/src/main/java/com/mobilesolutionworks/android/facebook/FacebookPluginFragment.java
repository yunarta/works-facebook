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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import bolts.Capture;
import bolts.Continuation;
import bolts.Task;
import com.facebook.*;

import java.util.concurrent.Callable;

/**
 * Created by yunarta on 7/9/14.
 */
public class FacebookPluginFragment extends Fragment implements WorksFacebook {

    public static final int REQUEST_CODE = 0xfb;

    UiLifecycleHelper mLifecycleHelper;

//    Session mSession;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

//        if (BuildConfig.DEBUG) {
        Settings.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
        Settings.addLoggingBehavior(LoggingBehavior.CACHE);
        Settings.addLoggingBehavior(LoggingBehavior.DEVELOPER_ERRORS);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_RAW_RESPONSES);
        Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);
//        }

        Session.openActiveSessionFromCache(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLifecycleHelper = new UiLifecycleHelper(getActivity(), null);
        mLifecycleHelper.onCreate(savedInstanceState);

//        SessionController instance = SessionController.getInstance(getActivity());
//        Session session = instance.getSession();
//
//        Log.d(BuildConfig.MODULE_NAME, "session = " + session);
//        Log.d(BuildConfig.MODULE_NAME, "session.getAccessToken() = " + session.getAccessToken());
//        Log.d(BuildConfig.MODULE_NAME, "session.getApplicationId() = " + session.getApplicationId());
//        Log.d(BuildConfig.MODULE_NAME, "session.getAuthorizationBundle() = " + session.getAuthorizationBundle());
//        Log.d(BuildConfig.MODULE_NAME, "session.getState() = " + session.getState());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mLifecycleHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycleHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLifecycleHelper.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        mLifecycleHelper.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifecycleHelper.onDestroy();
    }

    protected Session getSession() {
        return Session.getActiveSession();
    }

    @Override
    public void open(@NonNull final WorksFacebook.Callback callback) {
        checkSession().continueWithTask(new Continuation<Response, Task<Session>>() {
            @Override
            public Task<Session> then(Task<Response> responseTask) throws Exception {
                Response result = responseTask.getResult();
                return openSession(Session.getActiveSession());
            }
        }).continueWith(new Continuation<Session, Void>() {
            @Override
            public Void then(final Task<Session> task) throws Exception {
                if (task.isFaulted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCancelled();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSessionOpened(task.getResult());
                        }
                    });
                }
                return null;
            }
        });
    }

    @Override
    public void validate(@NonNull final WorksFacebook.Callback callback) {
        checkSession().continueWith(new Continuation<Response, Void>() {
            @Override
            public Void then(Task<Response> responseTask) throws Exception {
                Response result = responseTask.getResult();
                final Session session = Session.getActiveSession();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSessionOpened(session);
                    }
                });
//                if (session == null || session.isClosed()) {
//                    callback.onSessionOpened(session);
//                } else if (session.isOpened()) {

//                }

                return null;
            }
        });
    }

    @Override
    public void request(final Request request, @NonNull final WorksFacebook.ResponseCallback callback) {
        checkSession().continueWithTask(new Continuation<Response, Task<Session>>() {
            @Override
            public Task<Session> then(Task<Response> responseTask) throws Exception {
                Response result = responseTask.getResult();
                return openSession(Session.getActiveSession());
            }
        }).continueWithTask(new Continuation<Session, Task<Response>>() {
            @Override
            public Task<Response> then(Task<Session> task) throws Exception {
                if (task.isFaulted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCancelled();
                        }
                    });

                    throw new IllegalStateException();
                } else {
                    return makeRequest(task.getResult(), request);
                }
            }
        }).continueWith(new Continuation<Response, Void>() {
            @Override
            public Void then(final Task<Response> task) throws Exception {
                if (task.isFaulted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCancelled();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(task.getResult());
                        }
                    });
                }
                return null;
            }
        });
    }

    public void requestMe(@NonNull final WorksFacebook.ResponseCallback callback) {
        final Request request = Request.newMeRequest(null, null);
        final Capture<Response> successfulSaveCount = new Capture<Response>();

        checkSession().continueWithTask(new Continuation<Response, Task<Session>>() {
            @Override
            public Task<Session> then(Task<Response> responseTask) throws Exception {
                final Response response = responseTask.getResult();
                if (response != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(response);
                        }
                    });

                    throw new IllegalStateException();
                }

                return openSession(null);
            }
        }).continueWithTask(new Continuation<Session, Task<Response>>() {
            @Override
            public Task<Response> then(Task<Session> task) throws Exception {
                if (task.isFaulted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCancelled();
                        }
                    });
                    throw new IllegalStateException();
                } else {
                    return makeRequest(task.getResult(), request);
                }
            }
        }).continueWith(new Continuation<Response, Void>() {
            @Override
            public Void then(final Task<Response> task) throws Exception {
                if (task.isFaulted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCancelled();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCompleted(task.getResult());
                        }
                    });
                }

                return null;
            }
        });
    }

    @Override
    public void close() {
        getSession().closeAndClearTokenInformation();
    }

    private void toast(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Task<Response> checkSession() {
        return Task.callInBackground(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                Session session = getSession();
                if (session != null && session.isOpened()) {
                    Response response = Request.newMeRequest(session, null).executeAndWait();
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        FacebookErrorCode code = FacebookErrorCode.get(error.getErrorCode());
                        if (code == FacebookErrorCode.INVALID_SESSION_FACEBOOK_ERROR_CODE) {
                            // session invalidated here
                            return null;
                        }
                    }

                    return response;
                }

                return null;
            }
        });
    }

    private Task<Session> openSession(Session session) {
        final Task<Session>.TaskCompletionSource source = Task.<Session>create();

        if (session == null || session.isClosed()) {
            session = new Session(getActivity());
            Session.setActiveSession(session);
        }

        if (session.isOpened()) {
            source.setResult(session);
        } else {
            Session.OpenRequest request = new Session.OpenRequest(FacebookPluginFragment.this);
            request.setPermissions("email");
            request.setRequestCode(REQUEST_CODE);
            request.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            request.setCallback(new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    if (state.isClosed()) {
                        source.trySetError(exception);
                    } else if (state.isOpened()) {
                        source.trySetResult(session);
                    }
                }
            });

            session.openForRead(request);
        }

        return source.getTask();
    }

    private Task<Response> makeRequest(final Session session, final Request request) {
        return Task.callInBackground(new Callable<Response>() {
            @Override
            public Response call() throws Exception {
                request.setSession(session);
                Response response = request.executeAndWait();

                FacebookRequestError error = response.getError();
                if (error != null) {
                    FacebookErrorCode code = FacebookErrorCode.get(error.getErrorCode());
                    if (code == FacebookErrorCode.INVALID_SESSION_FACEBOOK_ERROR_CODE) {
                        // session invalidated here
                        throw new IllegalStateException();
                    }
                }

                return response;
            }
        });
    }
}