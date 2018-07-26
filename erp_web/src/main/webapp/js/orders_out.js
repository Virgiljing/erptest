//提交的方法名称

type = 2;
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		title:'销售订单列表',
		url:'orders!listByPage?t1.type=2',
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
		}
	});
	//销售订单详情
	$('#ordersDlg').dialog({    
	    title: '销售订单详情',    
	    width: 700,    
	    height: 340,    
	    closed: true,
		modal:true,
		toolbar:[
			{
				text:'导出',
				iconCls:'icon-excel',
				handler:function(){
					$.download("orders!exportById.action",{id:$('#uuid').html()})
				}
			},
			{
				text:'物流详情',
				iconCls:'icon-search',
				
				handler:function(){
					
					if ($('#waybillSn').html()=='') {
						$.messager.alert('提示',"当前没有物流详情",'info');
						return ;
					}
					$('#waybillDlg').dialog('open');
					$('#waybillGrid').datagrid({  
						title:'订单路径详情',
						url:'orders!waybilldetailList.action',
						queryParams:{waybillSn:$('#waybillSn').html()},
						singleSelect: true,
						columns:[[    
				  		    {field:'exedate',title:'执行日期',width:100},
				  		    {field:'exetime',title:'执行时间',width:100},
				  		    {field:'info',title:'执行信息',width:200}
						]]    
					});  
					
				}
			}
		]
	});  
	//订单详情表格
	
	
	//物流详情窗口
	$('#waybillDlg').dialog({    
	    title: '物流详情',    
	    width: 500,    
	    height: 340,    
	    closed: true,
		modal:true,
		onClose:function(){//dialog窗口关闭时触发的事件
			//datagrid的清空数据
			$('#waybillGrid').datagrid('loadData', { total: 0, rows: [] });
		}
	})
	
	//订单详情表格
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
	
});


