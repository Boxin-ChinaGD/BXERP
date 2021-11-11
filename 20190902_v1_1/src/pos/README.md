Android端基础框架

本架构使用MVP+Dagger2+Retrofit+OKHttp,参考了[Android-CleanArchitecture](https://github.com/android10/Android-CleanArchitecture)

#### 搭建环境

| 开发环境说明 |  语言/工具 | 备注            |
| :--------- | :--------:| :--:          |
| 开发语言     | Java       | 暂不使用kotlin |
| 编辑器IDE    |  Android Studio(AS)  | 版本3.0  |
| 编译         |    gradle  |  与AS版本有关,AS3.0使用4.1 |
| 版本控制     |    Git     | |

#### 编码规范

Code Style使用[square的java-code-style](https://github.com/square/java-code-styles)

#### 关于文档
基础说明请参照[docs](docs/README.md)

#### 版本控制策略

我们使用 Git 作为代码版本控制工具，在开发过程中，我们应该按照几个基本原则进行分支管理：

- `master` 分支是稳定版本，仅用来发布新版本，不能在该分支上修改代码；
- `pre-release` 分支是预发布版本，需要发布的功能代码先合并到该分支，然后再由该分支合并到 `master` 分支；
- `dev` 分支是开发分支，仅用于开发服测试，不能在该分支上进行编码；
- `ftr-xxx-username` 分支为新功能开发分支，中间的 `xxx` 为功能名称，并且以开发人员的名称作为后缀，如 `ftr-role-joker`；
- `bug-xxx-username` 分支为Bug修复分支，格式同上，前缀换成 `bug`
- `hotfix-xxx-username` 分支为Bug紧急修复分支，格式同上，前缀换成 `hotfix`

开发人员开发新的功能或修复 Bug 时，从 `master` 或 `dev` 切出一条分支，然后进行开发，当需要上到开发服或测试服或正式服时，再合并到对应的分支。

正式包上线后,需要在`master`分支创建以`release-version`命名的`Tag`并上传到服务器.

关于更多git的使用请参考以下文档
 - [GIT版本开发指引](https://github.com/thoughtbot/guides/tree/master/protocol/git) [中文版](http://www.ruanyifeng.com/blog/2015/08/git-use-process.html)
 - [GIT分支模型](http://nvie.com/posts/a-successful-git-branching-model/) [中文版](http://www.ruanyifeng.com/blog/2012/07/git.html)
 - [GIT风格指引](https://github.com/agis/git-style-guide) [中文版](https://github.com/aseaday/git-style-guide)

#### 编译配置

主要是对gradle的配置,具体可看各个gradle文件,里面都有注释

- 全局配置[build.gradle](build.gradle)
- 域名配置[gradle.properties](gradle.properties)
- 签名部分[signing.gradle](signing.gradle) & [signing.properties](signing.properties) & [签名文件目录](keystore/README.md)
- app项目配置[app/build.gradle](app/build.gradle)

正式包请执行build task `assembleRelease`

#### Log

可以直接使用android.util.Log, 已经在[混淆配置文件](app/proguard-rules.pro)设置好正式包自动删除Log.

#### 第三方库

本项目使用到以下第三方库

- [Okhttp](https://github.com/square/okhttp)
- [retrofit](https://github.com/square/retrofit)
- [leakcanary](https://github.com/square/leakcanary)
- [dagger2](https://github.com/google/dagger)
- [gson](https://github.com/google/gson)
- [butterknife](https://github.com/JakeWharton/butterknife)
- [glide](https://github.com/bumptech/glide)
- [glide-transformations](https://github.com/wasabeef/glide-transformations)
- [eventbus](https://github.com/greenrobot/EventBus)
- [stetho](https://github.com/facebook/stetho)
- [disklrucache](https://github.com/JakeWharton/DiskLruCache)

#### 其他扩展

- [干货集中营](http://gank.io/)
- [Android百大框架排行榜](https://github.com/ShaunSheep/Android_100_TOP-Projects)
- [AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
- [awesome-android-ui](https://github.com/wasabeef/awesome-android-ui)
- [android-architecture](https://github.com/googlesamples/android-architecture)

#### AS插件

提高开发效率的部分AS插件,直接在File->Settings->Plugin->Browse Repositories...

- ADB Idea
- ADB WIFI
- Android ButterKnife Zelezny
- Android Parcelable code generator
- Android strings.xml tools
- CodeGlance
- GsonFormat
- Markdown Navigator
- Translation

