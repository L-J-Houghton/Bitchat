<?php 
// Database connrction file containg credentials to connect else throw error message.

$con = mysqli_connect('localhost','lhoug001','Bizibit1515','lhoug001_bitchat');

if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }


?>
