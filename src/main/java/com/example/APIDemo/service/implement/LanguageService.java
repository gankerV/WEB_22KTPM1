package com.example.APIDemo.service.implement;

import com.example.APIDemo.model.Language;
import com.example.APIDemo.repository.ILanguageRepository;
import com.example.APIDemo.service.ILanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LanguageService implements ILanguageService {

    @Autowired
    ILanguageRepository languageRepository;

    @Override
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    @Override
    public Language getLanguageById(Long id) {
        return languageRepository.findById(id).orElse(null);
    }

    @Override
    public Language createLanguage(Language language) {
        return languageRepository.save(language);
    }

    @Override
    public Language updateLanguage(Long id, Language language) {
        Language existing = languageRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(language.getName());
            existing.setLastUpdate(language.getLastUpdate());
            return languageRepository.save(existing);
        }
        return null;
    }

    @Override
    public boolean deleteLanguage(Long id) {
        if (languageRepository.existsById(id)) {
            languageRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
