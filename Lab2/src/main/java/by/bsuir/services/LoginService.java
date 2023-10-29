package by.bsuir.services;

import by.bsuir.constants.ExceptionMessages;
import by.bsuir.constants.Messages;
import by.bsuir.constants.PagesPaths;
import by.bsuir.constants.RequestAttributesNames;
import by.bsuir.constants.RequestParams;
import by.bsuir.constants.SessionAttributesNames;
import by.bsuir.domain.User;
import by.bsuir.exceptions.ConnectionException;
import by.bsuir.exceptions.DbException;
import by.bsuir.exceptions.ServiceException;
import by.bsuir.repositories.UserRepository;
import by.bsuir.utils.HashUtils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginService {
    private final UserRepository userRepository = new UserRepository();

    public void getLoginPage(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
            throws ServiceException {
        try {
            RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(PagesPaths.PREFIX +
                    PagesPaths.LOGIN_PAGE + PagesPaths.POSTFIX);
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
            throws DbException, ServiceException {
        try {
            RequestDispatcher requestDispatcher = servlet.getServletContext().getRequestDispatcher(PagesPaths.PREFIX +
                    PagesPaths.LOGIN_PAGE + PagesPaths.POSTFIX);
            Optional<User> user = userRepository.getByCredentials(request.getParameter(RequestParams.EMAIL),
                    HashUtils.getHash(request.getParameter(RequestParams.PASSWORD)));
            if (user.isPresent()) {
                request.getSession().setAttribute(SessionAttributesNames.USER, user.get());
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute(RequestAttributesNames.STATUS, Messages.INVALID_CREDENTIALS);
                requestDispatcher.forward(request, response);
            }
        } catch (ConnectionException | SQLException e) {
            throw new DbException(ExceptionMessages.DB_EXCEPTION);
        } catch (IOException | ServletException e) {
            throw new ServiceException(ExceptionMessages.SERVICE_EXCEPTION);
        }
    }
}
