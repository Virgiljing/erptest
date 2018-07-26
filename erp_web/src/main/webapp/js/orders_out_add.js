
var existEditIndex = -1;
$(function(){
	//加载表格数据
	$('#addOrdersGrid').datagrid({
		showFooter:true,
		columns:[[
			{field:'goodsuuid',title:'商品编号',width:100,editor:{type:'numberbox',options:{disabled:true}}},
			{field:'goodsname',title:'商品名称',width:100,editor:{type:'combobox',options:{
				url:'goods!list.action',valueField:'name',textField:'name',
				onSelect:function(goods){
					var goodsUuidEditor = getEditor('goodsuuid');
					$(goodsUuidEditor.target).val(goods.uuid);
					var goodsPriceEditor = getEditor('price');
					$(goodsPriceEditor.target).val(goods.outprice);
					var goodsNumEditor = getEditor('num');
					$(goodsNumEditor.target).select();
					cal();
					sum();
				}

			}}},
			{field:'price',title:'价格',width:100,editor:{type:'numberbox',options:{min:0,precision:2,disabled:true}}},
			{field:'num',title:'数量',width:100,editor:{type:'numberbox',options:{min:0}}},
			{field:'money',title:'金额',width:100,editor:{type:'numberbox',options:{min:0,precision:2,disabled:true}}},
			{field:'-',title:'操作',width:100,formatter:function(value,rowData,rowIndex){
				if (rowData.num == '合计') {
					return;
				}
				return '<a href="javascript:void(0)" onclick="deleteRow('+rowIndex+')">删除</a>';
			}}
		]],
		singleSelect: true,
		//pagination: true,
		toolbar: [
			{
				text: '新增',
				iconCls: 'icon-add',
				handler: function(){
					if (existEditIndex > -1) {
						$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
					}
					$('#addOrdersGrid').datagrid('appendRow',{
						num: 0,
						money:0
					});
					existEditIndex = $('#addOrdersGrid').datagrid('getRows').length - 1;
					$('#addOrdersGrid').datagrid('beginEdit',existEditIndex);
					bindGridEvent();
				},
			},
			{
				text: '提交',
				iconCls: 'icon-save',
				handler: function(){
					var submitData = $('#orderForm').serializeJSON();
					if (submitData['t.supplieruuid']=='') {
						$.messager.alert('提示','客户','info');
						return;
					}
					if (existEditIndex > -1) {
						$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
					}
					var rows = $('#addOrdersGrid').datagrid('getRows');
					var json = JSON.stringify(rows);
					submitData.json = json;
					$.ajax({
						url:'orders!add_out.action',
						data:submitData,
						dataType:'json',
						type:'post',
						success:function(result){
							$.messager.alert('提示',result.message,'info',function(){
								if (result.success) {
									$('#supplier').combogrid('clear');
									$('#addOrdersGrid').datagrid('loadData',{total:0,rows:[],footer:[{num:'合计',money:0}]});
									$('#addOrdersDlg').dialog('close');
									$('#grid').datagrid('reload');
								}
							})
						}
					})
				},
			}
		],
		onClickRow:function(rowIndex,rowData){
			$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
			$('#addOrdersGrid').datagrid('beginEdit',rowIndex);
			existEditIndex = rowIndex;
			bindGridEvent();
		}
	});
	
	
	
	$('#addOrdersGrid').datagrid('reloadFooter',[
		{num: '合计', money: 0}
	]);
	
	$('#supplier').combogrid({
		panelWidth:750,
		idField:'uuid',
		textField:'name',
		url:'supplier!list.action?t1.type=2',
		mode:'remote',
		columns:[[    
		    	{field:'uuid',title:'编号',width:100},
		    	{field:'name',title:'名称',width:100},
		    	{field:'address',title:'联系地址',width:150},
		    	{field:'contact',title:'联系人',width:100},
		    	{field:'tele',title:'联系电话',width:100},
		    	{field:'email',title:'邮件地址',width:120}   
		 ]]
	});
	
	
	
})

function getEditor(field){
	return $('#addOrdersGrid').datagrid('getEditor',{index:existEditIndex,field:field});
}

function cal(){
	var goodsPriceEditor = getEditor('price');
	var price = $(goodsPriceEditor.target).val();
	var goodsNumEditor = getEditor('num');
	var num = $(goodsNumEditor.target).val();
	var goodsMoneyEditor = getEditor('money');
	var money = (num * price) ;
	$(goodsMoneyEditor.target).val(money.toFixed(2));
	//返回当前页的所有行
	var rows = $('#addOrdersGrid').datagrid('getRows');
	rows[existEditIndex].money = money;	
}

function bindGridEvent(){
	var numEditor = getEditor('num');
	$(numEditor.target).bind('keyup',function(){
		cal();
		sum();
	});
}

function deleteRow(idx){
	$('#addOrdersGrid').datagrid('endEdit',existEditIndex);
	existEditIndex = -1;
	$('#addOrdersGrid').datagrid('deleteRow',idx);
	var data = $('#addOrdersGrid').datagrid('getData');
	$('#addOrdersGrid').datagrid('loadData',data);
	sum();
}

function sum(){
	var rows = $('#addOrdersGrid').datagrid('getRows');
	var totalMoney = 0;
	$.each(rows,function(i,row){
		totalMoney += row.money*1;
	});
	
	totalMoney = totalMoney.toFixed(2);
	$('#addOrdersGrid').datagrid('reloadFooter',[
		{num: '合计', money: totalMoney}
	]);
}