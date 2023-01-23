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
		if(!empty($_GET["token"]))
		{
			nastavitve_uporabnika($_GET["token"]);
		}
		else
		{
			http_response_code(400);	// Bad Request
		}
		break;
		
	case 'POST':
        dodaj_nastavitve();
		break;

	case 'PUT':
		if(!empty($_GET["token"]))
		{
			popravi_nastavitve($_GET["token"]);
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

function nastavitve_uporabnika($token)
{
	global $zbirka;
	$token = mysqli_escape_string($zbirka, $token);
	$secret = 'sec!ReT423*&';
	$odgovor=array();

	// Validate token and retrieve payload
	if(Token::validate($token,$secret)){
		$payload = Token::getPayLoad($token,$secret);
		$vzdevek = $payload["user_id"];

		if(uporabnik_obstaja($vzdevek))
		{
			$poizvedba="SELECT spol, starost, teza, visina FROM nastavitve WHERE vzdevek = '$vzdevek'";
			
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

function dodaj_nastavitve()
{
	global $zbirka, $DEBUG;
	
	$podatki = json_decode(file_get_contents('php://input'), true);

	if(isset($podatki["vzdevek"], $podatki["spol"], $podatki["starost"],$podatki["teza"], $podatki["visina"]))
	{
		if(uporabnik_obstaja($podatki["vzdevek"]))	//preprecimo napako zaradi krsitve FK 
		{
			$vzdevek = mysqli_escape_string($zbirka, $podatki["vzdevek"]);
			$spol = mysqli_escape_string($zbirka, $podatki["spol"]);
			$starost = mysqli_escape_string($zbirka, $podatki["starost"]);
            $teza = mysqli_escape_string($zbirka, $podatki["teza"]);
            $visina = mysqli_escape_string($zbirka, $podatki["visina"]);
				
			$poizvedba="INSERT INTO nastavitve (vzdevek, spol, starost, teza, visina) VALUES ('$vzdevek', '$spol', '$starost', '$teza', '$visina')";

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

function popravi_nastavitve($token){
	global $zbirka;
	$podatki = json_decode(file_get_contents('php://input'), true);
	$token = mysqli_escape_string($zbirka, $token);
	$secret = 'sec!ReT423*&';

	// Validate token and retrieve payload
	if(Token::validate($token,$secret)){
		$payload = Token::getPayLoad($token,$secret);
		$vzdevek = $payload["user_id"];
		if(uporabnik_obstaja($vzdevek))	//preprecimo napako zaradi krsitve FK 
		{
			if(isset($podatki["spol"], $podatki["starost"], $podatki["teza"], $podatki["visina"])){
				$spol = mysqli_escape_string($zbirka, $podatki["spol"]);
				$starost = mysqli_escape_string($zbirka, $podatki["starost"]);
				$teza = mysqli_escape_string($zbirka, $podatki["teza"]);
				$visina = mysqli_escape_string($zbirka, $podatki["visina"]);
					
				$poizvedba="UPDATE nastavitve SET spol='$spol',starost='$starost',teza='$teza',visina='$visina' WHERE vzdevek='$vzdevek'";

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

?>