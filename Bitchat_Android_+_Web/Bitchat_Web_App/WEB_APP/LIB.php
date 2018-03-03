<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> <!-- official doctype -->
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> <!-- using meta tags (see meta tag under form submission) -->
		<title>LIB Chatroom</title> <!-- title of page/chatroom -->
		<link href="CSS/RHB.css" rel="stylesheet" type="text/css"> <!-- stylesheet /css code -->
	</head>
	<body>
		<div id="background">
	<table id="message_box"> <!-- creating table to store data etc. -->
	<?php 
	include("db.php"); // include database connection file
	include('session.php'); // including session file
	$get_message = "select * from messages where message_loc = 'LIB'"; // select all messages from appropriate chatroom using location field 
	
	$run_message = mysqli_query($con, $get_message); 
	
	$i = 0; // i will be used as a counter 
	
	while ($row_message=mysqli_fetch_array($run_message)){
		
		$message_id = $row_message['message_id']; // set the message id variable to that of the id fecthed from row
		$message_content = $row_message['message_content'];// set the message content variable to that of the text fecthed from row
		$message_location = $row_message['message_loc'];// set the message location variable to that of the location fecthed from row
		$message_sender = $row_message['message_sender'];// set the message sender variable to that of the sender fecthed from row
		$message_time = $row_message['time']; // set the message time variable to that of the time/timestamp fecthed from row
		$i++; // increment i on each cycle
		 //echo "<meta http-equiv='refresh' content='0'>"
	?> 
	<tr align="center" style="max-height:20px;"> <!-- align table row central with 20px being max height of each table row -->
		<td id="sender"><?php echo $message_sender;?></td><!-- echo the sender name -->
		<td id="message"><?php echo $message_content;?></td><!-- echo the message itself -->
		<td id="time"><?php echo date('H:i',strtotime($message_time));?></td> <!-- format timestamp into hour and minute for simplicity -->
		
	</tr>
	<?php } ?> <!-- end php tag -->
</table> <!-- end of table -->
			<!-- Below are the div tags that contain the images used for ui purposes 
				 along with the appropriate links that allow the user to logout / quit session
				 aswell as links to navigate around the web app itself.
				 
				 There is also a form used to allow the user to send a quick message within the chatroom.
			-->
			<div id="back"><img src="IMG/chatroom/back.png"></div>
			<div id="footer"><img src="IMG/chatroom/footer.png"></div>
			<div id="toolbar"><img src="IMG/chatroom/toolbar.png"></div>
			<div id="RHBChatroom"><img src="IMG/chatroom/LIBChatroom.png"></div>
			<div id="RHBshadow"></div>
			<div id="logofont">Bitchat</div>
            <div id="hi">Hi</div>
            <div id="home"><a href="home.php">Home</div>
            <div id="signout"><a href="logout.php">Sign out</a></div>
			<div id="logo"><img src="IMG/chatroom/logo.png"></div>
			<div id="username">  <?php echo $login_session; ?></div>
			<div id="Send"></div>
			<form action="LIB.php" method="post" enctype="multipart/form-data">
			<textarea id="textarea" name="msg_con" required>Enter message here...</textarea>
			<input type="submit" name="LIB" value=""/>
		</form>
		</div>
 </body>
 </html>
 	
<?php

     if(isset($_POST['LIB'])){ // the user has submitted the chat to chatroom via the form do the following
		 
		
		//$name = $_POST['name'];
		$content = $_POST['msg_con']; // get the message content submitted via the form
			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'LIB', -0.035977, 51.474986, ?)");
			 $sql->execute([$content, $message_sender]);
			 echo "<meta http-equiv='refresh' content='0'>"; // meta tag used in this case to prevent duplicate form entry on refresh

		

	}


?>
<!--	     
			REFERENCES:
			https://www.w3schools.com/html/default.asp (DOCTYPE reference).
			https://www.w3schools.com/php/php_forms.asp (PHP Guide).
			https://www.w3schools.com/sql/DEfaULT.asP(SQL Guide).
-->


 
<!--	                                                 ____  _ _    ____ _           _   
														| __ )(_) |_ / ___| |__   __ _| |_ 
														|  _ \| | __| |   | '_ \ / _` | __|
														| |_) | | |_| |___| | | | (_| | |_ 
														|____/|_|\__|\____|_| |_|\__,_|\__|
-->