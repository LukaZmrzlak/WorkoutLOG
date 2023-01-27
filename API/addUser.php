<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>WorkoutLOG - add user</title>
		<link rel="stylesheet" type="text/css" href="styleIndex.css" />
		<script src="JS/addUser.js"></script>
	</head>
	<body>
		<div class="center">
			<?php include "Menu.html"?>
		
			<form id="form" onsubmit="addUser(); return false;">
				<label for="vzdevek">Nickname:</label><br>
				<input type="text" name="vzdevek" required/> <br>

                <label for="ime">Name:</label><br>
				<input type="text" name="ime" required/> <br>

				<label for="priimek">Surname:</label><br>
				<input type="text" name="priimek" required/> <br>
				
				<label for="email">Email:</label><br>
				<input type="email" name="email" required/> <br>
                				
				<label for="geslo">Password:</label><br>
				<input type="password" name="geslo" required/> <br>
				
				<input type="submit" value="Submit" />
			</form>
			<div id="answer"></div>
		</div>
	</body>
</html>