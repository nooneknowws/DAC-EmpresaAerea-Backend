package br.ufpr.dac.voos.services;

import br.ufpr.dac.voos.models.Voo;
import br.ufpr.dac.voos.dto.UpdateVooDTO;
import br.ufpr.dac.voos.models.Aeroporto;
import br.ufpr.dac.voos.repository.VooRepository;
import br.ufpr.dac.voos.repository.AeroportoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VooService {
    
        @Autowired
        private VooRepository vooRepository;

        @Autowired
        private AeroportoRepository aeroportoRepository;


        // Get all
        public List<Voo> listarTodosVoos() {
            return vooRepository.findAllWithOrigemAndDestino();
        }
        
        // Get one
        public Voo listarUmVoo(Long id) {
            return vooRepository.findByIdWithOrigemAndDestino(id).orElse(null);         
        }
    
        // Post
        public Voo inserirVoo(Voo voo, String origemCodigo, String destinoCodigo) {
            Aeroporto origem = aeroportoRepository.findByCodigo(origemCodigo)
                .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado: " + origemCodigo));
        
        Aeroporto destino = aeroportoRepository.findByCodigo(destinoCodigo)
                .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado: " + destinoCodigo));
        
        voo.setOrigem(origem);
        voo.setDestino(destino);

        return vooRepository.save(voo);
        }
    
        // Put
        public Voo editarVoo(Long id, UpdateVooDTO dto) {
            Voo vooExistente = vooRepository.findById(id).orElseThrow(() -> new RuntimeException("Voo não encontrado!"));
    
            if (dto.getDataHoraPartida() != null) {
                vooExistente.setDataHoraPartida(dto.getDataHoraPartida());
            }
            if (dto.getCodigoOrigem() != null) {
                vooExistente.setOrigem(aeroportoRepository.findByCodigo(dto.getCodigoOrigem())
                    .orElseThrow(() -> new RuntimeException("Aeroporto de origem não encontrado!")));
            }
            if (dto.getCodigoDestino() != null) {
                vooExistente.setDestino(aeroportoRepository.findByCodigo(dto.getCodigoDestino())
                    .orElseThrow(() -> new RuntimeException("Aeroporto de destino não encontrado!")));
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
                vooExistente.setStatus(dto.getStatus());
            }
    
            return vooRepository.save(vooExistente);
        }
    
        // Delete
        public void deletarVoo(Long id) {
            if (vooRepository.existsById(id)) {
                vooRepository.deleteById(id);
            } else {
                throw new RuntimeException("Voo não encontrado com o ID: " + id);
            }
        }
    
}
