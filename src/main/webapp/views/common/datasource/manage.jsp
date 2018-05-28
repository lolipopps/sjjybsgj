<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/include_common.jsp"%>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>用户管理</title>
</head>
<body>
	<div id="main">
		<div id="toolbar">
			<a class="waves-effect btn btn-info btn-sm"
				href="javascript:addAction();"><i class="zmdi zmdi-plus"></i>
				新增数据源</a> <a class="waves-effect btn btn-warning btn-sm"
				href="javascript:editAction();"><i class="zmdi zmdi-edit"></i>
				编辑数据源</a> <a class="waves-effect btn btn-danger btn-sm"
				href="javascript:deleteAction();"><i class="zmdi zmdi-delete"></i>
				删除数据源</a>
		</div>
		<table id="table"></table>
	</div>

	<!-- 用户 -->
	<div id="addDialog" class="crudDialog" hidden>
		<div class="container col-md-11"
			style="margin-top: 10px; margin-left: 55px; display: table;">
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">用户名：</label>
				</div>
				<div class="col-md-7">
					<div class="form-group">
						<input type="text" id="userCode" name="userCode"
							class="form-control" placeholder="用户名（必填）" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">IP地址</label>
				</div>
				<div class="col-md-7">
					<div class="form-group">
						<input type="text" id="userName" name="userName"
							class="form-control" placeholder="IP地址（必填）" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">端口:</label>
				</div>
				<div class="col-md-7">
					<div class="form-group">
						<input type="password" id="userPassword" name="userPassword"
							class="form-control" placeholder="端口（必填）" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">数据库用户名:
					</label>
				</div>
				<div class="col-md-7">
					<div class="form-group">
						<input type="text" id="userAddress" name="userAddress"
							class="form-control" placeholder="数据库用户名" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">密码:</label>
				</div>
				<div class="col-md-7">
					<div class="form-group">
						<input type="text" id="userEmail" name="userEmail"
							class="form-control" placeholder="密码" />
					</div>
				</div>
			</div>
			<div class="row" style="margin-top: 10px; margin-bottom: 10px;">
				<div class="col-md-4 text-left"
					style="background-color: #D2E9FF; line-height: 26px; vertical-align: middle;">
					<label style="margin-top: 5px; font-size: 14px; color: grey;">数据库类型</label>
				</div>
				<div class="col-md-7">
					<select class="selectpicker" id="selectType">
						<option>SqlServer</option>
						<option>Oracle</option>
						<option>Mysql</option>
					</select>
				</div>

			</div>
		</div>
	</div>
</body>

<script type="text/javascript">
	var $table = $('#table');
	var treeObj;
	var userId;
	$(function() {
		$table.bsTable({
					url : '${pageContext.request.contextPath}/common//datasource/listsource',
					idField : 'dbSourceId',// 指定主键列
					singleSelect : true,
					search : true,
					columns : [ {
						field : 'state',
						checkbox : true
					}, {
						field : 'dbSourceId',
						title : '数据库ID',
						align : 'center'
					}, {
						field : 'connectName',
						title : '连接名',
						align : 'center'
					}, {
						field : 'dbName',
						title : '数据库名',
						align : 'center'
					}, {
						field : 'ip',
						title : 'IP地址',
						align : 'center'
					}, {
						field : 'port',
						title : '端口',
						align : 'center'
					}, {
						field : 'dbUserName',
						title : '用户名',
						align : 'center'
					}, {
						field : 'dbPassword',
						title : '数据库密码',
						align : 'center'
					}, {
						field : 'dbType',
						title : '数据库类型',
						align : 'center'
					} ]
				});

	});
	
	$("#selectType").selectpicker('refresh');
	$('#selectType').selectpicker('render');
	
	// 保存角色
	$('#roleSave-btn').click(function() {
		var nodes = treeObj.getCheckedNodes(true);
		var roleStr = "";
		$.map(nodes, function(item, index) {
			roleStr += "," + item.id;
		});

		$.post('${pageContext.request.contextPath}/common/user/roleSave', {
			'userId' : userId,
			'roleStr' : roleStr.substr(1)
		}, function(data) {
			$('#roleModal').modal('hide');
			$.alert(data.msg);
		});

	});

	// 添加
	function addAction() {
		$.confirm({
			type : 'blue',
			animationSpeed : 300,
			columnClass : 'col-md-9 col-md-offset-1',
			title : '添加用户',
			content : $('#addDialog').html(),
			buttons : {
				confirm : {
					text : '保存',
					btnClass : 'waves-effect waves-button',
					action : function() {
						$.alert('保存');
					}
				},
				cancel : {
					text : '取消',
					btnClass : 'waves-effect waves-button'
				}
			}
		});
	}
	// 删除
	function deleteAction() {
		var rows = $table.bootstrapTable('getSelections');
		if (rows.length == 0) {
			$.confirm({
				title : false,
				content : '请至少选择一条记录！',
				autoClose : 'cancel|3000',
				backgroundDismiss : true,
				buttons : {
					cancel : {
						text : '取消',
						btnClass : 'waves-effect waves-button'
					}
				}
			});
		} else {
			$.confirm({
				type : 'red',
				animationSpeed : 300,
				title : false,
				content : '确认删除该用户吗？',
				buttons : {
					confirm : {
						text : '确认',
						btnClass : 'waves-effect waves-button',
						action : function() {
							var ids = new Array();
							for ( var i in rows) {
								ids.push(rows[i].userId);
							}
							$.alert('删除：id=' + ids.join("-"));
						}
					},
					cancel : {
						text : '取消',
						btnClass : 'waves-effect waves-button'
					}
				}
			});
		}
	}
	// 用户角色
	function roleAction() {
		var rows = $table.bootstrapTable('getSelections');
		if (rows.length == 0) {
			$.confirm({
				title : false,
				content : '请至少选择一条记录！',
				autoClose : 'cancel|3000',
				backgroundDismiss : true,
				buttons : {
					cancel : {
						text : '取消',
						btnClass : 'waves-effect waves-button'
					}
				}
			});
		} else {
			var row = rows[0];
			if ('admin' == row.userType) {
				$.alert('对不起，您不能编辑管理员的角色！');
			} else {
				userId = row.userId;
				$('#roleModalTitle').html('用户[' + row.userName + ']拥有的角色');
				loadRoleTree();
			}
		}
	}
</script>

</html>