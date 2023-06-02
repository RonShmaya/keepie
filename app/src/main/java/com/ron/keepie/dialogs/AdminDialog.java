package com.ron.keepie.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ron.keepie.R;
import com.ron.keepie.server.RetrofitService;

public class AdminDialog {

        private TextInputLayout dialogAdmin_TIL_IP;
        private TextInputEditText dialogAdmin_TIETL_IP;
        private TextInputLayout dialogAdmin_TIL_PORT;
        private TextInputEditText dialogAdmin_TIETL_PORT;
        private MaterialButton dialogAdmin_BTN_create;

        public void show(Context context){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_admin);
            findViews(dialog);
            dialog.show();
        }

        private void findViews(Dialog dialog) {
            dialogAdmin_TIL_IP = dialog.findViewById(R.id.dialogAdmin_TIL_IP);
            dialogAdmin_TIETL_IP = dialog.findViewById(R.id.dialogAdmin_TIETL_IP);
            dialogAdmin_TIL_PORT = dialog.findViewById(R.id.dialogAdmin_TIL_PORT);
            dialogAdmin_TIETL_PORT = dialog.findViewById(R.id.dialogAdmin_TIETL_PORT);
            dialogAdmin_BTN_create = dialog.findViewById(R.id.dialogAdmin_BTN_create);


            dialogAdmin_BTN_create.setOnClickListener(view ->
                    RetrofitService.getInstance().initializeRetrofit(dialogAdmin_TIETL_IP.getText().toString(),
                            dialogAdmin_TIETL_PORT.getText().toString() ));
        }
}
