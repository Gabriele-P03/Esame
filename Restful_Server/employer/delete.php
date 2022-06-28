<?php 
/**
 * 
 * This is the script which provides deleting function for data inserted by harvesters
 * 
 * @author Gabriele-P03
 */

$usr = "ceo";
$psw = "h12@Lp05#k";
$HOST = "localhost";
$DB_NAME = "GH";
$PORT = 3306;

$connection = mysqli_connect($HOST, $usr, $psw, $DB_NAME);

if(mysqli_connect_errno()){

    echo ("Failed to connect: ".mysqli_connect_error());
    die();

}else{
    
    $table = $_GET['table'];

    //Check table name
    if($table != "Inside" && $table != "Outside"){
        header("HTTP/1.0 400 BAD INPUT");
        echo "Error selecting table";
        die();
    }

    //CHECK ID
    $dataId = $_GET['id_data'];
    if(!is_numeric($dataId)){
        header("HTTP/1.0 400 BAD INPUT");
        echo "Error: ID not numeric";
        die();
    }

    $query = "DELETE FROM $table WHERE Id = '$dataId'";
    
    echo $query;

    $result = mysqli_query($connection, $query);

    if($result){

        header("HTTP/1.0 200 OK");

    }else{
        header("HTTP/1.0 500 INTERNAL SERVER ERROR");
        echo "Error MYSQL: " . mysqli_errno($connection);
    }
}

mysqli_close($connection);
 ?>