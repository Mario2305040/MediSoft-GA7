package co.medisoft.citas;

import co.medisoft.citas.dao.CitaDao;
import co.medisoft.citas.database.ConexionBD;
import co.medisoft.citas.model.Cita;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Punto de entrada y menú de consola del módulo de gestión de citas. */
public final class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final CitaDao CITA_DAO = new CitaDao();

    private Main() {
    }

    public static void main(String[] args) {
        try (var connection = ConexionBD.obtenerConexion()) {
            System.out.println("Conexión JDBC establecida con MediSoft.");
        } catch (SQLException exception) {
            System.err.println("No fue posible conectar con MySQL: " + exception.getMessage());
            System.err.println("Revisa la base de datos y las variables de conexión descritas en README.md.");
            return;
        }

        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = leerTexto("Selecciona una opción: ");
            try {
                switch (opcion) {
                    case "1" -> registrarCita();
                    case "2" -> listarCitas();
                    case "3" -> consultarCita();
                    case "4" -> actualizarCita();
                    case "5" -> eliminarCita();
                    case "0" -> continuar = false;
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException exception) {
                System.err.println("No se pudo completar la operación: " + exception.getMessage());
            }
            System.out.println();
        }
        System.out.println("Módulo de citas finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println("=== MediSoft | Gestión de citas ===");
        System.out.println("1. Registrar cita (INSERT)");
        System.out.println("2. Listar citas (SELECT)");
        System.out.println("3. Consultar cita por ID (SELECT)");
        System.out.println("4. Actualizar cita (UPDATE)");
        System.out.println("5. Eliminar cita (DELETE)");
        System.out.println("0. Salir");
    }

    private static void registrarCita() throws SQLException {
        Cita cita = leerDatosCita();
        long id = CITA_DAO.insertar(cita);
        System.out.println("Cita registrada correctamente con ID " + id + ".");
    }

    private static void listarCitas() throws SQLException {
        List<Cita> citas = CITA_DAO.listarTodas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        citas.forEach(System.out::println);
    }

    private static void consultarCita() throws SQLException {
        long id = leerLong("ID de la cita: ");
        CITA_DAO.buscarPorId(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("No se encontró una cita con ese ID."));
    }

    private static void actualizarCita() throws SQLException {
        long id = leerLong("ID de la cita que deseas actualizar: ");
        if (CITA_DAO.buscarPorId(id).isEmpty()) {
            System.out.println("No se encontró una cita con ese ID.");
            return;
        }
        Cita citaActualizada = leerDatosCita();
        boolean actualizada = CITA_DAO.actualizar(id, citaActualizada);
        System.out.println(actualizada ? "Cita actualizada correctamente." : "No se realizaron cambios.");
    }

    private static void eliminarCita() throws SQLException {
        long id = leerLong("ID de la cita que deseas eliminar: ");
        boolean eliminada = CITA_DAO.eliminar(id);
        System.out.println(eliminada ? "Cita eliminada correctamente." : "No se encontró una cita con ese ID.");
    }

    private static Cita leerDatosCita() {
        String documento = leerTexto("Documento del paciente: ");
        String nombre = leerTexto("Nombre del paciente: ");
        String especialidad = leerTexto("Especialidad: ");
        String medico = leerTexto("Nombre del médico: ");
        LocalDateTime fechaHora = leerFechaHora();
        String estado = leerTexto("Estado (PROGRAMADA, ATENDIDA o CANCELADA): ").toUpperCase();
        return new Cita(documento, nombre, especialidad, medico, fechaHora, estado);
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = INPUT.nextLine().trim();
            if (!valor.isBlank()) {
                return valor;
            }
            System.out.println("El valor no puede estar vacío.");
        }
    }

    private static long leerLong(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            try {
                long numero = Long.parseLong(valor);
                if (numero > 0) {
                    return numero;
                }
            } catch (NumberFormatException ignored) {
                // Se solicita el dato de nuevo con un mensaje claro.
            }
            System.out.println("Escribe un ID numérico mayor que cero.");
        }
    }

    private static LocalDateTime leerFechaHora() {
        while (true) {
            String valor = leerTexto("Fecha y hora (AAAA-MM-DD HH:MM): ");
            try {
                return LocalDateTime.parse(valor.replace(' ', 'T'));
            } catch (DateTimeParseException exception) {
                System.out.println("Formato inválido. Ejemplo: 2026-11-25 14:30.");
            }
        }
    }
}
