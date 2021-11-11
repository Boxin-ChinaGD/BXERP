// pages/details/coupon/coupon.js
const app = getApp();
var api = require('../../../utils/api.js');
var error = require('../../../utils/error.js');
var storage = require('../../../utils/storage.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    showThis:true,
    background: ["../../../images/swiper1.png", "../../../images/swiper2.png", "../../../images/swiper3.png"],  //轮播图
    currentIndex: -1,  //当前选择的tab选项索引值
    httpHeader: {},
    vipInfo: {},
    couponList: [],
    couponRNParams: { "posID": -2, "bonus": 0, "type": -1, "pageIndex": 1 }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
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
    let that = this;
    console.log(this.data.couponList)
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

  // 用户点击领券中心的tab选项
  tabClick: function (e) {
    console.log(e);
    if (this.data.currentIndex != e.currentTarget.dataset.click) {
      this.setData({
        currentIndex: e.currentTarget.dataset.click
      });
      this.data.couponRNParams.pageIndex = 1;
      this.data.couponRNParams.type = e.currentTarget.dataset.click;
      this.setData({
        couponRNParams: this.data.couponRNParams
      });
      this.couponRN(this.data.couponRNParams);
    }
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
            that.setData({
              showThis: true,
            });
            console.log("拼接数组")
            that.setData({
              couponList: that.data.couponList.concat(res.data.objectList)
            });
            console.log(that.data.couponList)
          } else {
            that.setData({
              showThis: true,
            });
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
            that.setData({
              couponList: []
            });
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
  //跳转到积分商城
  toBonusShop: function (e) {
    console.log("toBonusShop")
    wx.navigateTo({
      url: '../bonusShop/bonusShop',
      fail: function () {
        wx.showToast({
          title: '跳转积分商城失败，请重试',
          icon: 'fail',
        });
      }
    });
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

  //领取优惠券
  getCoupon: function (e) {
    console.log(e);
    let that = this;
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
            title: res.data.msg == "" ? '领取优惠券失败，请重试' : res.data.msg,
            icon: 'none'
          })
          return;
        }
        wx.showToast({
          title: '领取优惠券成功',
          icon: 'none',
          success: function () {
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
          title: '领取优惠券失败，请重试',
          icon: 'none'
        })
      }
    })
  }
})