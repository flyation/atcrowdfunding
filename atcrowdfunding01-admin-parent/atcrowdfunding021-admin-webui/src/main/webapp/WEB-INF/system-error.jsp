<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/font-awesome.min.css">
    <link rel="stylesheet" href="css/login.css">
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script src="layer-v3.1.1/layer/layer.js"></script>
    <script>
        $(function () {
            $('#backBtn').click(function () {
                // 返回历史列表中的上一个url
                window.history.back()
            })
        })
    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container" style="text-align: center">
    <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 尚筹网消息系统</h2>
    <%--
        requestScope对应存放request域数据的map
        requestScope.exception相当于request.getAttribute("exception")
        requestScope.exception.message相当于request.getAttribute("exception").getMessage()
    --%>
    <h3>${requestScope.exception.message}</h3>
    <h3>${requestScope.exception}</h3>
<%--    <form class="form-signin">--%>
        <button id="backBtn" class="btn btn-lg btn-success btn-block" style="width: 200px;margin: 0 auto">返回上一步</button>
<%--    </form>--%>
</div>
</body>
</html>
