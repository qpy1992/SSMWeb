<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title>SmartHox登录</title>
    <script src="../js/jquery-1.10.0.js"></script>
    <style type="text/css">
        .d1{
            width:200px;
            height:100px;
            text-align:center;
            line-height:40px;
            margin:0 auto;
            margin-top: 75px;
        }
        .login-button { /* 按钮美化 */
            width: 240px; /* 宽度 */
            height: 40px; /* 高度 */
            border-width: 0px; /* 边框宽度 */
            border-radius: 3px; /* 边框半径 */
            background: #ED8500; /* 背景颜色 */
            cursor: pointer; /* 鼠标移入按钮范围时出现手势 */
            outline: none; /* 不显示轮廓线 */
            font-family: Microsoft YaHei; /* 设置字体 */
            color: white; /* 字体颜色 */
            font-size: 17px; /* 字体大小 */
        }
        .login-button:hover { /* 鼠标移入按钮范围时改变颜色 */
            background: #ED9900;
        }
        .inputxt{
            border-radius:3px;
            border-width: 0px;
            border:1px solid #ADADAD;
            width: 240px;
            height: 40px;
            font-family: 'Microsoft YaHei';
            font-size: 17px;
        }
    </style>
    <script type="text/javascript">
        function check(){
            var username = $("#username").val();
            var password = $("#password").val();
            if(username==''){
                alert("请输入用户名");
                return false;
            }
            if(password==''){
                alert("请输入密码");
                return false;
            }
        }
    </script>
</head>
<body>
<div>
    <div class="d1">
    <span><img width="200" height="80" src="../image/logo.png" /></span>
    </div>
    <form id="indexform" name="indexForm" action=${requestScope.url} method="post" style="margin-top: 25px" onsubmit="return check()">
        <input type="hidden" name="redirect_uri" value="${redirect_uri}"/>
        <table border="0" align="center">
            <tr>
                <%--<td><label style="font-family: 'Microsoft YaHei'">账号：</label></td>--%>
                <td><input type="text" class="inputxt" id="username" name="userName" placeholder="账号"></td>
            </tr>
            <tr>
                <%--<td><label style="">密码：</label></td>--%>
                <td><input type="password" class="inputxt" id="password" name="userPassword" placeholder="密码"></td>
            </tr>
        </table>
        <br>
        <div align="center">
            <input class="login-button" type="submit" value="登录" align="middle">
        </div>
        <div align="center" style="margin-top: 50px">
            <label style="font-family: 'Microsoft YaHei';font-size: 10px;color: #ADADAD">&copy;柏田科技</label>
        </div>
    </form>
</div>
</body>
</html>
