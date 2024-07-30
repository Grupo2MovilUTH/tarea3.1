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

        if (!isset($data->nombres) || !isset($data->password)) {
            echo json_encode(['status' => 'error', 'message' => 'Missing parameters']);
            exit;
        }

        $nombres = trim($data->nombres);
        $password = trim($data->password);

        // Preparar y ejecutar la consulta
        $stmt = $pdo->prepare("SELECT * FROM users WHERE nombres = ?");
        $stmt->execute([$nombres]);
        $user = $stmt->fetch(PDO::FETCH_ASSOC);

        if ($user && password_verify($password, $user['password'])) {
            // Generar y almacenar el token
            $token = bin2hex(random_bytes(16));
            $stmt = $pdo->prepare("UPDATE users SET token = ? WHERE id = ?");
            $stmt->execute([$token, $user['id']]);

            echo json_encode(['status' => 'success', 'token' => $token]);
        } else {
            echo json_encode(['status' => 'error', 'message' => 'Invalid credentials']);
        }
    } else {
        echo json_encode(['status' => 'error', 'message' => 'Invalid request method']);
    }
} catch (PDOException $e) {
    echo json_encode(['status' => 'error', 'message' => $e->getMessage()]);
}
?>
