package br.com.centralagendamento.service;

import br.com.centralagendamento.DTO.AgendamentoDTO;
import br.com.centralagendamento.model.Agendamento;
import br.com.centralagendamento.model.Procedimento;
import br.com.centralagendamento.repository.AgendamentoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Agendamento editarAgendamento(Long id, AgendamentoDTO novoAgendamentoDTO) {
        // Passo 1: Verificar se o agendamento com o ID existe
        Agendamento agendamentoExistente = repository.findById(id).orElse(null);

        if (agendamentoExistente == null) {
            return null; // Agendamento não encontrado, você pode retornar null ou lançar uma exceção adequada.
        }

        // Passo 2: Atualizar os campos do agendamento existente com os dados do DTO
        // Você pode fazer isso de maneira seletiva, apenas atualizando os campos que são fornecidos no DTO.
        agendamentoExistente.setNomeCliente(novoAgendamentoDTO.nomeCliente());
        agendamentoExistente.setData(novoAgendamentoDTO.data());
        agendamentoExistente.setHorario(novoAgendamentoDTO.horario());
        List<Procedimento> procedimentos = converterNomesParaProcedimentos(novoAgendamentoDTO.procedimentos());
        agendamentoExistente.setProcedimentos(procedimentos);
        // Atualize outros campos conforme necessário, como procedimentos, valorTotal, etc.

        // Passo 3: Salvar o agendamento atualizado no banco de dados
        Agendamento agendamentoEditado = repository.save(agendamentoExistente);

        return agendamentoEditado;
    }

    private List<Procedimento> converterNomesParaProcedimentos(List<String> nomesProcedimentos) {
        List<Procedimento> procedimentos = new ArrayList<>();

        for (String nome : nomesProcedimentos) {
            // Tente encontrar o procedimento pelo nome
            Optional<Procedimento> procedimentoOptional = Arrays.stream(Procedimento.values())
                    .filter(procedimento -> procedimento.name().equalsIgnoreCase(nome))
                    .findFirst();

            if (procedimentoOptional.isPresent()) {
                procedimentos.add(procedimentoOptional.get());
            }
            // Lide com cenários onde o nome não corresponde a um procedimento existente
        }

        return procedimentos;
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
