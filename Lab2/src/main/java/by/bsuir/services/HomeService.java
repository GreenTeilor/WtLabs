package by.bsuir.services;

import by.bsuir.constants.ExceptionMessages;
import by.bsuir.constants.PagesPaths;
import by.bsuir.constants.RequestAttributesNames;
import by.bsuir.constants.SessionAttributesNames;
import by.bsuir.domain.PagingParams;
import by.bsuir.exceptions.ConnectionException;
import by.bsuir.exceptions.DbException;
import by.bsuir.exceptions.ServiceException;
import by.bsuir.repositories.CategoryRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.sql.SQLException;

@NoArgsConstructor
public class HomeService {
    private final CategoryRepository categoryRepository = new CategoryRepository();

    public void getHomePage(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
            throws DbException, ServiceException {
        try {
            RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(PagesPaths.PREFIX +
                    PagesPaths.HOME_PAGE + PagesPaths.POSTFIX);
            PagingParams categoryPaging = (PagingParams) request.getSession().
                    getAttribute(SessionAttributesNames.CATEGORY_PAGING_PARAMS);
            request.setAttribute(RequestAttributesNames.CATEGORIES, categoryRepository.getCategories(categoryPaging));
            requestDispatcher.forward(request, response);
        } catch (ConnectionException | SQLException e) {
            throw new DbException(ExceptionMessages.DB_EXCEPTION);
        } catch (ServletException | IOException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }

    public void paging(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
            throws DbException, ServiceException {
        try {
            RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(PagesPaths.PREFIX +
                    PagesPaths.HOME_PAGE + PagesPaths.POSTFIX);
            request.setAttribute(RequestAttributesNames.CATEGORIES, categoryRepository.getCategories(PagingUtils.
                    updatePagingParams(request, SessionAttributesNames.CATEGORY_PAGING_PARAMS)));
            requestDispatcher.forward(request, response);
        } catch (ConnectionException | SQLException e) {
            throw new DbException(ExceptionMessages.DB_EXCEPTION);
        } catch (ServletException | IOException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServiceException {
        try {
            request.getSession().setAttribute(SessionAttributesNames.USER, null);
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (IOException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }
}
