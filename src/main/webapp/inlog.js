var loginKnop = document.querySelector("#go");
loginKnop.addEventListener("click",login);

function login(event) {
    var formData = new FormData(document.querySelector("#loginform"));
    var encData = new URLSearchParams(formData.entries());
    
    fetch("../restservices/authentication", { method: 'POST', body: encData })
        .then(function(response) {
          if (response.ok) return response.json();
          else alert("Wrong username/password");
        })
        .then(function(myJson){
            window.sessionStorage.setItem("myJWT", myJson.JWT);
            getMedewerker(formData.get("username"),formData.get("kassanummer"));
        })
        .catch(error => console.log(error));
}    

function getMedewerker(mnr, kassa){
    fetch('../restservices/medewerker/' + mnr)
        .then(function (response) {
         return response.json();
        })
        .then(function (myJson) {
            console.log(myJson);
            window.sessionStorage.setItem("mNummer", myJson.mnr);
            window.sessionStorage.setItem("voorletters", myJson.voorletters);
            window.sessionStorage.setItem("tussenvoegsel", myJson.tussenvoegsel);
            window.sessionStorage.setItem("achternaam", myJson.achternaam);
            window.sessionStorage.setItem("functie", myJson.functie);
            window.sessionStorage.setItem("kassa", kassa);
            if(myJson.functie == "Barhoofd"){
                window.location.href = '../barapp/barhoofd.html'
                //window.open("../barapp/barhoofd.html");
            }

            if(myJson.functie =="medewerker"){
                window.location.href = '../barapp/medewerker.html'
                //window.open("../barapp/medewerker.html");
            }
        })
}