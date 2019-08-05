package com.appproteam.sangha.bitdimo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.CurrentUser;
import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.RequestUser;
import com.appproteam.sangha.bitdimo.Utils.DialogConstant;
import com.appproteam.sangha.bitdimo.Utils.LoginConstant;
import com.appproteam.sangha.bitdimo.Utils.MailConstant;
import com.appproteam.sangha.bitdimo.Utils.RegisterConstant;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnFocusChangeListener {
    Button btn_backtologin;
    Button btn_submit;
    private EditText edtRegisterUsername;
    private EditText edtRegisterPassword;
    private EditText edtRegisterConfirmPassword;
    private EditText edtRegisterEmail;
    private EditText edtRegisterNickname;
    private RadioGroup radioGender;
    private Button btnRegister;
    private Button btnRegisterInLogin;
    private RadioGroup radiogrGender;
    private RadioButton radiobtnMale;
    private RadioButton radiobtnFemale;
    boolean gender = true;
    private CheckBox checkBoxConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createView();
        initFocusChangeListener();
        edtRegisterConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtRegisterPassword.getText().toString().equals(edtRegisterConfirmPassword.getText().toString())){
                    edtRegisterConfirmPassword.setError(RegisterConstant.COMFIRM_ERROR,customizeErrorIcon());
                }else edtRegisterConfirmPassword.setError(RegisterConstant.COMFIRM_ERROR);
            }
        });


    }

    private void createView() {
        btn_submit = (Button)findViewById(R.id.btn_RegisterSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSubmit();
            }
        });
        radiobtnMale = (RadioButton)findViewById(R.id.radiobtn_gender_Male);
        radiobtnFemale = (RadioButton)findViewById(R.id.radiobtn_gender_Female);
        btn_backtologin=(Button)findViewById(R.id.already_user);
        edtRegisterEmail = (EditText) findViewById(R.id.edt_register_Email);
        edtRegisterUsername = (EditText) findViewById(R.id.edt_register_Username);
        edtRegisterPassword = (EditText) findViewById(R.id.edt_register_Password);
        edtRegisterConfirmPassword = (EditText) findViewById(R.id.edt_register_PassworkVerify);
        edtRegisterNickname = (EditText) findViewById(R.id.edt_register_Nickname);
        radiogrGender = (RadioGroup) findViewById(R.id.radiogr_gender);
        radiobtnMale = (RadioButton) findViewById(R.id.radiobtn_gender_Male);
        radiobtnFemale = (RadioButton) findViewById(R.id.radiobtn_gender_Female);
        btnRegister = (Button) findViewById(R.id.btn_RegisterSubmit);
        checkBoxConfirm = (CheckBox) findViewById(R.id.terms_conditions);
        btn_backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void registerSubmit() {
        String username = edtRegisterUsername.getText().toString();
        String password = edtRegisterPassword.getText().toString();
        String confirmpassword = edtRegisterConfirmPassword.getText().toString();
        String nickname = edtRegisterNickname.getText().toString();
        String mail = edtRegisterEmail.getText().toString();

        if (!"".equals(username) && !"".equals(password) && !"".equals(confirmpassword) && !"".equals(nickname) && !"".equals(mail) && checkBoxConfirm.isChecked() ) {

             if(isValidUsername(username) && isValidPassword(password) && isValidEmail(mail) && confirmpassword.equals(password)) {
                 CurrentUser currentUser = new CurrentUser(username,password,mail,"https://cdn2.iconfinder.com/data/icons/rcons-user/32/male-shadow-circle-512.png",radiobtnFemale.isChecked()?"F":"M");
                 registerAccount(currentUser);
             }
              else
                 Toast.makeText(this, "Xin kiểm tra lại toàn bộ trước khi đăng ký", Toast.LENGTH_SHORT).show();


        }
        else Toast.makeText(this, "Nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();

    }

    private void registerAccount(CurrentUser currentUser) {
        LoginActivity.mSOService.registerAccount(currentUser).enqueue(new Callback<List<RequestUser>>() {
            @Override
            public void onResponse(Call<List<RequestUser>> call, Response<List<RequestUser>> response) {
                 if (response.isSuccessful())
                 {
                     if("Create Success".equals(response.body().get(0).getStatus())) {
                         Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                         startActivity(intent);
                         finish();
                     }
                     else if ("Username already exist".equals(response.body().get(0).getStatus()))
                         Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                         else Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                 }
            }

            @Override
            public void onFailure(Call<List<RequestUser>> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(LoginConstant.PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isValidUsername(String username) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(LoginConstant.USERNAME_PATTERN);
        matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(MailConstant.EMAIl_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void initFocusChangeListener() {
        edtRegisterUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterUsername.hasFocus()) {
                    if (!isValidUsername(edtRegisterUsername.getText().toString())) {
                        edtRegisterUsername.setError(LoginConstant.USERNAME_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterPassword.getText().toString())) {
                        edtRegisterPassword.setError(LoginConstant.PASSWORD_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterConfirmPassword.hasFocus()) {
                    if (!isValidPassword(edtRegisterConfirmPassword.getText().toString())) {
                        edtRegisterConfirmPassword.setError(LoginConstant.PASSWORD_ERROR_MESSAGE);
                    }
                }
            }
        });

        edtRegisterEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!edtRegisterEmail.hasFocus()) {
                    if (!isValidEmail(edtRegisterEmail.getText().toString())) {
                        edtRegisterEmail.setError(MailConstant.EMAIL_ERROR);
                    }
                }
            }
        });
    }
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus)
            hideSoftInputKeyboard(v);
    }

    private void hideSoftInputKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radiobtn_gender_Male:
                if (checked)

                    break;
            case R.id.radiobtn_gender_Female:
                if (checked)

                    gender = false;
                break;
        }
    }






    public Drawable customizeErrorIcon(){
        Drawable drawable = getResources().getDrawable(R.drawable.checked);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    public String md5(String str){
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1,digest.digest());
            result = bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }



}
