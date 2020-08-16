package okhttp.com.android.Idmemoryer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp.com.android.Idmemoryer.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText registerAccount_etext;
    private EditText registerPassword_etext;
    private EditText registerPhone_etext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step_one);

        registerAccount_etext = findViewById(R.id.registerAccount_etext1);
        registerPassword_etext = findViewById(R.id.registerPassword_etext1);
        registerPhone_etext = findViewById(R.id.registerPhone_etext1);
        Button registerBtn = findViewById(R.id.registerBtn1);

        registerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn1:
                String registerAddress="http://118.126.88.132:8080/IdMemoryer-server_war/RegisterServlet";
                String registerAccount = registerAccount_etext.getText().toString();
                String registerPassword = registerPassword_etext.getText().toString();
                String registerPhone = registerPhone_etext.getText().toString();
                registerWithOkHttp(registerAddress,registerAccount,registerPassword,registerPhone);
                break;
            default:
                break;
    }
}

    private void registerWithOkHttp(String address, String account, String password,String phone) {
        HttpUtil.registerWithOkHttp(address, account, password,phone, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")){
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    }
