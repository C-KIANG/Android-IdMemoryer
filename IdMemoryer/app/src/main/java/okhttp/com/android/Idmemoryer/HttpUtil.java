package okhttp.com.android.Idmemoryer;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

class HttpUtil {
    //登录
    static void loginWithOkHttp(String address,String account,String password,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();       //创建okHttpClient对象
        RequestBody body = new FormBody.Builder()       //构建FormBody传入参数
                .add("loginAccount",account)
                .add("loginPassword",password)
                .build();
        Request request = new Request.Builder()
                .url(address)   //请求链接
                .post(body)     //将FormBody作为post方法传入
                .build();       //创建Request对象
        client.newCall(request).enqueue(callback);  //将Request封装成Call,调用请求
    }
    //注册
    static void registerWithOkHttp(String address,String account,String password,String phone,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount",account)
                .add("registerPassword",password)
                .add("registerPhone",phone)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //轮播
    static void photoWithOkHttp(String address, String id, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", id)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //查询
    static void selectWithOkHttp(String address, String name, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name", name)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //验证
    static void forget1WithOkHttp(String address, String account,String phone, Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount",account)
                .add("registerPhone", phone)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //修改密码
    static void forget2WithOkHttp(String address,String account,String password,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("registerAccount",account)
                .add("forgetPwd",password)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //查询
    static void selectAllWithOkHttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //增加
    static void addWithOkHttp(String address,String addName,String addUrl,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();       //创建okHttpClient对象
        RequestBody body = new FormBody.Builder()       //构建FormBody传入参数
                .add("name",addName)
                .add("url",addUrl)
                .build();
        Request request = new Request.Builder()
                .url(address)   //请求链接
                .post(body)     //将FormBody作为post方法传入
                .build();       //创建Request对象
        client.newCall(request).enqueue(callback);  //将Request封装成Call,调用请求
    }

    //修改
    static void updateWithOkHttp(String address,String id,String updateName,String updateUrl,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();       //创建okHttpClient对象
        RequestBody body = new FormBody.Builder()       //构建FormBody传入参数
                .add("id",id)
                .add("name",updateName)
                .add("url",updateUrl)
                .build();
        Request request = new Request.Builder()
                .url(address)   //请求链接
                .post(body)     //将FormBody作为post方法传入
                .build();       //创建Request对象
        client.newCall(request).enqueue(callback);  //将Request封装成Call,调用请求
    }

    //删除
    static void deleteWithOkHttp(String address,String id,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();       //创建okHttpClient对象
        RequestBody body = new FormBody.Builder()       //构建FormBody传入参数
                .add("id",id)
                .build();
        Request request = new Request.Builder()
                .url(address)   //请求链接
                .post(body)     //将FormBody作为post方法传入
                .build();       //创建Request对象
        client.newCall(request).enqueue(callback);  //将Request封装成Call,调用请求
    }


}
