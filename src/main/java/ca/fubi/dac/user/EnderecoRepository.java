package ca.fubi.dac.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
		Optional<Endereco> findById(Long id);
		List<Endereco> findByCep(String cep);
		List<Endereco> findByLogradouro(String logradouro);
		List<Endereco> findByNumero(String numero);
		List<Endereco> findByComplemento(String complemento);
		List<Endereco> findByBairro(String bairro);
		List<Endereco> findByCidade(String cidade);
		List<Endereco> findByEstado(String estado);
}
