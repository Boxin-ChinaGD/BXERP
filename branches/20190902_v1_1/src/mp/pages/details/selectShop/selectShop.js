// pages/details/selectShop/selectShop.js
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
    shopList: {},
    companyInfo: {},
    isOnlyOne:false,

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      httpHeader: wx.getStorageSync(storage.NBR_Session),
      companyInfo: wx.getStorageSync(storage.DEFAULT_Company),
      shopList: wx.getStorageSync(storage.VIP_CompanyList),
      companyAddressList: wx.getStorageSync(storage.Company_Address)
    });  

    if (wx.getStorageSync(storage.VIP_CompanyList) == '' || wx.getStorageSync(storage.VIP_CompanyList) == undefined){
      this.setData({
        isOnlyOne: true
      });
    }else{
      this.setData({
        isOnlyOne: false
      });
    }
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
  //一家商家的时候
  isOnlyOne: function(){
    wx.reLaunch({
      url: '../../index/index',
      fail: function () {
        wx.showToast({
          title: '切换公司失败',
          icon: 'fail',
        });
      }
    });
  },
  //点击切换商家
  switchShop: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    var shopList = that.data.shopList;
    var shop = shopList[id];
    var companyId = shop.ID;
    console.log(companyId);
    wx.request({
      url: api.selectMyCompany,
      method: api.POST,
      header: {
        'content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
        'X-Requested-With': 'XMLHttpRequest',
        'cookie': that.data.httpHeader.cookie
      },
      data: { "ID": companyId },
      success(res) {
        if (res.header.sessionStatus == "timeOut") {
          app.afreshLogin('../../login/login');
          return;
        };
        console.log(res);

        if (res.data.ERROR != error.EC_NoError) {
          wx.showToast({
            title: '切换公司失败',
            icon: 'none'
          })
          console.log('切换公司失败!ERROR:' + res.data.ERROR + ',msg:' + res.data.msg);
          return;
        }
        util.writeStorage(storage.DEFAULT_Company, shop);
        util.writeStorage(storage.DEFAULT_VIP_INFO, res.data.object2);
        wx.reLaunch({
          url: '../../index/index',
          fail: function () {
            wx.showToast({
              title: '切换公司失败',
              icon: 'fail',
            });
          }
        });
      },
      fail(res) {
        console.log(res);
        wx.showToast({
          title: '切换公司失败',
          icon: 'none'
        })
      }
    });
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

})