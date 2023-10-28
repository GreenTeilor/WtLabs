package by.bsuir.servlets;

import com.kodgemisi.servlet_url_mapping.MappingServlet;
import com.kodgemisi.servlet_url_mapping.ServletUrl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/profile", "/profile/*"})
public class ProfileServlet extends MappingServlet {
    public ProfileServlet() {
        this.urlMappingRegistrar.
                get("/", this::open);
    }

    private void open(HttpServletRequest request, HttpServletResponse response, ServletUrl servletUrl) {

    }
}
