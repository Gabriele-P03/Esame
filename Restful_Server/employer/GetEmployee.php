<?php

/**
 * This is the php script which provides selecting-filtered query for
 * employees' data inserted
 * 
 * As it is filtered, employer will pass a filter for each fields in DB
 * 
 * @author Gabriele-P03
 */

    $usr = "user";
    $psw = "user";
    $HOST = "localhost";
    $PORT = 3306;
    $DB_NAME = "GH";

    $connection = mysqli_connect($HOST, $usr, $psw, $DB_NAME);

    if(mysqli_connect_errno()){

        echo ("Failed to connect: ".mysqli_connect_error());
        die();

    }else{
        $query = "SELECT * FROM Employee";

        $result = mysqli_query($connection, $query);

        if($result){
            header("HTTP/1.0 200 OK");

            $rows = array();
            while($r = mysqli_fetch_assoc($result)) {
                $rows[] = $r;
            }
            echo json_encode($rows);

        }else{
            header("HTTP/1.0 " . mysqli_errno($connection) . " SQL_ERROR");
        }
    }
?>