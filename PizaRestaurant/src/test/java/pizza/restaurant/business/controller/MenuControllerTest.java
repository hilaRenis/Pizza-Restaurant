package pizza.restaurant.business.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pizza.restaurant.domain.entity.Menu;
import pizza.restaurant.domain.service.MenuService;
import pizza.restaurant.presantation.controller.MenuController;
import pizza.restaurant.presantation.request.MenuRequest;
import pizza.restaurant.presantation.response.MenuResponse;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    @Mock
    private MenuService menuService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MenuController menuController;

    @Test
    void testListMenus() {
        // Setup
        List<Menu> menus = Arrays.asList(new Menu(), new Menu()); // Assuming Menu is a proper entity
        when(menuService.list()).thenReturn(menus);

        Type targetListType = new TypeToken<List<MenuResponse>>() {
        }.getType();
        List<MenuResponse> menuResponses = Arrays.asList(new MenuResponse(), new MenuResponse());
        when(modelMapper.map(menus, targetListType)).thenReturn(menuResponses);

        // Execute
        ResponseEntity<Object> response = menuController.list();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateMenuItem() {
        // Setup
        MenuRequest menuRequest = new MenuRequest();
        menuRequest.setArticle("Prod 1");
        menuRequest.setPrice(10);
        menuRequest.setDescription("test");

        Menu menu = new Menu(); // Assuming a proper entity
        when(modelMapper.map(menuRequest, Menu.class)).thenReturn(menu);
        when(menuService.create(menu)).thenReturn(menu);

        MenuResponse menuResponse = new MenuResponse();
        when(modelMapper.map(menu, MenuResponse.class)).thenReturn(menuResponse);

        // Execute
        ResponseEntity<Object> response = menuController.create(menuRequest);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void testUpdateMenuItemFound() {
        // Setup
        Long id = 1L;
        MenuRequest menuRequest = new MenuRequest(); // Populate with test data
        Menu menu = new Menu(); // Assuming a proper entity
        when(menuService.getById(id)).thenReturn(menu);

        when(modelMapper.map(menuRequest, Menu.class)).thenReturn(menu);
        when(menuService.update(menu, id)).thenReturn(menu);

        MenuResponse menuResponse = new MenuResponse();
        when(modelMapper.map(menu, MenuResponse.class)).thenReturn(menuResponse);

        // Execute
        ResponseEntity<Object> response = menuController.update(id, menuRequest);

        // Verify
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    void testUpdateMenuItemNotFound() {
        // Setup
        Long id = 1L;
        MenuRequest menuRequest = new MenuRequest(); // Populate with test data
        when(menuService.getById(id)).thenReturn(null);

        // Execute
        ResponseEntity<Object> response = menuController.update(id, menuRequest);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetMenuItemFound() {
        // Setup
        Long id = 1L;
        Menu menu = new Menu(); // Assuming a proper entity
        when(menuService.getById(id)).thenReturn(menu);

        MenuResponse menuResponse = new MenuResponse();
        when(modelMapper.map(menu, MenuResponse.class)).thenReturn(menuResponse);

        // Execute
        ResponseEntity<Object> response = menuController.get(id);

        // Verify
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        // Further assertions to check the response body
    }

    @Test
    void testGetMenuItemNotFound() {
        // Setup
        Long id = 1L;
        when(menuService.getById(id)).thenReturn(null);

        // Execute
        ResponseEntity<Object> response = menuController.get(id);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteMenuItemFound() {
        // Setup
        Long id = 1L;
        Menu menu = new Menu(); // Assuming a proper entity
        when(menuService.getById(id)).thenReturn(menu);

        doNothing().when(menuService).delete(menu);

        // Execute
        ResponseEntity<Object> response = menuController.delete(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteMenuItemNotFound() {
        // Setup
        Long id = 1L;
        when(menuService.getById(id)).thenReturn(null);

        // Execute
        ResponseEntity<Object> response = menuController.delete(id);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


}
