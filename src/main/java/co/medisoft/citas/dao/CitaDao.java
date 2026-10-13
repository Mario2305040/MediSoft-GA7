package co.medisoft.citas.dao;

import co.medisoft.citas.database.ConexionBD;
import co.medisoft.citas.model.Cita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Acceso a datos de citas mediante sentencias JDBC parametrizadas. */
public class CitaDao {
    private static final String SQL_INSERTAR = """
            INSERT INTO citas (documento_paciente, nombre_paciente, especialidad, medico, fecha_hora, estado)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String SQL_LISTAR = """
            SELECT id, documento_paciente, nombre_paciente, especialidad, medico, fecha_hora, estado
            FROM citas ORDER BY fecha_hora, id
            """;
    private static final String SQL_BUSCAR = """
            SELECT id, documento_paciente, nombre_paciente, especialidad, medico, fecha_hora, estado
            FROM citas WHERE id = ?
            """;
    private static final String SQL_ACTUALIZAR = """
            UPDATE citas SET documento_paciente = ?, nombre_paciente = ?, especialidad = ?,
                medico = ?, fecha_hora = ?, estado = ? WHERE id = ?
            """;
    private static final String SQL_ELIMINAR = "DELETE FROM citas WHERE id = ?";

    public long insertar(Cita cita) throws SQLException {
        try (Connection connection = ConexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            asignarDatos(statement, cita);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
            throw new SQLException("MySQL no devolvió el ID generado para la cita.");
        }
    }

    public List<Cita> listarTodas() throws SQLException {
        List<Cita> citas = new ArrayList<>();
        try (Connection connection = ConexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(SQL_LISTAR);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                citas.add(mapearCita(resultSet));
            }
        }
        return citas;
    }

    public Optional<Cita> buscarPorId(long id) throws SQLException {
        try (Connection connection = ConexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(SQL_BUSCAR)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapearCita(resultSet)) : Optional.empty();
            }
        }
    }

    public boolean actualizar(long id, Cita cita) throws SQLException {
        try (Connection connection = ConexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(SQL_ACTUALIZAR)) {
            asignarDatos(statement, cita);
            statement.setLong(7, id);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(long id) throws SQLException {
        try (Connection connection = ConexionBD.obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(SQL_ELIMINAR)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private static void asignarDatos(PreparedStatement statement, Cita cita) throws SQLException {
        statement.setString(1, cita.documentoPaciente());
        statement.setString(2, cita.nombrePaciente());
        statement.setString(3, cita.especialidad());
        statement.setString(4, cita.medico());
        statement.setTimestamp(5, Timestamp.valueOf(cita.fechaHora()));
        statement.setString(6, cita.estado());
    }

    private static Cita mapearCita(ResultSet resultSet) throws SQLException {
        return new Cita(
                resultSet.getLong("id"),
                resultSet.getString("documento_paciente"),
                resultSet.getString("nombre_paciente"),
                resultSet.getString("especialidad"),
                resultSet.getString("medico"),
                resultSet.getTimestamp("fecha_hora").toLocalDateTime(),
                resultSet.getString("estado"));
    }
}
