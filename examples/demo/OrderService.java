package demo;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final DiscountPolicy discountPolicy;

    public OrderService() {
        this(new ThresholdDiscountPolicy());
    }

    OrderService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public Receipt checkout(List<OrderLine> lines) {
        int subtotal = subtotal(lines);
        int discount = discountPolicy.discountFor(subtotal);
        int total = subtotal - discount;

        if (total < 0) {
            total = 0;
        }

        return new Receipt(subtotal, discount, total, labelsFor(lines));
    }

    public int subtotal(List<OrderLine> lines) {
        int total = 0;
        for (OrderLine line : lines) {
            total += line.priceInCents() * line.quantity();
        }
        return total;
    }

    private List<String> labelsFor(List<OrderLine> lines) {
        List<String> labels = new ArrayList<>();
        for (OrderLine line : lines) {
            labels.add(line.name() + " x" + line.quantity());
        }
        return labels;
    }

    interface DiscountPolicy {
        int discountFor(int subtotal);
    }

    static class ThresholdDiscountPolicy implements DiscountPolicy {
        @Override
        public int discountFor(int subtotal) {
            if (subtotal >= 10_000) {
                return subtotal / 10;
            }
            return 0;
        }
    }

    public record OrderLine(String name, int priceInCents, int quantity) {
    }

    public record Receipt(int subtotal, int discount, int total, List<String> labels) {
    }
}
