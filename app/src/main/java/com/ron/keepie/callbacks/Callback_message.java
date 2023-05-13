package com.ron.keepie.callbacks;

import com.ron.keepie.whatsup_objects.Message;

import java.util.HashMap;

public interface Callback_message {
    void get_all_msgs(HashMap<String, Message> stringMessageHashMap);
    void error();
}
