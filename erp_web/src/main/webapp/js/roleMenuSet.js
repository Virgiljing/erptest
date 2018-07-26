$(function(){
	$('#tree').tree({
		url:'role!readRoleMenus.action?id='+1,
		animate:true,
		checkbox:true
	});
	$('#grid').datagrid({
		url:'role!list',
		columns:[[
			{field:'uuid',title:'编号',width:100},
			{field:'name',title:'名称',width:100}
		]],
		singleSelect:true,
		onClickRow:function(rowIndex,rowData){
			var roleuuid = rowData.uuid;
			$('#tree').tree({
				checkbox:true,
				animate:true,
				url:'role!readRoleMenus.action?id='+roleuuid
			});
		}
	});
	$('#btnSave').bind('click',function(){
		var nodes = $('#tree').tree('getChecked');
		var ids = [];
		$.each(nodes,function(i,node){
			ids.push(node.id);
		});
		var submitData = {};
		submitData.ids = ids.toString();
		var row = $('#grid').datagrid('getSelected');
		if (row == null) {
			$.messager.alert('提示','请选择角色','info');
			return ;
		}
		submitData.id = row.uuid;
		$.ajax({
			url:'role!updateRoleMenus.action',
			data:submitData,
			dataType:'json',
			type:'post',
			success:function(result){
				$.messager.alert('提示',result.message,'info');
			}
		})
	})
})