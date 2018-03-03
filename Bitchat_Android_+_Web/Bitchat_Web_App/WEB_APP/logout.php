<?php
session_start();
if(session_destroy()) // Destroying current session
{
header("Location: bitchat.php"); // Redirecting To login page
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