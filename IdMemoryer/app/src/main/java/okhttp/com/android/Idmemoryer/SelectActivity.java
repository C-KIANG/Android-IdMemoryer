package okhttp.com.android.Idmemoryer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;


import java.io.IOException;
import java.lang.reflect.Method;

import okhttp.com.android.Idmemoryer.enity.Photo;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText;
    private Button btn;
    private String photoName = "";
    private ImageView ivImg;
    private String url;
    private Boolean selectResult = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    if (selectResult)
                    {
                        Glide.with(SelectActivity.this)
                                .load(url)
                                .placeholder(R.mipmap.loading)
                                .error(R.mipmap.error)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivImg);
                        Toast.makeText(SelectActivity.this,"查询成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(SelectActivity.this,"查询失败",Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        ivImg = findViewById(R.id.iv_img);
        editText = findViewById(R.id.phone_select);
        btn = findViewById(R.id.select_btn);
        btn.setOnClickListener(this);

        //1
        Toolbar toolbar = findViewById(R.id.toolbar);
        //2
        final DrawerLayout drawerLayout = findViewById(R.id.drawablelayout_main);
        //3
        NavigationView navigationView = findViewById(R.id.nav_view);

        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
        String account = pref.getString("account","");
        Long phone = pref.getLong("phone",0);
        String phone_out = String.valueOf(phone);
        //navigationView修改header TextView的方法
        if(navigationView.getHeaderCount() > 0) {
            View header = navigationView.getHeaderView(0);
            TextView username = (TextView) header.findViewById(R.id.username);
//            TextView number = header.findViewById(R.id.number);
            username.setText(account);
//            number.setText(phone_out);
        }


        //1
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //1
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.toolbar_help:
                        Toast.makeText(SelectActivity.this,"点了联系我们",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        //2
        final ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        //2
        mDrawerToggle.syncState();      //ActionBarDrawerToggle.syncState()将同步改变后的图标的状态，这个状态取决于DrawerLayout的动作
        drawerLayout.addDrawerListener(mDrawerToggle);     //给DrawerLayout设置开关的监听

        //3
        navigationView.setCheckedItem(R.id.nav_photo);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_play:
                        Intent intent_play = new Intent(SelectActivity.this, Carousel.class);
                        startActivity(intent_play);
                        break;
                    case R.id.nav_exit:
                        Intent intent_exit = new Intent(SelectActivity.this,MainActivity.class);
                        startActivity(intent_exit);
                        break;
                }
                return true;
            }
        });


    }
    //1
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar跟menu
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }

    //1
    //    要重写onPrepareOptionsPanel方法才可以显示出menu图标
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_btn:
                photoName = editText.getText().toString();

                if(!TextUtils.isEmpty(photoName)) {
//                      String Address="http://voidck.com:8080/WebDemo/SelectServlet";
                    String Address="http://118.126.88.132:8080/IdMemoryer-server_war/SelectServlet";
                    String name = photoName;
                    selectWithOkHttp(Address,name);

                }
                else {
                    Toast.makeText(SelectActivity.this,"人名不能为空",Toast.LENGTH_SHORT).show();
                }

        }

    }

    private void selectWithOkHttp(String Address, String Name) {
        HttpUtil.selectWithOkHttp(Address,Name, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();       //返回你的response

                System.out.println("响应信息： " + responseData);
                parseJSONWithGSON(responseData);


            }
        });
    }


    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        Photo photo = gson.fromJson(jsonData, Photo.class);
        url = photo.getUrl();
        selectResult = photo.getSelectResult();
        Log.d("SelectActivity", "name is " + photoName);
        Log.d("SelectActivity", "url is " + url);
        Log.d("SelectActivity", "selectResult is " + selectResult);

        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);

    }


}
