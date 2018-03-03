
<?php
require "includes/db_connect.php";
        if ($_SERVER['REQUEST_METHOD'] == "POST"){

        if ($_POST ['REQUEST_TYPE'] == "LOGIN")
        {
                login($link);
        }
	        if ($_POST ['REQUEST_TYPE'] == "REGISTER")
        {
                register($link);
        }
}
function register($link){
$name =mysqli_real_escape_string($link, substr($_POST ['email'],0,8));
$password = mysqli_real_escape_string($link,$_POST ['password']);
$email = mysqli_real_escape_string($link,$_POST ['email']);
//$password = "test";
//$email = "rcorb001@gold.ac.uk";
//$password = "test";
//$email = "rcorb001@gold.ac.uk";
$to      = $email; // Send email to our user
$sql = "INSERT INTO users (id, username, password, email) VALUES ('NULL','$name','$password','$email')";

$result = mysqli_query($link,$sql);
if ($result==false) echo "failed".$email." ".$password;
$subject = "Welcome to BitChat | Verification"; // Give the email a subject 
$message = "
 
Welcome to bitChat!
You will be able to login with the following credentials after you have activated your account by pressing the url below.
 
Email: '$email'
Password: '$password'
 
Please click this link to activate your account:
http://doc.gold.ac.uk/~rcorb001/bitChat/activate.php?email=".$email; // Our message above including the link

$headers = 'From:noreply@bitchat.com' . "\r\n"; // Set from headers
mail($to, $subject, $message, $headers);
}



function login($link){

$username = mysqli_real_escape_string($link,$_POST['username']);
$password = mysqli_real_escape_string($link,$_POST['password']);
/*
$stmt = $link->prepare("SELECT * FROM users WHERE email = '$username'  AND password = ?")


    $stmt->bind_param("ss", $username,$password);
 
        $stmt->execute();
  
    $result = $stmt->get_result(); 
 
*/
$sql = "SELECT * FROM users WHERE email = '$username' AND password = '$password'";
$result = mysqli_query($link,$sql);
$rowcount = mysqli_num_rows($result);
$activated = 0;
if ($rowcount==1) {
        $row = mysqli_fetch_assoc($result);
        $user = $row['username'];
        $activated = $row['Activated'];

if ($activated == 0) {

echo "unactivated";
}
else {
echo $user;
}}
else if ($rowcount==0) {
echo "incorrect";
}}
?>
