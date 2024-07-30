<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET");

class DataBase {
    private $host = "localhost";
    private $db_name = "crud";
    private $username = "root";
    private $password = "";
    public $conn;

    public function getConnection() {
        $this->conn = null;
        try {
            $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            $this->conn->exec("set names utf8");
        } catch(PDOException $exception) {
            echo "Connection error: " . $exception->getMessage();
        }
        return $this->conn;
    }
}

class Register {
    private $conexion;
    private $table = "users"; // Asegúrate de que el nombre de la tabla sea correcto

    public function __construct($db) {
        $this->conexion = $db;
    }

    public function GetListRegister() {
        $consulta = "SELECT id, nombres, apellidos, password, token FROM " . $this->table;
        $comando = $this->conexion->prepare($consulta);
        $comando->execute();
        return $comando;
    }
}

$db = new DataBase();
$instant = $db->getConnection();

$pinst = new Register($instant);
$cmd = $pinst->GetListRegister();
$count = $cmd->rowCount();

if($count > 0) {
    $personasarray = array();
    while($row = $cmd->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $e = array(
            "id" => $id,
            "nombres" => $nombres,
            "apellidos" => $apellidos,
            "password" => $password,
            "token" => $token
        );
        array_push($personasarray, $e);
    }
    http_response_code(200);
    echo json_encode($personasarray);
} else {
    http_response_code(404);
    echo json_encode(array("message" => "No hay datos"));
}
?>
