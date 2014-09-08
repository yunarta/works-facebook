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

package com.mobilesolutionworks.android.facebook.bolts;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by yunarta on 9/9/14.
 */
public abstract class Success<TTaskResult> implements Continuation<TTaskResult, Void> {

    @Override
    public Void then(Task<TTaskResult> task) throws Exception {
        if (task.isCompleted()) {
            success(task);
        }

        return null;
    }

    public abstract void success(Task<TTaskResult> task) throws Exception;
}
