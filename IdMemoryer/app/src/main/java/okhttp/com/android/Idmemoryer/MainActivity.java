package okhttp.com.android.Idmemoryer;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String Tag = "";
    private EditText loginAccount_etext;
    private EditText loginPassword_etext;
    private String account;
    private String password;
    private Boolean loginResult;
    private Long phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //初始化
        loginAccount_etext = findViewById(R.id.loginAccount_etext1);
        loginPassword_etext = findViewById(R.id.loginPassword_etext1);
        Button loginBtn = findViewById(R.id.loginBtn1);
        Button toRegisterBtn = findViewById(R.id.toRegisterBtn1);
        TextView toForgetPwdBtn = findViewById(R.id.toForgetPwd_btn1);

        //设置监听者对象
        toRegisterBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        toForgetPwdBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn1:
                String loginAddress="http://118.126.88.132:8080/IdMemoryer-server_war/LoginServlet";
                String loginAccount = loginAccount_etext.getText().toString();      //得到account的文本转化成String
                String loginPassword = loginPassword_etext.getText().toString();
                loginWithOkHttp(loginAddress,loginAccount,loginPassword);           //使用loginWithOkHttp通过OkHttp进行网络请求
                break;
            case R.id.toRegisterBtn1:
                Intent intent_register = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.toForgetPwd_btn1:
                Intent intent_forget = new Intent(MainActivity.this, ResetPwd1Activity.class);
                startActivity(intent_forget);
                break;
            default:
                break;
        }
    }

    /*
    * 登录
    * */

    private void loginWithOkHttp(String address, final String account, final String password) {
        HttpUtil.loginWithOkHttp(address,account,password, new Callback() {     //重写回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();       //返回你的response

                System.out.println("响应信息： " + responseData);
                parseJSONWithGSON(responseData);

                runOnUiThread(new Runnable() {      //调用runOnThread方法可以将Runnable任务放到主线程去执行
                    @Override
                    public void run() {
                        if (loginResult){
                            SharedPreferences.Editor editor = getSharedPreferences("user",MODE_PRIVATE).edit();
                            editor.putString("account",account);
                            editor.putString("password",password);
                            editor.putLong("phone",phone);
                            editor.apply();

                            //Toast要放在主线程中去执行
                            //Toast在应用程序上浮动显示信息给用户,使用静态方法创建Toast.makeText()
                            //第一个参数：当前的上下文环境。可用getApplicationContext()或this
                            //第二个参数：要显示的字符串。也可是R.string中字符串ID
                            //第三个参数：显示的时间长短。Toast默认的有两个LENGTH_LONG(长)和LENGTH_SHORT(短)，也可以使用毫秒如2000ms

                            Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Carousel.class);
                                startActivity(intent);

                        }else{
                            Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
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
        loginResult = user.getLoginResult();
        phone = user.getPhone();
            Log.d("MainActivity", "account is " + account);
            Log.d("MainActivity", "password is " + password);
            Log.d("MainActivity", "phone is " + phone);
    }





}

