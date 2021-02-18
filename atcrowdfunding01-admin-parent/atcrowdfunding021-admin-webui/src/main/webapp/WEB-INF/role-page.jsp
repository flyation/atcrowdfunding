<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<%-- 分页插件的css和js（注意，要在jq之后） --%>
<link rel="stylesheet" href="css/pagination.css">
<script src="jquery/jquery.pagination.js"></script>
<%-- 引入zTree的css和js --%>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script src="ztree/jquery.ztree.all-3.5.min.js"></script>
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

        // 打开新增模态框
        $('#showAddModalBtn').click(function () {
            $('#addModal').modal('show')
        });

        // 新增的保存按钮
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
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#addModal').modal('hide')
                    // 清理模态框
                    $('#addModal [name=roleName]').val('');
                }
            })
        });

        // 打开编辑模态框
        // 由于表格内的数据式动态生成的，翻页后的数据的编辑按钮的绑定事件失效了
        // 使用jq的on方法解决（核心：找到这些动态元素附着的静态元素）。
        // 第一个参数：要绑定的事件名；第二个参数：要绑定事件的元素选择器；第三个事件：事件响应函数
        $('#rolePageBody').on('click', '.showEditModalBtn', function () {
            // 打开编辑模态框
            $('#editModal').modal('show')
            // 回显roleName
            var roleName = $(this).parent().prev().text();
            $('#editModal [name=roleName]').val(roleName);
            // 绑定全局变量，当前数据的roleId
            window.roleId = this.id
        });

        // 执行更新
        $('#updateRoleBtn').click(function () {
            // 获取文本框roleName的值
            var roleName = $('#editModal [name=roleName]').val();
            // 发送更新请求
            $.ajax({
                url: 'role/update.json',
                type: 'post',
                async: false,
                data: {
                    id: window.roleId,
                    name: roleName
                },
                dataType: 'json',
                success: function (response) {
                    var result = response.result;
                    if (result === 'FAILED') {
                        layer.msg('操作失败！' + result.message);
                        return;
                    }
                    layer.msg('更新成功！');
                    // 重新加载分页
                    generatePage();
                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#editModal').modal('hide')
                }
            })
        });

        // 执行删除
        $('#removeRoleBtn').click(function () {
            // 发送删除请求
            $.ajax({
                url: 'role/remove/by/role/id/array.json',
                type: 'post',
                async: false,
                // 数组通过转json发送（从全局范围获得roleIdArray），后端使用@RequestBody接收
                data: JSON.stringify(window.roleIdArray),
                contentType: 'application/json;charset=UTF-8',
                dataType: 'json',
                success: function (response) {
                    var result = response.result;
                    if (result === 'FAILED') {
                        layer.msg('操作失败！' + result.message);
                        return;
                    }
                    layer.msg('删除成功！');
                    // 重新加载分页
                    generatePage();
                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#confirmModal').modal('hide')
                }
            })
        })

        // 单条删除按钮
        $('#rolePageBody').on('click', '.removeBtn', function () {
            var roleArray = [{
                roleId: this.id,
                roleName: $(this).parent().prev().text()
            }]
            // 打开模态框
            showConfirmModal(roleArray)
        });

        // 给总的checkbox绑定点击事件
        $('#summaryBox').click(function () {
            // 获取自身的选中状态
            let currentStatus = this.checked;
            // 设置子box的状态与总box相同
            // HTML元素的固有属性使用prop，自定义的属性使用attr
            $('.itemBox').prop('checked', currentStatus)
        });

        // 全选、全不选的反向操作
        $('#rolePageBody').on('click', '.itemBox', function () {
            // 已选中box的数量
            let checkedBoxlength = $('.itemBox:checked').length;
            // 所有box的数量
            let totalBoxlength = $('.itemBox').length;
            if (checkedBoxlength === 0) {
                $('#summaryBox').prop('checked', false);
            }
            if (checkedBoxlength === totalBoxlength) {
                $('#summaryBox').prop('checked', true);
            }
        });

        // 批量删除按钮
        $('#batchRemoveBtn').click(function () {
            let roleArray = [];
            // 遍历已选中的box
            // each中的this是原生dom，要想使用jq函数的话需要先包装为jq对象
            $('.itemBox:checked').each(function () {
                let roleId = this.id;
                // 通过dom操作获取roleName
                let roleName = $(this).parent().next().text();
                roleArray.push({
                    roleId: roleId,
                    roleName: roleName
                });
            });
            if (roleArray.length === 0) {
                layer.msg('请至少选择一个执行删除！')
                return;
            }
            // 打开模态框
            showConfirmModal(roleArray)
        });

        // 打开绑定权限的模态框
        $('#rolePageBody').on('click', '.checkBtn', function () {
            // 将roleId绑定到全局变量
            window.roleId = this.id
            // 打开绑定权限模态框
            $('#assignModal').modal('show')
            // 生成树
            fillAuthTree();
        });

        // 执行更新绑定的权限
        $('#assignBtn').click(function () {
            // 获取树中已选中的节点的id
            let authIdArray = [];
            let zTreeObj = $.fn.zTree.getZTreeObj('authTreeDemo');
            let checkedNodes = zTreeObj.getCheckedNodes(true);
            for (let i = 0; i < checkedNodes.length; i++) {
                authIdArray.push(checkedNodes[i].id)
            }
            // 请求体内容（为了方便后端接收，此处把roleId放到数组中，这样后端可以用Map<String, List<Integer>>统一接收）
            let requestBody = {
                authIdArray: authIdArray,
                roleId: [window.roleId]
            };
            // 发送ajax请求
            $.ajax({
                url: 'assign/do/role/assign/auth.json',
                type: 'post',
                contentType: 'application/json;charset=UTF-8',
                data: JSON.stringify(requestBody),
                dataType: 'json',
                success: function (response) {
                    if (response.result === 'FAILED') {
                        layer.msg('操作失败！' + response.message);
                        return;
                    }
                    // 消息提示
                    layer.msg('权限分配成功！');
                    // 关闭模态框
                    $('#assignModal').modal('hide')

                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                }
            });
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
                    <button id="batchRemoveBtn" type="button" class="btn btn-danger" style="float:right;margin-left:10px;">
                        <i class=" glyphicon glyphicon-remove"></i> 删除
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
                                <th width="30"><input type="checkbox" id="summaryBox"></th>
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
<%@include file="/WEB-INF/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/modal-role-confirm.jsp"%>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp"%>
</body>
</html>
