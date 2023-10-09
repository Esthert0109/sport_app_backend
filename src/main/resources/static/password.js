<script type="text/javascript" th:src="@{/js/md5.min.js}"></script>
const g_passsword_salt = "1a2b3c4d";
const salt = g_passsword_salt;
const str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
const password = md5(str);