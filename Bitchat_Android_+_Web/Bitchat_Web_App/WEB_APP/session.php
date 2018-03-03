<?php
// Establish database connection with igor by passing server name, user id and password as the parameter
$connection = mysql_connect("localhost", "lhoug001", "Bizibit1515");
// selecting bitchat database
$db = mysql_select_db("lhoug001_bitchat", $connection);
session_start();// starting session
$user_check=$_SESSION['login_user'];// storing session
// SQL query to fetch complete result set Of user(s) based on connection made above.
$ses_sql=mysql_query("select username from users where username='$user_check'", $connection);
$row = mysql_fetch_assoc($ses_sql);
$login_session =$row['username'];

if(!isset($login_session)){// if the login is not set then do following
mysql_close($connection); // close database connection
header('Location: index.php'); // redirect back to index.php which then goes to login screen if user session is not open.
}
?>
<!--	     
			REFERENCES:
			http://php.net/manual/en/features.sessions.php (PHP Guide).
			https://www.w3schools.com/php/default.asp (PHP Guide).
			
-->


 
<!--	                                                 ____  _ _    ____ _           _   
														| __ )(_) |_ / ___| |__   __ _| |_ 
														|  _ \| | __| |   | '_ \ / _` | __|
														| |_) | | |_| |___| | | | (_| | |_ 
														|____/|_|\__|\____|_| |_|\__,_|\__|
-->