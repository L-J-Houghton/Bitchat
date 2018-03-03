<?php
session_start(); // starting session
$error=''; // Variable To Store Error Message
if (isset($_POST['submit'])) { // if user has submitted login form do following
if (empty($_POST['username']) || empty($_POST['password'])) { // if the username or password field is empty 
$error = "Username or Password is invalid"; // print error message
}
else // else do
{
// Define $username and $password
$username=$_POST['username']; // store the 
$password=$_POST['password'];
// Establishing Connection with Server by passing server name, user id and password as the parameter
$connection = mysql_connect("localhost", "lhoug001", "Bizibit1515");
// Below used to protect MySQL injection for Security purpose
$username = stripslashes($username);
$password = stripslashes($password);
$username = mysql_real_escape_string($username);
$password = mysql_real_escape_string($password);
// selecting the correct database
$db = mysql_select_db("lhoug001_bitchat", $connection);
// SQL query to fetch information of registerd bitchat users and finds user match(s).
$query = mysql_query("select * from users where password='$password' AND username='$username'", $connection);
$rows = mysql_num_rows($query);
if ($rows == 1) { // if a record is found do following
$_SESSION['login_user']=$username; // Initializing Session  with the username entered by user via form.
header("location: web_app.php"); // redirecting to  main web app page.
} else {
$error = "Username or Password is invalid"; // else print error message
}
mysql_close($connection); // closing database connection
}
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