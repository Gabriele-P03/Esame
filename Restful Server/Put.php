<?php

/**
 * This is the Restful API for make communicating GreenHouse Android Application with the 
 * relative database which will store data about vital signs of plants
 * 
 * @author Gabriele-P03
*/
    echo "Ciao Dino";
    //As the database will be in local, we can decleare username and password right now
    $USERNAME = "root";
    $PASSWORD = "root";
    $HOST = "localhost";
    $PORT = 3006; //Php demands 3006 as port for MySQL, that is already set by SQLServer. In fact it will should not use it  
    $DB_NAME = "GH"; //Name of the databse

    $connection = mysqli_connect($HOST, $USERNAME, $PASSWORD, $DB_NAME);
    $errno = mysqli_connect_errno($connection);
    if( $errno > 0 ){
        echo "Error: " + $errno + ". "; 
    }   

    /*
        As already said in Readme.md, this project is about analysis how, growing in an environment
        with a right temperature and humidity, can improve plant's life.
        
        This analysis will be done about 2 SAME plants. One inside the greenhouse, the other one outside.
        Due to data of saving, we have to know if we're sending outside plant's one or inside's.
        In fact, about inside one, we're not sending data about temperature and humidity. They're the set one

        According to protocol: 0 means inside one, 1 outside one
    */
    $TYPE_GH = $_GET['type_gh'];
    $DATE = $_GET['date'];
    $LEAVES = $_GET['leaves'];
    $PLANTS = $_GET['plants'];
    $MAX_HEIGHT = $_GET['max_height'];

    $Fusti_COL = "Fusti";
    $Foglie_COL = "Foglie";
    $AltezzaMassima_COL = "Altezza_massima";
    $Data_COL = "Data";
    $Temperatura_COL = "Temperatura";
    $Umidita_COL = "Umidita";
    
    $query;

    if($TYPE_GH == 1){
        
        $TEMPERATURE = $_GET['temperature'];
        $HUMIDTY = $_GET['humidity'];

        $query = "INSERT INTO Conta_esterno 
        ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL, $Temperatura_COL, $Umidita_COL) 
        VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT, $TEMPERATURE, $HUMIDTY);";
    }else if($TYPE_GH == 0){
        $query = "INSERT INTO Conta_esterno 
        ($Data_COL, $Fusti_COL, $Foglie_COL, $AltezzaMassima_COL) 
        VALUES($DATE, $PLANTS, $LEAVES, $MAX_HEIGHT);";
    }

    $result = mysqli_query($conn, $query);

    if($result){
        echo $result;
    }
?>