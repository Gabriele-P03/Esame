<?php

    $usr = $_GET["usr"];
    $psw = $_GET["psw"];
    $HOST = "localhost";
    $PORT = 3306;

    $DB_NAME = "GH"; //Name of the databse
    $ID_IMPIEGATO = "Id_impiegato";
    $DATA_DI_NASCITA = "Data_nascita";
    $CF = "Codice_fiscale";
    $USERNAME = "Username";
    $PASSWORD = "Password";
    $GRADO = "Grado";
    $NOME = "Nome";
    $COGNOME = "Cognome";

    $connection = mysqli_connect($HOST, "user", "user", $DB_NAME);
    if(mysqli_connect_errno()){
        echo ("Failed to connect: ".mysqli_connect_error());
        die();
    }else{
        //No errors occurred while connecting to the server
        $QUERY = "SELECT * FROM Impiegato WHERE $USERNAME = '$usr' AND $PASSWORD = '$psw'";
        $RESULT = mysqli_query($connection, $QUERY);
        
        if($RESULT){

            /*
                Lines below are very efficient.
                Even if Username field has been declared as Unique, it should
                check for more fetches 
            */
            $row = mysqli_fetch_array($RESULT);

            if($row){
                //Username and password valid
                //Let's pass employee's grade
                echo "200";
            }else{
                //Invalid useername or password
                echo "-1";
            }
        }else{
            
        }
    }
?>