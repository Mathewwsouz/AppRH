package com.AppRh.AppRH.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.AppRh.AppRH.Model.Candidato;
import com.AppRh.AppRH.Model.Vaga;


public interface CandidatoRepository extends CrudRepository<Candidato, String>{
	Iterable<Candidato> findByVaga(Vaga vaga);
	
	Candidato findByRg(String rg);
	
	Candidato FindById(long id);
	
	List<Candidato> findByNome(String nomeCandidato);
	
}
