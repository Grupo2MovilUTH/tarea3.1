<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

$host = 'localhost';
$db = 'crud';
$user = 'root';
$pass = '';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    if ($_SERVER['REQUEST_METHOD'] === 'POST') {
        $data = json_decode(file_get_contents("php://input"));

        if (!isset($data->nombres) || !isset($data->apellidos) || !isset($data->password)) {
            echo json_encode(['status' => 'error', 'message' => 'Missing parameters']);
            exit;
        }

        $nombres = trim($data->nombres);
        $apellidos = trim($data->apellidos);
        $password = trim($data->password);

        $stmt = $pdo->prepare("INSERT INTO users (nombres, apellidos, password) VALUES (?, ?, ?)");
        $hashedPassword = password_hash($password, PASSWORD_BCRYPT);
        $stmt->execute([$nombres, $apellidos, $hashedPassword]);

        echo json_encode(['status' => 'success', 'message' => 'Registration successful']);
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Invalid request method']);
    }
} catch (PDOException $e) {
    echo json_encode(['status' => 'error', 'message' => $e->getMessage()]);
}
?>
