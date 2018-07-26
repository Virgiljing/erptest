//提交的方法名称

type = 2;
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		title:'我的采购销售订单列表',
		url:'orders!myListByPage?t1.type=2',
		columns:[[
			{field:'uuid',title:'编号',width:100},
			{field:'createtime',title:'生成日期',width:100,formatter:formatDate},	
			{field:'endtime',title:'入库日期',width:100,formatter:formatDate},
			{field:'createrName',title:'下单员',width:100},
			{field:'enderName',title:'库管员',width:100},
			{field:'supplierName',title:'客户',width:100},
			{field:'totalmoney',title:'合计金额',width:100},
			{field:'state',title:'状态',width:100,formatter:formatState},
			{field:'waybillsn',title:'运单号',width:100}
		]],
		singleSelect: true,
		pagination: true,
		onDblClickRow:function(rowIndex, rowData){
			$('#ordersDlg').dialog('open'); 
			$('#uuid').html(rowData.uuid);
			$('#supplierName').html(rowData.supplierName);
			$('#state').html(formatState(rowData.state));
			$('#createrName').html(rowData.createrName);
			$('#checkerName').html(rowData.checkerName);
			$('#starterName').html(rowData.starterName);
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#checktime').html(formatDate(rowData.checktime));
			$('#starttime').html(formatDate(rowData.starttime));
			$('#endtime').html(formatDate(rowData.endtime));
			$('#waybillSn').html(rowData.waybillsn);
			
//			$('#ordersTable td').each(function(i,td){
//				if(td.id){ // td.id {field:uuid....}
//					// 获取列的配置信息
//					var columnCfg = $('#grid').datagrid('getColumnOption',td.id);
//					//alert(td.id);
//					var value = rowData[td.id];
//					if(columnCfg.formatter){
//						// 有格式化器，调用格式化的方法
//						value = columnCfg.formatter(value);
//					}
//					$(td).html(value);
//				}
//			});
			
			// 加载明细的数据
			$('#itemgrid').datagrid('loadData',rowData.orderdetails);
		},
		toolbar:[
			{
				text:'销售订单录入',
				iconCls:'icon-add',
				handler:function(){
					$('#addOrdersDlg').dialog('open');
				}
			}
		]
	});
	
	$('#ordersDlg').dialog({    
	    title: '采购订单详情',    
	    width: 700,    
	    height: 340,    
	    closed: true,
		modal:true
	});  
	
	
	$('#itemgrid').datagrid({    
		singleSelect: true,
	    columns:[[    
	    	{field:'uuid',title:'编号',width:60},
			{field:'goodsuuid',title:'商品编号',width:80},
			{field:'goodsname',title:'商品名称',width:100},
			{field:'price',title:'价格',width:100},
			{field:'num',title:'数量',width:100},
			{field:'money',title:'金额',width:100},
			{field:'state',title:'状态',width:60,formatter:formatDetailsState}
	    ]]    
	});  
	
	//增加订单窗口
	$('#addOrdersDlg').dialog({
		title:'增加订单',
		width:700,
		height:400,
		modal:true,
		closed:true
	})
});


