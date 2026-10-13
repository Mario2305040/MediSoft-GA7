package co.medisoft.citas.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza la conexión JDBC a MySQL usando configuración del entorno. */
public final class ConexionBD {
    private static final String URL_PREDETERMINADA = "jdbc:mysql://localhost:3306/medisoft?serverTimezone=UTC";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        String url = leerVariable("MEDISOFT_DB_URL", URL_PREDETERMINADA);
        String usuario = leerVariable("MEDISOFT_DB_USER", "root");
        String contrasena = leerVariable("MEDISOFT_DB_PASSWORD", "");
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private static String leerVariable(String nombre, String valorPredeterminado) {
        String valor = System.getenv(nombre);
        return valor == null ? valorPredeterminado : valor;
    }
}
