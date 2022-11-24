$(document).ready(function(){

    const form = document.getElementById("form");
    const inputsForm = document.querySelectorAll("#form input");
    document.getElementById("btnModifyReview").disabled=true;


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

        userName: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/, // Email

    }

    switch(e.target.name){

        case "userName":
            if(!expresions.userName.test(e.target.value)){

            document.getElementById("userNameFail").innerHTML="Campo incorrecto. Formato válido: xxxxxxx@domain.xxx";
            document.getElementById("btnModifyReview").disabled=true;

            }else{
            document.getElementById("userNameFail").innerHTML="";
            document.getElementById("btnModifyReview").disabled=false;

            }
        break;
    }
 }