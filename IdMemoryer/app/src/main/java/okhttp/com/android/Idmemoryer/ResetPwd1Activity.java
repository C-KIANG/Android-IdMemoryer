package okhttp.com.android.Idmemoryer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;



import java.io.IOException;

import okhttp.com.android.Idmemoryer.enity.User;

import okhttp.com.android.Idmemoryer.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ResetPwd1Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText registerAccount_etext;
    private EditText registerPhone_etext;
    private Boolean forget1Result;
    private String account;
    private Long phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pwd);
        registerAccount_etext = findViewById(R.id.registerAccount_etext1);
        registerPhone_etext = findViewById(R.id.registerPhone_etext1);


        Button forgetPwd1Btn = findViewById(R.id.forgetPwd1_btn1);

        forgetPwd1Btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetPwd1_btn1:
                String registerAddress="http://118.126.88.132:8080/IdMemoryer-server_war/Forget1Servlet";

                String registerPhone = registerPhone_etext.getText().toString();
                String registerAccount = registerAccount_etext.getText().toString();
                forget1WithOkHttp(registerAddress,registerAccount,registerPhone);

            default:
                break;
        }
    }

    private void forget1WithOkHttp(String registerAddress, final String registerAccount, String registerPhone) {
        HttpUtil.forget1WithOkHttp(registerAddress, registerAccount, registerPhone, new Callback() {
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
                        if (forget1Result){


                            //Toast在应用程序上浮动显示信息给用户,使用静态方法创建Toast.makeText()
                            //第一个参数：当前的上下文环境。可用getApplicationContext()或this
                            //第二个参数：要显示的字符串。也可是R.string中字符串ID
                            //第三个参数：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)，也可以使用毫秒如2000ms

                            Toast.makeText(ResetPwd1Activity.this,"验证成功",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ResetPwd1Activity.this, ResetPwd2Activity.class);
                            Bundle account = new Bundle();
                            account.putString("registerAccount",registerAccount);
                            intent.putExtras(account);
                            startActivity(intent);

                        }else{
                            Toast.makeText(ResetPwd1Activity.this,"验证失败",Toast.LENGTH_SHORT).show();
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
        phone = user.getPhone();
        forget1Result = user.getLoginResult();
        Log.d("ResetPwd1Activity", "account is " + account);
        Log.d("ResetPwd1Activity", "phone is " + phone);
        Log.d("ResetPwd1Activity", "验证结果 is " + forget1Result);

    }
}
