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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import okhttp.com.android.Idmemoryer.enity.Photo;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class Carousel extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlay;
    private Integer btnPlayFlay=0;
    private ImageView ivImg ;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Integer GlideFlag=1;
    private String url;
    private String name;
    private TextView ivText;
    private Button btnJia;
    private Button btnJian;
    private Integer speed=4000;


    private boolean flag = true;
    //通过handle改变UI
    @SuppressLint("HandlerLeak")
    private  Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    if(flag) {

                        if (GlideFlag>37)
                            GlideFlag=1;

                        String Address="http://118.126.88.132:8080/IdMemoryer-server_war/PhotoServlet";
                        String id = String.valueOf(GlideFlag);
                        photoWithOkHttp(Address,id);
                        ivText.setText(name);



                        flag=false;
                    }
                    else {
                        //使用Glide加载图片
                        Glide.with(Carousel.this)
                                .load(url)
                                .placeholder(R.mipmap.loading)
                                .error(R.mipmap.error)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(ivImg);
                        ivText.setText("");
                        flag=true;
                        GlideFlag++;
                    }
                    break;
            }
        }
    };




    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);


        //跨布局获取id
//        LayoutInflater inflater = Carousel.this.getLayoutInflater();       //获取当前布局填充器
//        View view = inflater.inflate(R.layout.nav_header,null);         //通过填充器获取另外一个布局的对象
//        TextView username = view.findViewById(R.id.username);


        btnPlay = findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        ivImg = findViewById(R.id.iv_img);
        ivText = findViewById(R.id.iv_text);
        btnJia = findViewById(R.id.btn_jia);
        btnJia.setOnClickListener(this);
        btnJian = findViewById(R.id.btn_jian);
        btnJian.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawerlayout_main = findViewById(R.id.drawablelayout_main);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_play);


        //设置navigationView的监听
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_photo:
                        Intent intent_play = new Intent(Carousel.this, SelectActivity.class);
                        startActivity(intent_play);
                        break;
                    case R.id.nav_exit:
                        Intent intent_exit = new Intent(Carousel.this,MainActivity.class);
                        startActivity(intent_exit);
                        break;
                }
                return true;
            }
        });


        //通过SharedPreferences取出数据
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


        //使用ToolBar替换系统自带的ActionBar
        setSupportActionBar(toolbar);

        // 使左上角图标可以点击
        getSupportActionBar().setHomeButtonEnabled(true);

        // 为ActionBar左上角图标加上一个返回箭头的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //设置标题颜色,注意颜色值的书写
//        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        //设置标题文本
//        toolbar.setTitle("title");
//        toolbar.setTitleMargin(400,0,0,0);

        //创建抽屉的开关
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawerlayout_main, toolbar, R.string.app_name, R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerToggle.syncState();      //ActionBarDrawerToggle.syncState()将同步改变后的图标的状态，这个状态取决于DrawerLayout的动作
        drawerlayout_main.addDrawerListener(mDrawerToggle);     //给DrawerLayout设置开关的监听

        //菜单项监听器,对菜单按键进行监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.toolbar_help:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        // i.setType("text/plain"); //模拟器请使用这行
                        i.setType("message/rfc822"); // 真机上使用这行
                        i.putExtra(Intent.EXTRA_EMAIL,
                                new String[] { "1792803325@qq.com" });
                        i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
                        i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
                        startActivity(Intent.createChooser(i,
                                "Select email application."));
                        break;
                }

                return true;
            }
        });
//        toolbar_help.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                // i.setType("text/plain"); //模拟器请使用这行
//                i.setType("message/rfc822"); // 真机上使用这行
//                i.putExtra(Intent.EXTRA_EMAIL,
//                        new String[] { "1792803325@qq.com" });
//                i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
//                i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
//                startActivity(Intent.createChooser(i,
//                        "Select email application."));
////            }
//        });
    }

    //绑定菜单  加载了menu_toolbar_main这个菜单文件
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

    //监听事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:

                btnPlayFlay++;
                if (btnPlayFlay%2==1) {
                    btnPlay.setText("暂停");
                    Toast.makeText(Carousel.this,"当前速度为:"+speed/1000+"秒一次",Toast.LENGTH_SHORT).show();
                    startTimer();
                }
                else{
                    btnPlay.setText("开始");
                    stopTimer();
                }
                break;
            case R.id.btn_jia:
                if (speed<8000)
                    speed = speed+1000;
                Toast.makeText(Carousel.this,"当前速度为:"+speed/1000+"秒一次",Toast.LENGTH_SHORT).show();
                Log.d("IndexActivitty","speed:"+speed);
                break;
            case R.id.btn_jian:
                if (speed>1000)
                    speed = speed-1000;
                Toast.makeText(Carousel.this,"当前速度为:"+speed/1000+"秒一次",Toast.LENGTH_SHORT).show();
                Log.d("IndexActivitty","speed:"+speed);
                break;
        }
    }

    //开启计时器
    private void startTimer(){
        if (timer == null) {
            timer = new Timer();
        }

        //计时器任务
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            };
        }

        if(timer != null && timer != null )
            timer.schedule(timerTask,speed,speed);        //延迟1S后执行一次timerTask,然后每隔4s执行一次timerTask

    }

    //关闭计时器,应该关闭计时器并清空,关闭计时器任务并清空,否则开启关闭计时器后再开启计时器会造成崩溃
    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer= null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void photoWithOkHttp(String Address, String id) {
        HttpUtil.photoWithOkHttp(Address,id, new Callback() {
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
        name = photo.getName();
        Log.d("Carousel", "id is " + GlideFlag);
        Log.d("Carousel", "name is " + name);
        Log.d("Carousel", "url is " + url);
    }

}
