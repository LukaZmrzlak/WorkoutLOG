<?php
$DEBUG = true;

include("orodja.php"); 			// Vključitev 'orodij'

require 'vendor/autoload.php';
use ReallySimpleJWT\Token;

$zbirka = dbConnect();			//Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');	// Dovolimo dostop izven trenutne domene (CORS)
header("Cache-Control: no-cache, no-store, must-revalidate"); // HTTP 1.1.
header("Pragma: no-cache"); // HTTP 1.0.
header("Expires: 0"); // Proxies.

switch($_SERVER["REQUEST_METHOD"])			//glede na HTTP metodo izvedemo ustrezno dejanje nad virom
{
	case 'GET':
		if(!empty($_GET["token"]) & !empty($_GET["datum"]))
		{
			fitnes_uporabnika($_GET["token"], $_GET["datum"]);
		}
		else
		{
			http_response_code(400);	// Bad Request
		}
		break;
}

mysqli_close($zbirka);					// Sprostimo povezavo z zbirko

function fitnes_uporabnika($token, $datum)
{
	global $zbirka;
	$token = mysqli_escape_string($zbirka, $token);
	$datum = mysqli_escape_string($zbirka, $datum);
	$secret = 'sec!ReT423*&';
	$odgovor=array();
	// Validate token and retrieve payload
	if(Token::validate($token,$secret)){
		$payload = Token::getPayLoad($token,$secret);
		$vzdevek = $payload["user_id"];
		if(uporabnik_obstaja($vzdevek) & isset($datum))
		{
			$poizvedba="SELECT vaja, seti FROM fitnes WHERE vzdevek = '$vzdevek' AND datum = '$datum'";
			
			$result=mysqli_query($zbirka, $poizvedba);

			while($vrstica=mysqli_fetch_assoc($result))
			{
				$odgovor[]=$vrstica;
			}
			
			http_response_code(200);		//OK
			echo json_encode($odgovor);
		}
		else
		{
			http_response_code(404);	// Not Found
		}
	}
}
?>