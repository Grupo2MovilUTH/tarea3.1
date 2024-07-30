<?php
class Register {
    private $conexion;
    private $table = "users";

    public $id;
    public $nombres;
    public $apellidos;
    public $password;
    public $token;

    public function __construct($db) {
        $this->conexion = $db;
    }

    public function createRegister() {
        $consulta = "INSERT INTO " . $this->table . " SET nombres = :nombres, apellidos = :apellidos, password = :password, token = :token";
        $comando = $this->conexion->prepare($consulta);

        // Sanitización de datos
        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->apellidos = htmlspecialchars(strip_tags($this->apellidos));
        $this->password = htmlspecialchars(strip_tags($this->password));
        $this->token = htmlspecialchars(strip_tags($this->token));

        // Encriptar la contraseña
        $hashed_password = password_hash($this->password, PASSWORD_BCRYPT);

        // Bind de datos
        $comando->bindParam(":nombres", $this->nombres);
        $comando->bindParam(":apellidos", $this->apellidos);
        $comando->bindParam(":password", $hashed_password);
        $comando->bindParam(":token", $this->token);

        if ($comando->execute()) {
            return true;
        }

        return false;
    }

    public function authenticate() {
        $query = "SELECT * FROM " . $this->table . " WHERE nombres = :nombres LIMIT 0,1";
        $stmt = $this->conexion->prepare($query);

        $stmt->bindParam(':nombres', $this->nombres);
        $stmt->execute();

        if ($stmt->rowCount() > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            // Verificar la contraseña encriptada
            if (password_verify($this->password, $row['password'])) {
                return true;
            }
        }
        return false;
    }

    public function updateRegister() {
        $consulta = "UPDATE " . $this->table . " SET nombres = :nombres, apellidos = :apellidos, password = :password, token = :token WHERE id = :id";
        $comando = $this->conexion->prepare($consulta);

        $this->nombres = htmlspecialchars(strip_tags($this->nombres));
        $this->apellidos = htmlspecialchars(strip_tags($this->apellidos));
        $this->password = htmlspecialchars(strip_tags($this->password));
        $this->token = htmlspecialchars(strip_tags($this->token));

        // Encriptar la contraseña
        $hashed_password = password_hash($this->password, PASSWORD_BCRYPT);

        $comando->bindParam(":id", $this->id);
        $comando->bindParam(":nombres", $this->nombres);
        $comando->bindParam(":apellidos", $this->apellidos);
        $comando->bindParam(":password", $hashed_password);
        $comando->bindParam(":token", $this->token);

        if ($comando->execute()) {
            return true;
        }

        return false;
    }

    public function deleteRegister() {
        $consulta = "DELETE FROM " . $this->table . " WHERE id = :id";
        $comando = $this->conexion->prepare($consulta);

        $comando->bindParam(":id", $this->id);

        if ($comando->execute()) {
            return true;
        }

        return false;
    }
}
?>
