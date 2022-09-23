//index.js
//获取应用实例
const app = getApp();
var api = require('../../utils/api.js');
var error = require('../../utils/error.js');
var miniprogramBarcode = require('../../utils/wxbarcode.js');
var storage = require('../../utils/storage.js');
var util = require('../../utils/util.js');

Page({
  data: {
    httpHeader: {},
    userInfo: {},
    companyInfo: {},
    vipInfo: {},
    vipCardBackgroundImg: "../../images/default_brand_logo.png" // 默认门店招牌图片
  },
  onLoad: function (options) {
    let that = this;
    console.log(options)
    console.log(wx.getStorageSync(storage.DEFAULT_VIP_INFO))
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      userInfo: wx.getStorageSync(storage.WX_USER_Info),
      companyInfo: wx.getStorageSync(storage.DEFAULT_Company),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
    });
    console.log(this.data.vipInfo.vipCardSN)
    miniprogramBarcode.barcode('vipCardSn', this.data.vipInfo.vipCardSN, 614, 156);
    console.log(this.data.httpHeader)
    console.log(this.data.userInfo)
  },
  onShow: function () {
    let that = this;
    // wx.showLoading({
    //   title: "C" + that.data.companyInfo.brandName,
    //   mask: true
    // })
    // setTimeout(() => {
    //   wx.hideLoading();
    // }, 3000);
    // let that = this;
    // var obj = wx.getLaunchOptionsSync()
    // console.log('启动小程序的参数', obj);
    // if (obj.scene == 1047 || obj.scene == 1031 || obj.scene == 1032) {    //判断是否是扫码登录
    //   if (obj.query.scene == undefined || obj.query.scene.trim() == "") {   //判断扫的二维码是否有参数，如果无就登陆668866虚拟门店
    //     that.setData({
    //       compangSN: 668866,
    //     });
    //   } else {
    //     var str = obj.query.scene;
    //     var compangSn = str.slice(13);
    //     that.setData({
    //       compangSN: compangSn,
    //       scene: obj.scene,
    //     });
    //   }
    // } else {
    //   if (wx.getStorageSync(storage.DEFAULT_Company).SN) {
    //     that.setData({
    //       compangSN: wx.getStorageSync(storage.DEFAULT_Company).SN,
    //     });
    //   } else {
    //     that.setData({
    //       compangSN: '668866',
    //     });
    //   }
    // }
    //查询会员卡属性
    wx.request({
      url: api.vipCardR1,
      method: api.GET,
      header: that.data.httpHeader,
      data: { 'ID': 1 },  //目前每个公司or门店只有一种会员卡默认传1
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../login/login');
        } else {
          if (res.data.ERROR != error.EC_NoError) {
            wx.showToast({
              title: '查询会员卡属性失败',
              icon: 'none'
            })
            return;
          }
          var vipCardInfo = res.data.object;
          var vipCardBackground = vipCardInfo.backgroundColor.split(";")
          that.setData({ vipCardBackground: "linear-gradient(180deg,rgba(" + vipCardBackground[0] + ",1) 0%,rgba(" + vipCardBackground[1] + ",1) 100%)" });
        }
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '查询会员卡属性失败',
          icon: 'none'
        })
      }
    });
    //查看会员拥有的优惠券数量
    wx.request({
      url: api.couponTotal,
      method: api.POST,
      header: {
        'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest',
        'cookie': that.data.httpHeader.cookie
      },
      data: { 'vipID': that.data.vipInfo.ID },
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          console.log("session过期，第一个请求会触发返回登录页的函数。");
        } else {
          if (res.data.ERROR != error.EC_NoError) {
            wx.showToast({
              title: '查询拥有的优惠券数量失败',
              icon: 'none'
            })
            return;
          }
          that.setData({
            couponTotal: res.data.count
          });
        }
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '查询拥有的优惠券数量失败',
          icon: 'none'
        })
      }
    });
    //更新会员信息
    wx.request({
      url: api.vipR1,
      method: api.GET,
      header: that.data.httpHeader,
      data: { 'ID': that.data.vipInfo.ID },
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          console.log("session过期，第一个请求会触发返回登录页的函数。");
        } else {
          if (res.data.ERROR != error.EC_NoError) {
            wx.showToast({
              title: '更新会员信息失败',
              icon: 'none'
            })
            return;
          }
          util.writeStorage(storage.DEFAULT_VIP_INFO, res.data.vip);
          that.setData({
            vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
          });
        }
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '更新会员信息失败',
          icon: 'none'
        })
      }
    });
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    // let that = this;
    // let card_id = wx.getStorageSync('storageVipCardID')
    // return {
    //   title: '测试好友分享',
    //   path: '/pages/login/login?card_id=' + card_id,
    //   // imageUrl: '../../images/name.png',
    //   success: function(res){
    //     // 转发成功
    //     wx.showToast({
    //       title: '分享成功',
    //       icon: 'success',
    //       duration: 2000
    //     });
    //   },
    //   fail: function(res){
    //     // 转发失败
    //     wx.showToast({
    //       title: '分享失败',
    //       icon: 'fail',
    //       duration: 2000
    //     });
    //   }
    // };
    // console.log(path);
  },

  //查看我的优惠券
  myCouponRN: function () {
    console.log("couponRN")
    wx.navigateTo({
      url: '../details/myCoupon/myCoupon',
      fail: function () {
        wx.showToast({
          title: '查看我的优惠券失败，请重试',
          icon: 'fail',
        });
      }
    });
  },

  //查看积分明细
  mybonusRN: function (e) {
    console.log("bonusRn")
    wx.navigateTo({
      url: '../details/bonusHistory/bonusHistory',
      fail: function () {
        wx.showToast({
          title: '查看积分明细失败，请重试',
          icon: 'fail',
        });
      }
    });
  },

  /**
   * 选择商家
   */
  selectShop: function (e) {
    wx.navigateTo({
      url: '../details/selectShop/selectShop',
      fail: function () {
        console.log("进入商家选择页面失败");  //TODO 考虑刷新页面
      }
    });
  }
})