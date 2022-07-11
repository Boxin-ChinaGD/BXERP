// pages/details/bonusShop/bonusShop.js
const app = getApp();
var api = require('../../../utils/api.js');
var error = require('../../../utils/error.js');
var storage = require('../../../utils/storage.js');
var util = require('../../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    showThis:true,
    httpHeader: {},
    vipInfo: {},
    couponList: {},
    couponRNParams: { "posID": -2, "bonus": 1, "type": -1, "pageIndex": 1 }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session)
    });
    this.couponRN(this.data.couponRNParams);
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
    this.setData({
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
    });
    let that = this
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
          that.setData({ vipCardBackground: "linear-gradient(180deg,rgba(" + vipCardBackground[0] + ",1) 0%,rgba(" + vipCardBackground[1]   + ",1) 100%)" });
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
    this.data.couponRNParams.pageIndex = 1;
    this.onLoad();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("发送上拉触底事件");
    this.couponRN(this.data.couponRNParams);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  //查询优惠券并显示
  couponRN: function (params) {
    wx.showLoading({
      title: '加载中，请稍等',
      mask: true
    })
    let that = this;
    wx.request({
      url: api.couponRN,
      method: api.GET,
      header: that.data.httpHeader,
      data: params,
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../../login/login');
          return;
        }
        if (res.data.ERROR != error.EC_NoError) {
          wx.showToast({
            title: '查询优惠券失败，请重试',
            icon: 'none'
          })
          return;
        }
        if (res.data.objectList.length > 0) {
          for (var i = 0; i < res.data.objectList.length; i++) {
            res.data.objectList[i].beginDateTime = res.data.objectList[i].beginDateTime.substring(0, res.data.objectList[i].beginDateTime.length - 4);
            res.data.objectList[i].endDateTime = res.data.objectList[i].endDateTime.substring(0, res.data.objectList[i].endDateTime.length - 4);
          }
          if (that.data.couponRNParams.pageIndex > 1) {
            console.log("拼接数组")
            that.setData({
              couponList: that.data.couponList.concat(res.data.objectList)
            });
            console.log(that.data.couponList)
          } else {
            console.log("初始数组")
            that.setData({
              couponList: res.data.objectList
            });
            console.log(that.data.couponList)
          }
          that.data.couponRNParams.pageIndex = that.data.couponRNParams.pageIndex + 1;
          that.setData({
            couponRNParams: that.data.couponRNParams
          });
        } else {
          if (that.data.couponRNParams.pageIndex == 1) {
            that.setData({
              showThis: false,
            });
            wx.showToast({
              title: '暂无优惠券',
              icon: 'none'
            })
          } else {
            wx.showToast({
              title: '已到最底了',
              icon: 'none'
            })
          }
        }
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '查询优惠券失败，请重试',
          icon: 'none'
        })
      },
      complete(res) {
        wx.hideLoading();
      }
    })
  },

  //查看优惠券详情
  couponDetails: function (e) {
    console.log(e);
    wx.showLoading({
      title: '跳转中，请稍等',
      mask: true
    })
    wx.navigateTo({
      url: '../../details/coupon/details/details?info=' + JSON.stringify(e.currentTarget.dataset.info) + '&ifShow=false&code=',
      success: function (res) {
        console.log("进入优惠券详情页成功");
      },
      fail: function (res) {
        console.log("进入优惠券详情页失败");
      },
      complete: function (res) {
        wx.hideLoading();
      }
    })
  },

  //兑换优惠券
  getCoupon: function (e) {
    console.log(e);
    let that = this;
    if (that.data.vipInfo.bonus > e.currentTarget.dataset.info.bonus) {
      wx.request({
        url: api.getCoupon,
        method: api.POST,
        header: {
          'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
          'X-Requested-With': 'XMLHttpRequest',
          'cookie': that.data.httpHeader.cookie
        },
        data: { "vipID": that.data.vipInfo.ID, "couponID": e.currentTarget.dataset.info.ID },
        success(res) {
          console.log(res);
          if (res.header.sessionStatus == "timeOut") {
            app.afreshLogin('../../login/login');
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
              that.data.vipInfo.bonus = that.data.vipInfo.bonus - e.currentTarget.dataset.info.bonus;
              that.setData({
                vipInfo: that.data.vipInfo
              });
              util.writeStorage(storage.DEFAULT_VIP_INFO, that.data.vipInfo); //将vipInfo放入缓存
              wx.showLoading({
                title: '跳转中，请稍等',
                mask: true
              })
              wx.navigateTo({
                url: '../../details/coupon/details/details?info=' + JSON.stringify(e.currentTarget.dataset.info) + '&ifShow=true&code=' + res.data.object.SN,
                success: function (res) {
                  console.log("进入优惠券详情页成功");
                },
                fail: function (res) {
                  console.log("进入优惠券详情页失败");
                },
                complete: function (res) {
                  wx.hideLoading();
                }
              })
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
  }
})