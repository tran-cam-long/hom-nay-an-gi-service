package unit.com.camlong.homnayangi.service.impl;


import com.camlong.homnayangi.entity.CuisineDish;
import com.camlong.homnayangi.repository.CuisineDishRepository;
import com.camlong.homnayangi.service.impl.CuisineDishServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.camlong.homnayangi.constant.Cuisine.BEEF_PHO;
import static com.camlong.homnayangi.constant.Cuisine.HU_TIEU;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuisineDishServiceImplTest {

  @InjectMocks
  private CuisineDishServiceImpl cuisineDishService;

  @Mock
  private CuisineDishRepository repository;

  @Test
  void givenExistingCuisine_whenFindById_thenReturn() {
    final Long id = 1L;
    final CuisineDish record = CuisineDish.builder().id(id).name(BEEF_PHO.getValue()).build();

    when(repository.findById(id)).thenReturn(Optional.of(record));

    final CuisineDish actual = cuisineDishService.findById(id);

    verify(repository, times(1)).findById(id);

    assertNotNull(actual);
    assertEquals(id, actual.getId());
  }

  @Test
  void givenValidDishCreateRequest_whenCreateNewDish_thenSucceed() {
    final CuisineDish record = CuisineDish.builder().name(HU_TIEU.getValue()).build();
    final CuisineDish saved = CuisineDish.builder().id(2L).name(HU_TIEU.getValue()).build();
    final ArgumentCaptor<CuisineDish> captor = ArgumentCaptor.forClass(CuisineDish.class);

    when(repository.save(captor.capture())).thenReturn(saved);

    final CuisineDish result = cuisineDishService.createNewDish(record);

    verify(repository, times(1)).save(captor.capture());

    assertEquals(HU_TIEU.getValue(), result.getName());
  }

  @Test
  void givenExistingCuisine_whenPatchDish_thenSucceed() {
    final Long id = 3L;
    final CuisineDish record = CuisineDish.builder().id(id)
        .name(HU_TIEU.getValue())
        .culture("Chinese").imageUrl(null)
        .searchKeyword("Hủ tíu")
        .type("sample").build();
    final Map<String, Object> request = Map.of(
        "name", "Another name",
        "type", "Another type",
        "culture", "Another culture",
        "imageUrl", "https://conmeo.img"
    );
    final ArgumentCaptor<CuisineDish> captor = ArgumentCaptor.forClass(CuisineDish.class);

    when(repository.findById(id)).thenReturn(Optional.of(record));

    cuisineDishService.patchDish(id, request);

    verify(repository, times(1)).findById(id);
    verify(repository, times(1)).save(captor.capture());

    final CuisineDish actualSaved = captor.getValue();
    assertEquals("Another name", actualSaved.getName());
    assertEquals("Another type", actualSaved.getType());
    assertEquals("Another culture", actualSaved.getCulture());
    assertEquals("https://conmeo.img", actualSaved.getImageUrl());
  }

  @Test
  void givenNothing_whenFindAll_thenReturnAll() {
    final CuisineDish record1 = CuisineDish.builder().id(1L).name("Dish 1").build();
    final CuisineDish record2 = CuisineDish.builder().id(2L).name("Dish 2").build();
    final CuisineDish record3 = CuisineDish.builder().id(3L).name("Dish 3").build();

    when(repository.findAll()).thenReturn(List.of(record1, record2, record3));

    final List<CuisineDish> allDishes = cuisineDishService.findAll();

    assertNotNull(allDishes);
    assertEquals(3, allDishes.size());
  }

}
