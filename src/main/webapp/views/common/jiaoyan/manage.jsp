<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/include_common.jsp"%>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

</head>
<body>
	<div id="main">
		<div id="toolbar">
			<a class="waves-effect btn btn-info btn-sm"
				href="javascript:addAction();"><i class="zmdi zmdi-plus"></i>数据采集</a>
			<select id="tableList" class="selectpicker" name="tableList"
				data-max-options="1" data-live-search="true"
				data-live-search-placeholder="请选择要校验的库" multiple>

			</select> <a class="waves-effect btn btn-danger btn-sm"
				href="javascript:jiaoyanAction();"><i class="zmdi zmdi-delete"></i>
				校验</a> <a class="waves-effect btn btn-primary btn-sm"
				href="javascript:roleAction();"><i class="zmdi zmdi-male"></i>
				校验并上传</a>
		</div>
		<table id="table"></table>
	</div>
<!-- 实现一个等待的效果 -->
<div class="modal fade" class="crudDialog" role="dialog" id="loadingModal" aria-hidden="true">

	<div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; 
	left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
	<div class="progress progress-striped active" style="margin-bottom: 0;">
	<div class="progress-bar" style="width: 100%;"></div>

</div>
<h5 style="color:black"> <strong>正在校验数据...校验可能需要花费30秒左右,请稍等！</strong> </h5>
</div>
</div>
</body>

<script type="text/javascript">

var $table = $('#table');
var treeObj;
var userId;
$(function() {
	$table.bsTable({
		url: '${pageContext.request.contextPath}/common/jiaoyan/list',
		idField: 'updateLogId',// 指定主键列
		singleSelect: true,
		search: true,
		columns: [
			{field: 'state', checkbox: true},
			{field: 'updateLogId', title: '用户名', align: 'center'},
			{field: 'dbName', title: '数据库名', align: 'center'},
			{field: 'tableName', title: '表名', align: 'center'},
			{field: 'updateDate', title: '校验时间', align: 'center'},
			{field: 'ifPass', title: '是否通过', align: 'center'},
			{field: 'info', title: '详细信息', align: 'center'},

		]
	});
	
});




$('#tableList').on('shown.bs.select',function(e) {  // shown.bs.select bootstrap 的内置的
	// 获取checkBox值
 $.ajax({
        url: '${pageContext.request.contextPath}/common/jiaoyan/dbList',//写你自己的方法
        type: "post", //数据发送方式
        success: function (data) { 
            $.each(data.res, function (i) {
                $('#tableList.selectpicker').append("<option value=" + data.res[i].dbName + ">" + data.res[i].dbName + "</option>"); 
            });
            // 缺一不可  
            $('#tableList').selectpicker('refresh');  
            $('#tableList').selectpicker('render');  
        },
        error: function (data) {
            alert("查询表格失败" + data);
        }
        
    })
    $('#tableList').empty();
});


//添加
function jiaoyanAction() {
	
	var dbName = $('#tableList option:selected').val();//选中的值
	var userId = '${user.userId}';  // 获取当前用户 Id
	$('#loadingModal').modal({backdrop: 'static', keyboard: true});  <!-- 模态框不会隐藏 -->
	$("#loadingModal").modal('show');
	
	 $.ajax({
	        url: '${pageContext.request.contextPath}/common/jiaoyan/jiaoyandb',//写你自己的方法
	        type: "post", //数据发送方式
	        data: {dbName:dbName,userId:userId},
	        dataType: "json",//后台处理后返回的数据格式
	        success: function (data) {
	    	    if (data.status == 0) {
	    	    	$('#loadingModal').modal('hide');
	    	    	alert("校验失败 失败的原因" + data.msg);
	    		} else {
	    			$('#loadingModal').modal('hide');
	    			alert("校验成功" + data.msg);
	        }
	        },
	        error: function (data) {
	        	$('#loadingModal').modal('hide');
	            alert("校验失败 ,失败原因,数据库异常");
	        }
	        
	    })
	   
	    
}
// 删除



</script>

</html>