<?php
$DEBUG = true;
include("orodja.php"); 

require 'vendor/autoload.php';
use ReallySimpleJWT\Token;

$zbirka = dbConnect();

header('Content-Type: application/json');	// MIME type of response
header('Access-Control-Allow-Origin: *');	// Allow access from outside of current domain (CORS)
header('Access-Control-Allow-Methods: GET');	// Allowed methods

switch($_SERVER["REQUEST_METHOD"]){
	case 'GET':
		if(!empty($_GET["email"]) & !empty($_GET["geslo"])){
			loginUser($_GET["email"], $_GET["geslo"]);	
		}
		else{
			http_response_code(400);
		}
		break;

	default:
		http_response_code(405);
		break;
}

mysqli_close($zbirka);

// Function compares users login information to the data saved in the zbirka. If successful, the function creates jwt token that is used in further requests
function loginUser($email, $geslo){ 
	global $zbirka;
	$email=mysqli_escape_string($zbirka, $email);
	$gesloInput=mysqli_escape_string($zbirka, $geslo);
	$requestPW="SELECT geslo FROM projekt.vpisi WHERE email='$email'";
	$requestvzdevek="SELECT vzdevek FROM projekt.vpisi WHERE email='$email'";
	$resultPW=mysqli_query($zbirka, $requestPW);
	$resultvzdevek=mysqli_query($zbirka, $requestvzdevek);
	
	if(mysqli_num_rows($resultPW)>0){
		$vzdevek = $resultvzdevek->fetch_array()[0];
		$hashedgeslo = $resultPW->fetch_array()[0];
		// Verify if password is correct
		if(password_verify($gesloInput, $hashedgeslo)){
			// Create jwt token	
			$secret = 'sec!ReT423*&';
			$expiration = time() + 3600;
			$issuer = 'localhost';
			$token = Token::create($vzdevek, $secret, $expiration, $issuer);
			$response = 'Login successful!';
			$array = Array(
				"LoginStatus" => $response,
				"token" => $token);
			http_response_code(200);	// OK
			echo json_encode($array);
		}
		else{
			http_response_code(401);	// Unauthorized (wrong geslo)
		}
	}
	else{
		http_response_code(404);	// Not found
	}
}

?>