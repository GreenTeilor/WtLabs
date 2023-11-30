package by.bsuir.springbootproject.services.implementation;

import by.bsuir.springbootproject.constants.PagesPaths;
import by.bsuir.springbootproject.constants.RequestAttributesNames;
import by.bsuir.springbootproject.csv.OrderProductCsv;
import by.bsuir.springbootproject.csv.converters.OrdersProductsConverter;
import by.bsuir.springbootproject.entities.Cart;
import by.bsuir.springbootproject.entities.Order;
import by.bsuir.springbootproject.entities.PagingParams;
import by.bsuir.springbootproject.entities.Statistics;
import by.bsuir.springbootproject.entities.User;
import by.bsuir.springbootproject.exceptions.InsufficientFundsException;
import by.bsuir.springbootproject.exceptions.NoProductsInOrderException;
import by.bsuir.springbootproject.exceptions.NoResourceFoundException;
import by.bsuir.springbootproject.exceptions.UserAlreadyExistsException;
import by.bsuir.springbootproject.repositories.OrderRepository;
import by.bsuir.springbootproject.repositories.UserRepository;
import by.bsuir.springbootproject.services.UserService;
import by.bsuir.springbootproject.utils.ErrorPopulatorUtils;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrdersProductsConverter ordersProductsConverter;
    private final OrderRepository orderRepository;
    private final PasswordEncoder encoder;

    @Override
    public ModelAndView processLoginPage(String error) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.LOGIN_PAGE);
        if (error != null) {
            modelAndView.addObject("status", "Неверный логин или пароль");
        }
        return modelAndView;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public static ModelAndView makeModelAndView(User user, Statistics statistics, List<Order> orders) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.PROFILE_PAGE);
        modelAndView.addObject(RequestAttributesNames.USER, user);
        modelAndView.addObject(RequestAttributesNames.STATISTICS, statistics);
        modelAndView.addObject(RequestAttributesNames.ORDERS, orders);
        return modelAndView;
    }

    @Override
    public ModelAndView getUserInfo(User user, PagingParams params) {
        List<Order> orders = orderRepository.findAllByUserId(user.getId(), params);
        Statistics statistics = getUserStatistics(user.getId());
        return makeModelAndView(user, statistics, orders);
    }

    @Override
    @Transactional
    public ModelAndView addAddressAndPhoneNumberInfo(String address, String phoneNumber, User user, BindingResult bindingResult, PagingParams params) {
        Statistics statistics = getUserStatistics(user.getId());
        List<Order> orders = orderRepository.findAllByUserId(user.getId(), params);
        if (!bindingResult.hasFieldErrors(RequestAttributesNames.ADDRESS) && !bindingResult.hasFieldErrors(RequestAttributesNames.PHONE_NUMBER)) {
            user.setAddress(address);
            user.setPhoneNumber(phoneNumber);
            userRepository.update(user);
        }
        ModelAndView modelAndView = makeModelAndView(user, statistics, orders);
        ErrorPopulatorUtils.populateError(RequestAttributesNames.ADDRESS, modelAndView, bindingResult);
        ErrorPopulatorUtils.populateError(RequestAttributesNames.PHONE_NUMBER, modelAndView, bindingResult);
        return modelAndView;
    }

    @Override
    public Statistics getUserStatistics(int id) {
        String favoriteCategory = userRepository.getUserFavoriteCategory(id);
        return Statistics.builder().userId(id).daysRegistered(userRepository.getUserDaysRegistered(id)).
                orderCount(userRepository.getUserOrdersCount(id)).
                booksCount(userRepository.getUserPurchasedBooksCount(id)).
                favoriteCategory(favoriteCategory == null ? "Неизвестен" : favoriteCategory).build();
    }

    @Override
    @Transactional
    public ModelAndView makeOrder(User user, Cart cart) throws InsufficientFundsException, NoProductsInOrderException, NoResourceFoundException {
        BigDecimal orderPrice = cart.getPrice();
        if (userRepository.getUserById(user.getId()).map(User::getBalance).
                orElseThrow(() -> new NoResourceFoundException("No user with id " + user.getId() + " found")).
                compareTo(orderPrice) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }
        if (cart.isEmpty()) {
            throw new NoProductsInOrderException("Корзина пуста");
        }
        Order order = Order.builder().userId(user.getId()).date(LocalDate.now()).products(new ArrayList<>(cart.getProducts())).price(orderPrice).build();
        user.getOrders().add(order);
        user.setBalance(user.getBalance().subtract(orderPrice));
        userRepository.update(user);
        cart.clear();
        ModelAndView modelAndView = new ModelAndView(PagesPaths.CART_PAGE);
        modelAndView.addObject(RequestAttributesNames.STATUS, "Заказ оформлен!");
        modelAndView.addObject(RequestAttributesNames.COLOR, "green");
        return modelAndView;
    }

    @Override
    public void saveOrdersToFile(int userId, HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        try (Writer writer = new OutputStreamWriter(response.getOutputStream())) {
            StatefulBeanToCsv<OrderProductCsv> beanToCsv = new StatefulBeanToCsvBuilder<OrderProductCsv>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator('~')
                    .build();
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=" + "orders_products.csv");
            List<Order> orders = orderRepository.findAllByUserId(userId, new PagingParams(1, 1000_000_000));
            List<OrderProductCsv> productCsvs = ordersProductsConverter.fromOrders(orders);
            beanToCsv.write(productCsvs);
        }
    }

    @Override
    @Transactional
    public ModelAndView loadOrdersFromFile(User user, MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView("redirect:/profile");
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<OrderProductCsv> csvToBean = new CsvToBeanBuilder<OrderProductCsv>(reader)
                    .withType(OrderProductCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator('~')
                    .build();
            List<OrderProductCsv> orderProductCsvs = new ArrayList<>();
            csvToBean.forEach(orderProductCsvs::add);
            List<Order> orders = ordersProductsConverter.toOrders(orderProductCsvs);
            orders.forEach(user.getOrders()::add);
            userRepository.update(user);
            return modelAndView;
        }
    }

    @Override
    @Transactional
    public ModelAndView create(User user) throws UserAlreadyExistsException {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.REGISTER_PAGE);
        user.setBalance(BigDecimal.valueOf(0.0));
        user.setRegistrationDate(LocalDate.now());
        user.setPassword(encoder.encode(user.getPassword()));
        if (getUserByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Такой пользователь уже существует");
        }
        userRepository.create(user);
        modelAndView.addObject(RequestAttributesNames.STATUS, "Успешно");
        modelAndView.addObject(RequestAttributesNames.COLOR, "green");
        return modelAndView;
    }

    @Override
    public ModelAndView read(PagingParams params) {
        ModelAndView modelAndView = new ModelAndView(PagesPaths.HOME_PAGE);
        modelAndView.addObject(RequestAttributesNames.USER, userRepository.read(params));
        return modelAndView;
    }

    @Override
    @Transactional
    public User update(User user) throws NoResourceFoundException {
        userRepository.getUserById(user.getId()).orElseThrow(() ->
                new NoResourceFoundException("No user with id " + user.getId() + " found"));
        return userRepository.update(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        userRepository.delete(id);
    }
}
