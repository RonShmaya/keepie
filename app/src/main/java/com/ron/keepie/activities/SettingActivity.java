package com.ron.keepie.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.ron.keepie.activities.adult.ShieldActivity;
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


public class SettingActivity extends AppCompatActivity {
    private CoordinatorLayout container_adult;
    private CoordinatorLayout container_child;
    private MaterialButton profile_BTN_save;
    private BottomNavigationView nav_view;
    private TextInputEditText profile_EDT_name;
    private FloatingActionButton fab_logout;
    private FloatingActionButton fab_home;
    private CircleImageView profile_IMG_photo;
    private MaterialTextView profile_LBL_phone;
    private LottieAnimationView profile_Lottie_photo;


    private KeepieUser myUser = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        UserServerCommunicator.getInstance().setSetUserCallback(setUserCallback);
        MyDB.getInstance().setCallback_user_status(callback_user_status);
        MyStorage.getInstance().setCallback_upload_profile_img(callback_upload_img);
        findViews();
        init_page();
    }


    private void init_page() {
        myUser = DataManager.getDataManager().getMy_current_user();
        if (myUser == null) {
            Toast.makeText(this, "Some Error Occurred...", Toast.LENGTH_SHORT).show();
            profile_BTN_save.setVisibility(View.GONE);
            return;
        }
        if (myUser.getImage() == null || myUser.getImage().isEmpty())
            MyDB.getInstance().get_user_status(myUser.getPhone());
        else
            Glide.with(SettingActivity.this).load(myUser.getImage()).placeholder(R.drawable.ic_user).into(profile_IMG_photo);

        profile_EDT_name.setText(myUser.getName());
        profile_LBL_phone.setText(myUser.getPhone());
        if (myUser.isIs_child()) {
            init_toolbar_child();
        } else {
            init_toolbar_adult();
        }
    }



    private void findViews() {
        container_adult = findViewById(R.id.container_adult);
        container_child = findViewById(R.id.container_child);
        nav_view = findViewById(R.id.nav_view);
        fab_logout = findViewById(R.id.fab_logout);
        fab_home = findViewById(R.id.fab_home);
        profile_BTN_save = findViewById(R.id.profile_BTN_save);
        profile_IMG_photo = findViewById(R.id.profile_IMG_photo);
        profile_LBL_phone = findViewById(R.id.profile_LBL_phone);
        profile_EDT_name = findViewById(R.id.profile_EDT_name);
        profile_Lottie_photo = findViewById(R.id.profile_Lottie_photo);

        fab_logout.setOnClickListener( v -> {
            FirebaseAuth.getInstance().signOut();
            DataManager.getDataManager().clean();
            go_next(Enter_app_activity.class);
        });

        profile_IMG_photo.setOnClickListener(view -> new ImageDialog().show(this, myUser.getPhone(), myUser.getImage()));
        profile_Lottie_photo.setOnClickListener(view -> add_photo());
        profile_BTN_save.setOnClickListener(onClickListenerSave);
    }
    private void init_toolbar_child() {
        container_child.setVisibility(View.VISIBLE);
        fab_home.setOnClickListener(v -> go_next(SearchConnectionsActivity.class));

    }
    private void init_toolbar_adult() {
        container_adult.setVisibility(View.VISIBLE);
        nav_view.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_followers) {
                go_next(FollowActivity.class);
            } else if (item.getItemId() == R.id.action_profile) {
                go_next(SettingActivity.class);
            } else if (item.getItemId() == R.id.action_notify) {
                go_next(NotificationsActivity.class);
            } else if (item.getItemId() == R.id.action_test) {
                 go_next(ShieldActivity.class);

            }
            return false;
        });

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
                Toast.makeText(SettingActivity.this, "Name cannot be empty...", Toast.LENGTH_SHORT).show();
                return;
            }
            myUser.setName(profile_EDT_name.getText().toString());
            UserServerCommunicator.getInstance().updateUser(myUser);
        }
    };

    private SetUserCallback setUserCallback = new SetUserCallback() {
        @Override
        public void user_merged() {
            DataManager.getDataManager().set_account(myUser);
            Toast.makeText(SettingActivity.this, "Done!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void failed(int status_code, String info) {
            Toast.makeText(SettingActivity.this, "Some Error occurred during searching account " + status_code + " " + info, Toast.LENGTH_SHORT).show();
        }
    };
    private Callback_user_status callback_user_status = new Callback_user_status() {
        @Override
        public void get_user_status(UserStatus userStatus) {
            if (!userStatus.getImg().isEmpty()) {
                myUser.setImage(userStatus.getImg());
                UserServerCommunicator.getInstance().updateUser(myUser);
                DataManager.getDataManager().set_account(myUser);
            }
            Glide.with(SettingActivity.this).load(myUser.getImage()).placeholder(R.drawable.ic_user).into(profile_IMG_photo);

        }

        @Override
        public void not_found() {
            if (myUser.isIs_child()) {
                Toast.makeText(SettingActivity.this, "Child account must have a WhatsUp account... please create one", Toast.LENGTH_SHORT).show();
                profile_BTN_save.setText("Go Back");
                profile_BTN_save.setOnClickListener(view -> go_next(Enter_app_activity.class));
            }
        }

        @Override
        public void error() {
            Toast.makeText(SettingActivity.this, "Some Error occurred please check your internet...", Toast.LENGTH_SHORT).show();

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
            MyStorage.getInstance().uploadImageProfile(myUser.getPhone(), uri);

        } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "image upload failed please, try again", Toast.LENGTH_SHORT).show();
        }
    });
    private Callback_upload_img callback_upload_img = new Callback_upload_img() {
        @Override
        public void img_uploaded(String image_uri) {
            if (image_uri != null && !image_uri.isEmpty()) {
                myUser.setImage(image_uri);
            }
        }

        @Override
        public void failed() {
            Toast.makeText(SettingActivity.this, "Image upload failed, please try again", Toast.LENGTH_SHORT).show();
        }
    };

}