<?php

/**
 * This is the php script which provides selecting-filtered query for
 * employees' data inserted
 * 
 * As it is filtered, employer will pass a filter for each fields in DB
 * 
 * @author Gabriele-P03
 */

    $usr = "employee";
    $psw = "3mp10y33";
    $HOST = "localhost";
    $PORT = 3306;

    //These parameters for searching are only available for employer as it is the only
    //one who has the privilages for read data of everyone
    $EMPLOYE_NAME = $_GET["empee_usr"];

    /*  
        If user wanna read data from outside or inside the greenhouse
        Read from both is an already available feature
        0 is inside the GH
        1 is outside the GH

        All other values get will be catch as 0 (inside)
    */
    $bool_INSIDE = ($_GET["get_in"] == '1');
    $bool_OUTSIDE = ($_GET["get_out"] == '1');

    $Fusti_COL = "Fusti";
    $Foglie_COL = "Foglie";
    $AltezzaMassima_COL = "Altezza_massima";
    $Data_COL = "Data";
    $Temperatura_COL = "Temperatura";
    $Umidita_COL = "Umidita";

    $connection = mysqli_connect($HOST, $usr, $psw, $DB_NAME);

    if(mysqli_connect_errno()){

        echo ("Failed to connect: ".mysqli_connect_error());
        die();

    }else{
        $query = "SELECT * FROM ";

        if($bool_INSIDE && !$bool_OUTSIDE)
            $query . "Conta_serra ";
        else if(!$bool_INSIDE && $bool_OUTSIDE)
                $query . "Conta_esterno ";
        else if($bool_INSIDE && $bool_OUTSIDE)
            $query . "Conta_serra, Conta_esterno ";
        else{
            echo "Any table to query on has been passed";
            header("HTTP/1.0 400 Missing input");
        }

        /**
         * Adesso si dovrebbe filtrare la query
         * Devo provare a usare '*' nella query sulla WHERE per ovviare
         * alla molteplicità delle tabelle in cui cercare
         */

        $result = mysqli_query($connection, $query);

        if($result){
            header("HTTP/1.0 200 OK");
            while($row = mysqli_fetch_array($result) ){
                echo print_r($row);
            }
        }else{
            header("HTTP/1.0" . mysqli_errno($connection) . "ERROR");
        }
    }
?>