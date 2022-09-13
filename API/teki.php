<?php
$DEBUG = true;

include("orodja.php"); 			// Vključitev 'orodij'

$zbirka = dbConnect();			//Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');	// Dovolimo dostop izven trenutne domene (CORS)
header("Cache-Control: no-cache, no-store, must-revalidate"); // HTTP 1.1.
header("Pragma: no-cache"); // HTTP 1.0.
header("Expires: 0"); // Proxies.

switch($_SERVER["REQUEST_METHOD"])			//glede na HTTP metodo izvedemo ustrezno dejanje nad virom
{
	case 'GET':
		if(!empty($_GET["vzdevek"]))
		{
			teki_uporabnika($_GET["vzdevek"]);
		}
		else
		{
			http_response_code(400);	// Bad Request
		}
		break;
		
	case 'POST':
			dodaj_tek();
		break;
		
	default:
		http_response_code(405);	//Method Not Allowed
		break;
}

mysqli_close($zbirka);					// Sprostimo povezavo z zbirko

function teki_uporabnika($vzdevek)
{
	global $zbirka;
	$vzdevek=mysqli_escape_string($zbirka, $vzdevek);
	$odgovor=array();
	
	if(uporabnik_obstaja($vzdevek))
	{
		$poizvedba="SELECT datum, casovna_dolzina, dolzina, obcutek, vreme, zapiski FROM teki WHERE vzdevek = '$vzdevek'";
		
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

function dodaj_tek()
{
	global $zbirka, $DEBUG;
	
	$podatki = json_decode(file_get_contents('php://input'), true);
	
	if(isset($podatki["vzdevek"], $podatki["datum"], $podatki["casovna_dolzina"],$podatki["dolzina"], $podatki["obcutek"], $podatki["vreme"], $podatki["zapiski"]))
	{
		if(uporabnik_obstaja($podatki["vzdevek"]))	//preprecimo napako zaradi krsitve FK 
		{
			$vzdevek = mysqli_escape_string($zbirka, $podatki["vzdevek"]);
			$datum = mysqli_escape_string($zbirka, $podatki["datum"]);
			$casovna_dolzina = mysqli_escape_string($zbirka, $podatki["casovna_dolzina"]);
            $dolzina = mysqli_escape_string($zbirka, $podatki["dolzina"]);
            $obcutek = mysqli_escape_string($zbirka, $podatki["obcutek"]);
            $vreme = mysqli_escape_string($zbirka, $podatki["vreme"]);
            $zapiski = mysqli_escape_string($zbirka, $podatki["zapiski"]);
				
			$poizvedba="INSERT INTO teki (vzdevek, datum, casovna_dolzina, dolzina, obcutek, vreme, zapiski) VALUES ('$vzdevek', '$datum', '$casovna_dolzina', '$dolzina', '$obcutek', '$vreme', '$zapiski')";

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
?>