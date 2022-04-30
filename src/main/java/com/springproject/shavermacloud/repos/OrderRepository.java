package com.springproject.shavermacloud.repos;

import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
