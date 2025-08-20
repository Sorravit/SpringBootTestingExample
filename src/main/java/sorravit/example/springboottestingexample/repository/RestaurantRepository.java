package sorravit.example.springboottestingexample.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import sorravit.example.springboottestingexample.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RestaurantRepository {
    @Insert("INSERT INTO restaurants (name) VALUES (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Restaurant restaurant);

    @Select("SELECT * FROM restaurants WHERE id = #{id}")
    Optional<Restaurant> findById(Long id);

    @Select("SELECT * FROM restaurants")
    List<Restaurant> findAll();
}
