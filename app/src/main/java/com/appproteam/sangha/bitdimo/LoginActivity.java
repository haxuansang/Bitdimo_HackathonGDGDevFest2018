package com.appproteam.sangha.bitdimo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.appproteam.sangha.bitdimo.Retrofit.ApiUtils;
import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.RequestUser;
import com.appproteam.sangha.bitdimo.Retrofit.SOService;
import com.appproteam.sangha.bitdimo.Singleton.CurrentDataUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    RelativeLayout relativeLayoutLogin;
    Button btnLogin,btnSignUp;
    EditText edtUsername;
    EditText edtPassword;
    private static final int LOCATION_REQUEST_CODE=1001;
    public static SOService mSOService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSOService = ApiUtils.getSOService();
        createView();
        showLoginView();
        processLogin();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
    }

    private void processLogin() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String userName =edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                checkUserLogin(userName,password);
             /*  moveMainActivity();
                if((edtUsername.getText().toString().equals("haxuansang123")&&edtPassword.getText().toString().equals("haxuansang123")) || edtUsername.getText().toString().equals("")&&edtPassword.getText().toString().equals("")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(LoginActivity.this, "Đăng nhập không thành công, xin bạn kiểm tra lại! ", Toast.LENGTH_SHORT).show();*/
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkUserLogin(String userName, String password) {
        if("".equals(userName) || "".equals(password))
            Toast.makeText(this, "Tên tài khoản hoặc mật khẩu không được để trống vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
        else
        mSOService.getCurrentUser(userName+"&"+password).enqueue(new Callback<List<RequestUser>>() {
            @Override
            public void onResponse(Call<List<RequestUser>> call, Response<List<RequestUser>> response) {
                if (response.isSuccessful())
                {
                    RequestUser requestUser  = response.body().get(0);
                    if("RURP".equals(requestUser.getStatus())) {
                        CurrentDataUser.getInstance().setCurrentUser(requestUser.getCurrentUser().get(0));
                        moveMainActivity();
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Đăng nhập không thành công, vui lòng kiểm tra tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                    }

                }
                else Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<RequestUser>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        return false;
    }

    private void showLoginView() {
        Handler hander = new Handler();
        hander.postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayoutLogin.setVisibility(View.VISIBLE);
            }
        }, 2000);
    }

    private void createView() {
        relativeLayoutLogin = (RelativeLayout)findViewById(R.id.relativeLayout_Login);
        btnLogin=(Button)findViewById(R.id.btn_Login);
        btnSignUp=(Button)findViewById(R.id.btn_Register);
        edtUsername=(EditText)findViewById(R.id.edt_Username);
        edtPassword = (EditText)findViewById(R.id.edt_Password);

    }
    private void moveMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
