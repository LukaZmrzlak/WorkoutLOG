/**
 * Pridobi podatke iz obrazca in jih vrne v obliki JSON objekta.
 * @param  {HTMLFormControlsCollection} elements  Elementi obrazca
 * @return {Object}                               Object literal
 */
const formToJSON = elements => [].reduce.call(elements, (data, element) => 
{
    // if element has a name, add it and its value to the data object
	if(element.name!="")
	{
		data[element.name] = element.value;
	}
    // return the updated data object
  return data;
}, {});
 
/**
 * function to add user to the system by sending form data as a JSON object
 */
function addUser()
{
    // convert form data to JSON object
	const data = formToJSON(document.getElementById("form").elements);	
	var JSONdata = JSON.stringify(data, null, "  ");						
	
	// create new XMLHttpRequest
	var xmlhttp = new XMLHttpRequest();									
	 
	// handle response from server
	xmlhttp.onreadystatechange = function()								
	{
		// if user added successfully
		if (this.readyState == 4 && this.status == 201)					
		{
			document.getElementById("answer").innerHTML="User added!";
		}
		// if adding user failed
		if(this.readyState == 4 && this.status != 201)						
		{
			document.getElementById("answer").innerHTML="Failed to add user: "+this.status;
		}
	};
	 
	// send POST request to endpoint "uporabniki" with JSON data
	xmlhttp.open("POST", "uporabniki", true);				
	xmlhttp.send(JSONdata);	
}
