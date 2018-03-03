<?php
header("Content-type: text/xml"); // changing content type of the file to xml 
require("db_xml.php"); // getting the database info (connection credentials) from db_xml.php

function parseToXML($htmlStr) // function to parse the data fecthed from data base and strip it clean of excess (useless) character like commas etc.
{ 
$xmlStr=str_replace('<','&lt;',$htmlStr); // replace character to allow clean xml output from data parsed
$xmlStr=str_replace('>','&gt;',$xmlStr); // replace character to allow clean xml output from data parsed
$xmlStr=str_replace('"','&quot;',$xmlStr);   // replace character to allow clean xml output from data parsed
$xmlStr=str_replace("'",'&#39;',$xmlStr); // replace character to allow clean xml output from data parsed
$xmlStr=str_replace("&",'&amp;',$xmlStr); // replace character to allow clean xml output from data parsed
return $xmlStr; // return what is left after stripping the above characters from result set.
} 

// opening a connection to a database/server
$connection=mysql_connect ('localhost', $username, $password);
if (!$connection) { // if the following does not connect do the following
die('Not connected : ' . mysql_error()); // print error if cannot connect to database
}

// setting/activiate database connection
$db_selected = mysql_select_db($database, $connection);
if (!$db_selected) {// if the database is cannot be found do following
die ('Can\'t use db : ' . mysql_error());//  print error message
}

// Select all the rows.records in the markers table
$query = "SELECT * FROM markers";
$result = mysql_query($query);
if (!$result) { // if no result is found do the following
die('Invalid query: ' . mysql_error());// print error message.
}



// start XML file, by echoing the parent node
echo '<markers>'; // echo the marker element tag (parent)

// Iterate through the rows, printing XML nodes for each record found in markers table.
while ($row = @mysql_fetch_assoc($result)){
echo '<marker '; // echo single marker tag for each record
echo 'name="' . parseToXML($row['name']) . '" '; // echo name attribute for each record
echo 'content="' . parseToXML($row['marker_con']) . '" '; // echo content attribute for each record
echo 'lat="' . $row['lat'] . '" '; // echo lat (latitude) attribute for each record
echo 'lng="' . $row['lng'] . '" ';// echo long (longitude) attribute for each record
echo '/>'; // echo ending of tag
}

// end the XML file
echo '</markers>'; // echo ending/closing tag for markers

?>
<!--	     
			REFERENCES:
			https://www.w3schools.com/php/php_forms.asp (PHP Guide).
			https://www.w3schools.com/sql/DEfaULT.asP(SQL Guide).
			https://developers.google.com/maps/documentation/javascript/mysql-to-maps (Google maps guide).
-->


 
<!--	                                                 ____  _ _    ____ _           _   
														| __ )(_) |_ / ___| |__   __ _| |_ 
														|  _ \| | __| |   | '_ \ / _` | __|
														| |_) | | |_| |___| | | | (_| | |_ 
														|____/|_|\__|\____|_| |_|\__,_|\__|
-->