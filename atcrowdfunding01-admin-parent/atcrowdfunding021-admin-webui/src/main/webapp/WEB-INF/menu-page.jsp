<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<%-- 引入zTree的css和js --%>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script src="ztree/jquery.ztree.all-3.5.min.js"></script>
<%-- 引入我们自己写的my-menu.js --%>
<script src="crowd/my-menu.js"></script>
<script>
    $(function () {
        // 生成菜单树
        generateTree();

        // 给按钮组绑定点击事件
        // 新增按钮
        $('#treeDemo').on('click', '.addBtn', function () {
            // 将当前节点的id作为新添加节点的pid，保存到全局变量
            window.pid = this.id;
            // 打开模态框
            $('#menuAddModal').modal('show');
        });
        // 执行保存
        $('#menuSaveBtn').click(function () {
            $.ajax({
                url: 'menu/save.json',
                type: 'post',
                data: {
                    name: $.trim($('#menuAddModal input[name=name]').val()),
                    url: $.trim($('#menuAddModal input[name=url]').val()),
                    icon: $.trim($('#menuAddModal input[name=icon]:checked').val()),
                    pid: window.pid
                },
                dataType: 'json',
                success: function (response) {
                    if (response.result === 'FAILED') {
                        layer.msg('操作失败！' + response.message);
                        return;
                    }
                    layer.msg('保存成功！');
                    // 刷新菜单树
                    generateTree();
                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#menuAddModal').modal('hide');
                    // 清空表单（通过触发重置按钮的点击事件）
                    $('#menuResetBtn').click();
                }
            });
        });

        // 编辑按钮
        $('#treeDemo').on('click', '.editBtn', function () {
            // 将当前节点的id保存到全局变量
            window.id = this.id;
            // 打开编辑模态框
            $('#menuEditModal').modal('show');
            // 获取zTreeObj
            let zTreeObj = $.fn.zTree.getZTreeObj('treeDemo');
            // 用来搜索的属性名
            let key = 'id';
            // 用来搜索的属性值
            let value = this.id;
            // 搜索当前节点对象
            let currentNode = zTreeObj.getNodeByParam(key, value);
            console.log(currentNode)
            // 回显表单数据
            $('#menuEditModal input[name=name]').val(currentNode.name);
            $('#menuEditModal input[name=url]').val(currentNode.url);
            // 选中radio时需注意把value值放在数组里，道理可关联checkbox
            $('#menuEditModal input[name=icon]').val([currentNode.icon]);
        });
        // 执行更新
        $('#menuEditBtn').click(function () {
            $.ajax({
                url: 'menu/update.json',
                type: 'post',
                data: {
                    id: window.id,
                    name: $.trim($('#menuEditModal input[name=name]').val()),
                    url: $.trim($('#menuEditModal input[name=url]').val()),
                    icon: $.trim($('#menuEditModal input[name=icon]:checked').val()),
                },
                dataType: 'json',
                success: function (response) {
                    if (response.result === 'FAILED') {
                        layer.msg('操作失败！' + response.message);
                        return;
                    }
                    layer.msg('更新成功！');
                    // 刷新菜单树
                    generateTree();
                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#menuEditModal').modal('hide');
                }
            });
        });

        // 删除按钮
        $('#treeDemo').on('click', '.removeBtn', function () {
            // 将当前节点的id保存到全局变量
            window.id = this.id;
            // 打开删除模态框
            $('#menuConfirmModal').modal('show');
            // 获取zTreeObj
            let zTreeObj = $.fn.zTree.getZTreeObj('treeDemo');
            // 用来搜索的属性名
            let key = 'id';
            // 用来搜索的属性值
            let value = this.id;
            // 搜索当前节点对象
            let currentNode = zTreeObj.getNodeByParam(key, value);
            console.log(currentNode)
            // 回显表单数据
            $('#removeNodeSpan').html('【<i class="' + currentNode.icon + '"></i>' + currentNode.name + '】');
        });
        // 执行删除
        $('#confirmBtn').click(function () {
            $.ajax({
                url: 'menu/remove.json',
                type: 'post',
                data: {
                    menuId: window.id,
                },
                dataType: 'json',
                success: function (response) {
                    if (response.result === 'FAILED') {
                        layer.msg('操作失败！' + response.message);
                        return;
                    }
                    layer.msg('删除成功！');
                    // 刷新菜单树
                    generateTree();
                },
                error: function (xhr) {
                    layer.msg('服务端调用失败！响应状态码=' + xhr.status + '，说明信息=' + xhr.responseText)
                },
                complete: function () {
                    // 关闭模态框
                    $('#menuConfirmModal').modal('hide');
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
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<%-- 引入增删改菜单的模态框 --%>
<%@include file="modal-menu-add.jsp"%>
<%@include file="modal-menu-edit.jsp"%>
<%@include file="modal-menu-confirm.jsp"%>
</html>
