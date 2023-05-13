package com.ron.keepie.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.ron.keepie.R;
import com.ron.keepie.adapters.SpeackingAdapter;
import com.ron.keepie.callbacks.Callback_message;
import com.ron.keepie.mytools.MyDB;
import com.ron.keepie.whatsup_objects.Chat;
import com.ron.keepie.whatsup_objects.Message;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ListenChatsActivity extends AppCompatActivity {
    private final int PHONE_INPUT_SIZE = 9;
    private TextInputEditText search_TIETL_phone1;
    private TextInputEditText search_TIETL_phone2;
    private TextInputEditText search_TIETL_keywords;
    private FloatingActionButton search_BTN_add_key;
    private SpeackingAdapter speackingAdapter;
    private MaterialTextView search_LBL_keyword;
    private MaterialButton search_BTN_submit;
    private RecyclerView search_LST;
    private ArrayList<String> keywords_lst = new ArrayList<>();
    private ArrayList<Message> chat_messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_chats);
        MyDB.getInstance().setCallBack_get_all_chats(get_chat_msgs);
        findViews();
        init_actions();
        speackingAdapter = new SpeackingAdapter(this, chat_messages, "","");
        search_LST.setAdapter(speackingAdapter);
    }

    private void init_actions() {
        search_BTN_add_key.setOnClickListener(click_add_keyword);
        search_BTN_submit.setOnClickListener(click_search);
    }

    private void findViews() {
        search_TIETL_phone1 = findViewById(R.id.search_TIETL_phone1);
        search_TIETL_phone2 = findViewById(R.id.search_TIETL_phone2);
        search_TIETL_keywords = findViewById(R.id.search_TIETL_keywords);
        search_BTN_add_key = findViewById(R.id.search_BTN_add_key);
        search_LBL_keyword = findViewById(R.id.search_LBL_keyword);
        search_BTN_submit = findViewById(R.id.search_BTN_submit);
        search_LST = findViewById(R.id.search_LST);
    }

    private View.OnClickListener click_add_keyword = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            add_key_word();
        }
    };
    private View.OnClickListener click_search = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            search();
        }
    };

    private void search() {
        if (keywords_lst.isEmpty()) {
            Toast.makeText(this, "You need to add at least one keyword before", Toast.LENGTH_SHORT).show();
            return;
        }
        String phone1 = search_TIETL_phone1.getText().toString();
        String phone2 = search_TIETL_phone2.getText().toString();
        if (!is_phone_input_ok(phone1))
            return;
        if (!is_phone_input_ok(phone2))
            return;
        phone1 = "+972"+phone1;
        phone2 = "+972"+phone2;
        speackingAdapter.setPhone1(phone1).setPhone2(phone2);
        String chat_id = Chat.make_chat_id(phone1,phone2);
        MyDB.getInstance().get_all_messages(chat_id);

    }

    private boolean is_phone_input_ok(String phone_input) {
        if (phone_input == null || phone_input.isEmpty()) {
            Toast.makeText(this, "Phone cannot be empty", Toast.LENGTH_SHORT).show();
            clear_keywords();
            return false;
        }
        if (phone_input.length() != PHONE_INPUT_SIZE) {
            Toast.makeText(this, "Phone input size is uncorrected", Toast.LENGTH_SHORT).show();
            clear_keywords();
            return false;
        }
        if (!phone_input.matches("\\d+")) {
            Toast.makeText(this, "Phone input must contains only digits", Toast.LENGTH_SHORT).show();
            clear_keywords();
            return false;
        }

        return true;
    }

    private void clear_keywords() {
        search_LBL_keyword.setText("");
        search_TIETL_keywords.setText("");
        keywords_lst.clear();
        chat_messages.clear();
        runOnUiThread(() ->  speackingAdapter.notifyDataSetChanged());
    }
    private void clear_keywords_without_adapter() {
        search_LBL_keyword.setText("");
        keywords_lst.clear();
        search_TIETL_keywords.setText("");
    }

    private void add_key_word() {
        String keyword = search_TIETL_keywords.getText().toString();
        if (keyword == null || keyword.isEmpty()) {
            Toast.makeText(this, "Keyword cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        keywords_lst.add(keyword);
        StringBuilder keys_str = new StringBuilder();
        for (int i = 0; i < keywords_lst.size() - 1; i++) {
            keys_str.append(keywords_lst.get(i) + ", ");
        }
        keys_str.append(keywords_lst.get(keywords_lst.size() - 1));
        search_LBL_keyword.setText(keys_str.toString());
        search_TIETL_keywords.setText("");
    }

    private Callback_message get_chat_msgs = new Callback_message() {
        @Override
        public void get_all_msgs(HashMap<String, Message> stringMessageHashMap) {
            if (stringMessageHashMap == null) {
                Toast.makeText(ListenChatsActivity.this, "There isn't chat between those persons", Toast.LENGTH_SHORT).show();
                clear_keywords();
                return;
            }
            ArrayList<Message> tmp_msgs = new ArrayList<>(stringMessageHashMap.values());
            chat_messages.clear();
            for (int i = 0; i < tmp_msgs.size(); i++) {
                Log.d("MyLog",tmp_msgs.get(i).getContent());
                if (is_msg_contain_keyword(tmp_msgs.get(i))) {
                    chat_messages.add(tmp_msgs.get(i));
                }
            }
            chat_messages.sort(msg_comparator);
            if(chat_messages.isEmpty()){
                Toast.makeText(ListenChatsActivity.this, "Chat founded without a matching keywords", Toast.LENGTH_SHORT).show();
            }

            runOnUiThread(() ->  speackingAdapter.notifyDataSetChanged());

            clear_keywords_without_adapter();
        }

        @Override
        public void error() {
            Toast.makeText(ListenChatsActivity.this, "Some Error occurreded, please check your internet", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean is_msg_contain_keyword(Message message) {
        for (int i = 0; i <keywords_lst.size(); i++) {
            if(message.getContent().contains(keywords_lst.get(i))){
                return true;
            }
        }
        return false;
    }
    private Comparator<Message> msg_comparator = new Comparator<Message>() {
        @Override
        public int compare(Message message, Message t1) {
            if (message.get_msg_calender().before(t1.get_msg_calender())) {
                return -1;
            }
            return 1;
        }
    };
}