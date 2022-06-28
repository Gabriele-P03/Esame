<?php

    $iv = "1234567890abcdef";
    $key = "1234567890abcdef";
    $cipher_algo = "aes-128-cbc";

    function decrypt($data){

        $iv = "1234567890abcdef";
        $key = "1234567890abcdef";
        $cipher_algo = "aes-128-cbc";

        $decData = openssl_decrypt($data, $cipher_algo, $key, 0, $iv);

        return $decData;
    }

    function encrypt($data){

        global $cipher_algo, $key, $iv;

        return openssl_encrypt($data, $cipher_algo, $key, 0, $iv);
    }
?>