package com.reaksa.e_wingshop_api.service;

import com.reaksa.e_wingshop_api.entity.Category;
import com.reaksa.e_wingshop_api.exception.DuplicateResourceException;
import com.reaksa.e_wingshop_api.exception.ResourceNotFoundException;
import com.reaksa.e_wingshop_api.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Transactional
    public Category create(String name, String description) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Category already exists: " + name);
        }
        return categoryRepository.save(
                Category.builder().name(name).description(description).build());
    }

    @Transactional
    public Category update(Long id, String name, String description) {
        Category cat = findById(id);
        cat.setName(name);
        cat.setDescription(description);
        return categoryRepository.save(cat);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(findById(id));
    }
}
