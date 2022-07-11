
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs);
    // this.overShare();
  },
  onShow: function (options) {
    console.log(options);
    // if (options.scene == 1028) { // TODO 暂时写死用于测试当前用户是会员并且从卡包进入小程序,小程序发布后则直接从options.query获取card_id和code
    //   options.query = {
    //     card_id: 'p1uoyw6ScJsvTlFSHqVsEXcCIpCc', // 测试时需要测试人员在nbr插入会员卡数据
    //     code: '003454947843' // 
    //   };
    //   console.log('app.js->option.query:->' + JSON.stringify(options.query));
    // } else if (options.scene == 1007) {// TODO 暂时写死用于测试当前用于不是会员并且从好友分享进入小程序,小程序发布后则直接从options.query获取card_id和code
    //   options.query = {
    //     card_id: 'p1uoyw6NFBFdiHK8mk84tPBkqqbc', // 测试时需要测试人员在nbr插入会员卡数据
    //     code: ''
    //   };
    //   console.log('app.js->option.query:->' + JSON.stringify(options.query));
    // }else{ // TODO 用于通过微信下滑搜索途径进入小程序
    //   options.query = {
    //     card_id: '',
    //     code: '' // 
    //   };
    //   console.log('app.js->option.query:->' + JSON.stringify(options.query));
    // }
    let resultScene = this.sceneInfo(options, options.scene);
    console.log('resultScene:->' + resultScene);
  },
  //场景值判断
  sceneInfo: function (options, scene) {
    var result = [];
    switch (scene) {
      case 1001:
        result.push(scene, "发现栏小程序主入口");
        // 
        // wx.setStorage({ // 把card_id写入缓存
        //   key: 'storageVipCardID',
        //   data: options.query.card_id
        // });
        // wx.setStorage({ // 把code写入缓存
        //   key: 'storageCode',
        //   data: options.query.code
        // });
        break;
      case 1005:
        result.push(scene, "顶部搜索框的搜索结果页");
        break;
      case 1006:
        result.push(scene, "发现栏小程序主入口搜索框的搜索结果页");
        break;
      case 1007:
        result.push(scene, "单人聊天会话中的小程序消息卡片");
        // 
        wx.setStorage({ // 把card_id写入缓存
          key: 'storageVipCardID',
          data: options.query.card_id
        });
        wx.setStorage({ // 把code写入缓存
          key: 'storageCode',
          data: options.query.code
        });
        break;
      case 1008:
        result.push(scene, "群聊会话中的小程序消息卡片");
        break;
      case 1011:
        result.push(scene, "扫描二维码");
        break;
      case 1012:
        result.push(scene, "长按图片识别二维码");
        break;
      case 1014:
        result.push(scene, "手机相册选取二维码");
        break;
      case 1017:
        result.push(scene, "前往体验版的入口页");
        break;
      case 1019:
        result.push(scene, "微信钱包");
        break;
      case 1020:
        result.push(scene, "公众号profile页相关小程序列表");
        break;
      case 1022:
        result.push(scene, "聊天顶部置顶小程序入口");
        break;
      case 1023:
        result.push(scene, "安卓系统桌面图标");
        break;
      case 1024:
        result.push(scene, "小程序profile页");
        break;
      case 1025:
        result.push(scene, "扫描一维码");
        break;
      case 1026:
        result.push(scene, "附近小程序列表");
        break;
      case 1027:
        result.push(scene, "顶部搜索框搜索结果页“使用过的小程序”列表");
        break;
      case 1028:
        result.push(scene, "我的卡包");
        // 因为小程序还没有发布，暂时把"scene=1001"当做是从我的卡包进入小程序
        wx.setStorage({ // 把card_id写入缓存
          key: 'storageVipCardID',
          data: options.query.card_id
        });
        wx.setStorage({ // 把code写入缓存
          key: 'storageCode',
          data: options.query.code
        });
        break;
      case 1029:
        result.push(scene, "卡券详情页");
        // 因为小程序还没有发布，暂时把"scene=1001"当做是从卡券详情页进入小程序
        wx.setStorage({ // 把card_id写入缓存
          key: 'storageVipCardID',
          data: options.query.card_id
        });
        wx.setStorage({ // 把code写入缓存
          key: 'storageCode',
          data: options.query.code
        });
        break;
      case 1031:
        result.push(scene, "长按图片识别一维码");
        break;
      case 1032:
        result.push(scene, "手机相册选取一维码");
        break;
      case 1034:
        result.push(scene, "微信支付完成页");
        break;
      case 1035:
        result.push(scene, "公众号自定义菜单");
        break;
      case 1036:
        result.push(scene, "App分享消息卡片");
        break;
      case 1037:
        result.push(scene, "小程序打开小程序");
        break;
      case 1038:
        result.push(scene, "从另一个小程序返回");
        break;
      case 1039:
        result.push(scene, "摇电视");
        break;
      case 1042:
        result.push(scene, "添加好友搜索框的搜索结果页");
        break;
      case 1044:
        result.push(scene, "带shareTicket的小程序消息卡片");
        break;
      case 1047:
        result.push(scene, "扫描小程序码");
        break;
      case 1048:
        result.push(scene, "长按图片识别小程序码");
        break;
      case 1049:
        result.push(scene, "手机相册选取小程序码");
        break;
      case 1052:
        result.push(scene, "卡券的适用门店列表");
        break;
      case 1053:
        result.push(scene, "搜一搜的结果页");
        break;
      case 1054:
        result.push(scene, "顶部搜索框小程序快捷入口");
        break;
      case 1056:
        result.push(scene, "音乐播放器菜单");
        break;
      case 1058:
        result.push(scene, "公众号文章");
        break;
      case 1059:
        result.push(scene, "体验版小程序绑定邀请页");
        break;
      case 1064:
        result.push(scene, "微信连Wifi状态栏");
        break;
      case 1067:
        result.push(scene, "公众号文章广告");
        break;
      case 1068:
        result.push(scene, "附近小程序列表广告");
        break;
      case 1072:
        result.push(scene, "二维码收款页面");
        break;
      case 1073:
        result.push(scene, "客服消息列表下发的小程序消息卡片");
        break;
      case 1074:
        result.push(scene, "公众号会话下发的小程序消息卡片");
        break;
      case 1089:
        result.push(scene, "微信聊天主界面下拉");
        break;
      case 1090:
        result.push(scene, "长按小程序右上角菜单唤出最近使用历史");
        break;
      case 1092:
        result.push(scene, "城市服务入口");
        break;
      default:
        result.push(scene, "未知入口");
        break;
    }
    return result;
  }, // 其中只有在传递 1020、1035、1036、1037、1038、1043 这几个场景值时，才会返回referrerInfo.appId
  //
  globalData: {
    userInfo: null,
    header: {
      // 'content-type': 'application/x-www-form-urlencoded',
      'content-Type': 'application/json;charset=UTF-8',
      // 'content-type': 'application/json', // 默认值
      'X-Requested-With': 'XMLHttpRequest',
      'cookie': ''
    },
    openid: '',
    phone: '',
    unionID: '',
    authSetting: {},
    version: '', // 微信版本号
    terminal: '', // 操作系统版本
    accessToken: '' //小程序的Token
  },
  //session过期后重新登录
  afreshLogin: function (loginUrl) {
    console.log("session过期了");
    wx.showModal({
      title: '提示',
      content: '登录过期，将自动重新登录',
      showCancel: false,
      success (res) {
        if (res.confirm) {
          wx.reLaunch({
            url: loginUrl,
            fail: function () {
              wx.showToast({
                title: '重新登录失败，请重新进入小程序',
                icon: 'none',
                duration: 2000,
                mask: true
              })
            }
          })
        }
      },
      fail (res) {
        wx.showToast({
          title: '登录过期，请重新进入小程序',
          icon: 'none',
          duration: 2000,
          mask: true
        })
      }
    })
  }
})