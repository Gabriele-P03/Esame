<?php

    /**
     * This is the Restful PHP API for sending DATA to MySQL DB
     * 
     * @author Gabriele-P03
    */

    
    $usr = $_GET['usr'];
    $USERNAME = "employee";
    $PASSWORD = "3mp10y33";
    $HOST = "localhost";
    $PORT = 3306;
    $DB_NAME = "GH"; //Name of the databse

    /**
     * Since Impiehato table can only be read by root,
     * it will query via a root-connection to the DB
     */

    $connection = mysqli_connect($HOST, "root", "root", $DB_NAME);
    if(mysqli_connect_errno()){
        echo ("Failed to connect: ".mysqli_connect_error());
        die();
    }
    //Let's get the ID of the user who did the query
    $query = "SELECT * FROM Employee WHERE Username = '$usr'";
    $result = mysqli_query($connection, $query);
    $ID;
    if(!$result){
        echo "Invalid User: " . mysqli_errno($connection);
    }else{
        $ID = mysqli_fetch_array($result)[0];
    }
    mysqli_close($connection);





    $connection = mysqli_connect($HOST, $USERNAME, $PASSWORD, $DB_NAME);
    if(mysqli_connect_errno()){
        echo ("Failed to connect: ".mysqli_connect_error());
        die();
    }

    /*
        As already said in Readme.md, this project is about analysis how, growing in an environment
        with a right temperature and humidity, can improve plant"s life.
        
        This analysis will be done about 2 SAME plants. One inside the greenhouse, the other one outside.
        Due to data of saving, we have to know if we"re sending outside plant"s one or inside"s.
        In fact, about inside one, we"re not sending data about temperature and humidity. They"re the set one
        According to protocol: 0 means inside one, 1 outside one
    */
    $TYPE_GH = $_GET["type_gh"];
    $DATE = $_GET["date"];
    $LEAVES = $_GET["leaves"];
    $PLANTS = $_GET["plants"];
    $MAX_HEIGHT = $_GET["max_height"];

    $Id_impiegato = "Id_employee";
    $Fusti_COL = "Plants";
    $Foglie_COL = "Leaves";
    $AltezzaMassima_COL = "Max_height";
    $Data_COL = "Date";
    $Temperatura_COL = "Temperature";
    $Umidita_COL = "Humidity";
    $Light_COL = "Light";

    if($TYPE_GH == "o"){
        
        $TEMPERATURE = $_GET["temperature"];
        $HUMIDTY = $_GET["humidity"];
        $LIGHT = $_GET['light'];

        $query = "INSERT INTO Outside 
        ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL, $Temperatura_COL, $Umidita_COL, $Light_COL, $Id_impiegato) 
        VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT, $TEMPERATURE, $HUMIDTY, $LIGHT, $ID)";
    }else if($TYPE_GH == "i"){
        $query = "INSERT INTO Inside ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL, $Id_impiegato) 
        VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT, $ID)";
    }

    if(!mysqli_query($connection, $query)){
        echo mysqli_errno($connection);
    }else{
        echo http_response_code();
    }

    mysqli_close($connection);

    
?>