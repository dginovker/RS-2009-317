<?php

class Database {

  private $host;

  private $port;

  private $db_name;

  private $username;

  private $password;

  public function __construct($host, $port, $db_name, $username, $password) {
    $this->host = $host;
    $this->port = $port;
    $this->db_name = $db_name;
    $this->username = $username;
    $this->password = $password;
  }

  public function getHost() {
    return $this->host;
  }

  public function getPort() {
    return $this->port;
  }

  public function getName() {
    return $this->$db_name;
  }

  public function getUsername() {
    return $this->username;
  }

  public function __ToString() {
    return "[".$this->host.",".$this->port.",".$this->db_name.",".$this->username."]";
  }

}

 ?>
