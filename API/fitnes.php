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
		
	case 'POST':
			if(!empty($_GET["token"]))
			{
				dodaj_fitnes($_GET["token"]);
			}
			else
			{
				http_response_code(400);	// Bad Request

			}
			break;
		
	default:
		http_response_code(405);	//Method Not Allowed
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

function dodaj_fitnes($token)
{
	global $zbirka, $DEBUG;
	$token = mysqli_escape_string($zbirka, $token);
	$secret = 'sec!ReT423*&';
	$podatki = json_decode(file_get_contents('php://input'), true);
	
	if(Token::validate($token,$secret)){
		$payload = Token::getPayLoad($token,$secret);
		$vzdevek = $payload["user_id"];
		if(isset($podatki["datum"], $podatki["vaja"],$podatki["seti"]))
		{
			if(uporabnik_obstaja($vzdevek))	//preprecimo napako zaradi krsitve FK 
			{
				$datum = mysqli_escape_string($zbirka, $podatki["datum"]);
				$vaja = mysqli_escape_string($zbirka, $podatki["vaja"]);
				$seti = mysqli_escape_string($zbirka, $podatki["seti"]);
					
				$poizvedba="INSERT INTO fitnes (vzdevek, datum, vaja, seti) VALUES ('$vzdevek', '$datum', '$vaja', '$seti')";

				if(mysqli_query($zbirka, $poizvedba))
				{
					http_response_code(201);	// Created
				}
				else
				{
					http_response_code(500);	// Internal Server Error

					if($DEBUG)
					{
						pripravi_odgovor_napaka(mysqli_error($zbirka));
					}
				}
			}
			else
			{
				http_response_code(409);	// Conflict
				pripravi_odgovor_napaka("Uporabnik ne obstaja!");
			}
		}
		else
		{
			http_response_code(400);	// Bad Request
		}
	}
}
?>