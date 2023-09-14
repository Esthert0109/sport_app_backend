<script type="text/javascript" th:src="@{/js/md5.min.js}"></script>
var g_passsword_salt="1a2b3c4d"
var salt = g_passsword_salt;
var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
var password = md5(str);