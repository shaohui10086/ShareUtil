# ShareUtil
社会化分享工具库



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

## Usage

1. 添加依赖

        compile 'me.shaohui.shareutil:shareutil:1.0'

2. 添加必要的Jar包

    微信版本：3.1.1
    QQ版本：3.1.0 lite版
    微博版本: 3.1.2

3. 在使用之前设置wxId，sinaId，qqId，以及分享的回调（推荐放在Application的onCreate方法中）
    
            ShareUtil.setQQId("QQ_xxxx");
            ShareUtil.setWXId("WX_xxxx");
            ShareUtil.setWeiboId("WB_xxxx");
            ShareUtil.setShareListener(new ShareListener() {
                @Override
                public void shareSuccess() {Log.i("share", "share_success");
                }
    
                @Override
                public void shareFailure() {Log.i("share", "share_failure");
                }
    
                @Override
                public void shareCancel() {Log.i("share", "share_cancel");
                }
            });
    
4. 必要的回调拦截处理
    1. 在调用分享操作所在的Activity中添加微博和QQ的回调处理，建议放到BaseActivity中
    
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
    
    2. 微信的回调有两种处理方式可以选择：
    
        1. 如果你对微信的回调没有特殊需求，可以直接在AndroidManifest文件中添加如下代码：
            
                <activity android:name="me.shaohui.shareutil.share.WXEntryParent"/>
                <activity-alias
                            android:name=".wxapi.WXPayEntryActivity"
                            android:exported="true"
                            android:targetActivity="me.shaohui.shareutil.share.WXEntryParent"/>
     
        2. 或者按照微信的规范在包的根目录下wxapi， WXPayEntryActivity
               
               
                   public class WXEntryActivity extends WXEntryParent {
                       @Override
                       public void onResp(BaseResp baseResp) {
                           
                           // do anything you want
                           
                           super.onResp(baseResp);         // 处理微信分享回调
                       }
                   }

5. 去分享吧！
    
            ShareUtil shareUtil = ShareUtil.newInstnce(this);
            
            shareUtil.shareText(SharePlatform.WX, "分享文字");
            
            shareUtil.shareImage(SharePlatform.QQ, "分享图片链接");
            
            shareUtil.shareMedia(SharePlatform.QZONE, "分享title", "分享简洁", "分享的链接", "分享缩略图链接");
            


## 使用说明

1. QQ不支持纯文字分享

## ChangeLog

## TODO

1. 测试case 
2. simple app
3. 登录功能

## License
