package br.com.centralagendamento.repository;

import br.com.centralagendamento.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByNomeCliente(String nomeCliente);


}
