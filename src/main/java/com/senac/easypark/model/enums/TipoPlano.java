package com.senac.easypark.model.enums;

import org.springframework.cglib.core.Local;

import java.time.LocalTime;

public enum TipoPlano {
    INTEGRAL(LocalTime.of(8,0), LocalTime.of(22,0) ),
    MANHA(LocalTime.of(8, 0), LocalTime.of(12, 0)),
    TARDE(LocalTime.of(13, 0), LocalTime.of(18, 0)),
    NOITE(LocalTime.of(18, 0), LocalTime.of(22, 0));

    private final LocalTime horaInicio;
    private final LocalTime horaFim;

    TipoPlano(LocalTime horaInicio, LocalTime horaFim) {
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public boolean contemHorario(LocalTime horario) {
        return !horario.isBefore(horaInicio) && !horario.isAfter(horaFim);
    }
}