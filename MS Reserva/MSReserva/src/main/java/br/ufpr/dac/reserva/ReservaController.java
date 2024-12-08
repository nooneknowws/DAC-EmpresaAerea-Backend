package br.ufpr.dac.reserva;

import br.ufpr.dac.reserva.cqrs.command.ReservaCommandService;
import br.ufpr.dac.reserva.cqrs.query.ReservaQueryService;
import br.ufpr.dac.reserva.dto.CriarReservaDTO;
import br.ufpr.dac.reserva.dto.ReservaDTO;
import br.ufpr.dac.reserva.model.Reserva;
import br.ufpr.dac.reserva.model.StatusReserva;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaCommandService commandService;

    @Autowired
    private ReservaQueryService queryService;

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody CriarReservaDTO reservaDTO) {
        Reserva reserva = commandService.criarReserva(reservaDTO);
        return ResponseEntity.ok(reserva);
    }
    
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarTodasReservas() {
        List<ReservaDTO> reservas = queryService.listarTodasReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> consultarReserva(@PathVariable Long id) {
        Optional<ReservaDTO> reserva = queryService.consultarReserva(id);
        return reserva.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Long id) {
        Reserva reserva = commandService.atualizarStatusReserva(id, StatusReserva.CANCELADO);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}/checkin")
    public ResponseEntity<Reserva> realizarCheckIn(@PathVariable Long id) {
        Reserva reserva = commandService.atualizarStatusReserva(id, StatusReserva.EMBARCADO);
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorCliente(@PathVariable Long clienteId) {
        List<ReservaDTO> reservas = queryService.listarReservasPorCliente(clienteId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/voo/{vooId}")
    public ResponseEntity<List<ReservaDTO>> listarReservasPorVoo(@PathVariable Long vooId) {
        List<ReservaDTO> reservas = queryService.listarReservasPorVoo(vooId);
        return ResponseEntity.ok(reservas);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<Reserva> confirmarReserva(@PathVariable Long id) {
        Reserva reserva = commandService.atualizarStatusReserva(id, StatusReserva.CONFIRMADO);
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Reserva> finalizarReserva(@PathVariable Long id) {
        Reserva reserva = commandService.atualizarStatusReserva(id, StatusReserva.FINALIZADO);
        return ResponseEntity.ok(reserva);
    }
}
