//提交的方法名称
var method = "";
var height = 200;
var listParam = "";
var name="storedetail";
var storeuuid = "";
var goodsuuid = "";
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:name + '!listByPage' + listParam,
		columns:[[
			{field:'uuid',title:'编号',width:100},
			{field:'storeName',title:'仓库',width:100},
			{field:'goodsName',title:'商品',width:100},
			{field:'num',title:'数量',width:100}
        ]],
		singleSelect: true,
		pagination: true,
		onDblClickRow:function(rowIndex, rowData){
			$('#storeoperDlg').dialog('open');
			$('#ordersTable td').each(function(i,td){
				if(td.id){ // td.id {field:uuid....}
					// 获取列的配置信息
					var columnCfg = $('#grid').datagrid('getColumnOption',td.id);
					//alert(td.id);
					var value = rowData[td.id];
					if(columnCfg.formatter){
						// 有格式化器，调用格式化的方法
						value = columnCfg.formatter(value);
					}
					$(td).html(value);
				}
			});
			var data = {'t1.storeuuid':rowData.storeuuid,'t1.goodsuuid':rowData.goodsuuid}
			$('#storeoperGrid').datagrid('load',data);
		}
		
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		$('#grid').datagrid('load',formData);
	});
	   
	
	$('#storeoperDlg').dialog({    
	    title: '库存操作详情',    
	    width: 600,    
	    height: 340,    
	    closed: true,
		modal:true
	}); 
	$('#storeoperGrid').datagrid({
		url:'storeoper!listByPage',
		singleSelect: true,
		pagination: true,
	    columns:[[
	    	{field:'uuid',title:'编号',width:100},
			{field:'empName',title:'操作员工',width:100},
			{field:'opertime',title:'操作日期',width:150,formatter:function(value){
				if (value) {
					return new Date(value).Format("yyyy-MM-dd hh:mm:ss");
				}
				return "";
			}},
			{field:'num',title:'数量',width:100},
			{field:'type',title:'操作类型',width:100,formatter:function(value){
				switch(value*1){
					case 1:return '入库';
					case 2:return '出库';
					default:return '';
				}
			}}
	    ]]
	});  
	
});

