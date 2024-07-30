<?php
// Incluir los archivos de base de datos y la clase Register
include_once 'database.php';
include_once 'register.php';

// Crear una instancia de la base de datos y obtener la conexión
$database = new Database();
$db = $database->getConnection();

// Crear una instancia de la clase Register
$register = new Register($db);

// Obtener los datos enviados en la solicitud POST
$data = json_decode(file_get_contents("php://input"));

// Asegurarse de que los datos necesarios estén presentes
if(isset($data->id)) {
    // Asignar el ID del usuario a la instancia de Register
    $register->id = $data->id;

    // Intentar eliminar el usuario
    if($register->deleteRegister()) {
        // Si la eliminación es exitosa, responder con un código de éxito y un mensaje
        http_response_code(200);
        echo json_encode(array("message" => "User was deleted."));
    } else {
        // Si la eliminación falla, responder con un código de error y un mensaje
        http_response_code(503);
        echo json_encode(array("message" => "Unable to delete user."));
    }
} else {
    // Si los datos necesarios no están presentes, responder con un código de error y un mensaje
    http_response_code(400);
    echo json_encode(array("message" => "Incomplete data. Unable to delete user."));
}
?>
