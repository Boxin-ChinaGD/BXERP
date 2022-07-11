// pages/details/myCoupon/details/details.js
var api = require('../../../../utils/api.js');
var miniprogramBarcode = require('../../../../utils/wxbarcode.js');
var storage = require('../../../../utils/storage.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    serverURL: "",
    companyInfo: {},
    couponDetails: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    this.setData({
      serverURL: api.server,
      companyInfo: wx.getStorageSync(storage.DEFAULT_Company),
      couponDetails: JSON.parse(options.info)
    });
    miniprogramBarcode.barcode('couponCode', this.data.couponDetails.SN, 616, 156);
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

  }
})