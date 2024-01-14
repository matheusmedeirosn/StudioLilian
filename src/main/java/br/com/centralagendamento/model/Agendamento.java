package br.com.centralagendamento.model;

import br.com.centralagendamento.DTO.AgendamentoDTO;
import br.com.centralagendamento.model.Procedimento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String nomeCliente;
        private List<Procedimento> procedimentos;
    private LocalDate data;
    private LocalTime horario;
    private int valorTotal;

    public int calcularValorTotal() {
        int total = 0;
        for (Procedimento procedimento : procedimentos) {
            total += procedimento.getCusto();
        }
        return total;
    }

    public Agendamento(AgendamentoDTO dto) {
        this.nomeCliente = dto.nomeCliente();
        this.procedimentos = dto.procedimentos().stream()
                .map(Procedimento::valueOf)
                .collect(Collectors.toList());
        this.data = dto.data();
        this.horario = dto.horario();
    }


    @PrePersist
    @PreUpdate
    private void preSalvar() {
        this.valorTotal = calcularValorTotal();
    }
}
