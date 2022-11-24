//Load before HTML
$(document).ready(function(){

    const form = document.getElementById("form");
    const inputsForm = document.querySelectorAll("#form input");
    document.getElementById("btnNewCustomer").disabled=true;

    //add to inputs
    inputsForm.forEach((input) => {

        //When keyup:
        input.addEventListener('keyup', addEvent);

        //When blur:
        input.addEventListener('blur', addEvent);

    });
 });

function addEvent(e){

    //Save expresions (form)
    const expresions = {

        name: /^[a-zA-ZÀ-ÿ\s]{1,40}$/, // Letras y espacios, pueden llevar acentos.
        surname: /^[a-zA-ZÀ-ÿ\s]{1,40}$/,
        email: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/, // Email
        phoneNumber: /^[(]{0,1}[0-9]{3}[)]{0,1}[-\s\.]{0,1}[0-9]{3}[-\s\.]{0,1}[0-9]{3}$/, //Format: 000-000-000
        password: /^(?=(?:.*\d){2})(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){1})\S{8,}$/, // 1 capital letter, 2 letters, 2 numbers, 8 caracteres min
        address: /^[a-zA-ZÀ-ÿ0-9\s]{1,40}$/,

    }

    switch(e.target.name){

        case "name":
            if(!expresions.name.test(e.target.value)){

            document.getElementById("nameFail").innerHTML="Campo incorrecto, posee caracteres no permitidos (numeros, caracteres especiales...)";
            document.getElementById("btnNewCustomer").disabled=true;

            }else{

            document.getElementById("nameFail").innerHTML="";
            document.getElementById("btnNewCustomer").disabled=false;

            }
        break;

        case "surname":
            if(!expresions.surname.test(e.target.value)){

            document.getElementById("surnameFail").innerHTML="Campo incorrecto, posee caracteres no permitidos (numeros, caracteres especiales...)";
            document.getElementById("btnNewCustomer").disabled=true;

            }else{

            document.getElementById("surnameFail").innerHTML="";
            document.getElementById("btnNewCustomer").disabled=false;

            }
        break;

        case "email":
            if(!expresions.email.test(e.target.value)){

            document.getElementById("emailFail").innerHTML="Campo incorrecto. Formato válido: xxxxxxx@domain.xxx";
            document.getElementById("btnNewCustomer").disabled=true;

            }else{

            document.getElementById("emailFail").innerHTML="";
            document.getElementById("btnNewCustomer").disabled=false;

            }
        break;
        
        case "phoneNumber":
             if(!expresions.phoneNumber.test(e.target.value)){
        
             document.getElementById("phoneNumberFail").innerHTML="Campo incorrecto. Formato válido: 000-000-000.";
             document.getElementById("btnNewCustomer").disabled=true;
        
             }else{

              document.getElementById("phoneNumberFail").innerHTML="";
              document.getElementById("btnNewCustomer").disabled=false;

             }
        break;
        
        case "password":
             if(!expresions.password.test(e.target.value)){
              
             document.getElementById("passwordFail").innerHTML="Campo incorrecto. Formato: 1 letra mayúscula, 2 minúsculas, 2 números, mínimo 8 caracteres.";
             document.getElementById("btnNewCustomer").disabled=true;
              
             }else{

             document.getElementById("passwordFail").innerHTML="";
             document.getElementById("btnNewCustomer").disabled=false;

             }
        break;
          
        case "address":
             if(!expresions.address.test(e.target.value)){
                  
             document.getElementById("addressFail").innerHTML="Campo incorrecto. Dirección demasiado larga";
             document.getElementById("btnNewCustomer").disabled=true;
                  
             }else{

             document.getElementById("addressFail").innerHTML="";
             document.getElementById("btnNewCustomer").disabled=false;

             }
        break;
                             
    }
 }
