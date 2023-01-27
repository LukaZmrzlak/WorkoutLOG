<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>WorkoutLOG all users</title>
		<link rel="stylesheet" type="text/css" href="styleIndex.css" />
		<script src="JS/allUsers.js"></script>
	</head>
	<body onload="allUsers();">
		<div class="center">
			<?php include "Menu.html"?>
		
			<table id="table">
				<tr>
					<th>Vzdevek</th>
					<th>Ime</th>
					<th>Priimek</th>
					<th>E-mail</th>
				</tr>
			</table>
			<div id="odgovor"></div>
		</div>
	</body>
</html>