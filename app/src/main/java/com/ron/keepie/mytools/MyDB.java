package com.ron.keepie.mytools;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ron.keepie.callbacks.Callback_find_account;
import com.ron.keepie.callbacks.Callback_message;
import com.ron.keepie.objects.Message;

import java.util.HashMap;

public class MyDB {
    public static final String ACCOUNTS_KEEPIE = "ACCOUNTS_KEEPIE";
    public static final String CHATS = "CHATS";

    private static MyDB _instance = new MyDB();
    private FirebaseDatabase database;
    private DatabaseReference refAccounts;
    private DatabaseReference refChats;
    private Callback_find_account callback_find_user;
   // private Callback_creating_account callback_account_creating;
    private Callback_message callback_get_messages;
    //private CallBack_get_all_chats callBack_get_all_chats;

    private MyDB() {
        database = FirebaseDatabase.getInstance("https://keepie-cb5ec-default-rtdb.europe-west1.firebasedatabase.app");
        refAccounts = database.getReference(ACCOUNTS_KEEPIE);
        refChats = database.getReference(CHATS);
    }

    public static MyDB getInstance() {
        return _instance;
    }

    public MyDB setCallback_find_user(Callback_find_account callback_find_user) {
        this.callback_find_user = callback_find_user;
        return this;
    }


//    public MyDB setCallback_account_creating(Callback_creating_account callback_account_creating) {
//        this.callback_account_creating = callback_account_creating;
//        return this;
//    }

    public MyDB setCallBack_get_all_chats(Callback_message callback_get_messages) {
        this.callback_get_messages = callback_get_messages;
        return this;
    }

    public void get_all_messages(String chat_id) {
        if (this.callback_get_messages != null) {
            refChats.child(chat_id).child("messages").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (callback_get_messages != null) {
                        GenericTypeIndicator<HashMap<String, Message>> dataType = new GenericTypeIndicator<HashMap<String, Message>>() {
                        };
                        HashMap<String, Message> stringMessageHashMap = dataSnapshot.getValue(dataType);
                        callback_get_messages.get_all_msgs(stringMessageHashMap);
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    callback_get_messages.error();
                }
            });
        }
    }


//    public void isAccountExists(String phoneID) {
//        if (this.callback_find_user != null) {
//            refAccounts.child(phoneID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    MyUser myUser = null;
//                    myUser = dataSnapshot.getValue(MyUser.class);
//                    callback_find_user.account_found(myUser);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    callback_find_user.error();
//                }
//            });
//        }
//    }


//    public void create_account(MyUser myUser) {
//        if (this.callback_account_creating != null) {
//            refAccounts.child(myUser.getPhone()).setValue(myUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        callback_account_creating.account_created(myUser);
//                        return;
//                    }
//                    callback_account_creating.error();
//                }
//            });
//        }
//    }



    public void clear_callbacks() {
        callback_find_user = null;
       // callback_account_creating = null;
        callback_get_messages = null;

    }

}