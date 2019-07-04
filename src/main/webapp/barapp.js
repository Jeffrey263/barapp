//Laad de hoofd pagina
function initPage() {
    getProducten();
    document.querySelector("#account").innerHTML = window.sessionStorage.getItem("voorletters") + ". " + window.sessionStorage.getItem("tussenvoegsel") + " " + window.sessionStorage.getItem("achternaam");
}

document.querySelector("#Alles").addEventListener("click",getProducten);
function getProducten(){
    var myNode = document.querySelector("#prod");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }

    fetch('../restservices/product/' + window.sessionStorage.getItem("kassa"))
        .then(function (response) {
         return response.json();
        })
        .then(function (myJson) {
            console.log(myJson);

            for(i in myJson){
                product = myJson[i];
                var naam = product["naam"];
                var prijs = product["prijs"];
                var pn = product["productnummer"];
                voegProductToe(pn, naam, prijs);
            }
        })
}

//Laad een product card in.
function voegProductToe(pn, naam, prijs){
    var productVak = document.querySelector("#prod");
    var product = document.createElement("div");
    product.className = "cardProduct";
    product.addEventListener("click", voegLijstToe);
    product.setAttribute("value", pn);
    var productText = document.createElement("p");
    productText.setAttribute("value", pn);
    var text = document.createTextNode(naam + " - \u20AC" + prijs);
    productText.appendChild(text);
    product.appendChild(productText);
    productVak.appendChild(product);
}

var productenLijst = [];
var totaal = 0;
function voegLijstToe(event) {
    var product = [];
    
    fetch('../restservices/product/byId/' + event.target.attributes.value.value)
    .then(function (response) {
        return response.json();
    })
    .then(function (myJson) {
        var aantal = 1;
        product.push(myJson.productnummer);
        product.push(myJson.naam);
        product.push(myJson.prijs);
        totaal += myJson.prijs;
        document.querySelector("#tot").innerHTML = totaal;

        if(productenLijst.length > 0){
            var found = null;
            for (i in productenLijst){
                var pr = productenLijst[i];
                if(pr[0]==myJson.productnummer){
                    var p = productenLijst[i];
                    found = myJson.productnummer

                }
            }

            if (found > null){     
                aantal = p[3] + 1;
                p[3] += 1;
                p[2] += myJson.prijs;

                product.push(aantal);
                var myNode = document.querySelector("#pr" + myJson.productnummer);
                while (myNode.firstChild) {
                    myNode.removeChild(myNode.firstChild);
                }
                var text = document.createTextNode(aantal + "x  " + myJson.naam + " \u20AC " + p[2]);
                myNode.appendChild(text);
                //productenLijst.push(product);
            }
            else {
                aantal = 1;
              
                product.push(aantal);
                var lijstVak = document.querySelector("#lijst");
                lijstVak.appendChild(document.createElement("br"));
                var lijstText = document.createElement("p3");
                var text = document.createTextNode(aantal + "x  " + myJson.naam + " \u20AC " + myJson.prijs);
                lijstText.id = "pr" + myJson.productnummer;
                lijstText.appendChild(text);
                lijstVak.appendChild(lijstText);
                productenLijst.push(product);
              }
        }
        else{
            product.push(aantal);
            var lijstVak = document.querySelector("#lijst");
            var lijstText = document.createElement("p3");
            var text = document.createTextNode(aantal + "x  " + myJson.naam + " \u20AC" + myJson.prijs);
            lijstText.id = "pr" + myJson.productnummer;
            lijstText.appendChild(text);
            lijstVak.appendChild(lijstText);
            productenLijst.push(product);
        }
        console.log(productenLijst);
    })
}

//anuleer bestelling
document.querySelector("#anu").addEventListener("click", function(){
    productenLijst = [];
    totaal = 0;

    var myNode = document.querySelector("#lijst");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    document.querySelector("#tot").innerHTML = "";
})

document.querySelector("#Bier").addEventListener("click",laadCategorie);
document.querySelector("#Wijn").addEventListener("click",laadCategorie);
document.querySelector("#Fris").addEventListener("click",laadCategorie);
document.querySelector("#Sterk").addEventListener("click",laadCategorie);
document.querySelector("#Mix").addEventListener("click",laadCategorie);
function laadCategorie(event){
    var myNode = document.querySelector("#prod");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    fetch('../restservices/product/byCat/' + event.target.attributes.value.value)
    .then(function (response) {
     return response.json();
    })
    .then(function (myJson) {

        for(i in myJson){
            product = myJson[i];
            var naam = product["naam"];
            var prijs = product["prijs"];
            var pn = product["productnummer"];
            voegProductToe(pn, naam, prijs);
        }
    })
}

//Post de transactie
document.querySelector("#af").addEventListener("click", transactie);
function transactie(){
    var producten = []
    var hoeveelheden = [];
    for (i in productenLijst){
        var prod = productenLijst[i];
        producten.push(prod[0]);
        hoeveelheden.push(prod[3]);
    }
    var medewerker = window.sessionStorage.getItem("mNummer");
    var kassa = window.sessionStorage.getItem("kassa");
    
    console.log(medewerker + " - " + kassa + " - " + producten + " - " + hoeveelheden);

    var transactie = new Object();
    transactie.mijnproducten = producten;
    transactie.hoeveelheden = hoeveelheden;
    transactie.medewerker = medewerker;
    transactie.kassanummer = kassa;
    var jsonString= JSON.stringify(transactie);
    console.log(transactie);

    fetch("../restservices/transactie", { 
                                                                    method: 'POST',
                                                                    headers: {
                                                                        'Accept': 'application/json',
                                                                        'Content-Type': 'application/json',
                                                                        'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT")
                                                                        }, 
                                                                    body: jsonString })
    .then(response => response.json())
    .then(function(myJson) { console.log(myJson);
        productenLijst = [];
        totaal = 0;
    
        var myNode = document.querySelector("#lijst");
        while (myNode.firstChild) {
            myNode.removeChild(myNode.firstChild);
        }
        document.querySelector("#tot").innerHTML = "";
        alert("Bestelling afgerond");
    })
}

//haal het budget op.
function getBudget(){
    fetch('../restservices/kassa/' + window.sessionStorage.getItem("kassa"))
    .then(function (response) {
     return response.json();
    })
    .then(function (myJson) {
        var budget = myJson.budget;
        console.log(budget);
        document.querySelector("#bud").innerHTML = budget;
    })
}

//Haal account op.
function getAccount(){
    document.querySelector("#accNummer").innerHTML = "werknemer nummer: " + window.sessionStorage.getItem("mNummer");
    document.querySelector("#accNaam").innerHTML = "naam: " + window.sessionStorage.getItem("voorletters") + ". " +  window.sessionStorage.getItem("tussenvoegsel") + " " +  window.sessionStorage.getItem("achternaam");
    document.querySelector("#accFunctie").innerHTML = "functie: " + window.sessionStorage.getItem("functie");
}

//De komende aantal knoppen wisselen tussen de verschillende pagina's
document.querySelector("#verkoop").addEventListener("click", function(event){
    if(window.sessionStorage.getItem("functie") == "Barhoofd"){
        document.querySelector("#funcBes").style.display = "none";
        document.querySelector("#bestelling").style.display = "none";

        document.querySelector("#trans").style.display = "none";
        document.querySelector("#tTools").style.display = "none";

        document.querySelector("#ovFunc").style.display = "none";
        document.querySelector("#ovTools").style.display = "none";
        document.querySelector("#ov").style.display = "none";

        getProducten();
    }

    document.querySelector("#prod").style.display = "block";
    document.querySelector("#func").style.display = "block";
    document.querySelector("#cat").style.display = "block";
    document.querySelector("#lijst").style.display = "block";

    document.querySelector("#acc").style.display = "none";
});

document.querySelector("#account").addEventListener("click", function(event){
    document.querySelector("#prod").style.display = "none";
    document.querySelector("#func").style.display = "none";
    document.querySelector("#cat").style.display = "none";
    document.querySelector("#lijst").style.display = "none";
    
    if(window.sessionStorage.getItem("functie") == "Barhoofd"){
        document.querySelector("#funcBes").style.display = "none";
        document.querySelector("#bestelling").style.display = "none";

        document.querySelector("#trans").style.display = "none";
        document.querySelector("#tTools").style.display = "none";

        document.querySelector("#ovFunc").style.display = "none";
        document.querySelector("#ovTools").style.display = "none";
        document.querySelector("#ov").style.display = "none";
    }

    document.querySelector("#acc").style.display = "block";

    getAccount();
});

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#transacties").addEventListener("click", function(event){
        getTransacties();
        document.querySelector("#prod").style.display = "none";
        document.querySelector("#func").style.display = "none";
        document.querySelector("#cat").style.display = "none";
        document.querySelector("#lijst").style.display = "none";
    
        document.querySelector("#funcBes").style.display = "none";
        document.querySelector("#bestelling").style.display = "none";
    
        document.querySelector("#trans").style.display = "block";
        document.querySelector("#tTools").style.display = "block";

        document.querySelector("#ovFunc").style.display = "none";
        document.querySelector("#ovTools").style.display = "none";
        document.querySelector("#ov").style.display = "none";
        
        document.querySelector("#acc").style.display = "none";
    });
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#overzicht").addEventListener("click", function(event){
        ovOpenKassa();
        document.querySelector("#prod").style.display = "none";
        document.querySelector("#func").style.display = "none";
        document.querySelector("#cat").style.display = "none";
        document.querySelector("#lijst").style.display = "none";
    
        document.querySelector("#funcBes").style.display = "none";
        document.querySelector("#bestelling").style.display = "none";
    
        document.querySelector("#trans").style.display = "none";
        document.querySelector("#tTools").style.display = "none";

        document.querySelector("#ovFunc").style.display = "block";
        document.querySelector("#ovTools").style.display = "block";
        document.querySelector("#ov").style.display = "block";
      
        document.querySelector("#acc").style.display = "none";
    });
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
document.querySelector("#bestellingen").addEventListener("click", function(event){
        document.querySelector("#prod").style.display = "none";
        document.querySelector("#func").style.display = "none";
        document.querySelector("#cat").style.display = "none";
        document.querySelector("#lijst").style.display = "none";

        document.querySelector("#funcBes").style.display = "block";
        document.querySelector("#bestelling").style.display = "block";

        document.querySelector("#trans").style.display = "none";
        document.querySelector("#tTools").style.display = "none";

        document.querySelector("#ovFunc").style.display = "none";
        document.querySelector("#ovTools").style.display = "none";
        document.querySelector("#ov").style.display = "none";

        document.querySelector("#acc").style.display = "none";

        getBudget();
    });
}

document.querySelector("#uitlog").addEventListener("click",function(event){
    window.sessionStorage.setItem("mNummer", null);
    window.sessionStorage.setItem("voorletters", null);
    window.sessionStorage.setItem("tussenvoegsel", null);
    window.sessionStorage.setItem("achternaam", null);
    window.sessionStorage.setItem("functie", null);
    window.sessionStorage.setItem("kassa", null);

    window.location.href = '../';
});

//Haal alle transacties op.
function getTransacties(){
    var myNode = document.querySelector("#trans");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }

    fetch('../restservices/transactie/' + window.sessionStorage.getItem("kassa"))
    .then(function (response) {
     return response.json();
    })
    .then(function (myJson) {
        console.log(myJson);
        
        for(i in myJson){
            transactie = myJson[i];
            voegTransactieToe(transactie.datum, transactie.medewerker, transactie.mijnproducten, transactie.hoeveelheden, transactie.winst);
        }
    })}

//Voeg nieuwe Transactie toe
function voegTransactieToe(datum, medewerker, producten, hoeveelheden, winst){
    var transVak = document.querySelector("#trans");
    var transactie = document.createElement("div");
    transactie.className = "card";

    transactie.appendChild(document.createElement("br"));
    var transactieText = document.createElement("p6");
    var text = document.createTextNode(datum + ", " + medewerker.voorletters + ". " + medewerker.tussenvoegsel + " " + medewerker.achternaam);
    transactieText.appendChild(text);
    transactie.appendChild(transactieText);
    transactie.appendChild(document.createElement("br"));
    transactie.appendChild(document.createElement("br"));

    for (i in producten){
        var product = producten[i];
        transactieText = document.createElement("p5");
        text = document.createTextNode(hoeveelheden[i] + "x -" + product.naam + ", Prijs: \u20AC" + hoeveelheden[i]*product.prijs);
        transactieText.appendChild(text);
        transactie.appendChild(transactieText);
        transactie.appendChild(document.createElement("br"));
    }
    transactie.appendChild(document.createElement("br"));
        
    transactieText = document.createElement("p5");
    text = document.createTextNode("Winst: \u20AC" + winst);
    transactieText.appendChild(text);
    transactie.appendChild(transactieText);
    transactie.appendChild(document.createElement("br"));
    transactie.appendChild(document.createElement("br"));

    transVak.appendChild(transactie);
}
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#AlleTrans").addEventListener("click", function(event){
        getTransacties();
    });
}

//Haal transacties op op basis van de datum.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#DatumTrans").addEventListener("click", function(event){
        var myNode = document.querySelector("#trans");
        while (myNode.firstChild) {
            myNode.removeChild(myNode.firstChild);
        }

        var datum = document.querySelector("#datumInput").value;
        console.log(datum);

        fetch('../restservices/transactie/byDate/' + window.sessionStorage.getItem("kassa") + "/" + datum)
        .then(function (response) {
        return response.json();
        })
        .then(function (myJson) {
            console.log(myJson);
            
            for(i in myJson){
                transactie = myJson[i];
                voegTransactieToe(transactie.datum, transactie.medewerker, transactie.mijnproducten, transactie.hoeveelheden, transactie.winst);
            }
        })
    });
}

//Haal transacties op op basis van de medewerker.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#MedTrans").addEventListener("click", function(event){
        var myNode = document.querySelector("#trans");
        while (myNode.firstChild) {
            myNode.removeChild(myNode.firstChild);
        }

        var mnr = document.querySelector("#medInput").value;
        console.log(mnr);

        fetch('../restservices/transactie/byMnr/' + window.sessionStorage.getItem("kassa") + "/" + mnr)
        .then(function (response) {
        return response.json();
        })
        .then(function (myJson) {
            console.log(myJson);
            
            for(i in myJson){
                transactie = myJson[i];
                voegTransactieToe(transactie.datum, transactie.medewerker, transactie.mijnproducten, transactie.hoeveelheden, transactie.winst);
            }
        })
    });
}

//Print de bestelbon uit.
var totaalKosten = 0;
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#print").addEventListener("click", function(event){
        var totaalKosten = 0;
        var myNode = document.querySelector("#bestelling");
        while (myNode.firstChild) {
            myNode.removeChild(myNode.firstChild);
        }

        fetch('../restservices/product/bestel/' + window.sessionStorage.getItem("kassa"))
        .then(function (response) {
        return response.json();
        })
        .then(function (myJson) {
            console.log(myJson);
            var bestellingVak = document.querySelector("#bestelling");
            var bestelling = document.createElement("div");
            bestelling.className = "card";
            bestelling.appendChild(document.createElement("br"));
            var bestellingText = document.createElement("p6");
            var text = document.createTextNode("Bestelling: ");
            bestellingText.appendChild(text);
            bestelling.appendChild(bestellingText);
            bestelling.appendChild(document.createElement("br"));       
            bestelling.appendChild(document.createElement("br"));

            for (i in myJson){
                var product = myJson[i];
                var productText = document.createElement("p5");
                var text = document.createTextNode(product.aantal + "x  " + product.naam + " - \u20AC" + product.kosten);
                productText.appendChild(text);
                bestelling.appendChild(productText);
                bestelling.appendChild(document.createElement("br"));
                bestellingVak.appendChild(bestelling);

                totaalKosten += product.kosten;
                console.log(totaalKosten + " -=- " + product.kosten);
            }
            bestelling.appendChild(document.createElement("br"));
            var bestellingText = document.createElement("p5");
            var text = document.createTextNode("Totale koste: \u20AC" + totaalKosten);
            bestellingText.appendChild(text);
            bestelling.appendChild(bestellingText);
            bestelling.appendChild(document.createElement("br"));
            bestelling.appendChild(document.createElement("br"));
            window.localStorage.setItem("totaal",totaalKosten);
            window.localStorage.setItem("order","true");
        })
    });
}

//Bevestig de bestelling.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#bevestig").addEventListener("click", function(event){
        if(window.localStorage.getItem("order") == "true"){
            fetch('../restservices/product/bevestig/' + window.sessionStorage.getItem("kassa") + "/" + window.localStorage.getItem("totaal"), {method: 'POST', headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
            .then(function() { 
                getBudget();    
                var myNode = document.querySelector("#bestelling");
                while (myNode.firstChild) {
                    myNode.removeChild(myNode.firstChild);
                }
                alert("Bestelling bevestigt")
                window.localStorage.setItem("order","false");
                window.localStorage.setItem("totaal",0);
            })
        }
    });
}

//Open het scherm om producten/medewerkers/kassa aan te passen. Wissel tussen de verschillende categorieën met de variabele ovCat (overzicht categorie).
var ovCat = null;
var codeWeizig = null;
function openWijzig(event){
    console.log(ovCat + " - " + event.target.value);

    if(ovCat == "Product"){
        document.querySelector("#ov").style.display = "none";
        document.querySelector("#prodPop").style.display = "block";

        fetch('../restservices/product/byId/' + event.target.value)
        .then(function (response) {
             return response.json();
        })
        .then(function (myJson) {
            codeWeizig = event.target.value;
            document.querySelector("#wijzigHeaderProd").innerHTML = "Wijzig: " + myJson.naam;
            document.querySelector("#ovNaam").value = myJson.naam;
            document.querySelector("#ovPrijs").value = myJson.prijs;
            document.querySelector("#ovInkoop").value = myJson.inkoop;
            document.querySelector("#ovMinV").value = myJson.minVoorraad;
            document.querySelector("#ovVoorraad").value = myJson.voorraad;
            document.querySelector("#ovCat").value = myJson.categorie;
        })

    }

    if(ovCat == "Medewerker"){
        document.querySelector("#ov").style.display = "none";
        document.querySelector("#medPop").style.display = "block";

        fetch('../restservices/medewerker/' + event.target.value)
        .then(function (response) {
             return response.json();
        })
        .then(function (myJson) {
            codeWeizig = event.target.value;
            document.querySelector("#wijzigHeaderMed").innerHTML = "Wijzig: " + myJson.voorletters + ". " + myJson.tussenvoegsel + " " + myJson.achternaam;
            document.querySelector("#medVL").value = myJson.voorletters;
            document.querySelector("#medTV").value = myJson.tussenvoegsel;
            document.querySelector("#medA").value = myJson.achternaam;
            document.querySelector("#medF").value = myJson.functie;
        })

    }
    
    if(ovCat == "kassa"){
        document.querySelector("#ov").style.display = "none";
        document.querySelector("#kassaPop").style.display = "block";

        fetch('../restservices/kassa/' + event.target.value)
        .then(function (response) {
             return response.json();
        })
        .then(function (myJson) {
            codeWeizig = event.target.value;
            document.querySelector("#wijzigHeaderKassa").innerHTML = "Wijzig kassaysteem: " + myJson.naam;
            document.querySelector("#kassaNaam").value = myJson.naam;
            document.querySelector("#kassaLocatie").value = myJson.locatie;
            document.querySelector("#kassaBudget").value = myJson.budget;
        })

    }
}

//Laad het toevoeg scherm.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#voegtoe").addEventListener("click", laadToevoeg);
}

function laadToevoeg(event){
    console.log(ovCat);
    if(ovCat == "Product"){
        document.querySelector("#ov").style.display = "none";
        document.querySelector("#prodPopToe").style.display = "block";
    }

    if(ovCat == "Medewerker"){
        document.querySelector("#ov").style.display = "none";
        document.querySelector("#medPopToe").style.display = "block";
    }
}

//Voeg product toe.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#VoegProdToe").addEventListener("click",function(event){
        var frmToevoeg = new FormData(document.querySelector("#prodFormToe"));
        var toeEncData = new URLSearchParams(frmToevoeg.entries());

        fetch("../restservices/product/" + window.sessionStorage.getItem("kassa"), { method: 'POST', body: toeEncData, headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("Product toegevoegd")
            anu();
            ovOpenProd();} );
    });
}

//Voeg medewerker toe.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#medToe").addEventListener("click",function(event){
        var frmToevoeg = new FormData(document.querySelector("#medFormToe"));
        var toeEncData = new URLSearchParams(frmToevoeg.entries());

        fetch("../restservices/medewerker/" + window.sessionStorage.getItem("kassa"), { method: 'POST', body: toeEncData, headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("Product toegevoegd")
            anu();
            ovOpenMed();} );
    });
}

//Voeg de functie anuleren (anu()) toe aan alle anuleer knoppen.
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#kassaOv").addEventListener("click", ovOpenKassa);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#medOv").addEventListener("click", ovOpenMed);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#prodOv").addEventListener("click", ovOpenProd);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#anuWijzig").addEventListener("click",anu);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#anuToevoeg").addEventListener("click",anu);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#anuWijzigMed").addEventListener("click",anu);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#anuMedToe").addEventListener("click",anu);
}

if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#anuWijzigKassa").addEventListener("click",anu);
}

//anuleer
function anu(){
    document.querySelector("#ov").style.display = "block";
    document.querySelector("#prodPop").style.display = "none";
    document.querySelector("#prodPopToe").style.display = "none";
    document.querySelector("#medPop").style.display = "none";
    document.querySelector("#medPopToe").style.display = "none";
    document.querySelector("#kassaPop").style.display = "none";
}

//Verwijder medewerker/product. Wederom gebruik makend van de variabele ovCat.
function verwijderFunc(event){
    console.log(ovCat + " - " + event.target.value);

    if(ovCat == "Product"){
        fetch("../restservices/product/"+event.target.value, { method: 'DELETE', headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("product verwijderd");
            anu();
            ovOpenProd()} );   
    }

    if(ovCat == "Medewerker"){
        fetch("../restservices/medewerker/"+event.target.value, { method: 'DELETE', headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("medewerker verwijderd");
            anu();
            ovOpenMed()} );      
    }
}

//WIJZIG Medewerker
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#wijzigMedewerker").addEventListener("click",function(){
        var frmToevoeg = new FormData(document.querySelector("#medForm"));
        var toeEncData = new URLSearchParams(frmToevoeg.entries());
        
        fetch("../restservices/medewerker/"+codeWeizig, { method: 'PUT', body: toeEncData, headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("medewerker gewijzigd");
            anu();
            ovOpenMed()} );
    });
}

//WIJZIG PRODUCT
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#wijzigProd").addEventListener("click",function(){
        var frmToevoeg = new FormData(document.querySelector("#prodForm"));
        var toeEncData = new URLSearchParams(frmToevoeg.entries());
        
        fetch("../restservices/product/"+codeWeizig, { method: 'PUT', body: toeEncData, headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            console.log(myJson); 
            alert("product gewijzigd");
            anu();
            ovOpenProd()} );
    });
}

//WIJZIG KASSA
if(window.sessionStorage.getItem("functie") == "Barhoofd"){
    document.querySelector("#WijzigKassa").addEventListener("click",function(){
        var frmToevoeg = new FormData(document.querySelector("#kassaForm"));
        var toeEncData = new URLSearchParams(frmToevoeg.entries());
        
        fetch("../restservices/kassa/"+codeWeizig, { method: 'PUT', body: toeEncData, headers : { 'Authorization': 'Bearer ' +  window.sessionStorage.getItem("myJWT") }})
        .then(response => response.json())
        .then(function(myJson) { 
            alert("kassa gewijzigd");
            anu();
            ovOpenKassa()} );
    });
}

//Open het kassa overzicht.
function ovOpenKassa(){
    ovCat = "kassa";
    var myNode = document.querySelector("#ov");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    document.querySelector("#voegtoe").style.display = "none";

    fetch('../restservices/kassa/' + window.sessionStorage.getItem("kassa"))
    .then(function (response) {
            return response.json();
    })
    .then(function (myJson) {
        console.log(myJson);
        var kassa = document.createElement("div");
        kassa.className = "card";
        
        kassa.appendChild(document.createElement("br"));
        var kassaText = document.createElement("p5");
        var text = document.createTextNode("Kassanummer: " + myJson.kassanummer);
        kassaText.appendChild(text);
        kassa.appendChild(kassaText);

        kassa.appendChild(document.createElement("br"));
        kassaText = document.createElement("p5");
        text = document.createTextNode("naam: " + myJson.naam);
        kassaText.appendChild(text);
        kassa.appendChild(kassaText);

        kassa.appendChild(document.createElement("br"));
        kassaText = document.createElement("p5");
        text = document.createTextNode("locatie: " + myJson.locatie);
        kassaText.appendChild(text);
        kassa.appendChild(kassaText);

        kassa.appendChild(document.createElement("br"));
        kassaText = document.createElement("p5");
        text = document.createTextNode("budget: " + myJson.budget);
        kassaText.appendChild(text);

        kassa.appendChild(kassaText);
        kassa.appendChild(document.createElement("br"));
        kassa.appendChild(document.createElement("br"));

        var wijzig = document.createElement("button");
        wijzig.className = "btn";
        wijzig.innerHTML = "Wijzig";
        wijzig.style.width = "15%";
        wijzig.value = myJson.kassanummer;
        wijzig.addEventListener("click", openWijzig);
        kassa.appendChild(wijzig);
        kassa.appendChild(document.createElement("br"));

        myNode.appendChild(kassa);
    })
}

//open het Medewerkers overzicht.
function ovOpenMed(){
    ovCat = "Medewerker";
    var myNode = document.querySelector("#ov");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    document.querySelector("#voegtoe").style.display = "block";
    document.querySelector("#voegtoe").innerHTML = "Voeg medewerker toe";

    fetch('../restservices/medewerker/byKassa/' + window.sessionStorage.getItem("kassa"))
    .then(function (response) {
            return response.json();
    })
    .then(function (myJson) {
        console.log(myJson);

        for (i in myJson){
            var medewerker = myJson[i];

            var med = document.createElement("div");
            med.className = "card";
            
            med.appendChild(document.createElement("br"));
            var medText = document.createElement("p5");
            var text = document.createTextNode("medewerkernummer: " + medewerker.mnr);
            medText.appendChild(text);
            med.appendChild(medText);
    
            med.appendChild(document.createElement("br"));
            medText = document.createElement("p5");
            text = document.createTextNode("voorletters: " + medewerker.voorletters);
            medText.appendChild(text);
            med.appendChild(medText);

            med.appendChild(document.createElement("br"));
            medText = document.createElement("p5");
            text = document.createTextNode("tussenvoegsel: " + medewerker.tussenvoegsel);
            medText.appendChild(text);
            med.appendChild(medText);

            med.appendChild(document.createElement("br"));
            medText = document.createElement("p5");
            text = document.createTextNode("achternaam: " + medewerker.achternaam);
            medText.appendChild(text);
            med.appendChild(medText);

            med.appendChild(document.createElement("br"));
            medText = document.createElement("p5");
            text = document.createTextNode("functie: " + medewerker.functie);
            medText.appendChild(text);
            med.appendChild(medText);
            med.appendChild(document.createElement("br"));
    
            var wijzig = document.createElement("button");
            wijzig.className = "btn";
            wijzig.innerHTML = "Wijzig";
            wijzig.style.width = "15%";
            wijzig.value = medewerker.mnr;
            wijzig.addEventListener("click", openWijzig);
            med.appendChild(wijzig);

            var verwijder = document.createElement("button");
            verwijder.className = "btn";
            verwijder.innerHTML = "Verwijder";
            verwijder.style.width = "15%";
            verwijder.value = medewerker.mnr;
            verwijder.addEventListener("click", verwijderFunc);
            med.appendChild(verwijder);
            med.appendChild(document.createElement("br"));
    
            myNode.appendChild(med);
        }
    })
}

//open producten overzicht.
function ovOpenProd(){
    ovCat = "Product";
    var myNode = document.querySelector("#ov");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }
    document.querySelector("#voegtoe").style.display = "block";
    document.querySelector("#voegtoe").innerHTML = "Voeg product toe";

    fetch('../restservices/product/' + window.sessionStorage.getItem("kassa"))
    .then(function (response) {
            return response.json();
    })
    .then(function (myJson) {
        console.log(myJson);

        for (i in myJson){
            var product = myJson[i];

            var prod = document.createElement("div");
            prod.className = "card";
            
            prod.appendChild(document.createElement("br"));
            var prodText = document.createElement("p5");
            var text = document.createTextNode("productnummer: " + product.productnummer);
            prodText.appendChild(text);
            prod.appendChild(prodText);
    
            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("naam: " + product.naam);
            prodText.appendChild(text);
            prod.appendChild(prodText);

            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("prijs: " + product.prijs);
            prodText.appendChild(text);
            prod.appendChild(prodText);

            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("inkoop prijs: " + product.inkoop);
            prodText.appendChild(text);
            prod.appendChild(prodText);

            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("minimale voorraad: " + product.minVoorraad);
            prodText.appendChild(text);
            prod.appendChild(prodText);

            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("op voorraad: " + product.voorraad);
            prodText.appendChild(text);
            prod.appendChild(prodText);

            prod.appendChild(document.createElement("br"));
            prodText = document.createElement("p5");
            text = document.createTextNode("categorie: " + product.categorie);
            prodText.appendChild(text);
            prod.appendChild(prodText);
            prod.appendChild(document.createElement("br"));
    
            var wijzig = document.createElement("button");
            wijzig.className = "btn";
            wijzig.innerHTML = "Wijzig";
            wijzig.style.width = "15%";
            wijzig.value = product.productnummer;
            wijzig.addEventListener("click", openWijzig);
            prod.appendChild(wijzig);

            var verwijder = document.createElement("button");
            verwijder.className = "btn";
            verwijder.innerHTML = "Verwijder";
            verwijder.style.width = "15%";
            verwijder.value = product.productnummer;
            verwijder.addEventListener("click", verwijderFunc);
            prod.appendChild(verwijder);
            prod.appendChild(document.createElement("br"));
    
            myNode.appendChild(prod);
        }
    })
}


