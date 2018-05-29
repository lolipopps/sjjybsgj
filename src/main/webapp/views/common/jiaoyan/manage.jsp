<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/include_common.jsp"%>

<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<style type="text/css">
.layout-left {
	position: fixed;
	float: left;
	width: 20%;
	background: #fff;
	height: 100%;
	transition: all;
	-webkit-transition-duration: .3s;
	transition-duration: .3s;
	z-index: 10;
	overflow-y: auto;
	box-shadow: 1px 0 4px rgba(0, 0, 0, .3);
}

.layout-right {
	position: fixed;
	float: left;
	width: 80%;
	background: #fff;
	left: 20%;
	height: 100%;
}
</style>
</head>
<body>
	<div class="layout-left">
		<div id="middle" style="background: #F5F5F5;">
			<div id="ztree" class="ztree"></div>
		</div>
	</div>
	<div class="layout-right" id="main">
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
	<div class="modal fade" class="crudDialog" role="dialog"
		id="loadingModal" aria-hidden="true">

		<div
			style="width: 200px; height: 20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%; margin-left: -100px; margin-top: -10px">
			<div class="progress progress-striped active"
				style="margin-bottom: 0;">
				<div class="progress-bar" style="width: 100%;"></div>

			</div>
			<h5 style="color: black">
				<strong>正在校验数据...校验可能需要花费30秒左右,请稍等！</strong>
			</h5>
		</div>
	</div>
</body>

<script type="text/javascript">
	$(function() {
		var setting = { //此处根据自己需要进行配置
			view : {
				fontCss : setFontCss
			},
			data : {
				simpleData : {
					enable : true
				}
			}
		};
		$
				.ajax({
					type : "post",
					url : "${pageContext.request.contextPath}/common/datasource/dbTree", //ajax请求地址
					success : function(data) {
						$.fn.zTree.init($("#ztree"), setting, data); //加载数据
					},
				});
	});

	function setFontCss(treeId, treeNode) {
		if (treeNode.isParent == true)
			return {};
		else if (treeNode.valid == false)
			return {
				color : "green"
			}
		else
			return {
				color : "red"
			}
	};

	var $table = $('#table');
	var userId;
	$(function() {
		$table.bsTable({
			url : '${pageContext.request.contextPath}/common/jiaoyan/list',
			idField : 'updateLogId',// 指定主键列
			singleSelect : true,
			search : true,
			columns : [ {
				field : 'state',
				checkbox : true
			}, {
				field : 'ruleType',
				title : '校验类型',
				align : 'center'
			}, {
				field : 'userName',
				title : '用户名',
				align : 'center'
			}, {
				field : 'dbName',
				title : '数据库名',
				align : 'center'
			}, {
				field : 'tableName',
				title : '表名',
				align : 'center'
			}, {
				field : 'vaildDate',
				title : '校验时间',
				align : 'center'
			}, {
				field : 'ifPass',
				title : '是否通过',
				align : 'center'
			}, {
				field : 'info',
				title : '详细信息',
				align : 'center'
			},

			]
		});

	});

	$('#tableList')
			.on(
					'shown.bs.select',
					function(e) { // shown.bs.select bootstrap 的内置的
						// 获取checkBox值
						$
								.ajax({
									url : '${pageContext.request.contextPath}/common/jiaoyan/dbList',//写你自己的方法
									type : "post", //数据发送方式
									success : function(data) {
										$
												.each(
														data.res,
														function(i) {
															$(
																	'#tableList.selectpicker')
																	.append(
																			"<option value=" + data.res[i].dbName + ">"
																					+ data.res[i].dbName
																					+ "</option>");
														});
										// 缺一不可  
										$('#tableList').selectpicker('refresh');
										$('#tableList').selectpicker('render');
									},
									error : function(data) {
										alert("查询表格失败" + data);
									}

								})
						$('#tableList').empty();
					});

	//添加
	function jiaoyanAction() {

		var dbName = $('#tableList option:selected').val();//选中的值
		var userId = '${user.userId}'; // 获取当前用户 Id
		// <!-- 模态框不会隐藏 -->
		$('#loadingModal').modal({
			backdrop : 'static',
			keyboard : true
		});
		$("#loadingModal").modal('show');

		$
				.ajax({
					url : '${pageContext.request.contextPath}/common/jiaoyan/jiaoyandb',//写你自己的方法
					type : "post", //数据发送方式
					data : {
						dbName : dbName,
						userId : userId
					},
					dataType : "json",//后台处理后返回的数据格式
					success : function(data) {
						if (data.status == 0) {
							$('#loadingModal').modal('hide');
							alert("校验失败 失败的原因" + data.msg);
						} else {
							$('#loadingModal').modal('hide');
							alert("校验成功" + data.msg);
						}
					},
					error : function(data) {
						$('#loadingModal').modal('hide');
						alert("校验失败 ,失败原因,数据库异常");
					}

				})

	}
	// 删除
</script>

</html>