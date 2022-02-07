<?php

    function decrypt($PASSWORD){

        $buf = $PASSWORD;
        echo $PASSWORD . "\n";

        for($i = strlen($buf) - 1; $i > 0; $i--){
            $curr = ord(substr($buf, $i, 1));
            $prev = ord(substr($buf, $i-1, 1));
            $next = ord(substr($buf, $i >= strlen($buf) - 1 ? 0 : $i+1, 1));

            echo $prev . " - " . $curr . " - " . $next . "    ";

            $diff = max($prev, $next) - min($prev, $next);

            $curr = $diff;

            $buf[$i] = chr($curr);
            echo $buf . "\n";
        }
    
        return $buf;
    }

?>