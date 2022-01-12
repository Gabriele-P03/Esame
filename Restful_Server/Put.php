<?php

    /**
     * This is the Restful PHP API for sending DATA to MySQL DB
     * 
     * @author Gabriele-P03
    */

    //As the database will be in local, we can decleare username and password right now
    $USERNAME = "root";
    $PASSWORD = "root";
    $HOST = "localhost";
    $PORT = 3306; //Php demands 3006 as port for MySQL, that is already set by SQLServer. In fact it will should not use it  
    $DB_NAME = "GH"; //Name of the databse

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

    $Fusti_COL = "Fusti";
    $Foglie_COL = "Foglie";
    $AltezzaMassima_COL = "Altezza_massima";
    $Data_COL = "Data";
    $Temperatura_COL = "Temperatura";
    $Umidita_COL = "Umidita";
    
    $query;
    
    if($TYPE_GH == "o"){
        
        $TEMPERATURE = $_GET["temperature"];
        $HUMIDTY = $_GET["humidity"];

        $query = "INSERT INTO Conta_esterno 
        ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL, $Temperatura_COL, $Umidita_COL) 
        VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT, $TEMPERATURE, $HUMIDTY)";
    }else if($TYPE_GH == "i"){
        $query = "INSERT INTO Conta_serra ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL) VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT)";
    }

    //Debugging purpose
    //echo $query;

    if(!mysqli_query($connection, $query)){
        echo("Error: ".mysqli_error($connection));
    }else{
        echo 200;
    }

    mysqli_close($connection);
?>