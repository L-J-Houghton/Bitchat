<!---   
		This php file (web_app.php) is the central page of which the web app consists of.
		This page displays the map with a custom overlay made to enhance the user experience. 
		The page also consists of links (buttons) which if selected by the user will redirect them
		to the chosen chat area. The user can also access these chat areas by clicking on the markers
		on the map itself as links have been added to enable the user to visit the chat room of their 
		choice.

		This page also contains a sign out feature to enable the user to sign out if necessary. Thus ending the session
		and logging user out of web app. Below is the code used to generate this page with comments. Javascript has been
		used to create and display the campus map, this has been done by first taking set bounds (longitude and latitude values)
		to focus on the map. Javascript was required to implement the map to the web app as the google maps web api is written
		in javascript thus allowing cross-origin browser viewing for users that use other browser such as firefox etc.

--->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> <!-- official doctype -->
<?php

include("db.php"); // including db connection.
include('session.php'); // including user session (session.php)
?>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> <!-- using meta tag -->
        <title>Bitchat - map</title> <!-- title of page -->
        <link href="CSS/map.css" rel="stylesheet" type="text/css"><!-- (see map.css) -->
   <script 
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDakXpQNVyex4fG0eNk0o7WlFwKL-JZpbA&callback="> // cdn for google maps api using key generated.
    </script>
    	
<script> // start of maps 
    var overlay;// variable to store overlay data
    testOverlay.prototype = new google.maps.OverlayView(); // overlay object made using overlay view made within maps api.

        function initialize() { // initialize the map
        var mapOptions = {
            center: new google.maps.LatLng(51.47397285957636,-0.03599117286546516), // setting long/lat center point values used to create position of map.
			zoom: 17, // The initial zoom level when the map loads (0-20) 
			minZoom: 17, // Minimum zoom level allowed (0-20)
			maxZoom: 18, // max zoom 18 to allow user to zoom in on campus.
			mapTypeControl:false,// disable controls in map and thus setting campus map to a set state.
			streetViewControl:false, // Set to disable to hide street view
			overviewMapControl:false, // Set to false to remove overview control
			draggable: false // disable drag option.
        };
        var infoWindow = new google.maps.InfoWindow; // creating new information window.
	    var map = new google.maps.Map(document.getElementById('map'), mapOptions); // creating map variable using the map variable aboe containing the set values.
        var swBound = new google.maps.LatLng(51.47181841731777, -0.04331452386475121); // creating south west bound for overlay
        var neBound = new google.maps.LatLng(51.476112107879196, -0.028639217834665942);// creating north east bound for overlay
        var bounds = new google.maps.LatLngBounds(swBound, neBound); // storing those variables above into one variable called bounds which holds value of the sizing of map later used for overlay image.

        var Image = 'IMG/web_app/test_overlay.png'; // image of the overlay , stored as variable
        overlay = new testOverlay(bounds,Image, map); // creating a new overlay object using overlay class built within google api.

		  var link; // var link later used to store links to the chat areas.
          downloadUrl('http://doc.gold.ac.uk/~lhoug001/bitchat/xml_output.php', function(data) { // using download url attribute to grab data from xml output file 
            var xml = data.responseXML;// storing the data grabbed from xml file into variable as required.
            var markers = xml.documentElement.getElementsByTagName('marker');// storing the marker 
            Array.prototype.forEach.call(markers, function(markerElem) { // creating an array and looping through each marker
              var name = markerElem.getAttribute('name'); // storing name/location of marker
              var content = markerElem.getAttribute('content');// getting additional info of marker
              var point = new google.maps.LatLng( // setting the geolocation of marker.
                  parseFloat(markerElem.getAttribute('lat')),// latitude value 
                  parseFloat(markerElem.getAttribute('lng')));// longitude value
			  if(name == "RHB"){// if the name/location of marker is RHB
				  link = "RHB.php";// then the link/varibale made above will be RHB
			  }else if(name == "IGLT"){// if the name/location of marker is IGLT
				  link="IGLT.php";// then the link/varibale made above will be IGLT
			  }else if(name == "LIB"){// if the name/location of marker is IGLT
				  link="LIB.php";// then the link/varibale made above will be IGLT
			  }else if(name == "PSH"){// if the name/location of marker is PSH
				  link="PSH.php";// then the link/varibale made above will be PSH
			  }else if(name == "DOC"){// if the name/location of marker is DOC
				  link="DOC.php";// then the link/varibale made above will be DOC
			  }else if(name == "GREEN"){// if the name/location of marker is GREEN
				  link="GREEN.php";// then the link/varibale made above will be GREEN
			  }else if(name == "GYM"){// if the name/location of marker is GYM
				  link="GYM.php";// then the link/varibale made above will be GYM
			  }else{
				  link = "";// then the link/varibale made above will be blank
			  }
              var infowincontent = document.createElement('div');// creating div tag for info window
              var strong = document.createElement('strong');// creating element strong 
              strong.textContent = name // setting the content of the element made above to the name/location
              infowincontent.appendChild(strong);//  adding that element to the info window
              infowincontent.appendChild(document.createElement('br'));// adding a break line to signify a new line.

              var text = document.createElement('text');
              text.textContent = content
			  // adding text element to info window of marker
              infowincontent.appendChild(text);
			  // using base 64 encoding to encode marker image form use within maps api.
			  //Base 64 online encoder used to encode the marker image: https://www.base64-image.de/
			var image = new google.maps.MarkerImage("data:image/png;base64,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", null, null, null, new google.maps.Size(20,26)); // Create a variable for the marker image.
			var marker = new google.maps.Marker({ // Set the marker
			position: point, // Position marker to coordinates
			icon:image, //use our image as the marker
			url: 'http://doc.gold.ac.uk/~lhoug001/bitchat/'+link, // set url to appropriate destination based upon the name of the 
			map: map, // assign the market to our map variable
			title: 'Open Message!' // Marker ALT Text
		});
		var infowindow = new google.maps.InfoWindow({ // Create a new InfoWindow
  			content: content// message contents of the InfoWindow
  		});
              marker.addListener('click', function() { // event listener to check mouse click on 
				window.location.href = marker.url; // set the marker as a anchor tag (link) using the appropriate url infomration of the selected marker 
                //infoWindow.setContent(infowincontent); // ignore
                infoWindow.open(map, marker); // open info window
              });
              marker.addListener('mouseover', function() { // event listener for mouse hover when over marker
                infoWindow.setContent(infowincontent); // set the correct content of each markers info window using data fetched from database.
                infoWindow.open(map, marker); // open info window
              });
            });
          });
        }


			
      function downloadUrl(url, callback) { // function to essentially get the output xml from url 
        var request = window.ActiveXObject ? new ActiveXObject('Microsoft.XMLHTTP') : // making XML request using the ActiveXObject class, see next line
            new XMLHttpRequest; // XML request using http request as igor/server runs on http and not https (no SSL)

        request.onreadystatechange = function() { // function used to 
          if (request.readyState == 4) { // once request is complete
            request.onreadystatechange = doNothing; // use the do nothing function which does nothing
            callback(request, request.status); // call back the http request
          }
        };

        request.open('GET', url, true); // using http request to get 
        request.send(null); // send no data to http request
      }
			// do nothing function used if no events have occured.
      function doNothing() {}
	  // testoverlay function used to get the sw and ne bounds, image and the map created in above code in script
    function testOverlay(bounds, image, map) {
        this.bounds_ = bounds;
        this.image_ = image;
        this.map_ = map;
        this.div_ = null;
        this.setMap(map);
    }
		// using the testoverlay function made above and assigning values to the overlay image , these include positioning and styling
    testOverlay.prototype.onAdd = function () {
        var div = document.createElement('div');
        div.style.borderStyle = 'none';
        div.style.borderWidth = '0px';
        div.style.position = 'absolute';

        var img = document.createElement('img'); // creating img element used to hold the image overlay made.
        img.src = this.image_;
		// setting proportions/styles + positioning.
        img.style.width = '100%';
        img.style.height = '100%';
        img.style.opacity = '0.5';
        img.style.position = 'absolute';
        div.appendChild(img); // adding image element to to the div tag 
        this.div_ = div;
		// varibale to store the panes (panes within maps api)
        var panes = this.getPanes();
        panes.overlayLayer.appendChild(div); // overlay all panes with the the overlay image.
    };
	
    testOverlay.prototype.draw = function () { // using draw method within the maps api to draw the overaly using south west + north east max bounds 
        var overlayProjection = this.getProjection(); // get projection method
        var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest()); // changing the geolocation values to pixel values allowing the overlay to be displayed in browser
        var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast()); // changing the geolocation values to pixel values allowing the overlay to be displayed in browser
        var div = this.div_;
		
		//
		
        div.style.left = sw.x + 'px';  // 
        div.style.top = ne.y + 'px';  // changing the geolocation values to pixel values allowing the overlay to be displayed in browser
        div.style.width = (ne.x - sw.x) + 'px';  // changing the geolocation values to pixel values allowing the overlay to be displayed in browser
        div.style.height = (sw.y - ne.y) + 'px';  // changing the geolocation values to pixel values allowing the overlay to be displayed in browser
    };

    testOverlay.prototype.updateBounds = function (bounds) { // updating the bounds , therefore making the overlay relative to the map for example when zooming in etc.
        this.bounds_ = bounds;
        this.draw();
    };

    testOverlay.prototype.onRemove = function () { // remove function: to remove child elements from parent nodes for overlay calibration etc.
        this.div_.parentNode.removeChild(this.div_); // remove the child element from the parent node made.
        this.div_ = null; // set the div tag to null
    };

    google.maps.event.addDomListener(window, 'load', initialize); // loading all of the above made including markers, image overlay etc.
</script>
    </head>
    <body>
	<!-- 
		Below I've used div tags to seperate each element of the webapp 
		these tags contain images used for ui/ux, user options and links
		to external pages to enable the user to navigate around the web app.
	-->
        <div id="background">
            <div id="map"></div>
            <div id="footer"><img src="IMG/web_app/footer.png"></div>
            <div id="sendheader"><img src="IMG/web_app/sendheader.png"></div>
            <div id="option">				 
				  <select name="loc">
				  <option value="RHB">RHB</option>
				  <option value="IGLT">IGLT</option>
				  <option value="LIB">LIB</option>
				  <option value="PSH">PSH</option>
				  <option value="DOC">DOC</option>
				  <option value="GREEN">GREEN</option>
				  <option value="GYM">GYM</option>
				 </select></div>
            <div id="toolbar"><img src="IMG/web_app/toolbar.png"></div>
            <div id="logofont">Bitchat</div>
            <div id="Hi">Hi</div>
            <div id="Home"><a href="home.php">Home</a></div>
            <div id="Signout"><a href="logout.php">Sign out</a></div>
            <div id="logo"><img src="IMG/web_app/logo.png"></div>
            <div id="IGLTbutton"><a href="IGLT.php">IGLT</a></div>
            <div id="LIBbutton"><a href="LIB.php">LIB</a></div>
            <div id="PSHbutton"><a href="PSH.php">PSH</a></div>
            <div id="GREENbutton"><a href="GREEN.php">GREEN</a></div>
            <div id="DOCbutton"><a href="DOC.php">DOC</a></div>
            <div id="RHBbutton"><a href="RHB.php">RHB</a></div>
            <div id="username"><?php echo $login_session;?></div>
			<!-- form made allowing user to send quick message to area of their choice. -->
	 <form action="web_app.php" method="post" enctype="multipart/form-data">
			
		     <tr>
		         <td align="right">Message:</td> <!-- title -->
				 <textarea type="text" name="msg_con" id="chatbox" required>Send a quick message!</textarea> <!-- message area -->
			 </tr>
		<div id="submit"><input type="submit" name="web_app" value=""/></div> <!-- submit button -->
		</form>
        </div>
 </body>
 </html>
 <?php

     if(isset($_POST['web_app'])){ // if user has pressed submit do following
		 
		
		//$name = $_POST['name'];
		$content = $_POST['msg_con']; // post message content user has submitted
		$loc = $_POST['loc']; // post location user has selected from drop down
		
		 $long; // var to store longitude
		 $lat;// var to store latitude
		 
			/* below are a series of if/else statements used that are made to direct the message sent within the form above to the 
			   correct chatroom. Inside these statements I've used set longitude and latitude values to set the messages to a
			   specific geolocation on the campus map.
			   
			   Note: using prepared statements to prevent SQL injection and to increase security level of the data interaction between client and server.
			   
			 */
			 
		 if($loc == 'RHB'){
			 
			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'RHB', -0.038573, 51.473728, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'IGLT'){

			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'IGLT', -0.037050, 51.473696, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'LIB'){

			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'LIB', -0.035977, 51.474986, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'PSH'){
			 
			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'PSH', -0.037050, 51.472674, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'GYM'){
			 
			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'GYM', -0.037789, 51.473129, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'GREEN'){

			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'GREEN', -0.036459, 51.473293, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else if ($loc == 'DOC'){

			 $sql = $con->prepare("INSERT INTO messages(message_content,message_loc,message_long,message_lat,message_sender) VALUES (?, 'DOC', -0.038573, 51.473728, ?)");
			 $sql->execute([$content, $message_sender]);
			 
		 }else{
			 
			 echo '<script type="text/javascript">alert("Try again!");</script>'; // javascript prompt used if all else fails.
			 
		 }
		
		

		

	}


?>
<!--	     
			REFERENCES:
			https://www.w3schools.com/html/default.asp (DOCTYPE reference).
			https://www.w3schools.com/php/php_forms.asp (PHP Guide).
			https://www.w3schools.com/sql/DEfaULT.asP(SQL Guide).
			https://developers.google.com/maps/documentation/javascript/reference(Google maps api guide).
			https://www.base64-image.de/ (Base 64 - encoder link).
-->


 
<!--	                                                 ____  _ _    ____ _           _   
														| __ )(_) |_ / ___| |__   __ _| |_ 
														|  _ \| | __| |   | '_ \ / _` | __|
														| |_) | | |_| |___| | | | (_| | |_ 
														|____/|_|\__|\____|_| |_|\__,_|\__|
-->