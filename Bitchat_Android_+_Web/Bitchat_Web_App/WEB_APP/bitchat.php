<?php
include('login.php'); // Including login script used within this script allowing user to login / start a session (see login.php).

if(isset($_SESSION['login_user'])){ // if the session has already been set (in other words if user is logged in) then go straight to the main web app page (see web_app.php).
header("location: web_app.php"); // header (php) used to redirect user to web_app.php if session is open.
}
?>

<!---

	This page is used as a login form to allow the user to login.
	The page is quite simple and simply asks the user to input their 
	username and password to and finally with the use of the submit button
	allows the user to submit those credentials. Notice the name attribute is 
	used within the form as this is used within the post requests which takes the 
	information enetered into the form by the user and is then used within login.php
	to authenticate the user based on those credentials. (see login.php).
	
-->

<!DOCTYPE html> 
<html>
<head>
<title>Login - Bitchat</title>
<link href="CSS/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="Home"><a href="home.php">Home</a></div>
<div id="logo"><img src="IMG/bitchat/logo.png"></div>
<div id="logofont">Bitchat</div>
<div id="toolbar"><img src="IMG/bitchat/toolbar.png"></div>
<div id="footer"><img src="IMG/bitchat/footer.png"></div>
<div id="signpanel"><img src="IMG/bitchat/signpanel.png"></div>
<div id="shadow"></div>
<div id="background">
<form action="" method="post">
<input id="username" name="username" placeholder="username" type="varchar"> <!--- username field   --->
<input id="password" name="password" placeholder="**********" type="password"> <!--- password field   --->
<input id ="signbutton" name="submit" type="submit" value=""> <!--- submit button  --->
<!-- <span><?php //echo $error; ?></span> <<<< testing purposes -->
</form>
</div>

</body>
</html>
<!--	     
			REFERENCES:
			https://www.w3schools.com/html/default.asp (DOCTYPE reference).
			http://php.net/manual/en/features.sessions.php (PHP Guide).
-->


 
<!--	                                                 ____  _ _    ____ _           _   
														| __ )(_) |_ / ___| |__   __ _| |_ 
														|  _ \| | __| |   | '_ \ / _` | __|
														| |_) | | |_| |___| | | | (_| | |_ 
														|____/|_|\__|\____|_| |_|\__,_|\__|
-->