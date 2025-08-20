package sorravit.example.springboottestingexample.repository;

import org.apache.ibatis.annotations.*;
import sorravit.example.springboottestingexample.model.Order;
import sorravit.example.springboottestingexample.model.OrderStatus;

import java.util.Optional;

@Mapper
public interface OrderRepository {
    @Insert("INSERT INTO orders (restaurant_id, total_price, status) VALUES (#{restaurantId}, #{totalPrice}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Order order);

    @Select("SELECT * FROM orders WHERE id = #{id}")
    Optional<Order> findById(Long id);

    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") OrderStatus status);

//    @Select("SELECT * FROM orders WHERE restaurant_id = #{restaurantId}")
//    Optional<Order> findByRestaurantId(Long restaurantId);
}
