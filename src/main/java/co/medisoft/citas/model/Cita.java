package co.medisoft.citas.model;

import java.time.LocalDateTime;

/** Entidad que representa una cita médica de MediSoft. */
public record Cita(
        long id,
        String documentoPaciente,
        String nombrePaciente,
        String especialidad,
        String medico,
        LocalDateTime fechaHora,
        String estado) {
    public Cita(String documentoPaciente, String nombrePaciente, String especialidad,
                 String medico, LocalDateTime fechaHora, String estado) {
        this(0, documentoPaciente, nombrePaciente, especialidad, medico, fechaHora, estado);
    }

    @Override
    public String toString() {
        return "ID " + id + " | " + nombrePaciente + " (" + documentoPaciente + ") | "
                + especialidad + " | " + medico + " | " + fechaHora + " | " + estado;
    }
}
