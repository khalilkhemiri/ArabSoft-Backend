package com.basicauth.app.repository;

import com.basicauth.app.entity.Conge;
import com.basicauth.app.entity.DocumentAdministratif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<DocumentAdministratif,Long> {
}
