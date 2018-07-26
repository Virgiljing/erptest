//提交的方法名称

type = 2;
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		title:'销售订单列表',
		url:'orders!myListByPage?t1.type=2&t1.state=0',
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
			$('#enderName').html(rowData.enderName);
			$('#createtime').html(formatDate(rowData.createtime));
			$('#endtime').html(formatDate(rowData.endtime));
			
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
		}
	});
	//初始化销售订单详情
	$('#ordersDlg').dialog({    
	    title: '销售订单详情',    
	    width: 700,    
	    height: 340,    
	    closed: true,
		modal:true
	});  
	//出库
	$('#itemDlg').dialog({    
		title: '出库',    
		width: 300,    
		height: 200,    
		closed: true,
		modal:true,
		buttons:[
			{
				text:'出库',
				iconCls:'icon-save',
				handler:doOutStore
			}
		]
	});  
	//出库窗口
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
	    ]],
	    onDblClickRow:function(rowIndex, rowData){
			$('#storeTable td').each(function(i,td){
				if(td.id){ // td.id {field:uuid....}
					// 获取列的配置信息
					var columnCfg = $('#itemgrid').datagrid('getColumnOption',td.id);
					//alert(td.id);
					var value = rowData[td.id];
					if(columnCfg.formatter){
						// 有格式化器，调用格式化的方法
						value = columnCfg.formatter(value);
					}
					$(td).html(value);
				}
			});
			$('#itemuuid').val(rowData.uuid)
			$('#itemDlg').dialog('open');
	    }
	});  
});

function doOutStore(){
	$.messager.confirm('提示','确认出库',function(yes){
		if (yes) {
			var submitData = $('#itemForm').serializeJSON();
			$.ajax({
				url:'orderdetail!doOutStore.action',
				data:submitData,
				dataType:'json',
				type:'post',
				success:function(result){
					$.messager.alert('提示',result.message,'info',function(){
						if (result.success) {
							$('#itemDlg').dialog('close')
							var row = $('#itemgrid').datagrid('getSelected');
							row.state = '1';
							var data = $('#itemgrid').datagrid('getData');
							$('#itemgrid').datagrid('loadData',data);
							var flag = true;
							$.each(data.rows,function(i,r){
								if (r.state*1 == 0) {
									flag = false;
									return false;
								}
							});
							if (flag) {
								$('#ordersDlg').dialog('close');
								$('#grid').datagrid('reload');
							}
						}
					})
				}
			})
		}
	})
}

