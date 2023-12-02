package by.bsuir.springbootproject.services.impl;

import by.bsuir.springbootproject.constants.PagesPaths;
import by.bsuir.springbootproject.constants.RequestAttributesNames;
import by.bsuir.springbootproject.constants.Values;
import by.bsuir.springbootproject.entities.Category;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.exceptions.NoResourceFoundException;
import by.bsuir.springbootproject.repositories.CategoryRepository;
import by.bsuir.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ModelAndView create(Category category) {
        categoryRepository.create(category);
        return new ModelAndView(PagesPaths.CATEGORY_PAGE);
    }

    @Override
    public ModelAndView read(PagingParams params) {
        if (params.getPageNumber() < 0) {
            params.setPageNumber(Values.DEFAULT_START_PAGE);
        }
        ModelAndView modelAndView = new ModelAndView(PagesPaths.HOME_PAGE);
        modelAndView.addObject(RequestAttributesNames.CATEGORIES, categoryRepository.read(params));
        return modelAndView;
    }

    @Override
    @Transactional
    public Category update(Category category) throws NoResourceFoundException {
        categoryRepository.findById(category.getId()).orElseThrow(() ->
                new NoResourceFoundException("No category with id " + category.getId() + " found"));
        return categoryRepository.create(category);
    }

    @Override
    @Transactional
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
