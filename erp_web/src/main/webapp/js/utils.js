/**
 * 日期格式化器
 * @param value
 * @returns
 */

var type = 1;
function formatDate(value){
	if (value) {
		return new Date(value).Format("yyyy-MM-dd");
	}
	return "";
}

function formatState(value){
	//'采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库'
	if (type == 1) {
		switch (value*1) {
			case 0:return '未审核';
			case 1:return '已审核';
			case 2:return '已确认';
			case 3:return '已入库';
			default:return '';
		}
	}
	if (type == 2) {
		switch (value*1) {
			case 0:return '未出库';
			case 1:return '已出库';
			default:return '';
		}
	}
}
function formatDetailsState(value){
	//'采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库'
	if (type == 1) {
		
		switch (value*1) {
			case 0:return '未入库';
			case 1:return '已入库';
			default:return '';
		}
	}
	if (type == 2) {
		switch (value*1) {
			case 0:return '未出库';
			case 1:return '已出库';
			default:return '';
		}
	}
}