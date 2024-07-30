<?php
include_once 'database.php';
include_once 'register.php';

// Instancia de la base de datos
$database = new Database();
$db = $database->getConnection();

// Instancia de la clase Register
$register = new Register($db);

// Obtención de datos JSON del cuerpo de la solicitud
$data = json_decode(file_get_contents("php://input"));

// Verificación de los datos recibidos
if (isset($data->id) && isset($data->nombres) && isset($data->apellidos) && isset($data->password) && isset($data->token)) {
    $register->id = $data->id;
    $register->nombres = $data->nombres;
    $register->apellidos = $data->apellidos;
    $register->password = $data->password;
    $register->token = $data->token;

    // Actualización del registro
    if ($register->updateRegister()) {
        http_response_code(200);
        echo json_encode(array("message" => "User was updated."));
    } else {
        http_response_code(503);
        echo json_encode(array("message" => "Unable to update user."));
    }
} else {
    http_response_code(400);
    echo json_encode(array("message" => "Incomplete data."));
}
?>
