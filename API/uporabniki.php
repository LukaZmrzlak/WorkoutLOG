<?php
$DEBUG = true;							// Priprava podrobnejših opisov napak (med testiranjem)

include("orodja.php"); 					// Vključitev 'orodij'


$zbirka = dbConnect();					// Pridobitev povezave s podatkovno zbirko

header('Content-Type: application/json');	// Nastavimo MIME tip vsebine odgovora
header('Access-Control-Allow-Origin: *');	// Dovolimo dostop izven trenutne domene (CORS)
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE');		//v preflight poizvedbi za CORS sta dovoljeni le metodi GET in POST

switch($_SERVER["REQUEST_METHOD"])		// Glede na HTTP metodo v zahtevi izberemo ustrezno dejanje nad virom
{
	case 'GET':
		if(!empty($_GET["vzdevek"]))
		{
			pridobi_uporabnika($_GET["vzdevek"]);
		}
		else
		{
			pridobi_vse_uporabnike();	// Če ne posredujemo vzdevka je to 'Bad Request'
		}
		break;
		
	case 'POST':
		dodaj_uporabnika();
		break;
		
	case 'PUT':
		if(!empty($_GET["vzdevek"]))
		{
			posodobi_uporabnika($_GET["vzdevek"]);
		}
		else
		{
			http_response_code(400);	// Če ne posredujemo vzdevka je to 'Bad Request'
		}
		break;
		
	case 'DELETE':
		if(!empty($_GET["vzdevek"]))
		{
			izbrisi_uporabnika($_GET["vzdevek"]);
		}
		else
		{
			http_response_code(400);	// Bad Request
		}
		break;
		
	case 'OPTIONS':						//Options dodan zaradi pre-fight poizvedbe za CORS (pri uporabi metod PUT in DELETE)
		http_response_code(204);
		break;
		
	default:
		http_response_code(405);		//Če naredimo zahtevo s katero koli drugo metodo je to 'Method Not Allowed'
		break;
}

mysqli_close($zbirka);					// Sprostimo povezavo z zbirko


// Funkcije

function pridobi_uporabnika($vzdevek)
{
	global $zbirka;
	$vzdevek=mysqli_escape_string($zbirka, $vzdevek);
	
	$poizvedba="SELECT vzdevek, ime, priimek, email FROM vpisi WHERE vzdevek='$vzdevek'";
	
	$rezultat=mysqli_query($zbirka, $poizvedba);

	if(mysqli_num_rows($rezultat)>0)	//uporabnik obstaja
	{
		$odgovor=mysqli_fetch_assoc($rezultat);
		
		http_response_code(200);		//OK
		echo json_encode($odgovor);
	}
	else							// uporabnik ne obstaja
	{
		http_response_code(404);		//Not found
	}
}

function dodaj_uporabnika()
{
	global $zbirka;
	
	$podatki = json_decode(file_get_contents('php://input'), true);
	
	if(isset($podatki["vzdevek"], $podatki["geslo"], $podatki["ime"], $podatki["priimek"], $podatki["email"]))
	{
		$vzdevek = mysqli_escape_string($zbirka, $podatki["vzdevek"]);
		$geslo = password_hash(mysqli_escape_string($zbirka, $podatki["geslo"]), PASSWORD_DEFAULT);
		$ime = mysqli_escape_string($zbirka, $podatki["ime"]);
		$priimek = mysqli_escape_string($zbirka, $podatki["priimek"]);
		$email = mysqli_escape_string($zbirka, $podatki["email"]);
			
		if(!uporabnik_obstaja($vzdevek))
		{	
			$poizvedba="INSERT INTO vpisi (vzdevek, geslo, ime, priimek, email) VALUES ('$vzdevek', '$geslo', '$ime', '$priimek', '$email')";
			
			if(mysqli_query($zbirka, $poizvedba))
			{
				http_response_code(201);	// Created
				$odgovor=URL_vira($vzdevek);
				echo json_encode($odgovor);
			}
			else
			{
				http_response_code(500);	// Internal Server Error

			}
		}
		else
		{
			http_response_code(409);	// Conflict
			pripravi_odgovor_napaka("Uporabnik že obstaja!");
		}
	}
	else
	{
		http_response_code(400);	// Bad Request
	}
}

function posodobi_uporabnika($vzdevek)
{
	global $zbirka, $DEBUG;
	
	$vzdevek = mysqli_escape_string($zbirka, $vzdevek);
	
	$podatki = json_decode(file_get_contents("php://input"),true);
		
	if(uporabnik_obstaja($vzdevek))
	{
		if(isset($podatki["geslo"], $podatki["ime"], $podatki["priimek"], $podatki["email"]))
		{
			$geslo = password_hash(mysqli_escape_string($zbirka, $podatki["geslo"]), PASSWORD_DEFAULT);
			$ime = mysqli_escape_string($zbirka, $podatki["ime"]);
			$priimek = mysqli_escape_string($zbirka, $podatki["priimek"]);
			$email = mysqli_escape_string($zbirka, $podatki["email"]);
			
			$poizvedba = "UPDATE vpisi SET geslo='$geslo', ime='$ime', priimek='$priimek', email='$email' WHERE vzdevek='$vzdevek'";
			
			if(mysqli_query($zbirka, $poizvedba))
			{
				http_response_code(204);	//OK with no content
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
			http_response_code(400);	// Bad Request
		}
	}
	else
	{
		http_response_code(404);	// Not Found
	}
}	
	
function izbrisi_uporabnika($vzdevek)
{	
	global $zbirka, $DEBUG;
	$vzdevek=mysqli_escape_string($zbirka, $vzdevek);

	if(uporabnik_obstaja($vzdevek))
	{
		$poizvedba="DELETE FROM vpisi WHERE vzdevek='$vzdevek'";
		
		if(mysqli_query($zbirka, $poizvedba))
		{
			http_response_code(204);	//OK with no content
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
		http_response_code(404);	// Not Found
	}
}

function pridobi_vse_uporabnike()
{
	global $zbirka;
	$odgovor=array();
	$poizvedba="SELECT vzdevek, ime, priimek, email FROM vpisi";
	$rezultat=mysqli_query($zbirka, $poizvedba);
	while($vrstica=mysqli_fetch_assoc($rezultat)){
		$odgovor[]=$vrstica;
	}
	http_response_code(200);
	echo json_encode($odgovor);
}

?>