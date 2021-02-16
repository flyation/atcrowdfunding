<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<%-- 分页插件的css和js（注意，要在jq之后） --%>
<link rel="stylesheet" href="css/pagination.css">
<script src="jquery/jquery.pagination.js"></script>
<%-- 引入我们自己写的js --%>
<script type="text/javascript" src="crowd/my-role.js"></script>
<script>
    $(function () {
        // 为分页做初始化
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = '';

        // 进入页面时获取分页数据
        generatePage();

        // 绑定搜索按钮事件
        $('#searchBtn').click(function () {
            // 获取搜索关键词
            window.keyword = $('#keywordInput').val();
            // 调用分页函数刷新页面
            generatePage();
        });

        // 绑定新增按钮事件
        $('#showAddModalBtn').click(function () {
            // 打开模态窗
            $('#addModal').modal('show')
        });

        // 绑定新增保存按钮事件
        $('#saveRoleBtn').click(function () {
            // 获取输入的角色名（通过jq属性选择器）
            let roleName = $('#addModal [name=roleName]').val();
            // 发送ajax
            $.ajax({
                url: 'role/save.json',
                type: 'post',
                async: false,
                data: {
                    name: roleName
                },
                dataType: 'json',
                success: function (response) {
                    var result = response.result;
                    if (result === 'FAILED') {
                        layer.msg('操作失败！' + result.message);
                        return;
                    }
                    layer.msg('操作成功！');
                    // 重新加载分页
                    window.pageNum = 999999; //设置一个大值，使跳到最后一页
                    generatePage();
                },
                error: function (response) {
                    layer.msg('服务端调用失败！响应状态码=' + response.status + '，说明信息=' + ajaxResult.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#addModal').modal('hide')
                    // 清理模态框
                    $('#addModal [name=roleName]').val('');
                }
            })
        });

    })
</script>
<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button id="showAddModalBtn" type="button" class="btn btn-primary" style="float:right;">
                        <i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- 引入模态框 --%>
<%@include file="/WEB-INF/modal-role-add.jsp"%>
</body>
</html>
