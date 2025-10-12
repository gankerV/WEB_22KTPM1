package com.example.APIDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.APIDemo.model.Language;

public interface ILanguageRepository extends JpaRepository<Language, Long> {

}
