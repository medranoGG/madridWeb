$(document).ready(function(){

    const form = document.getElementById("form");
    const inputsForm = document.querySelectorAll("#form input");
    document.getElementById("btnLogin").disabled=true;


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

        email: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/, // Email
        password: /^(?=(?:.*\d){2})(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){1})\S{8,}$/, // 1 capital letter, 2 letters, 2 numbers, 8 caracteres min


    }





    switch(e.target.name){

        case "email":
            if(!expresions.email.test(e.target.value)){

            document.getElementById("emailFail").innerHTML="Campo incorrecto. Formato válido: xxxxxxx@domain.xxx";
            document.getElementById("btnLogin").disabled=true;


            }else{

            document.getElementById("emailFail").innerHTML="";
            document.getElementById("btnLogin").disabled=false;



            }
        break;

        case "password":
            if(!expresions.password.test(e.target.value)){

            document.getElementById("passwordFail").innerHTML="Campo incorrecto. Formato: 1 letra mayúscula, 2 minúsculas, 2 números, mínimo 8 caracteres.";
            document.getElementById("btnLogin").disabled=true;

            }else{

            document.getElementById("passwordFail").innerHTML="";
            document.getElementById("btnLogin").disabled=false;


            }
        break;
    }
 }
