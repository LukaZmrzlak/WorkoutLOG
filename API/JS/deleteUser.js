function userData(){
	var vzdevek=document.getElementById("form")['vzdevek'].value;
	
	document.getElementById('delete').style.display="none";
	document.getElementById('answer').innerHTML="";
	
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function()
	{
		if (this.readyState == 4 && this.status == 200)
		{
			try{
				var odgovorJSON = JSON.parse(this.responseText);
			}
			catch(e){
				console.log("Data parsing error");
				return;
			}
			modifyShow(odgovorJSON);
		}
		if(this.readyState == 4 && this.status != 200)
		{
			document.getElementById("answer").innerHTML="Error: "+this.status;
		}
	};

	xmlhttp.open("GET", "uporabniki/"+vzdevek, true);
	xmlhttp.send();
}

function modifyShow(odgovorJSON)
{
	var form = document.getElementById('delete');
	form.style.display="block";
	
	form.ime.value = odgovorJSON['ime'];
	form.priimek.value = odgovorJSON['priimek'];
	form.email.value = odgovorJSON['email'];
}

const formToJSON = elements => [].reduce.call(elements, (data, element) => 
{
	if(element.name!="")
	{
		data[element.name] = element.value;
	}
  return data;
}, {});

function deleteUser()
{
	const data = formToJSON(document.getElementById("delete").elements);	
	var JSONdata = JSON.stringify(data, null, "  ");						

	var xmlhttp = new XMLHttpRequest();										
	 
	xmlhttp.onreadystatechange = function()								
	{
		if (this.readyState == 4 && this.status == 204)					
		{
			document.getElementById("answer").innerHTML="Delete successful!";
		}
		if(this.readyState == 4 && this.status != 204)				
		{
			document.getElementById("answer").innerHTML="Delete unsuccessful: "+this.status;
		}
	};
	
	var vzdevek = document.getElementById('form').vzdevek.value;
	
	xmlhttp.open("DELETE", "uporabniki/"+vzdevek, true);
	xmlhttp.send(JSONdata);
}



