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
    <script src="jquery/jquery-2.1.1.min.js"></script>
    <script>
        $(function () {
            $('#btn1').click(function () {
                $.ajax({
                    url: 'send/array1.html',
                    type: 'post',
                    data: {
                        'array[]': [1, 2, 8]
                    },
                    dataType: 'text',
                    success: function (response) {
                        alert(response)
                    },
                    error: function (response) {
                        alert(response)
                    }
                })
            })

            $('#btn2').click(function () {
                $.ajax({
                    url: 'send/array2.json',
                    type: 'post',
                    data:JSON.stringify([1, 2, 8]),
                    contentType: 'application/json;charset=UTF-8',
                    dataType: 'json',
                    success: function (response) {
                        console.log(response)
                    },
                    error: function (response) {
                        alert(response)
                    }
                })
            })
        })
    </script>
</head>
<body>
    <a href="test/ssm.html">测试ssm</a>

    <button id="btn1">send1</button>
    <button id="btn2">send2</button>
</body>
</html>
