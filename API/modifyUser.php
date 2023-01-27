<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>WorkoutLOG - modify user</title>
		<link rel="stylesheet" type="text/css" href="styleIndex.css" />
		<script src="JS/modifyUser.js"></script>
	</head>
	<body>
		<div class="center">
			<?php include "Menu.html"?>
		
			<form id="form" onsubmit="userData(); return false;">
				<label for="vzdevek">Nickname:</label>
				<input type="text" name="vzdevek" required />
				<input type="submit" value="Fetch" />
			</form>
			<br/>
			<form id="update" onsubmit="modifyUser(); return false;" style="display: none">
				<label for="geslo">Password:</label>
				<input type="password" name="geslo" required /><br/>
				
				<label for="ime">Name:</label>
				<input type="text" name="ime" required /><br/>

				<label for="priimek">Surname:</label>
				<input type="text" name="priimek" required /><br/>
								
				<label for="email">Email:</label>
				<input type="email" name="email" required /><br/>

				<input type="submit" value="Update" />
			</form>
			<div id="answer"></div>
		</div>
	</body>
</html>