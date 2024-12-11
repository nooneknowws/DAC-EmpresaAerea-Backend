package br.ufpr.dac.voos.services;

import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.models.Aeroporto;
import br.ufpr.dac.voos.models.ReservaTracking;
import br.ufpr.dac.voos.dto.UpdateVooDTO;
import br.ufpr.dac.voos.repository.VooRepository;
import br.ufpr.dac.voos.repository.AeroportoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class VooService {
    private static final Logger logger = LoggerFactory.getLogger(VooService.class);
    
    @Autowired
    private VooRepository vooRepository;

    @Autowired
    private AeroportoRepository aeroportoRepository;

    public List<Voo> findByOrigemAndDestino(String origemCodigo, String destinoCodigo) {
        return vooRepository.findByOrigemCodigoAndDestinoCodigo(origemCodigo, destinoCodigo);
    }

    public List<Voo> listarTodosVoos() {
        return vooRepository.findAllWithOrigemAndDestino();
    }
    
    public Voo listarUmVoo(Long id) {
        return vooRepository.findByIdWithOrigemAndDestino(id)
            .orElseThrow(() -> new EntityNotFoundException("Voo não encontrado: " + id));         
    }

    @Transactional
    public Voo inserirVoo(Voo voo, String origemCodigo, String destinoCodigo) {
        Aeroporto origem = aeroportoRepository.findByCodigo(origemCodigo)
            .orElseThrow(() -> new EntityNotFoundException("Aeroporto de origem não encontrado: " + origemCodigo));
        
        Aeroporto destino = aeroportoRepository.findByCodigo(destinoCodigo)
            .orElseThrow(() -> new EntityNotFoundException("Aeroporto de destino não encontrado: " + destinoCodigo));
        
        voo.setOrigem(origem);
        voo.setDestino(destino);

        return vooRepository.save(voo);
    }

    @Transactional
    public Voo editarVoo(Long id, UpdateVooDTO dto) {
        Voo vooExistente = vooRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Voo não encontrado: " + id));

        if (dto.getDataHoraPartida() != null) {
            vooExistente.setDataHoraPartida(dto.getDataHoraPartida());
        }
        if (dto.getCodigoOrigem() != null) {
            vooExistente.setOrigem(aeroportoRepository.findByCodigo(dto.getCodigoOrigem())
                .orElseThrow(() -> new EntityNotFoundException("Aeroporto de origem não encontrado: " + dto.getCodigoOrigem())));
        }
        if (dto.getCodigoDestino() != null) {
            vooExistente.setDestino(aeroportoRepository.findByCodigo(dto.getCodigoDestino())
                .orElseThrow(() -> new EntityNotFoundException("Aeroporto de destino não encontrado: " + dto.getCodigoDestino())));
        }
        if (dto.getValorPassagem() != null) {
            vooExistente.setValorPassagem(dto.getValorPassagem());
        }
        if (dto.getQuantidadeAssentos() != null) {
            vooExistente.setQuantidadeAssentos(dto.getQuantidadeAssentos());
        }
        if (dto.getQuantidadePassageiros() != null) {
            vooExistente.setQuantidadePassageiros(dto.getQuantidadePassageiros());
        }
        if (dto.getStatus() != null) {
            validarStatus(dto.getStatus());
            vooExistente.setStatus(dto.getStatus());
        }

        return vooRepository.save(vooExistente);
    }

    @Transactional
    public void atualizarStatus(Long id, String status) {
        Voo voo = vooRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Voo não encontrado: " + id));

        validarStatus(status);
        voo.setStatus(status);
        vooRepository.save(voo);
    }

    private void validarStatus(String status) {
        if (!status.equals("CONFIRMADO") && !status.equals("REALIZADO") && !status.equals("CANCELADO")) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }

   
    @Transactional
    public boolean adicionarReservaTracking(Long vooId, ReservaTracking tracking) {
        try {
            Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new EntityNotFoundException("Voo não encontrado: " + vooId));

            if (!voo.hasAvailableSeats(tracking.getQuantidade())) {
                logger.error("Não há assentos suficientes disponíveis no voo {}", vooId);
                return false;
            }

            voo.getReservasTracking().add(tracking);
            voo.updatePassengerCount();
            
            vooRepository.save(voo);
            
            logger.info("Successfully added reservation tracking for voo {} with {} seats", 
                       vooId, tracking.getQuantidade());
            return true;

        } catch (Exception e) {
            logger.error("Error adding reservation tracking to voo {}: {}", vooId, e.getMessage());
            return false;
        }
    }

    @Transactional
    public boolean updateReservaStatus(Long vooId, Long reservaId, String newStatus) {
        try {
            Voo voo = vooRepository.findById(vooId)
                .orElseThrow(() -> new EntityNotFoundException("Voo não encontrado: " + vooId));

            ReservaTracking tracking = voo.getReservasTracking().stream()
                .filter(r -> r.getReservaId().equals(reservaId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Reserva tracking não encontrado"));

            tracking.setStatus(newStatus);
            voo.updatePassengerCount();
            
            vooRepository.save(voo);
            return true;

        } catch (Exception e) {
            logger.error("Error updating reservation status for voo {} reserva {}: {}", 
                        vooId, reservaId, e.getMessage());
            return false;
        }
    }
}