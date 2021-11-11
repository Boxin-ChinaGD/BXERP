layui.use(['table', 'form'], function () {
	var table = layui.table;		//表格模块
	var form = layui.form;		//表单模块

	const bonusHistoryRN_url = "bonusConsumeHistory/retrieveNEx.bx";
	const bonusHistoryTable_tableID = "bonusHistoryTable";
	const data_reloadTable = { "vipID": "-1", "vipName": "", "vipMobile": "" };

	//渲染表格bonusHistoryTable
	table.render({
		elem: "#bonusHistoryTable",
		url: bonusHistoryRN_url,
		id: bonusHistoryTable_tableID,
		method: "POST",
		where: data_reloadTable,
		request: {
			pageName: "pageIndex",
			limitName: "pageSize"
		},
		response: {
			dataName: "objectList",
		},
		skin: "nob",
		limit: '10',
		limits: [10],
		page: true,
		cols: [[
			{ title: "序号", type: "numbers", align: "center" },
			{ field: "vipName", title: "会员名称", align: "center" },
			{ field: "bonus", title: "当前积分", align: "center" },
			{
				field: "addedBonus", title: "积分变动", align: "center",
				templet: function (data) {
					if (data.addedBonus > 0) {
						return "+" + data.addedBonus + "积分";
					} else if (data.addedBonus < 0) {
						return "-" + Math.abs(data.addedBonus) + "积分";
					} else {
						return data.addedBonus;
					}
				}
			},
			{ field: "remark", title: "变动原因", align: "center" },
			{
				field: "createDatetime", title: "积分变动时间", align: "center",
				templet: function (data) {
					return data.createDatetime.substring(0, data.createDatetime.length - 4);
				}
			},
			{ field: "staffName", title: "操作人", align: "center" }

		]],
		done: function (res, curr, count) {
			console.log(res);
			if (res.ERROR != "EC_NoError") {
				var msg = res.msg == "" ? "搜索积分历史失败" : res.msg;
				layer.msg(msg);
			}
		}
	});

	//根据关键字进行搜索
	form.on("submit(queryByKeyword)", function (data) {
		console.log(data.field);
		var keyWord = data.field.keyWord;
		if (keyWord.length == 0) {
			data_reloadTable.vipName = "";
			data_reloadTable.vipMobile = "";
		} else if (/^[1][3,4,5,7,8][0-9]{9}$/.test(keyWord) && keyWord.length <= 11) {
			data_reloadTable.vipName = "";
			data_reloadTable.vipMobile = keyWord;
		} else if (keyWord.length >= 2 && keyWord.length <= 32) {
			data_reloadTable.vipName = keyWord;
			data_reloadTable.vipMobile = "";
		} else {
			layer.msg("输入的关键字不符合要求，需提供完整的手机号或至少2个字符的会员名称");
			return;
		}
		console.log(data_reloadTable);
		reloadTable(table, bonusHistoryTable_tableID, "POST", bonusHistoryRN_url, 1, data_reloadTable);
		return false;
	})
})