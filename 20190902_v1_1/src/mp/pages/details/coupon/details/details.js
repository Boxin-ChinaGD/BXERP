// pages/details/coupon/details/details.js
const app = getApp();
var api = require('../../../../utils/api.js');
var error = require('../../../../utils/error.js');
var miniprogramBarcode = require('../../../../utils/wxbarcode.js');
var storage = require('../../../../utils/storage.js');
var util = require('../../../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    httpHeader: {},
    serverURL: "",
    companyInfo: {},
    vipInfo: {},
    couponDetails: {},
    showCouponCode: false,
    couponCode: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      serverURL: api.server,
      companyInfo: wx.getStorageSync(storage.DEFAULT_Company),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO),
      couponDetails: JSON.parse(options.info),
      showCouponCode: JSON.parse(options.ifShow),
      couponCode: options.code
    });
    miniprogramBarcode.barcode('couponCode', options.code, 616, 156);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  //积分兑换优惠券
  bonusExchangeCoupon: function () {
    let that = this;
    if (that.data.vipInfo.bonus > that.data.couponDetails.bonus) {
      wx.request({
        url: api.getCoupon,
        method: api.POST,
        header: {
          'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
          'X-Requested-With': 'XMLHttpRequest',
          'cookie': that.data.httpHeader.cookie
        },
        data: { "vipID": that.data.vipInfo.ID, "couponID": that.data.couponDetails.ID },
        success(res) {
          console.log(res);
          if (res.header.sessionStatus == "timeOut") {
            app.afreshLogin('../../../login/login');
            return;
          }
          if (res.data.ERROR != error.EC_NoError) {
            wx.showToast({
              title: res.data.msg == "" ? '兑换优惠券失败，请重试' : res.data.msg,
              icon: 'none'
            })
            return;
          }
          wx.showToast({
            title: '兑换优惠券成功',
            icon: 'none',
            success: function () {
              that.data.vipInfo.bonus = that.data.vipInfo.bonus - that.data.couponDetails.bonus;
              that.setData({
                vipInfo: that.data.vipInfo,
                showCouponCode: true,
                couponCode: res.data.object.SN
              });
              miniprogramBarcode.barcode('couponCode', res.data.object.SN, 616, 156);
              util.writeStorage(storage.DEFAULT_VIP_INFO, that.data.vipInfo);
            }
          })
        },
        fail(res) {
          console.log(res);
          wx.showToast({
            title: '兑换优惠券失败，请重试',
            icon: 'none'
          })
        }
      })
    } else {
      wx.showToast({
        title: '积分不足',
        icon: 'none'
      })
    }
  },

  //领取优惠券
  getCoupon: function () {
    let that = this;
    wx.request({
      url: api.getCoupon,
      method: api.POST,
      header: {
        'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest',
        'cookie': that.data.httpHeader.cookie
      },
      data: { "vipID": that.data.vipInfo.ID, "couponID": that.data.couponDetails.ID },
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../../../login/login');
          return;
        }
        if (res.data.ERROR != error.EC_NoError) {
          wx.showToast({
            title: res.data.msg == "" ? '领取优惠券失败，请重试' : res.data.msg,
            icon: 'none'
          })
          return;
        }
        wx.showToast({
          title: '领取优惠券成功',
          icon: 'none',
          success: function () {
            that.setData({
              showCouponCode: true,
              couponCode: res.data.object.SN
            });
            miniprogramBarcode.barcode('couponCode', res.data.object.SN, 616, 156);
          }
        })
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '领取优惠券失败，请重试',
          icon: 'none'
        })
      }
    })
  }
})