package br.com.centralagendamento.DTO;

import br.com.centralagendamento.model.Procedimento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoDTO(String nomeCliente,
                             List<String> procedimentos, // Lista de strings
                             LocalDate data,
                             LocalTime horario) {}
