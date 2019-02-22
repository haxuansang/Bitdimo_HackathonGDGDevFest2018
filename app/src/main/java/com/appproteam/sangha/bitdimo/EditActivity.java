package com.appproteam.sangha.bitdimo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.CurrentUser;
import com.appproteam.sangha.bitdimo.Singleton.CurrentDataUser;


public class EditActivity extends AppCompatActivity {
    Button btnConfirm;
    EditText et_username,et_email,et_nickname,et_password,et_confirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        createView();
    }

    private void createView() {
        btnConfirm=(Button)findViewById(R.id.btn_EditSubmit);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_username = (EditText)findViewById(R.id.edt_edit_Username);
        et_email = (EditText)findViewById(R.id.edt_edit_Email);
        et_nickname = (EditText)findViewById(R.id.edt_edit_Nickname);
        et_password = (EditText)findViewById(R.id.edt_edit_Password);
        et_confirmpassword = (EditText)findViewById(R.id.edt_edit_PassworkVerify);
        et_username.setText(CurrentDataUser.getInstance().getCurrentUser().getUsername());
        et_username.setEnabled(false);
        et_nickname.setText(CurrentDataUser.getInstance().getCurrentUser().getUsername());
        et_email.setText(CurrentDataUser.getInstance().getCurrentUser().getEmail());


    }

}
