package com.vet.VetSystemRework.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vet.VetSystemRework.domain.Perfil;

public interface PerfilRepository extends JpaRepository<Perfil, Long>{


}
