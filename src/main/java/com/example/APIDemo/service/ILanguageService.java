package com.example.APIDemo.service;

import com.example.APIDemo.model.Language;
import java.util.List;

public interface ILanguageService {
    List<Language> getAllLanguages();
    Language getLanguageById(Long id);
    Language createLanguage(Language language);
    Language updateLanguage(Long id, Language language);
    boolean deleteLanguage(Long id);
}
