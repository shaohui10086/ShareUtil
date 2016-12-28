# ShareUtil
`ShareUtil`是一个综合性的分享及登录工具库，支持微信分享，微博分享，QQ分享，QQ空间分享以及Android系统默认分享，支持微信登录，微博登录以及QQ登录并获取用户信息。

## Preview 
![share](/preview/shareutil_share.gif)
![login](/preview/shareutil_login.gif)
## Feature

1. 多种分享方式：
    
2. 支持分享图片本地链接，网络链接或者Bitmap， 不需要考虑各个平台的不一致性。

3. 支持微信、QQ、微博登录并获取登录用户信息

## Usage

### 添加依赖

免第三方Jar包配置

        compile 'me.shaohui.shareutil:shareutil:1.3.8'

手动添加第三方jar

		compile 'me.shaohui.shareutil:shareutil:1.3.8.no_jar'

### 使用配置

1. build.gradle 配置
在defaultConfig节点下增加你的qq id信息

        defaultConfig {
        	...
        	
            manifestPlaceholders = [
                    //  替换成你的qq_id
                    qq_id: "123456789"
            ]
            
        }
2. 在使用之前设置在各个平台申请的Id，以及分享的回调（推荐放在Application的onCreate方法中）
    
            // init
            ShareConfig config = ShareConfig.instance()
                    .qqId(QQ_ID)
                    .wxId(WX_ID)
                    .weiboId(WEIBO_ID)
                    // 下面两个，如果不需要登录功能，可不填写
                    .weiboRedirectUrl(REDIRECT_URL)
                    .wxSecret(WX_ID);
            ShareManager.init(config);

### 分享使用

        ShareUtil.shareImage(this, SharePlatform.QQ, "http://image.com", shareListener);
        ShareUtil.shareText(this, SharePlatform.WX, "分享文字", shareListener);
        ShareUtil.shareMedia(this, SharePlatform.QZONE, "title", "summary", "targetUrl", "thumb", shareListener);


### 登录使用

            // LoginPlatform.WEIBO  微博登录   
            // LoginPlatform.WX     微信登录
            // LoginPlatform.QQ     QQ登录 
            final LoginListener listener = new LoginListener() {
                    @Override
                    public void loginSuccess(LoginResult result) {
                        //登录成功， 如果你选择了获取用户信息，可以通过
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


## 使用说明

1. QQ不支持纯文字分享，会直接分享失败
2. 使用Jar文件的版本如下：

        微信版本：3.1.1
        QQ版本：3.1.0 lite版
        微博版本: 3.1.4
3. 分享的bitmap，会在分享之后被回收掉，所以分享之后最好不要再对该bitmap做任何操作。
4. example 中的代码可以参考，但是不可运行，因为需要保证包名以及签名文件和你申请各个平台id所填写信息保持一致
5. ShareListener的回调结果仅供参考，不可当做分享是否返回的依据，它并不是那么完全可靠，因为某些操作，例如微博分享取消，但是用户选择了保存草稿，这时候客户端并不会收到回调，所以也就不会调用ShareListener的onCancel

## ChangeLog

#### 1.3.8
- 修复在低端设备上，可能导致的 `NullPointException` 问题

#### 1.3.7
- 修复微信分享大图没反应bug
- 修复在5.0以下设备使用登录功能会意外收到登录取消回调

#### 1.3.6
- `ShareUtil`新增`isInstalled`方法，方便检查客户端是否安装

#### 1.3.5
- 修复QQ分享media类型错误
- 完善log
- 开放Application Context调用
- 增加检查外部存储
- 重构部分代码

#### 1.3.3  
- 增加Debug模式

#### 1.3.2  
- 修复若干分享相关的bug

#### 1.3.1
- 修复微博分享的bug

#### 1.3.0
- 重构使用方式，minSdkVersion - > 9

#### 1.2.8
- 增加分享失败Exception

#### 1.2.7
- 解决内存泄露的问题

## TODO

1. 微信内存泄露
2. 图片加载失败，关闭Activity

## Thanks

- https://github.com/tianzhijiexian/ShareLoginLib

## License

	Copyright 2016 shaohui10086

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.