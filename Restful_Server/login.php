
<?php

    //include "EnctyptFunc.php";

    $usr = $_GET["usr"];
    $psw = $_GET["psw"];
    $HOST = "localhost";
    $PORT = 3306;

    $DB_NAME = "GH"; //Name of the databse
    $ID_IMPIEGATO = "Id";
    $DATA_DI_NASCITA = "Birthday";
    $CF = "CF";
    $USERNAME = "Username";
    $PASSWORD = "Password";
    $GRADO = "Grade";
    $NOME = "Name";
    $COGNOME = "LastName";

    //Decrypting username and password
    $iv = "1234567890abcdef";
    $key = "1234567890abcdef";
    $cipher_algo = "aes-128-cbc";
    $usr = openssl_decrypt($usr, $cipher_algo, $key, 0, $iv);
    $psw = openssl_decrypt($psw, $cipher_algo, $key, 0, $iv);


    $connection = mysqli_connect($HOST, "user", "user", $DB_NAME);

    if(mysqli_connect_errno()){

        echo ("Failed to connect: ".mysqli_connect_error());
        die();

    }else{
        //No errors occurred while connecting to the server
        $QUERY = "SELECT Id,Name,LastName,Birthday,CF,Username,Grade,Id_employee FROM Employee WHERE $USERNAME = '$usr' AND $PASSWORD = '$psw'";
        $RESULT = mysqli_query($connection, $QUERY);
        
        if($RESULT){


            $row = mysqli_fetch_assoc($RESULT);

            if($row){
                //Username and password valid
                //Let's pass employee's grade
                header("HTTP/1.0 200 0K");
                echo json_encode($row);

            }else{
                //Invalid username or password
                //401 status code means Unuthorized, it must be seen as invalid credentials
                header("HTTP/1.0 403 FORBIDDEN");
                echo "Unuthorized";
            }
        }else{
            header("HTTP/1.0 500 INTERNAL_SERVER_ERROR");
            echo "Invalid credentials";
        }
        
    }

    mysqli_close($connection);
?>
