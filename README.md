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

#### 添加依赖

        compile 'me.shaohui:shareutil:1.3.0'

#### 使用配置

1. build.gradle 配置
在defaultConfig节点下增加你的qq id信息

        defaultConfig {
        
            manifestPlaceholders = [
                    qq_id: "123456789"
            ]
            
        }
2. 在使用之前设置wxId，sinaId，qqId，以及分享的回调（推荐放在Application的onCreate方法中）
    
            // init
            ShareConfig config = ShareConfig.instance()
                    .qqId(QQ_ID)
                    .wxId(WX_ID)
                    .weiboId(WEIBO_ID)
                    .wxSecret(WX_ID);
            ShareManager.init(config);

            
                              
3. 分享
    
            ShareUtil.shareImage(this, SharePlatform.QQ, "http://image.com", shareListener);
            ShareUtil.shareText(this, SharePlatform.WX, "分享文字", shareListener);
            ShareUtil.shareMedia(this, SharePlatform.QZONE, "标题", "简介", "目标Url", "缩略图", shareListener);
            

6. 登录

            // LoginPlatform.WEIBO  微博登录   
            // LoginPlatform.WX     微信登录
            // LoginPlatform.QQ     QQ登录 
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
            LoginUtil.login(MainActivity.this, LoginPlatform.WEIBO, mLoginListener, isFetchUserInfo);
            
### 分享使用



### 登录使用



## 使用说明

1. QQ不支持纯文字分享
2. 使用Jar文件的版本如下：

        微信版本：3.1.1
        QQ版本：3.1.0 lite版
        微博版本: 3.1.2
3. 分享的bitmap，会在分享之后被回收掉，所以分享之后最好不要再对该bitmap做任何操作。
## ChangeLog
- 1.3.1  修复微博分享的bug
- 1.3.0  重构使用方式，minSdkVersion - > 9
- 1.2.8  增加分享失败Exception
- 1.2.7  解决内存泄露的问题

## TODO

1. 测试case 
2. simple app
5. tencent 内存泄露
7. 超类 LoginResult BaseToken，BaseUser
8. 判断是否安装
9. 链接 改成 uri
10. 初始化方式太挫
13. qq分享文字失败
14. 是否在recycle 中 回收掉bitmap


## License
