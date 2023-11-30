package by.bsuir.springbootproject.services.implementation;

import by.bsuir.springbootproject.constants.PagesPaths;
import by.bsuir.springbootproject.constants.RequestAttributesNames;
import by.bsuir.springbootproject.constants.Values;
import by.bsuir.springbootproject.entities.Category;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.exceptions.NoResourceFoundException;
import by.bsuir.springbootproject.repositories.CategoryRepository;
import by.bsuir.springbootproject.services.CategoryService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public void saveToFile(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<Category> beanToCsv = new StatefulBeanToCsvBuilder<Category>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "categories.csv");
            List<Category> categories = categoryRepository.read(new PagingParams(1, 1000_000_000));
            categories.forEach(c -> c.setId(null));
            beanToCsv.write(categories);
        }
    }

    @Override
    @Transactional
    public ModelAndView loadFromFile(MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView("redirect:/" + PagesPaths.HOME_PAGE);
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<Category> csvToBean = new CsvToBeanBuilder<Category>(reader)
                    .withType(Category.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator('~')
                    .build();
            List<Category> categories = new ArrayList<>();
            csvToBean.forEach(categories::add);
            categories.forEach(categoryRepository::create);
            return modelAndView;
        }
    }

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
