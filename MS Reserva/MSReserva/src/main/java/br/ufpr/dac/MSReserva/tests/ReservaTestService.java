package br.ufpr.dac.MSReserva.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.time.LocalDateTime;

import br.ufpr.dac.MSReserva.model.Reserva;
import br.ufpr.dac.MSReserva.model.StatusReserva;
import br.ufpr.dac.MSReserva.cqrs.command.ReservaCommandRepository;
import br.ufpr.dac.MSReserva.cqrs.query.ReservaQueryRepository;

@Service
public class ReservaTestService {
    @Autowired
    private ReservaCommandRepository commandRepo;
    
    @Autowired
    private ReservaQueryRepository queryRepo;
    
    @Transactional
    public void testRepositories() {
        System.out.println("Creating test reservation...");
        
        // Create and save through command
        Reserva reserva = new Reserva();
        reserva.setDataHora(LocalDateTime.now());
        reserva.setAeroportoOrigemCod("GRU");
        reserva.setAeroportoDestinoCod("CWB");
        reserva.setValor(369.0);
        reserva.setMilhas(0);
        reserva.setStatus(StatusReserva.PENDENTE);
        reserva.setVooId(8L);
        reserva.setClienteId(5L);
        
        Reserva saved = commandRepo.save(reserva);
        System.out.println("Saved through command: ID=" + saved.getId() + ", ClienteID=" + saved.getClienteId());
        
        // Immediately try to read through query
        List<Reserva> found = queryRepo.findByClienteId(saved.getClienteId());
        System.out.println("Found through query: " + found.size() + " reservations");
        
        if (!found.isEmpty()) {
            System.out.println("First found reservation - ID: " + found.get(0).getId() + ", ClienteID: " + found.get(0).getClienteId());
        }
        
        // Also try to find all reservations
        List<Reserva> all = queryRepo.findAll();
        System.out.println("Total reservations in database: " + all.size());
    }
}