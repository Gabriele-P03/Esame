<?php 

    $con = mysqli_connect("localhost", "root", "root", "GH");

    $lastConnErr = mysqli_connect_errno($con);

    if($lastConnErr > 0){
        echo $lastConnErr;
    }else{

        //$username = $_GET['username'];
        //$password = $_GET['password'];

        $result = mysqli_query($con, "SELECT @@hostname");

        $row = mysqli_fetch_array($result);
        
        if($row){
            echo $row[0];
        }

        mysqli_close(($con));
    }


?>