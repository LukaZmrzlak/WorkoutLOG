function allUsers(){
		
	var httpRequest = new XMLHttpRequest();
	httpRequest.onreadystatechange = function()
	{
		if (this.readyState == 4 && this.status == 200)
		{
			try{
				var answerJSON = JSON.parse(this.responseText);
			}
			catch(e){
				console.log("Data parsing error");
				return;
			}
			show(answerJSON);
		}
	};
	 
	httpRequest.open("GET", "uporabniki", true);
	httpRequest.send();
}

function show(answerJSON){
	var fragment = document.createDocumentFragment();	
	
	for (var i=0; i<answerJSON.length; i++) {
		var tr = document.createElement("tr");

		for(var column in answerJSON[i]){
			var td = document.createElement("td");
			td.innerHTML=answerJSON[i][column];		
			tr.appendChild(td);						
		}
		fragment.appendChild(tr);
	}
	document.getElementById("table").appendChild(fragment);
}