package pizza.restaurant.presantation.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pizza.restaurant.application.constants.Messages;
import pizza.restaurant.application.mapper.Response;
import pizza.restaurant.domain.entity.Menu;
import pizza.restaurant.domain.service.MenuService;
import pizza.restaurant.presantation.request.MenuRequest;
import pizza.restaurant.presantation.response.MenuResponse;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Menu Controller
 */
@RestController
@RequestMapping("/menus")
@Slf4j
public class MenuController {

    /**
     * MenuService menuService
     */
    private MenuService menuService;

    /**
     * ModelMapper instance
     */
    private final ModelMapper modelMapper;

    public MenuController(MenuService menuService, ModelMapper modelMapper) {
        this.menuService = menuService;
        this.modelMapper = modelMapper;
    }

    /**
     * List all menus
     *
     * @return ResponseEntity
     */
    @GetMapping("/list")
    public ResponseEntity<Object> list() {
        List<Menu> menus = this.menuService.list();
        Type targetListType = new TypeToken<List<MenuResponse>>() {
        }.getType();
        List<MenuResponse> menuResponseList = modelMapper.map(menus, targetListType);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.OK,
                menuResponseList,
                Messages.RECORDS_RECEIVED
        );

    }

    /**
     * Create a new menu item
     *
     * @param menuRequest MenuRepository
     * @return ResponseEntity
     */
    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody @Valid MenuRequest menuRequest) {
        Menu menu = modelMapper.map(menuRequest, Menu.class);
        menu = menuService.create(menu);
        MenuResponse menuResponse = modelMapper.map(menu, MenuResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.CREATED,
                menuResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    /**
     * Update Menu Item
     *
     * @param id          Long
     * @param menuRequest MenuRequest
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid MenuRequest menuRequest) {
        Menu menu = menuService.getById(id);

        if (menu == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        menu = modelMapper.map(menuRequest, Menu.class);
        menu = menuService.update(menu, id);
        MenuResponse menuResponse = modelMapper.map(menu, MenuResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.ACCEPTED,
                menuResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    /**
     * Fetch single menu item
     *
     * @param id Long
     * @return ResponseEntity
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        Menu menu = menuService.getById(id);

        if (menu == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        MenuResponse menuResponse = modelMapper.map(menu, MenuResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.ACCEPTED,
                menuResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Menu menu = menuService.getById(id);

        if (menu == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        try {
            menuService.delete(menu);
            return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, null, Messages.RECORD_DELETED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, null, Messages.SERVER_ERROR);
        }
    }
}
