package br.com.centralagendamento.service;

import br.com.centralagendamento.model.Agendamento;
import br.com.centralagendamento.repository.AgendamentoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final EntityManager entityManager;

    @Autowired
    public AgendamentoService(AgendamentoRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public Agendamento agendar(Agendamento agenda){
        Agendamento agendamento = new Agendamento();
        return repository.save(agenda);
    }

    public boolean isHorarioDisponivel(LocalDate data, LocalTime horario) {
        try {
            Agendamento agendamentoExistente = (Agendamento) entityManager.createQuery(
                            "SELECT a FROM Agendamento a WHERE a.data = :data AND a.horario = :horario")
                    .setParameter("data", data)
                    .setParameter("horario", horario)
                    .getSingleResult();

            return agendamentoExistente == null;

        } catch (NoResultException e) {
            return true;
        }
    }

    public List<Agendamento> listarTodos() {
        return repository.findAll();
    }

    public Agendamento obterPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Agendamento atualizar(Long id, Agendamento agendamentoAtualizado) {
        Optional<Agendamento> agendamentoExistente = repository.findById(id);

        if (agendamentoExistente.isPresent()) {
            Agendamento agendamentoParaAtualizar = agendamentoExistente.get();

            // Atualize apenas os campos permitidos
            agendamentoParaAtualizar.setNomeCliente(agendamentoAtualizado.getNomeCliente());
            agendamentoParaAtualizar.setProcedimentos(agendamentoAtualizado.getProcedimentos());
            agendamentoParaAtualizar.setData(agendamentoAtualizado.getData());
            agendamentoParaAtualizar.setHorario(agendamentoAtualizado.getHorario());

            // Salve a entidade atualizada no banco de dados
            return repository.save(agendamentoParaAtualizar);
        }
        return null;
    }


    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Agendamento> obterPorClienteId(String nomeCliente) {
        return repository.findByNomeCliente(nomeCliente); // Assumindo que você tem um método correspondente no repositório
    }


}
