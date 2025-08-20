package sorravit.example.springboottestingexample.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import sorravit.example.springboottestingexample.model.MenuItem;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MenuItemRepository {
    @Insert("INSERT INTO menu_items (restaurant_id, name, price) VALUES (#{restaurantId}, #{name}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(MenuItem menuItem);

    @Select("SELECT * FROM menu_items WHERE id = #{id}")
    Optional<MenuItem> findById(Long id);

    @Select("SELECT * FROM menu_items WHERE restaurant_id = #{restaurantId}")
    List<MenuItem> findByRestaurantId(Long restaurantId);
}
