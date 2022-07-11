// pages/details/bonusHistory/bonusHistory.js
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
    bonusHistoryList: [],
    bonusHistoryRNParams: { 'vipName': '', 'vipMobile': '', 'pageIndex': 1 }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      vipInfo: wx.getStorageSync(storage.DEFAULT_VIP_INFO)
    });
    this.data.bonusHistoryRNParams.vipID = this.data.vipInfo.ID;
    this.setData({
      bonusHistoryRNParams: this.data.bonusHistoryRNParams
    });
    this.bonusHistoryRN(this.data.bonusHistoryRNParams);
    console.log(this.data.vipInfo)
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
    this.data.bonusHistoryRNParams.pageIndex = 1;
    this.onLoad();
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("发送上拉触底事件");
    this.bonusHistoryRN(this.data.bonusHistoryRNParams);
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  //查询积分明细
  bonusHistoryRN: function (params) {
    console.log(params)
    wx.showLoading({
      title: '加载中，请稍等',
      mask: true
    })
    let that = this;
    wx.request({
      url: api.bonusHistoryRN,
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
            title: '查询积分明细失败，请重试',
            icon: 'none'
          })
          return;
        }
        if (res.data.objectList.length > 0) {
          for (var i = 0; i < res.data.objectList.length; i++) {
            res.data.objectList[i].createDatetime = res.data.objectList[i].createDatetime.substring(0, res.data.objectList[i].createDatetime.length - 4);
          }
          if (that.data.bonusHistoryRNParams.pageIndex > 1) {
            console.log("拼接数组")
            that.setData({
              bonusHistoryList: that.data.bonusHistoryList.concat(res.data.objectList)
            });
            console.log(that.data.bonusHistoryList)
          } else {
            console.log("初始数组")
            that.setData({
              bonusHistoryList: res.data.objectList
            });
            console.log(that.data.bonusHistoryList)
          }
          that.data.bonusHistoryRNParams.pageIndex = that.data.bonusHistoryRNParams.pageIndex + 1;
          that.setData({
            bonusHistoryRNParams: that.data.bonusHistoryRNParams
          });
        } else {
          if (that.data.bonusHistoryRNParams.pageIndex == 1) {
            that.setData({
              showThis: false,
            });
            wx.showToast({
              title: '暂无积分明细',
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
          title: '查询积分明细失败，请重试',
          icon: 'none'
        })
      },
      complete(res) {
        wx.hideLoading();
      }
    })
  }
})