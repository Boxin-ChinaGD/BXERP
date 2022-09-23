## 目录说明

### app项目架构

目前主要目录结构如下。

    ├─debug                                             区别于其他包（release或其他）的功能处理
    │  ├─java
    │  │  └─com
    │  │      └─bx
    │  │          └─erp
    │  │              └─utils                           这里设置Stetho在debug模式使用，在release不使用，需要修改（release包也需要这样一个）
    │  └─res
    │      └─values
    ├─main                                              默认功能处理
    │  ├─java
    │  │  └─com
    │  │      └─bx
    │  │          └─erp
    │  │              ├─helper                          一些helper类
    │  │              ├─di                              dagger的component和module
    │  │              │  ├─components
    │  │              │  └─modules
    │  │              ├─mapper                          处理一些实体转换
    │  │              ├─model                           model
    │  │              ├─presenter                       presenter
    │  │              ├─retrofit                        服务器接口定义
    │  │              ├─service                         服务相关
    │  │              ├─utils                           一些utils工具类
    │  │              └─view                            view
    │  │                  ├─activity                    activity
    │  │                  ├─adapter                     列表的adapter
    │  │                  ├─component                   view的组件（自定义控件）
    │  │                  └─fragment                    fragment
    │  └─res
    │      ├─drawable                                   drawable的xml
    │      ├─drawable-v24                               android6.0以上的drawable
    │      ├─layout                                     UI布局
    │      ├─mipmap-hdpi                                各个logo的图片
    │      ├─mipmap-mdpi
    │      ├─mipmap-xhdpi
    │      ├─mipmap-xxhdpi
    │      ├─mipmap-xxxhdpi
    │      └─values                                     字符串、样式、色值、尺寸等
    └─release                                           区别于其他包（debug或其他）的功能处理
       └─java
           └─com
               └─bx
                   └─erp
                       └─utils

### 安装包
放置在`apk`目录下

### 签名文件
放置在`keystore`里面。

### 文档区(docs文件夹)

放置功能模块和一些技术的文档,命名请用功能模块或技术命名.