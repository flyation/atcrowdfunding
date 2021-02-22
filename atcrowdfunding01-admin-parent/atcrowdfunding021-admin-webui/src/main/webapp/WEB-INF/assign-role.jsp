<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-CN">
<%@ include file="/WEB-INF/include-head.jsp" %>
<script>
    $(function () {
        // 为角色分配的左右箭头绑定事件
        // :eq(0)表示选择页面上的第一个
        // :eq(1)表示选择页面上的第二个
        // >表示选择子元素
        // :selected表示被选中的option
        // appendTo表示将指定的jq对象追加到指定元素内部的最后
        // prependTo表示将指定的jq对象追加到指定元素内部的最前
        // 右箭头（未分配->已分配）
        $('.glyphicon-chevron-right').click(function () {
            $('select:eq(0) > option:selected').appendTo('select:eq(1)');
        });
        // 左箭头（已分配->未分配）
        $('.glyphicon-chevron-left').click(function () {
            $('select:eq(1) > option:selected').prependTo('select:eq(0)');
        });

        // 点击保存时将已分配下拉框中的所有选项都选中
        $('#submitBtn').click(function () {
            $('select:eq(1) > option').prop('selected', 'selected');
        });
    })
</script>
<body>
<%@ include file="/WEB-INF/include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@ include file="/WEB-INF/include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <h3>param:${param}</h3>
                    <form role="form" class="form-inline" action="assign/do/role/assign.html" method="post">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="exampleInputPassword1">未分配角色列表</label><br>
                            <select class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${unassignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="exampleInputPassword1">已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <br>
                        <br>
                        <button id="submitBtn" class="btn btn-lg btn-success btn-block" style="width: 200px;">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
