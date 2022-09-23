// pages/details/userCenter/userCenter.js
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
    httpHeader: {},
    userInfo: {},
    vipInfo: {},
    toUpdateVip: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    let theUserInfo = wx.getStorageSync(storage.DEFAULT_VIP_INFO);
    let theBirthday = theUserInfo.birthday.slice(0,10);
    theUserInfo.birthday = theBirthday;
    console.log(theUserInfo);
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      userInfo: wx.getStorageSync(storage.WX_USER_Info),
      vipInfo: theUserInfo
    });
    console.log(this.data.vipInfo);
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

  checkVipName: function (e) {
    console.log(e);
    if (e.detail.value.length < 2 || e.detail.value.length > 32) {
      wx.showToast({
        title: '昵称需要至少2个字符且最大支持32个字符',
        icon: 'none'
      })
      this.setData({ toUpdateVip: false });
    } else {
      this.data.vipInfo.name = e.detail.value;
      this.setData({
        vipInfo: this.data.vipInfo,
        toUpdateVip: true
      });
    }
  },

  bindDateChange: function (e) {
    console.log(e);
    this.data.vipInfo.birthday = e.detail.value.replace(/-/g, '/');
    this.setData({ vipInfo: this.data.vipInfo });
    console.log(this.data.vipInfo.birthday);
  },

  updateVipInfo: function () {
    let that = this;
    console.log(that.data.vipInfo)
    console.log(that.data.httpHeader)
    wx.request({
      url: api.vipUpdate,
      method: api.POST,
      header: {
        'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest',
        'cookie': that.data.httpHeader.cookie
      },
      data: {
        'ID': that.data.vipInfo.ID,
        'birthday': that.data.vipInfo.birthday == "" ? '1970/1/1 01:01:01' : that.data.vipInfo.birthday,
        'category': that.data.vipInfo.category
      },  //在小程序只允许修改昵称和生日
      success(res) {
        console.log(res);
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../../login/login');
          return;
        }
        if (res.data.ERROR != error.EC_NoError) {
          wx.showToast({
            title: '修改会员信息失败，请重试',
            icon: 'none'
          })
          return;
        }
        wx.showToast({
          title: '修改会员信息成功',
          icon: 'none'
        })
        //将修改后的会员信息放入缓存中
        util.writeStorage(storage.DEFAULT_VIP_INFO, that.data.vipInfo);
        console.log(wx.getStorageSync(storage.DEFAULT_VIP_INFO));
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '修改会员信息失败，请重试',
          icon: 'none'
        })
      }
    })
  }
})