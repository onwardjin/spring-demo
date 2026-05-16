package com.example.userorder.repository;

import com.example.userorder.domain.order.Order;
import com.example.userorder.dto.order.OrderSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.example.userorder.domain.order.QOrder.order;

public class OrderRepositoryImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Slice<Order> searchOrders(Long userId, OrderSearchCondition condition, Pageable pageable) {
        List<Order> orders = queryFactory
                .selectFrom(order)
                .where(
                        userIdEq(userId),
                        priceGoe(condition.minPrice()),
                        priceLoe(condition.maxPrice())

                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = orders.size() > pageable.getPageSize();
        if (hasNext) {
            orders.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(orders, pageable, hasNext);
    }

    private BooleanExpression userIdEq(Long userId) {
        return order.user.id.eq(userId);
    }

    private BooleanExpression priceGoe(Long minPrice) {
        return minPrice != null ? order.totalPrice.amount.goe(minPrice) : null;
    }

    private BooleanExpression priceLoe(Long maxPrice) {
        return maxPrice != null ? order.totalPrice.amount.loe(maxPrice) : null;
    }
}