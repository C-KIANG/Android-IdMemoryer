package okhttp.com.android.Idmemoryer;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;



import java.io.IOException;

import okhttp.com.android.Idmemoryer.enity.User;

import okhttp.com.android.Idmemoryer.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class ResetPwd2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView registerAccount_edext;
    private EditText forgetPwd_etext;
    private Button forget2Btn;
    private String Iaccount;
    private Boolean forget2Result;
    private String account;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        registerAccount_edext = findViewById(R.id.registerAccount_etext1);
        forgetPwd_etext = findViewById(R.id.registerPassword_etext1);
        forget2Btn = findViewById(R.id.forgetPwd2_btn1);

        forget2Btn.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Iaccount = bundle.getString("registerAccount");
        registerAccount_edext.setText(Iaccount);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetPwd2_btn1:
                String registerAddress="http://118.126.88.132:8080/IdMemoryer-server_war/Forget2Servlet";
                String registerAccount = Iaccount;
                String forgetPwd = forgetPwd_etext.getText().toString();
                forget2WithOkHttp(registerAddress,registerAccount,forgetPwd);

            default:
                break;
        }
    }

    private void forget2WithOkHttp(String registerAddress,  String registerAccount, String forgetPwd) {
        HttpUtil.forget2WithOkHttp(registerAddress, registerAccount, forgetPwd, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();       //返回你的response

                System.out.println("响应信息： " + responseData);
                parseJSONWithGSON(responseData);

                runOnUiThread(new Runnable() {      //调用runOnThread方法可以将Runnable任务放到主线程去执行
                    @Override
                    public void run() {
                        if (forget2Result){


                            Toast.makeText(ResetPwd2Activity.this,"修改成功",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ResetPwd2Activity.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(ResetPwd2Activity.this,"修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }

    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        User user = gson.fromJson(jsonData, User.class);
        account = user.getLoginAccount();
        password = user.getLoginPassword();
        forget2Result = user.getLoginResult();
        Log.d("ResetPwd2Activity", "account is " + account);
        Log.d("ResetPwd2Activity", "password is " + password);
        Log.d("ResetPwd2Activity", "修改结果 is " + forget2Result);

    }
}
