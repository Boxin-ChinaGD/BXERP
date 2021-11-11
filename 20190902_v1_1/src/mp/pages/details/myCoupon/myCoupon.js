// pages/details/myCoupon/myCoupon.js
const app = getApp();
var api = require('../../../utils/api.js');
var error = require('../../../utils/error.js');
var storage = require('../../../utils/storage.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    httpHeader: {},
    vipInfo: {},
    myCouponList: [],
    myCouponRNParams: { "vipID": "", "subStatus": 0, "pageIndex": 1 },   
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
    });
    this.data.myCouponRNParams.vipID = this.data.vipInfo.ID;
    console.log(this.data.myCouponRNParams);
    this.setData({
      myCouponRNParams: this.data.myCouponRNParams
    });
    this.myCouponRN(this.data.myCouponRNParams);
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
    this.data.myCouponRNParams.pageIndex = 1;
    this.onLoad();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("发送上拉触底事件");
    this.myCouponRN(this.data.myCouponRNParams);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  // 用户点击我的优惠券的tab选项
  tabClick: function (e) {
    console.log(e);
    if (this.data.currentIndex != e.currentTarget.dataset.click) {
      this.setData({
        currentIndex: e.currentTarget.dataset.click
      });
      this.data.myCouponRNParams.pageIndex = 1;
      this.data.myCouponRNParams.subStatus = e.currentTarget.dataset.click;
      this.setData({
        myCouponRNParams: this.data.myCouponRNParams
      });
      console.log(this.data.myCouponRNParams);
      this.myCouponRN(this.data.myCouponRNParams);
    }
  },

  //查询优惠券并显示
  myCouponRN: function (params) {
    wx.showLoading({
      title: '加载中，请稍等',
      mask: true
    })
    let that = this;
    wx.request({
      url: api.myCouponRN,
      method: api.POST,
      header: {
        'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest',
        'cookie': that.data.httpHeader.cookie
      },
      data: params,
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../../login/login');
          return;
        }
        if (res.data.ERROR != error.EC_NoError) {
          wx.showToast({
            title: '查询我的优惠券失败，请重试',
            icon: 'none'
          })
          return;
        }
        if (res.data.objectList.length > 0) {
          var couponList = res.data.objectList2;
          var couponCodeList = res.data.objectList;
          var dataList = [];
          for (var i = 0; i < couponCodeList.length; i++) {
            var indexObject = {};
            var show = true ;
            var notUsed = true;
            indexObject.thisUseStatus = show;
            indexObject.thisDueStatus = show;

            indexObject.SN = couponCodeList[i].SN;
            if (params.subStatus == 1) {
              indexObject.status = "未使用";
              indexObject.color = "#FA5151";
            } else if (params.subStatus == 2) {
              indexObject.status = "已使用";
              indexObject.color = "#eee";
              indexObject.thisUseStatus = !show;
              
            } else if (params.subStatus == 3) {
              indexObject.status = "已过期";
              indexObject.color = "#eee";
              
            } else {
              if (couponCodeList[i].status == 0) {
                indexObject.status = "未使用";
                indexObject.color = "#FA5151";
              } else if (couponCodeList[i].status == 1) {
                indexObject.status = "已使用";
                indexObject.color = "#eee";
                indexObject.thisUseStatus = !show;
              } else {
                indexObject.status = "未知状态";
                indexObject.color = "#eee"; 
              }
            }
            for (var j = 0; j < couponList.length; j++) {
              if (couponCodeList[i].couponID == couponList[j].ID) {
                indexObject.title = couponList[j].title;
                indexObject.type = couponList[j].type;
                indexObject.reduceAmount = couponList[j].reduceAmount;
                indexObject.discount = couponList[j].discount;
                indexObject.leastAmount = couponList[j].leastAmount;
                indexObject.scope = couponList[j].scope;
                indexObject.description = couponList[j].description;
                indexObject.beginDateTime = couponList[j].beginDateTime.substring(0, couponList[j].beginDateTime.length - 4);
                indexObject.endDateTime = couponList[j].endDateTime.substring(0, couponList[j].endDateTime.length - 4);
                var nowTime = new Date().getTime();
                var couponTime = new Date(indexObject.endDateTime).getTime();
                if (nowTime > couponTime) {
                  indexObject.status = "已过期";
                  indexObject.color = "#eee";
                  indexObject.thisDueStatus = !show;
                }
                dataList[i] = indexObject;
              }
            }
          }
          if (that.data.myCouponRNParams.pageIndex > 1) {
            console.log("拼接数组")
            that.setData({
              myCouponList: that.data.myCouponList.concat(dataList)
            });
            console.log(that.data.myCouponList)
          } else {
            console.log("初始数组")
            that.setData({
              myCouponList: dataList
            });
            console.log(that.data.myCouponList)
          }
          that.data.myCouponRNParams.pageIndex = that.data.myCouponRNParams.pageIndex + 1;
          that.setData({
            myCouponRNParams: that.data.myCouponRNParams
          });
        } else {
          if (that.data.myCouponRNParams.pageIndex == 1) {
            wx.showToast({
              title: '暂无优惠券,请前往领券中心领取',
              icon: 'none'
            })
            that.setData({
              myCouponList: []
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
          title: '查询我的优惠券失败，请重试',
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
  myCouponDetails: function (e) {
    console.log(e);
    wx.showLoading({
      title: '跳转中，请稍等',
      mask: true
    })
    wx.navigateTo({
      url: '../../details/myCoupon/details/details?info=' + JSON.stringify(e.currentTarget.dataset.info),
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