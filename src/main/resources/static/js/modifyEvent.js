$(document).ready(function(){

    const form = document.getElementById("form");
    const inputsForm = document.querySelectorAll("#form input");
    document.getElementById("btnModifyEvent").disabled=true;


    //add to inputs
    inputsForm.forEach((input) => {

        //When keyup: cuando pulsas tecla
        input.addEventListener('keyup', addEvent);

        //When blur: cuando quitas foco
        input.addEventListener('blur', addEvent);

    });
 });

function addEvent(e){

    //Save expresions (form)
    const expresions = {

        name: /^[a-zA-ZÀ-ÿ0-9\s]{1,40}$/, // Letras y espacios, pueden llevar acentos.
        price: /^\d*(\.\d{1})?\d{0,1}$/, //Valida número decimal con dos dígitos de precisión.

    }

    switch(e.target.name){

        case "name":
            if(!expresions.name.test(e.target.value)){

            document.getElementById("nameFail").innerHTML="Campo incorrecto, posee caracteres no permitidos (numeros, caracteres especiales...)";
            document.getElementById("btnModifyEvent").disabled=true;

            }else{

            document.getElementById("nameFail").innerHTML="";
            document.getElementById("btnModifyEvent").disabled=false;

            }
        break;


        case "price":
            if(!expresions.price.test(e.target.value)){

            document.getElementById("priceFail").innerHTML="Campo incorrecto. El precio sólo puede ser un número y puede contener hasta dos decimales.";
            document.getElementById("btnModifyEvent").disabled=true;

            }else{

            document.getElementById("priceFail").innerHTML="";
            document.getElementById("btnModifyEvent").disabled=false;

            }
        break;

    }
 }
