/*
 * Copyright (C) 2008 ZXing authors
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

package com.zhephone.hotspot.app.ui.chat;

import android.os.Handler;
import android.os.Message;


/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class GroupListHandler extends Handler {

	private static final String TAG = GroupListHandler.class.getSimpleName();

	private final GroupListActivity activity;
	public static final int FRESH_GROUP_LIST = 1001;

	public GroupListHandler(GroupListActivity activity) {
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case FRESH_GROUP_LIST:
			activity.loadAllGroupByUserId();
			break;
		}
	}

}
