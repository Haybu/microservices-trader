package io.pivotal.portfolio.repository;

import io.pivotal.portfolio.domain.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * 
 * @author David Ferreira Pinto
 *
 */
public interface OrderRepository extends CrudRepository<Order,Integer> {

	List<Order> findByAccountId(String accountId);
}
