<?php
require "includes/db_connect.php";
if (isset($_GET['email'])) {
$email = $_GET['email'];
$sql = "UPDATE users SET Activated = '1' WHERE email = '$email'";
$result = mysqli_query($link,$sql);
if ($result==true) echo "account has been activated";
else echo "failed to find the account, please try registering again";
}



?>
