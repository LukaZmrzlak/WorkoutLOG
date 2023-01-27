<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>WorkoutLOG - delete user</title>
		<link rel="stylesheet" type="text/css" href="styleIndex.css" />
		<script src="JS/deleteUser.js"></script>
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
			<form id="delete" onsubmit="deleteUser(); return false;" style="display: none">
				<label for="ime">Name:</label>
				<input type="none" name="ime" required /><br/>

				<label for="priimek">Surname:</label>
				<input type="text" name="priimek" required /><br/>
								
				<label for="email">Email:</label>
				<input type="email" name="email" required /><br/>

				<input type="submit" value="Delete" />
			</form>
			<div id="answer"></div>
		</div>
	</body>
</html>