//login.js

//获取应用实例
const app = getApp();
var api = require('../../utils/api.js');
var error = require('../../utils/error.js');
var storage = require('../../utils/storage.js');
var util = require('../../utils/util.js');
//
Page({
  data: {
    decryptSessionCode: '',  //通过wx.login获取的code
    encrypted_UnionID: '',  //包括敏感数据在内的完整用户信息的加密数据,从getUserInfo接口获得，命名保留意见
    iv_UnionID: '',  //加密算法的初始向量，从getUserInfo接口获得，命名保留意见
    showModal: false,  //true：出现获取手机号弹窗，false：隐藏获取手机号弹窗
    showShopName: false, //true：显示商家名称，false：隐藏商家名称
    httpHeader: {},
    scene: '',
    companySN: ''
  },
  //
  onLoad: function (options) {
    let that = this;
    console.log(that.data.companySN);
    console.log('login.js->options:->' + JSON.stringify(options));
    wx.login({ // 
      success(res) {
        console.log(res);
        that.setData({ decryptSessionCode: res.code });
      },
      fail(res) { // 这里需要处理调用接口失败的情况
        console.log('调用wx.login失败');
      }
    });
  },
  onShow: function () {
    // 判断有没有授权就是查看小程序缓存
    // 1、未授权:弹出授权框->发送登录请求->跳转到首页
    // 2、已授权:发送登录请求->跳转到首页
    let that = this;
    var obj = wx.getLaunchOptionsSync(); 
    console.log('启动小程序的参数', obj);
    if (obj.scene == 1047 || obj.scene == 1031 || obj.scene == 1032 || obj.scene == 1048 || obj.scene == 1049) {    //判断是否是扫码登录
      if (obj.query.scene == undefined || obj.query.scene.trim() == "") {   //判断扫的二维码是否有参数，如果无就登陆668866虚拟门店 
        that.setData({
          companySN: "668866",
        });

      } else {
        var sceneSN = obj.query.scene.slice(-6).toString();
        console.log(sceneSN)
        that.setData({
          companySN: sceneSN.toString(),
        });
      }
      // wx.showLoading({
      //   title: "A" + that.data.companySN.slice(4) + "-" + obj.scene,
      //   mask: true
      // });
      // setTimeout(() => {
      //   wx.hideLoading();
      // }, 2000);

    } else { // 非扫码进入小程序
      if (wx.getStorageSync(storage.DEFAULT_Company).SN) {
        that.setData({
          companySN: wx.getStorageSync(storage.DEFAULT_Company).SN,
        });
      } else {
        that.setData({
          companySN: '668866',
        });
      }
      // wx.showLoading({
      //   title: "B" + that.data.companySN.slice(4)+ "-" + obj.scene,
      //   mask: true
      // });
      // setTimeout(() => {
      //   wx.hideLoading();
      // }, 2000);
    }
    let authorise = wx.getStorageSync(storage.AUTHORISED);
    if (authorise == 1) {
      that.autoLogin();
    } else {
      console.log("未授权：" + authorise + ",清空本地缓存");
      try {
        wx.clearStorageSync();
      } catch (e) {
        console.log("清空本地缓存失败");
      }
    }
  },
  // 通过按钮让微信用户授权以获取其信息
  onGetUserInfo: function (e) {
    var that = this;
    wx.getUserInfo({
      success(res) {
        console.log(res);
        if (res.errMsg == 'getUserInfo:ok') {
          //将信息放入缓存
          util.writeStorage(storage.WX_USER_Info, res.userInfo);
          that.setData({
            encrypted_UnionID: res.encryptedData,
            iv_UnionID: res.iv
          });
          // that.obtainAuthorizationInformation();
          that.setData({ showModal: true });  //显示授权手机号弹窗
        } else {
          wx.showToast({
            title: '授权用户信息失败，请重试',
            icon: 'none'
          })
        }
        // that.setData({
        //   encrypted_UnionID: res.encryptedData,
        //   iv_UnionID: res.iv
        // });
      },
      fail(res) {
        wx.showToast({
          title: '获取用户基本信息失败',
          icon: 'none'
        })
        console.log('获取用户基本信息失败!');
        util.writeStorage(storage.AUTHORISED, 0);
      }
    });
  },
  // 绑定手机号
  getPhoneNumber: function (e) {
    console.log("xxxxxxxxxxxxxxxx");
    var that = this;
    that.setData({ showModal: false });  //隐藏获取手机号弹窗;
    wx.checkSession({
      success: function (res) { // session_key没有过期
        console.log(res);
        var loginParams = {
          'decryptSessionCode': that.data.decryptSessionCode, // 解密需要的参数
          'encryptedUnionID': that.data.encrypted_UnionID, // 加密的unionID
          'ivUnionID': that.data.iv_UnionID, // 解密需要的参数
          'encryptedPhone': e.detail.encryptedData, // 加密的手机号
          'ivPhone': e.detail.iv, // 解密需要的参数
          'mobile': "",
          'companySN': that.data.companySN
        };
        that.login(loginParams);
      },
      fail: function (res) { // session_key已过期,重新获取
        console.log(res);
        wx.login({
          success(res) {
            that.setData({ decryptSessionCode: res.code });
          },
          fail(res) { // 这里需要处理调用接口失败的情况
            console.log('调用wx.login失败');
          }
        });
        wx.showToast({
          title: '授权手机号失败，请重试',
          icon: 'none'
        })
      }
    });
  },

  //更新缓存
  autoLogin: function () {
    let that = this;
    wx.login({
      success: function (res) {
        console.log(res.code);
        var loginParams = {
          'decryptSessionCode': res.code, // 解密需要的参数
          'encryptedUnionID': '', // 加密的unionID
          'ivUnionID': '', // 解密需要的参数
          'encryptedPhone': '', // 加密的手机号
          'ivPhone': '', // 解密需要的参数
          'mobile': wx.getStorageSync(storage.WX_USER_Mobile).toString(),
          'companySN': that.data.companySN
        };
        that.login(loginParams);
      },
      fail(res) { // TODO 这里需要处理调用接口失败的情况
        console.log('调用wx.login失败');
      }
    })
  },

  onLoginSuccess: function (res) {
    let that = this;
    wx.removeStorage({
      key: storage.DEFAULT_Company,
      success: function (res) {
        console.log(res.data)
      }
    })
    wx.removeStorage({
      key: storage.DEFAULT_VIP_INFO,
      success: function (res) {
        console.log(res.data)
      }
    })
    console.log(wx.getStorageSync(storage.DEFAULT_VIP_INFO))
    console.log(wx.getStorageSync(storage.DEFAULT_Company))
    if (res.data.objectList) {
      var objList = res.data.objectList;
      util.writeStorage(storage.VIP_CompanyList, res.data.objectList);
      for (var i = 0; i < Object.keys(objList).length; i++) {
        console.log(objList[i].SN)
        if (objList[i].SN.toString() === that.data.companySN.toString()) {
          console.log('当前公司的信息：' + objList[i]);
          util.writeStorage(storage.DEFAULT_Company, wx.getStorageSync(storage.VIP_CompanyList)[i]);
          util.writeStorage(storage.DEFAULT_VIP_INFO, res.data.object);
          break;
        };
      };
    } else {  // vip只有一家公司
      util.writeStorage(storage.DEFAULT_Company, res.data.object2);
      util.writeStorage(storage.DEFAULT_VIP_INFO, res.data.object);
    }
    that.writeCommonStorageOnLoginSuccess(res);
    wx.reLaunch({
      url: '../index/index',
      success: function (res) {
        console.log("进入登录后的首页成功");
      },
      fail: function (res) {
        console.log("进入登录后的首页失败");
      },
      complete: function (res) {
        wx.hideLoading();
      }
    })
  },

  // 登录成功后，无论是有多个公司还是1个公司，共同都要写的storage
  writeCommonStorageOnLoginSuccess: function (res) {
    util.writeStorage(storage.AUTHORISED, 1);
    util.writeStorage(storage.WX_USER_Mobile, res.data.mobile);
    util.writeStorage(storage.WX_USER_UnionID, res.data.unionID);
    util.writeStorage(storage.WX_USER_Openid, res.data.openid);

    if (res.cookies != null) { // TODO 需要处理nbr会话失效的情况
      app.globalData.header.cookie = res.header["Set-Cookie"];
      console.log(app.globalData.header);

      util.writeStorage(storage.NBR_Session, app.globalData.header);

      app.globalData.header.cookie = "";
    }
  },

  onRequestSuccess: function (res) {
    let that = this
    console.log('登录请求成功（不是登录成功），服务器返回：', res);
    if (res.data.ERROR != error.EC_NoError) { // TODO 这里需要处理获取openid失败的情况
      wx.hideLoading();
      wx.showToast({
        title: '登录失败，请重试',
        icon: 'none'
      })
      console.log('请求' + api.login + '失败!ERROR:' + res.data.ERROR + ',msg:' + res.data.msg);
      return;
    };
    that.onLoginSuccess(res);
  },


  // 第一次登录，非自动登录
  login: function (loginParams) {
    // let header = wx.getStorageSync('storageHeader'); // TODO 需要处理nbr会话失效的情况
    let that = this;
    wx.showLoading({
      title: '登录中，请稍等',
      mask: true
    })
    wx.request({
      url: api.login,
      method: api.POST,
      header: app.globalData.header,
      data: loginParams,
      success(res) {
        console.log(res);
        console.log(res.data.objectList2);
        util.writeStorage(storage.Company_Address, res.data.objectList2);
        that.onRequestSuccess(res)
         
      },
      fail(res) { // 这里需要处理调用接口失败的情况
        console.log(res);
        wx.hideLoading();
        wx.showToast({
          title: '登录失败，请重试',
          icon: 'none'
        })
        return null;
      }
    });
  },



  // 显示一键获取手机号弹窗
  // showDialogBtn: function () {
  //   this.setData({ showModal: true });
  // },
  // 隐藏一键获取手机号弹窗
  // hideModel: function () {
  //   this.setData({ showModal: false });
  // },

  // 未授权登录时页面跳转按钮的提示函数
  noLoginTip: function () {
    wx.showToast({
      title: '请先进行登录',
      icon: 'none'
    })
  },
  // 点击其他区域关闭授权手机号弹窗
  closeModal: function () {
    this.setData({ showModal: false });
  }

  // obtainAuthorizationInformation() {
  //   let that = this;
  //   //获取用户的授权信息，放到本地缓存中
  //   wx.getSetting({
  //     success: (res) => {
  //       console.log(res);
  //       if (res.authSetting['scope.userInfo']) {
  //         wx.setStorage({
  //           key: 'authorise',
  //           data: 1,
  //         })
  //       }
  //       else {
  //         wx.setStorage({
  //           key: 'authorise',
  //           data: 0,
  //         })
  //       }
  //     }
  //   });
  //   //1:从本地缓存中获取数据，如果获取到根据结果显示login页面的授权按钮是否显示，
  //   //2:如果从本地缓存获取不到数据则说明用户清空了本地数据，默认设置为0，让用户重新授权
  //   wx.getStorage({
  //     key: 'authorise',
  //     success: function (res) {
  //       console.log(res.data);
  //       that.setData({
  //         authorise: res.data  //0,1
  //       });
  //     }, fail: function () {
  //       that.setData({
  //         authorise: 0
  //       });
  //     }
  //   });
  // },

  //事件处理函数
  // bindViewTap: function () {
  //   wx.navigateTo({
  //     url: '../logs/logs'
  //   })
  //   // 获取用户手机配置信息
  //   wx.getSystemInfo({
  //     success(res) {
  //       app.globalData.terminal = res.model;
  //       app.globalData.version = res.version;
  //     },
  //     fail(res) { // 这里需要处理调用接口失败的情况
  //       console.log(res);
  //       console.log('获取用户手机配置失败!');
  //     }
  //   });
  // },
})