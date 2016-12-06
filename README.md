# ShareUtil
社会化分享以及登录工具库



## Preview 

## Feature

1. 多种分享方式：
    - 分享到QQ: SharePlatform.QQ  
    - 分享到微信: SharePlatform.WX
    - 分享到QQ空间: SharePlatform.QZONE
    - 分享到微博: SharePlatform.SINA
    - 分享到朋友圈: SharePlatform.WX_TIMELINE
    - Android系统默认分享: SharePlatform.DEFAULT
    
2. 支持分享图片本地链接，网络链接或者Bitmap， 不需要考虑各个平台的不一致性。

3. 支持微信、QQ、微博登录并获取登录用户信息

## Usage

1. 添加依赖

        compile 'me.shaohui:shareutil:1.2.5'

2. 在使用之前设置wxId，sinaId，qqId，以及分享的回调（推荐放在Application的onCreate方法中）
    
            // 分享和登录都需要设置
            ShareManager.WX_ID = "wx_XXXXXX";
            ShareManager.WEIBO_ID = "123XXXXX";
            ShareManager.QQ_ID = "123XXX";
            ShareManager.WX_SECRET = "XXXXXXX";     // 微信登录所需，如果只分享，不需要设置
                       
            // 分享设置
            ShareUtil.setShareListener(new ShareListener());
            
3. AndroidManifest配置

            <uses-permission android:name="android.permission.INTERNET"/>
            <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
            <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>

            <activity
                    android:name="com.tencent.tauth.AuthActivity"
                    android:noHistory="true"
                    android:launchMode="singleTask">
                <intent-filter>
                    <action android:name="android.intent.action.VIEW" />
                    <category android:name="android.intent.category.DEFAULT" />
                    <category android:name="android.intent.category.BROWSABLE" />
                    <!--注意要将这个改成你的QQ_ID-->
                    <data android:scheme="tencent123456789" />
                </intent-filter>
            </activity>
    
            <!-- 接收微信的回调 -->
            <activity android:name="me.shaohui.shareutil.WXEntryParent"/>
            <activity-alias
                    android:name=".wxapi.WXEntryActivity"
                    android:exported="true"
                    android:targetActivity="me.shaohui.shareutil.WXEntryParent"/>
                    
4. 必要的回调处理
    1. 分享操作所在的Activity中添加微博和QQ的回调处理，建议放到BaseActivity中
    
            @Override
            protected void onNewIntent(Intent intent) {
                super.onNewIntent(intent);
                ShareUtil.handleWeiboResponse(this, intent);   // 用于处理微博分享的回调
            }
        
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                ShareUtil.handleQQResult(data);    // 用于处理QQ分享的回调
            }
    
     2. 登录的回调：
            
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                LoginUtil.handleResult(requestCode, resultCode, data);      // 注意不要和分享的回调重复
            }
           
5. 分享
    
            ShareUtil shareUtil = ShareUtil.newInstnce(this);
            
            shareUtil.shareText(SharePlatform.WX, "分享文字");
            
            // 三种图片分享方式
            shareUtil.shareImage(SharePlatform.QQ, "分享图片链接");
            shareUtil.shareImage(SharePlatform.QQ, "分享图片本地路径");
            shareUtil.shareImage(SharePlatform.QQ, bitmap);
            
            shareUtil.shareMedia(SharePlatform.QZONE, "分享title", "分享简洁", "分享的链接", "分享缩略图链接");

6. 登录

            // LoginPlatform.WEIBO  微博登录   
            // LoginPlatform.WX     微信登录
            // LoginPlatform.QQ     QQ登录
            
            
            LoginUtil.login(MainActivity.this, LoginPlatform.WEIBO, mLoginListener, isFetchUserInfo);
            
            final LoginListener listener = new LoginListener() {
                    @Override
                    public void loginSuccess(LoginResult result) {
                        //登录成功， 如果你选择了获取用户信息，可以通过
                    }
        
                    @Override
                    public void beforeFetchUserInfo(BaseToken token) {
                        // 如果选择了获取用户信息
                    }
        
                    @Override
                    public void loginFailure(Exception e) {
                        Log.i("TAG", "登录失败");
                    }
        
                    @Override
                    public void loginCancel() {
                        Log.i("TAG", "登录取消");
                    }
                };
            
### 分享使用



### 登录使用



## 使用说明

1. QQ不支持纯文字分享
2. 使用Jar文件的版本如下：

        微信版本：3.1.1
        QQ版本：3.1.0 lite版
        微博版本: 3.1.2

## ChangeLog

1.2.8  增加分享失败Exception
1.2.7  解决内存泄露的问题

## TODO

1. 测试case 
2. simple app
3. 登录功能
4. sharelistener 为空
5. tencent 内存泄露
6. 登录成功以后，logininstance置空
7. 超类 LoginResult BaseToken，BaseUser
8. 判断是否安装

## License
