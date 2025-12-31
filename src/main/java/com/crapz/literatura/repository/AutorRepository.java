package com.crapz.literatura.repository;

import com.crapz.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :ano AND a.fechaDeFallecimiento >= :ano")
    List<Autor> autoresVivosEnDeterminadoAno(Integer ano);

    Optional<Autor> findByNombreContainingIgnoreCase(String nombre);
}
