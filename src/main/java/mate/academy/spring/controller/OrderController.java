package mate.academy.spring.controller;

import java.util.List;
import java.util.stream.Collectors;
import mate.academy.spring.mapper.DtoResponseMapper;
import mate.academy.spring.model.Order;
import mate.academy.spring.model.ShoppingCart;
import mate.academy.spring.model.dto.response.OrderResponseDto;
import mate.academy.spring.service.OrderService;
import mate.academy.spring.service.ShoppingCartService;
import mate.academy.spring.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartService shoppingCartService;
    private final DtoResponseMapper<OrderResponseDto, Order> orderDtoResponseMapper;
    private final UserService userService;

    public OrderController(OrderService orderService, ShoppingCartService shoppingCartService,
                           DtoResponseMapper<OrderResponseDto, Order> orderDtoResponseMapper,
                           UserService userService) {
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.orderDtoResponseMapper = orderDtoResponseMapper;
        this.userService = userService;
    }

    @PostMapping("/complete")
    public OrderResponseDto add(@RequestParam Long userId) {
        ShoppingCart shoppingCart = shoppingCartService.getByUser(userService.get(userId));
        Order order = orderService.completeOrder(shoppingCart);
        return orderDtoResponseMapper.toDto(order);
    }

    @GetMapping
    public List<OrderResponseDto> getAll(@RequestParam Long userId) {
        return orderService.getOrdersHistory(userService.get(userId))
                .stream()
                .map(orderDtoResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
