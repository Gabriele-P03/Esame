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
    $DB_NAME = "GH";
    $PORT = 3306;


    $bool_INSIDE = ($_GET["inside"] == 'true');
    //Id and grade about user who has sent the request
    $usrId = $_GET['usr'];
    $usrGrade = $_GET['grade']; 
    //Used only when user's grade is not 0 (harvester)
    $users = $_GET['users'];

    $minHeight = $_GET['minHeight'];
    $maxHeight = $_GET['maxHeight'];
    $minPlants = $_GET['minPlants'];
    $maxPlants = $_GET['maxPlants'];
    $minLeaves = $_GET['minLeaves'];
    $maxLeaves = $_GET['maxLeaves'];
    $minTemperature = $_GET['minTemperature'];
    $maxTemperature = $_GET['maxTemperature'];
    $minHumidity = $_GET['minHumidity'];
    $maxHumidity = $_GET['maxHumidity'];
    $minLight = $_GET['minLight'];
    $maxLight = $_GET['maxLight'];

    //let's check if all params are numeric (Avoiding SQLInjection)
    if( !is_numeric($minHeight) || !is_numeric($maxHeight) ||
        !is_numeric($minPlants) || !is_numeric($maxPlants) ||
        !is_numeric($minLeaves) || !is_numeric($maxLeaves) ||
        !is_numeric($usrId)){

            header("HTTP/1.0 400 BAD INPUT");
            echo "Some basic inputs are invalid...";
            exit;
    }

    if(!$bool_INSIDE){
        if( !is_numeric($minTemperature) || !is_numeric($maxTemperature) ||
            !is_numeric($minHumidity) || !is_numeric($maxHumidity) ||
            !is_numeric($minLight) || !is_numeric($maxLight)){

                header("HTTP/1.0 400 BAD INPUT");
                echo "Some inputs are invalid...";
                exit;
        }
    }
    

    $connection = mysqli_connect($HOST, $usr, $psw, $DB_NAME);

    if(mysqli_connect_errno()){

        echo ("Failed to connect: ".mysqli_connect_error());
        die();

    }else{
        
        $query = "";

        if($bool_INSIDE){
            $query = "SELECT Inside.Id,Plants,Leaves,Max_height,Date,Inside.Id_employee,Employee.Username FROM Inside INNER JOIN Employee ON Inside.Id_employee = Employee.Id WHERE Inside.Id_employee ";
            if($usrGrade == 0){
                $query = $query . " = '$usrId'";
            }else{
                $query = $query . " IN ('$users')";
            }      
        }else{
            $query = $query . "SELECT Outside.Id,Plants,Leaves,Max_height,Date,Temperature,Humidity,Light,Outside.Id_employee,Employee.Username FROM Outside INNER JOIN Employee ON Outside.Id_employee = Employee.Id WHERE Outside.Id_employee ";
            if($usrGrade == 0){
                $query = $query . " = '$usrId'";
            }else{
                $query = $query . " IN ('$users')";
            }
        }

        //Filtering query basing on min and max values supplied
        $query = $query . " AND '$minHeight' <= Max_height AND '$maxHeight' >= Max_height 
                            AND '$minPlants' <= Plants AND '$maxPlants' >= Plants
                            AND '$minLeaves' <= Leaves AND '$maxLeaves' >= Leaves";

        if(!$bool_INSIDE){
            $query = $query . " AND '$minTemperature' <= Temperature AND '$maxTemperature' >= Temperature 
                                AND '$minHumidity' <= Humidity AND '$maxHumidity' >= Humidity
                                AND '$minLight' <= Light AND '$maxLight' >= Light";
        }


        $result = mysqli_query($connection, $query);
        

        if($result){

            header("HTTP/1.0 200 OK");

            $rows = array();
            while($r = mysqli_fetch_assoc($result)) {
                $rows[] = $r;

            }
            echo json_encode($rows);

        }else{
            header("HTTP/1.0 500 INTERNAL SERVER ERROR");
            echo mysqli_errno($connection);
        }
    }

    mysqli_close($connection);
?>