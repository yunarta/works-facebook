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

package com.mobilesolutionworks.android.facebook.test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.mobilesolutionworks.android.facebook.FacebookPluginFragment;
import com.mobilesolutionworks.android.facebook.WorksFacebook;

/**
 * Created by yunarta on 7/9/14.
 */
public class FacebookTestActivity extends FragmentActivity implements View.OnClickListener {

    WorksFacebook mFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_test);

        Session.openActiveSessionFromCache(this);

        FacebookPluginFragment fragment = new FacebookPluginFragment();
        mFacebook = fragment;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragment, "facebook").commit();

        mFacebook.validate(new StatusCallback());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open: {
                mFacebook.open(new StatusCallback());
                break;
            }

            case R.id.btn_validate: {
                mFacebook.validate(new StatusCallback());
                break;
            }

            case R.id.btn_close: {
                mFacebook.close();
                break;
            }

            case R.id.btn_request1: {
                Request request = new Request(null, "/me", null, HttpMethod.GET, null);
                mFacebook.request(request, new WorksFacebook.ResponseCallback() {
                    @Override
                    public void onCancelled() {

                    }


                    @Override
                    public void onCompleted(Response response) {
                        Toast.makeText(FacebookTestActivity.this, "test response = " + response, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }

            case R.id.btn_request2: {
                mFacebook.requestMe(new WorksFacebook.ResponseCallback() {
                    @Override
                    public void onCancelled() {

                    }


                    @Override
                    public void onCompleted(Response response) {
                        Toast.makeText(FacebookTestActivity.this, "test response = " + response, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }


    private class StatusCallback implements WorksFacebook.Callback {

        @Override
        public void onCancelled() {
            TextView textView = (TextView) findViewById(R.id.status);
            textView.setText("Facebook Disonnected");
        }

        @Override
        public void onSessionOpened(Session session) {
            TextView textView = (TextView) findViewById(R.id.status);
            if (session != null) {
                textView.setText(session.isOpened() ? "Facebook Connected" : "Facebook Disconnected");
            } else {
                textView.setText("Facebook Disconnected");
            }
        }
    }
}
