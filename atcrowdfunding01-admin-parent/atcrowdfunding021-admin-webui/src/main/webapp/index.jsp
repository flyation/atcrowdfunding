<%--
  Created by IntelliJ IDEA.
  User: FLY
  Date: 2021/2/14
  Time: 1:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%-- 设置base请求路径 --%>
    <%-- 要让发送的请求参考base标签，那么请求前就不能加斜杠，所以base路径最后要加斜杠 --%>
    <%-- pageContext.request.contextPath的值开头自己带了斜杠 --%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
</head>
<body>
    <a href="test/ssm.html">测试ssm</a>
</body>
</html>
