
$(function(){
	//加载表格数据
	var date = new Date();
	var year = date.getFullYear();
	$('#year').combobox('select',year);
	$('#grid').datagrid({
		url:'report!trendReport',
		columns:[[
			{field:'name',title:'销售类型',width:100},
			{field:'y',title:'销售总价',width:100}	
			
		]],
		singleSelect: true,
		pagination: true,
		queryParams:{year:year},
		onLoadSuccess:function(data){
			showChart(data.rows);
		}
		
	});

	//点击查询按钮
	$('#btnSearch').bind('click',function(){
		//把表单数据转换成json对象
		var formData = $('#searchForm').serializeJSON();
		if (formData['endDate'] != '') {
			formData['endDate'] += " 23:59:59.999";
		}
		$('#grid').datagrid('load',formData);
	});

	
	
});

function showChart(_data){
	var monthData = new Array();
//	var date = new Date();
//	var yearData = $('#year').combobox('getValue');
//	if (yearData=="") {
//		$('#year').combobox('setValue',date.getFullYear());
//		var formData = $('#searchForm').serializeJSON();
//		$('#grid').datagrid('load',formData);
//		return ;
//	}
	for(var i = 1;i<=12;i++){
		monthData.push(i+"月");
	}
	 $('#charts').highcharts({
		 title: {
	            text: $('#year').combobox('getValue') + '年销售趋势分析',
	            x: -20 //center
	        },
	        subtitle: {
	            text: 'Source:www.itcast.com',
	            x: -20
	        },
	        xAxis: {
	            categories: monthData
	        },
	        yAxis: {
	            title: {
	                text: '销售额（元）'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        tooltip: {
	            valueSuffix: '￥'
	        },
	        legend: {
	            layout: 'vertical',
	            align: 'right',
	            verticalAlign: 'bottom',
	            borderWidth: 0
	        },
	        series: [{
	            name: '全部商品',
	            data: $('#grid').datagrid('getRows')
	        }]
	    });
}

