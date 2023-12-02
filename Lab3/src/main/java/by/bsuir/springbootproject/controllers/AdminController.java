package by.bsuir.springbootproject.controllers;

import by.bsuir.springbootproject.constants.PagesPaths;
import by.bsuir.springbootproject.constants.RequestAttributesNames;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;

    @GetMapping
    public ModelAndView openAdminPage() {
        ModelAndView modelAndView = categoryService.read(new PagingParams(1, 1000_000_000));
        modelAndView.setViewName(PagesPaths.ADMIN_PAGE);
        return modelAndView;
    }

    @PostMapping("/addCategory")
    public ModelAndView addCategory(@RequestParam(RequestAttributesNames.CATEGORY_NAME) String categoryName) {
        return new ModelAndView();
    }

    @PostMapping("addProduct")
    public ModelAndView addProduct(@RequestParam(RequestAttributesNames.PRODUCT_NAME) String productName) {
        return new ModelAndView();
    }
}