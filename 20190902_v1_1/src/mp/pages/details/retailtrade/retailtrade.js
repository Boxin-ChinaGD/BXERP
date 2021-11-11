// pages/details/retailtrade/retailtrade.js
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
    httpHeader: {},
    vipInfo: {},
    retailtradeList: [],
    retailtradeRNParams: { 'pageIndex': 1, 'queryKeyword': '' }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var timeStart = this.getStartTime();
    var timeEnd = this.getEndtTime()
    console.log(timeEnd)
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
    });
    this.data.retailtradeRNParams.vipID = this.data.vipInfo.ID;
    this.data.retailtradeRNParams.datetimeStart = timeStart;
    this.data.retailtradeRNParams.datetimeEnd = timeEnd;
    this.setData({
      retailtradeRNParams: this.data.retailtradeRNParams
    });
    this.retailtradeRN(this.data.retailtradeRNParams);
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
    this.data.retailtradeRNParams.pageIndex = 1;
    this.onLoad();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("发送上拉触底事件");
    this.retailtradeRN(this.data.retailtradeRNParams);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  //获取当月开始时间并转换格式
  getStartTime:function () {
    var dateTime = new Date()
    var year = dateTime.getFullYear();
    var month = dateTime.getMonth() + 1;
    var day = '01';
    var hour = '00';
    var minute = '00';
    var second = '00';
    var timeSpanStr = year + '/' + month + '/' + day + ' ' + hour + ':' + minute + ':' + second;
    return timeSpanStr;
  },
  //获取当月结束时间并转换格式
  getEndtTime: function () {
    var dateTime = new Date()
    var year = dateTime.getFullYear();
    var month = dateTime.getMonth() + 1;
    var day = new Date(year, month, 0).getDate();
    var hour = '23';
    var minute = '59';
    var second = '59';
    var timeSpanStr = year + '/' + month + '/' + day + ' ' + hour + ':' + minute + ':' + second;
    return timeSpanStr;
  },
  //查询零售单
  retailtradeRN: function (params) {
    console.log(params)
    wx.showLoading({
      title: '加载中，请稍等',
      mask: true
    })
    let that = this;
    wx.request({
      url: api.retailtradeRN,
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
            title: '查询零售单失败，请重试',
            icon: 'none'
          })
          return;
        }
        if (res.data.objectList.length > 0) {
          for (var i = 0; i < res.data.objectList.length; i++) {
            res.data.objectList[i].saleDatetime = res.data.objectList[i].saleDatetime.substring(0, res.data.objectList[i].saleDatetime.length - 4);
          }
          if (that.data.retailtradeRNParams.pageIndex > 1) {
            console.log("拼接数组")
            that.setData({
              retailtradeList: that.data.retailtradeList.concat(res.data.objectList)
            });
            console.log(that.data.retailtradeList)
          } else {
            console.log("初始数组")
            that.setData({
              retailtradeList: res.data.objectList
            });
            console.log(that.data.retailtradeList)
          }
          that.data.retailtradeRNParams.pageIndex = that.data.retailtradeRNParams.pageIndex + 1;
          that.setData({
            retailtradeRNParams: that.data.retailtradeRNParams
          });
        } else {
          if (that.data.retailtradeRNParams.pageIndex == 1) {
            that.setData({
              showThis: false,
            });
            wx.showToast({
              title: '暂无零售单',
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
          title: '查询零售单失败，请重试',
          icon: 'none'
        })
      },
      complete(res) {
        wx.hideLoading();
      }
    })
  }
})