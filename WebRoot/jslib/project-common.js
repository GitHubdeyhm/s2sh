/**
 * 定义项目常用js常量
 */
var Constant = {
	emptyTipMsg : "请选择要操作的记录！",//编辑或删除未选择行的提示信息
	editFirstRow: "您选中了多条记录，默认编辑第一条选中的记录！",//easyui编辑时选中了多条记录，则默认选择编辑第一行
	inputWidth: 170,//设置easyui控件的默认宽度和高度
	inputHeight: 24,
	width: 140,
	height: 24,
	emptyContent: "--"
};

//设置下拉框和下拉树的默认宽度和高度
$.fn.combobox.defaults.width = Constant.inputWidth;
$.fn.combotree.defaults.width = Constant.inputWidth;
$.fn.datebox.defaults.width = Constant.inputWidth;
$.fn.datetimebox.defaults.width = Constant.inputWidth;
$.fn.combobox.defaults.height = Constant.inputHeight;
$.fn.combotree.defaults.height = Constant.inputHeight;
$.fn.datebox.defaults.height = Constant.inputHeight;
$.fn.datetimebox.defaults.height = Constant.inputHeight;

/**
 * 用于easyui表格的默认formatter，当不存在值时表格列显示的默认值
 */
function defaultFmt(value) {
	//值为数字0
	if (value === 0) {
		return 0;
	}
	return value ? value : Constant.emptyContent;
}
/**
 * 用于0：否1：是的格式化列
 * @Date 2016-10-22下午2:29:45
 * @returns
 */
function yesNoFmt(value) {
	if (value == 1) {
		return "是";
	}
	if (value == 0) {
		return "否";
	}
	return Constant.emptyContent;
}

