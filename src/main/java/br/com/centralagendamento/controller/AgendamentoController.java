package br.com.centralagendamento.controller;

import br.com.centralagendamento.DTO.AgendamentoDTO;
import br.com.centralagendamento.model.Agendamento;
import br.com.centralagendamento.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://studiolilianfront-production.up.railway.app")
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    AgendamentoService agendamentoService;

    // Criar Agendamento
    @PostMapping
    public ResponseEntity<Agendamento> criarAgendamento(@RequestBody AgendamentoDTO agendamento) {
        Agendamento agendamento1 = new Agendamento(agendamento);
        if (agendamentoService.isHorarioDisponivel(agendamento.data(), agendamento.horario())) {
            Agendamento novoAgendamento = agendamentoService.agendar(agendamento1);
            return ResponseEntity.ok(novoAgendamento);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Listar Agendamentos
    @GetMapping
    public ResponseEntity<List<Agendamento>> listarAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.listarTodos();
        return ResponseEntity.ok(agendamentos);
    }

    // Obter Detalhes do Agendamento
    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> obterAgendamento(@PathVariable Long id) {
        Agendamento agendamento = agendamentoService.obterPorId(id);
        if (agendamento != null) {
            return ResponseEntity.ok(agendamento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Atualizar Agendamento
    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> editarAgendamento(@PathVariable Long id, @RequestBody AgendamentoDTO novoAgendamentoDTO) {
        // Chame o serviço para editar o agendamento
        Agendamento agendamentoEditado = agendamentoService.editarAgendamento(id, novoAgendamentoDTO);

        if (agendamentoEditado != null) {
            return ResponseEntity.ok(agendamentoEditado); // Retorna o agendamento editado se a operação for bem-sucedida
        } else {
            return ResponseEntity.notFound().build(); // Retorna status 404 se o agendamento não for encontrado
        }
    }



    // Cancelar/Deletar Agendamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAgendamento(@PathVariable Long id) {
        if (agendamentoService.deletar(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Verificar Disponibilidade
    @GetMapping("/disponibilidade")
    public ResponseEntity<Boolean> verificarDisponibilidade(@RequestParam LocalDate data, @RequestParam LocalTime horario) {
        boolean disponivel = agendamentoService.isHorarioDisponivel(data, horario);
        return ResponseEntity.ok(disponivel);
    }

    // Agendamentos do Cliente (supondo que você tem um identificador para o cliente)
    @GetMapping("/cliente/{nomeCliente}")
    public ResponseEntity<List<Agendamento>> obterAgendamentosDoCliente(@PathVariable String nomeCliente) {
        List<Agendamento> agendamentos = agendamentoService.obterPorClienteId(nomeCliente);
        return ResponseEntity.ok(agendamentos);
    }

}
