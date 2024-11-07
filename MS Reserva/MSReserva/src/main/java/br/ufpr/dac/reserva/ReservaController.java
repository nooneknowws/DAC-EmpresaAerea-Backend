package br.ufpr.dac.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
	@Autowired
	private ReservaService reservaService;

	@GetMapping("/{id}")
	public ReservaDTO getReserva(@PathVariable Long id) throws Exception {
		Reserva reserva = reservaService.findById(id);
		return reservaService.toDTO(reserva);
	}

	@PostMapping
	public ReservaDTO createReserva(@RequestBody ReservaDTO reservaDTO) {
		Reserva reserva = reservaService.toEntity(reservaDTO);
		Reserva savedReserva = reservaService.save(reserva);
		return reservaService.toDTO(savedReserva);
	}
}
