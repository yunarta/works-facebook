package com.mobilesolutionworks.android.facebook.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.mobilesolutionworks.android.facebook.FacebookPluginFragment;
import com.mobilesolutionworks.android.facebook.WorksFacebook;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yunarta on 7/9/14.
 */
public class FacebookChildFragmentTestActivity extends FragmentActivity {

    protected Map<Integer, FragmentTrackInfo> mTrackInfoMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mTrackInfoMap = new HashMap<Integer, FragmentTrackInfo>();

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new TestFragment()).commit();
        }
    }

    public static class TestFragment extends Fragment implements View.OnClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.add(new FacebookPluginFragment(), "facebook").commit();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            view.findViewById(R.id.btn_start).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new FacebookFragment()).commit();
        }
    }

    public static class FacebookFragment extends Fragment implements View.OnClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Session.openActiveSessionFromCache(getActivity());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_facebook_test, null);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            view.findViewById(R.id.btn_open).setOnClickListener(this);
            view.findViewById(R.id.btn_validate).setOnClickListener(this);
            view.findViewById(R.id.btn_close).setOnClickListener(this);
            view.findViewById(R.id.btn_request1).setOnClickListener(this);
            view.findViewById(R.id.btn_request2).setOnClickListener(this);
            view.findViewById(R.id.btn_request3).setOnClickListener(this);
        }

        @Override
        public void onStart() {
            super.onStart();

            WorksFacebook facebook = (WorksFacebook) getFragmentManager().findFragmentByTag("facebook");
            facebook.validate(new StatusCallback());
        }

        @Override
        public void onClick(View view) {
            WorksFacebook facebook = (WorksFacebook) getFragmentManager().findFragmentByTag("facebook");
            switch (view.getId()) {
                case R.id.btn_open: {
                    facebook.open(new StatusCallback());
                    break;
                }

                case R.id.btn_validate: {
                    facebook.validate(new StatusCallback());
                    break;
                }

                case R.id.btn_close: {
                    facebook.close();
                    break;
                }

                case R.id.btn_request1: {
                    Request request = new Request(null, "/804889722866833/likes", null, HttpMethod.POST, null);
                    facebook.publishRequest(request, new WorksFacebook.ResponseCallback() {
                        @Override
                        public void onCancelled() {

                        }

                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getActivity(), "test response = " + response, Toast.LENGTH_SHORT).show();
                        }
                    }, "publish_actions");
                    break;
                }

                case R.id.btn_request2: {
                    Request request = new Request(null, "/804889722866833/likes", null, HttpMethod.GET, null);
                    facebook.readRequest(request, new WorksFacebook.ResponseCallback() {
                        @Override
                        public void onCancelled() {

                        }

                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getActivity(), "test response = " + response, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }

                case R.id.btn_request3: {
                    facebook.requestMe(new WorksFacebook.ResponseCallback() {
                        @Override
                        public void onCancelled() {

                        }


                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(getActivity(), "test response = " + response, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }
            }
        }

        private class StatusCallback implements WorksFacebook.Callback {

            @Override
            public void onCancelled() {
                TextView textView = (TextView) getView().findViewById(R.id.status);
                textView.setText("Facebook Disonnected");
            }

            @Override
            public void onSessionOpened(Session session) {
                TextView textView = (TextView) getView().findViewById(R.id.status);
                if (session != null) {
                    textView.setText(session.isOpened() ? "Facebook Connected" : "Facebook Disconnected");
                } else {
                    textView.setText("Facebook Disconnected");
                }
            }
        }
    }

    /**
     * Called by Fragment.startActivityForResult() to implement its behavior.
     */
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode)
    {
//        if (fragment instanceof WaitingForResult)
        {
            FragmentManager fm = fragment.getFragmentManager();
            int id = fm.getFragments().indexOf(fragment);

            FragmentTrackInfo info = new FragmentTrackInfo(id);

            Fragment parent = fragment;
            while ((parent = parent.getParentFragment()) != null)
            {
                fm = parent.getFragmentManager();
                id = fm.getFragments().indexOf(parent);
                info = new FragmentTrackInfo(id, info);
            }

            mTrackInfoMap.put(requestCode, info);
        }

        super.startActivityForResult(intent, requestCode);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        FragmentTrackInfo trackInfo = mTrackInfoMap.remove(requestCode & 0xffff);
        if (trackInfo != null)
        {
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.getFragments().get(trackInfo.mId);

            if (trackInfo.mChild != null)
            {
                FragmentTrackInfo childInfo = trackInfo;
                Fragment child = fragment;

                while ((childInfo = childInfo.mChild) != null) {
                    fm = child.getChildFragmentManager();
                    child = fm.getFragments().get(childInfo.mId);
                }

                fragment = child;
            }

            fragment.onActivityResult(requestCode, resultCode, data);
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public class FragmentTrackInfo
    {

        public FragmentTrackInfo mChild;

        public int mId;

        public FragmentTrackInfo(int id)
        {
            mId = id;
        }

        public FragmentTrackInfo(int id, FragmentTrackInfo info)
        {
            mId = id;
            mChild = info;
        }

        public int getId()
        {
            int requestId = 0;
            if (mChild != null)
            {
                requestId |= mChild.getId() << 8;
            }

            requestId |= mId;
            return requestId;
        }
    }

    interface WaitingForResult {

    }
}
