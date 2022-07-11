// const formatTime = date => {
//   const year = date.getFullYear()
//   const month = date.getMonth() + 1
//   const day = date.getDate()
//   const hour = date.getHours()
//   const minute = date.getMinutes()
//   const second = date.getSeconds()

//   return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
// }

// const formatNumber = n => {
//   n = n.toString()
//   return n[1] ? n : '0' + n
// }

// module.exports = {
//   formatTime: formatTime
// }

//时间戳转换成日期时间
function js_date_time(unixtime) {
  var dateTime = new Date(parseInt(unixtime))
  var year = dateTime.getFullYear();
  var month = dateTime.getMonth() + 1;
  var day = dateTime.getDate();
  var hour = dateTime.getHours();
  var minute = dateTime.getMinutes();
  var second = dateTime.getSeconds();
  var now = new Date();
  var now_new = Date.parse(now.toDateString()); //typescript转换写法
  var milliseconds = now_new - dateTime;
  var timeSpanStr = year + '-' + month + '-' + day + ' ' + hour + ':' + minute;
  return timeSpanStr;
};

function writeStorage(_key, _data) {
  // wx.setStorage({
  //   key: _key,
  //   data: _data,
  //   fail: function () {
  //     console.log("写入" + _key + "失败：" + _data);
  //   }
  // })
  try {//同步存储
    wx.setStorageSync(_key, _data)
  } catch (e) {
    console.log(e);
    console.log("写入" + _key + "失败：" + _data);
  }
};

// function scanCodeLogin(){
//   let that = this;
//   var obj = wx.getLaunchOptionsSync()
//   console.log('启动小程序的参数', obj);
//   if (obj.scene == 1047 || obj.scene == 1031 || obj.scene == 1032) {    //判断是否是扫码登录
//     if (obj.query.scene == undefined || obj.query.scene.trim() == "") {   //判断扫的二维码是否有参数，如果无就登陆668866虚拟门店
//       that.setData({
//         compangSN: 668866,
//       });
//     } else {
//       var str = obj.query.scene;
//       var compangSn = str.slice(13);
//       that.setData({
//         compangSN: compangSn,
//         scene: obj.scene,
//       });
//     }
//   } else {
//     if (wx.getStorageSync(storage.DEFAULT_Company).SN) {
//       that.setData({
//         compangSN: wx.getStorageSync(storage.DEFAULT_Company).SN,
//       });
//     } else {
//       that.setData({
//         compangSN: '668866',
//       });
//     }
//   }
// };

module.exports = {
  js_date_time: js_date_time,
  writeStorage: writeStorage
}

