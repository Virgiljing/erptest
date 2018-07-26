
$(function(){
	//加载表格数据
	$('#grid').datagrid({
		url:'report!orderReport',
		columns:[[
			{field:'name',title:'销售类型',width:100},
			{field:'y',title:'销售总价',width:100}	
			
		]],
		singleSelect: true,
		pagination: true,
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
	 $('#charts').highcharts({
	        chart: {
	            type: 'pie',
	            options3d: {
	                enabled: true,
	                alpha: 45,
	                beta: 0
	            }
	        },
	        title: {
	            text: '销售统计图'
	        },
	        tooltip: {
	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    }
	                },
	                showInLegend:true
	            }
	        },
	        series: [{
	            name: "所占比例",
	            colorByPoint: true,
	            data: _data
	       }],
	       credits: {
	        	enabled: true,
	        	href: "http://www.itheima.com",
	        	text: 'itheima.com'
	         }
	    });
}

