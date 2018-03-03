<?php
require "includes/db_connect.php";
	if ($_SERVER['REQUEST_METHOD'] == "POST"){

	if ($_POST ['REQUEST_TYPE'] == "PULL_FORUM")
	{
		pull_forum($link);		
	}
	if ($_POST ['REQUEST_TYPE'] == "PULL_CHAT")
	{
		pull_chat($link);	
	}
	if ($_POST ['REQUEST_TYPE'] == "POST_MESSAGE")
	{
		post_message($link);	
	}	
	        if ($_POST ['REQUEST_TYPE'] == "PULL_FRIENDS")
        {
                pull_friends($link);
        }
                if ($_POST ['REQUEST_TYPE'] == "UPDATE_USERNAME")
        {
                update_username($link);
        }

	}

	
function update_username($link){
$oldName = mysqli_real_escape_string($link,$_POST['old_username']);
$newName = mysqli_real_escape_string($link,$_POST['new_username']);
$sql = "UPDATE users
SET username = '$newName'
WHERE username = '$oldName'";
$result = mysqli_query($link,$sql);
if ($result == true)
echo "username successfully updated";
else
echo "could not upload username, please try a different one";
}

function post_message($link){
$message = mysqli_real_escape_string($link,$_POST['message']);
$user = mysqli_real_escape_string($link,$_POST['username']);
$location = mysqli_real_escape_string($link,$_POST['area']);
$sql = "INSERT INTO messages (message_content, message_loc, message_sender) VALUES ('$message','$location','$user')";
$result = mysqli_query($link,$sql);
echo "sent";
}


function pull_chat($link){
$location = mysqli_real_escape_string($link,$_POST['area']);
$sql = "SELECT message_content, message_sender FROM (SELECT message_id, message_content ,message_loc,message_sender FROM messages WHERE message_loc = '$location' ORDER BY message_id DESC LIMIT 20) g ORDER BY g.message_id";
$result = mysqli_query($link,$sql);
$chat = "";
$sender_array = array();
$array = array();

	while ($row = mysqli_fetch_assoc($result)) {

	//$chat .= "" .($row['message_content']) . ",";
	//$array[$row['message_sender']] =  $row['message_content'];
	$array[] = $row['message_sender'] .": ". $row['message_content'];
	//$message_array[] = $row['message_content'];
	//$sender_array[] = $row['message_sender'];


//pushToArray($array,$row['message_sender'],$row['message_content']);
}
	//$jsonMessage = json_encode($message_array);
	//$jsonSender = json_encode($sender_array);
	//$combined = [$jsonMessage,$jsonSender];
	echo json_encode($array);

}


function pull_forum($link){
$forums = array("RHB","Lib","PSH","DTH","DoC","CG","Gym","WHB");
$jsonforums = json_encode($forums);
echo $jsonforums;

}

function pull_friends($link){
$sql = "SELECT username FROM users";
$result = mysqli_query($link,$sql);
$users = "";
$array = array();
        while ($row = mysqli_fetch_assoc($result)) {

        $users .= "" .($row['username']) . ",";
        $array[] = $row['username'];
}

                echo json_encode($array);

}
	


?>
