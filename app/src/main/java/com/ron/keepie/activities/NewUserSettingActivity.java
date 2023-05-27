package com.ron.keepie.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.ron.keepie.R;
import com.ron.keepie.activities.adult.FollowActivity;
import com.ron.keepie.activities.adult.NotificationsActivity;
import com.ron.keepie.activities.child.SearchConnectionsActivity;
import com.ron.keepie.callbacks.Callback_upload_img;
import com.ron.keepie.callbacks.Callback_user_status;
import com.ron.keepie.dialogs.ImageDialog;
import com.ron.keepie.mytools.DataManager;
import com.ron.keepie.mytools.MyDB;
import com.ron.keepie.mytools.MyStorage;
import com.ron.keepie.objects.KeepieUser;
import com.ron.keepie.server.UserServerCommunicator;
import com.ron.keepie.server.server_callbacks.SetUserCallback;
import com.ron.keepie.whatsup_objects.UserStatus;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

public class NewUserSettingActivity extends AppCompatActivity {
    private MaterialButton profile_BTN_save;
    private TextInputEditText profile_EDT_name;
    private CircleImageView profile_IMG_photo;
    private MaterialTextView profile_LBL_phone;
    private LottieAnimationView profile_Lottie_photo;
    private FloatingActionButton fab_back;
    private KeepieUser myUser = null;
    private UserStatus userStatus = null;
    private String phone = "";
    private String setting_type = "";
    private String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_new_user);
        UserServerCommunicator.getInstance().setSetUserCallback(setUserCallback);
        MyDB.getInstance().setCallback_user_status(callback_user_status);
        MyStorage.getInstance().setCallback_upload_profile_img(callback_upload_img);
        setting_type = getIntent().getStringExtra(DataManager.SETTING_TYPE);
        findViews();
        init_page();
    }

    private void init_page() {
        phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        if (phone == null) {
            Toast.makeText(this, "Authentication error...", Toast.LENGTH_SHORT).show();
            go_next(Enter_app_activity.class);
        }
        MyDB.getInstance().get_user_status(phone);
        profile_LBL_phone.setText(phone);
        profile_EDT_name.setText(phone);
    }


    private void findViews() {
        fab_back = findViewById(R.id.fab_back);
        profile_BTN_save = findViewById(R.id.profile_BTN_save);
        profile_IMG_photo = findViewById(R.id.profile_IMG_photo);
        profile_LBL_phone = findViewById(R.id.profile_LBL_phone);
        profile_EDT_name = findViewById(R.id.profile_EDT_name);
        profile_Lottie_photo = findViewById(R.id.profile_Lottie_photo);

        profile_IMG_photo.setOnClickListener(view -> {
            new ImageDialog().show(this, phone, imageUri);
        });
        profile_Lottie_photo.setOnClickListener(view -> add_photo());
        fab_back.setOnClickListener(view -> go_next(Register_activity.class));
        profile_BTN_save.setOnClickListener(onClickListenerSave);
    }


    private <T extends AppCompatActivity> void go_next(Class<T> nextActivity) {
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }

    private View.OnClickListener onClickListenerSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (profile_EDT_name.getText() == null || profile_EDT_name.getText().toString().isEmpty()) {
                Toast.makeText(NewUserSettingActivity.this, "Name cannot be empty...", Toast.LENGTH_SHORT).show();
                return;
            }
            myUser = new KeepieUser(setting_type.equals(DataManager.SETTING_TYPE_CHILD),
                    profile_EDT_name.getText().toString(),
                    profile_LBL_phone.getText().toString(),
                    imageUri);
            UserServerCommunicator.getInstance().createUser(myUser);

        }
    };

    private SetUserCallback setUserCallback = new SetUserCallback() {
        @Override
        public void user_merged() {
            DataManager.getDataManager().set_account(myUser);
            Toast.makeText(NewUserSettingActivity.this, "Done!", Toast.LENGTH_SHORT).show();

            if (myUser.isIs_child()) {
                go_next(SearchConnectionsActivity.class);
            } else {
                go_next(FollowActivity.class);
            }


        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(NewUserSettingActivity.this, "" + status_code + " " + info, Toast.LENGTH_LONG).show();
        }
    };
    private Callback_user_status callback_user_status = new Callback_user_status() {
        @Override
        public void get_user_status(UserStatus userStatus) {
            imageUri = userStatus.getImg();
            NewUserSettingActivity.this.userStatus = userStatus;

            if (!userStatus.getImg().isEmpty())
                Glide.with(NewUserSettingActivity.this).load(userStatus.getImg()).placeholder(R.drawable.ic_user).into(profile_IMG_photo);
        }

        @Override
        public void not_found() {
            if (setting_type.equals(DataManager.SETTING_TYPE_CHILD)) {
                Toast.makeText(NewUserSettingActivity.this, "Child account must have a WhatsUp account... please create one", Toast.LENGTH_SHORT).show();
                profile_BTN_save.setText("Go Back");
                profile_BTN_save.setOnClickListener(view -> go_next(Enter_app_activity.class));
            }
        }

        @Override
        public void error() {
            Toast.makeText(NewUserSettingActivity.this, "Some Error occurred please check your internet...", Toast.LENGTH_SHORT).show();

        }
    };

    private void add_photo() {
        ImagePicker.Companion.with(this)
                .crop()
                .cropOval()
                .maxResultSize(512, 512, true)
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog((Function1) (new Function1() {
                    public Object invoke(Object var1) {
                        this.invoke((Intent) var1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull Intent it) {
                        Intrinsics.checkNotNullParameter(it, "it");
                        launcher.launch(it);
                    }
                }));
    }

    private ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResult result) -> {
        if (result.getResultCode() == RESULT_OK) {
            Uri uri = result.getData().getData();
            profile_IMG_photo.setImageURI(uri);
            imageUri = uri.toString();
            MyStorage.getInstance().uploadImageProfile(phone, uri);

        } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "image upload failed please, try again", Toast.LENGTH_SHORT).show();
        }
    });
    private Callback_upload_img callback_upload_img = new Callback_upload_img() {
        @Override
        public void img_uploaded(String image_uri) {

            if (image_uri != null && !image_uri.isEmpty()) {
                NewUserSettingActivity.this.imageUri = image_uri;
            }
        }

        @Override
        public void failed() {
            Toast.makeText(NewUserSettingActivity.this, "Image upload failed, please try again", Toast.LENGTH_SHORT).show();
        }
    };

}