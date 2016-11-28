package com.example.cw.slidemeuetest;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.MainActivityFragment.FragmentOne;
import com.example.cw.slidemeuetest.MainActivityFragment.FragmentThree;
import com.example.cw.slidemeuetest.MainActivityFragment.FragmentTwo;
import com.example.cw.slidemeuetest.Setting.Setting;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //ViewPager声明
    private ViewPager mViewPager;

    //ViewPager适配器
    private FragmentPagerAdapter mAdapter;

    //Fragment列表
    private List<android.support.v4.app.Fragment> mDatas;
    //tab里的LinearLayout
    private LinearLayout L1;
    private LinearLayout L2;
    private LinearLayout L3;
    //tab 里的文本 1
    private TextView texttabone;
    //tab 里的文本 2
    private TextView texttabtwo;
    //tab 里的文本 3
    private TextView texttabthree;
    //tab下的指示线
    private ImageView mTabline;
    private int mScreen1_3;
    //当前页数
    private int mCurrentIndex;

    //检测网络情况的广播
    private IntentFilter intentFilter;

    private NetworkChangeReciver networkChangeReciver;

    //是否退出
    private static Boolean isQuit = false;

    //网页
    private WebView webView;

    //DrawerLayout声明
    private DrawerLayout drawer;
    //Register情况
    private TextView userName;

    private TextView userEmail;

    public String name;

    public String email;

    //用户id
    private int id;

    //用户token
    private String token;

    //更新token api
    private static String tokenValidTestUrl = "http://lsuplus.top/api/refresh/?token=";

    //扫码登录接口
    public  String QRloninUrl="http://lsuplus.top/QRLogin/";

    //扫码结果
    private String QrScanResult;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //透明状态栏实现
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

//        webView=(WebView)findViewById(R.id.id_webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("http://lsuplus.top/");

//        AppCompatActivity mAppCompatActivity = new AppCompatActivity();
//        Toolbar toolbar = (Toolbar) mAppCompatActivity.findViewById(R.id.toolbar);
//        mAppCompatActivity.setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        //初始化tabline
        initTabline();


        //初始化viewpager
        initViewPager();



        //检测网络状态
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReciver = new NetworkChangeReciver();
        registerReceiver(networkChangeReciver,intentFilter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        registerBroadcastReceiver();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //打开app时更新登录ui
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset==0.1)
                Log.d("slide", "onDrawerSlide: ");
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                name = sharedPreferences.getString("name","");
                email = sharedPreferences.getString("email","");
                userName =(TextView)findViewById(R.id.id_userNameText);
                userEmail =(TextView)findViewById(R.id.id_userEmailText);
                if(name!=""||!name.equals("")) {
                    userName.setText(name);
                    userEmail.setText(email);
                }else{
                    userName.setText("立即登录");
                    userEmail.setText("");
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.d("slide", "onDrawerOpened: ");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d("slide", "onDrawerClosed: ");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.d("slide", "onDrawerStateChanged: ");
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void initTabline() {
        //初始化tabline
        mTabline = (ImageView)findViewById(R.id.id_ivTabline);
        //获取屏幕宽度和高度
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        mScreen1_3 = outMetrics.widthPixels/3;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScreen1_3;
        mTabline.setLayoutParams(lp);
    }


    private void initViewPager() {
        //初始化ViewPager相关控件
        //mViewPager.setOffscreenPageLimit(2);
        mViewPager = (ViewPager)findViewById(R.id.id_viewpager);
        texttabone = (TextView)findViewById(R.id.id_TVtabone);
        texttabtwo = (TextView)findViewById(R.id.id_TVtabtwo);
        texttabthree = (TextView)findViewById(R.id.id_TVtabthree);

        //缓存页数设置
        mViewPager.setOffscreenPageLimit(2);

        L1 = (LinearLayout)findViewById(R.id.id_LLaoutOne);
        L2 = (LinearLayout)findViewById(R.id.id_LLaoutTwo);
        L3 = (LinearLayout)findViewById(R.id.id_LLaoutThree);

        mDatas = new ArrayList<android.support.v4.app.Fragment>();
        FragmentOne fragmentone = new FragmentOne();
        FragmentTwo fragmenttwo = new FragmentTwo();
        FragmentThree fragmentthree = new FragmentThree();

        //添加到列表里
        mDatas.add(fragmentone);
        mDatas.add(fragmenttwo);
        mDatas.add(fragmentthree);

        //重写适配器方法
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getCount() {
                return mDatas.size();
            }
        };

        //设置适配器
        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //滑动时
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTabline.getLayoutParams();


                //如果从0到1
                if(mCurrentIndex==0&&position==0){
                    layoutParams.leftMargin = (int) (positionOffset*mScreen1_3
                                                +mCurrentIndex*mScreen1_3);
                }else if(mCurrentIndex==1&&position==0){
                    //第一页到第0页
                    layoutParams.leftMargin = (int) (mCurrentIndex*mScreen1_3+(positionOffset-1)*mScreen1_3);
                }else if(mCurrentIndex==1&&position==1){
                    //从1到2
                    layoutParams.leftMargin = (int)(mCurrentIndex*mScreen1_3+positionOffset*mScreen1_3);
                }else if(mCurrentIndex==2&&position==1){
                    //从2到1
                    layoutParams.leftMargin = (int) (mCurrentIndex*mScreen1_3+(positionOffset-1)*mScreen1_3);
                }
                mTabline.setLayoutParams(layoutParams);


            }

            @Override
            public void onPageSelected(int position) {
                    //滑动结束
                resetTextView();
                switch (position)
                {
                    case 0:
                        texttabone.setTextColor(Color.WHITE);
                        break;
                    case 1:
                        texttabtwo.setTextColor(Color.WHITE);
                        break;
                    case 2:
                        texttabthree.setTextColor(Color.WHITE);
                        break;
                }
                mCurrentIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //添加监听事件
        L1.setOnClickListener(new MyOnClickListener(0));
        L2.setOnClickListener(new MyOnClickListener(1));
        L3.setOnClickListener(new MyOnClickListener(2));
    }

    private void resetTextView() {
        //改变字体颜色
        texttabone.setTextColor(Color.parseColor("#CCCCCC"));
        texttabtwo.setTextColor(Color.parseColor("#CCCCCC"));
        texttabthree.setTextColor(Color.parseColor("#CCCCCC"));

    }


    @Override
    protected void onResume() {
        super.onResume();
        //registerReceiver(networkChangeReciver,intentFilter);
    }


    //网络检测
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(networkChangeReciver);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection Simplifiab0leIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //相机权限申请
                requestPermission();

            //二维码
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent,0);

        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent("com.example.cw.slidemeuetest.ACTION_START");
            intent.addCategory("android.intent.category.DEFAULT");
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            //设置
            Intent intent = new Intent(MainActivity.this,Setting.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void login(View v)
    {
        userName =(TextView)findViewById(R.id.id_userNameText);
        String loginStatus = (String) userName.getText();
        if(loginStatus.equals("立即登录")){
            //还未登录
            Intent intent = new Intent(MainActivity.this, Register_main.class);
            startActivity(intent);
        }else{

            return;
        }
//        //暂时开启 测试ui
//            Intent intent = new Intent(MainActivity.this, Register_main.class);
//            startActivity(intent);

    }

    private void registerBroadcastReceiver(){
        UserBroadcastReceiver receiver = new UserBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.example.broadcasttest.USERUI_BROADCAST");
        registerReceiver(receiver, filter);
    }

    public class UserBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            name = sharedPreferences.getString("name","");
            email = sharedPreferences.getString("email","");
            id = sharedPreferences.getInt("id",0);
            userName =(TextView)findViewById(R.id.id_userNameText);
            userEmail =(TextView)findViewById(R.id.id_userEmailText);
            if(name!=""||!name.equals("")) {
                userName.setText(name);
                userEmail.setText(email);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            QrScanResult= data.getExtras().getString("result");
            RefreshToken();
            //sendQrloginHttpURLConnection();
        }
    }

    private void sendQrloginHttpURLConnection() {
        //开启子线程访问网络 扫码登录模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                //获取SharedPreferences里的用户信息
                SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                token = sharedPreferences.getString("token","");

                try {
                    URL url = new URL(QRloninUrl+QrScanResult+"/?token="+token.toString());

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.connect();

                    //连接超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //对获取的流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);

                    }
                    Log.e("url", url.toString());

                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }

    //更新token
    private void RefreshToken(){

        //取出token
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

        //测试token是否过期
        //开启子线程访问网络 测试token模块
        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection connection = null;
                try {

                    URL url = new URL(tokenValidTestUrl+token);

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    //连接超时设置
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    //获取输入流
                    InputStream in = connection.getInputStream();

                    //对获取的流进行读取
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    final StringBuilder response = new StringBuilder();
                    String line=null;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }

                    token = connection.getHeaderField("Authorization");
                    token = token.substring(7,token.length());
                    savaToken();
                    sendQrloginHttpURLConnection();

                }   catch (Exception e) {
                    try {
                        int status_code = connection.getResponseCode();
                        if (status_code==400){
                            String error = connection.getResponseMessage();
                            if(error == "token_invalid"){
                                //登录时间到达两周 需要重新登录

                                        Toast.makeText(MainActivity.this,"长时间未登录 请重新登录！",Toast.LENGTH_SHORT).show();

                                        //跳转到登录界面
                                        Intent intent = new Intent(MainActivity.this, Register_main.class);
                                        startActivity(intent);


                            }else {

                                        Toast.makeText(MainActivity.this,"未知错误！",Toast.LENGTH_SHORT).show();

                            }

                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    Log.e("errss", e.getMessage());
                }
            }
        }).start();

    }

    private void savaToken() {
        //保存token
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }

    //退出登录 sendLogout
    private void sendLogoutHttpURLConnection() {
        //开启子线程访问网络 退出登录模块
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;

                try {
                    URL url = new URL("http://lsuplus.top/auth/logout");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    //连接超时
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);


                }   catch (Exception e) {
                    Log.e("errss", e.getMessage());

                }
            }
        }).start();
    }

    //点击tab监听
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            mViewPager.setCurrentItem(index);
        }
    }

    //权限请求
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //第一次被拒绝后，第二次访问时，向用户说明为什么需要此权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Toast.makeText(this, "开启后使用相机功能", Toast.LENGTH_SHORT).show();
            }
            //若权限没有开启，则请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 0x01);
        }
    }


    //相机权限反馈 6.0以上机型使用
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                //请求失败则提醒用户
                Toast.makeText(MainActivity.this, "请求权限失败！", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //延迟发送
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isQuit = false;
        }
    };
    //实体按键检测
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (BackOrExit())
                return true;


        }
        return false;
    }

    //判断是网页返回还是app退出
    private boolean BackOrExit() {
        if(mCurrentIndex==0) {
            if (FragmentOne.goback()) {
                //如果网页能返回 则返回true 直接跳出
                return true;
            }
            //如果网页不能返回 则判断按两次退出app
            if (!isQuit) {
                isQuit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                //留在后台
                moveTaskToBack(false);

            }
//        }else if(mCurrentIndex == 1){
//            if(FragmentTwo.goback()){
//                //如果网页能返回 则返回true 直接跳出
//                return true;
//            }
//            //如果网页不能返回 则判断按两次退出app
//            if (!isQuit) {
//                isQuit = true;
//                Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                        Toast.LENGTH_SHORT).show();
//                // 利用handler延迟发送更改状态信息
//                mHandler.sendEmptyMessageDelayed(0, 2000);
//            } else {
//                //留在后台
//                moveTaskToBack(false);
////                finish();
////                System.exit(0);
//            }
//        }
        }else if(mCurrentIndex == 2){
            if(FragmentThree.goback()){
                //如果网页能返回 则返回true 直接跳出
                return true;
            }
            //如果网页不能返回 则判断按两次退出app
            if (!isQuit) {
                isQuit = true;
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                mHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                //留在后台
                moveTaskToBack(false);
//                finish();
//                System.exit(0);
            }
        }
        return false;
    }


}

