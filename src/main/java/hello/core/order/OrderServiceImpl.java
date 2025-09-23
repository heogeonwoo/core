package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
// @RequiredArgsConstructor // 생성자를 만들어 준다. (Lombok 사용)
public class OrderServiceImpl implements OrderService {

/*
    // DIP 위반
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
*/

    // @RequiredArgsConstructor로 자동 의존관계 주입이 가능하다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

/*
    // 수정자 주입 (setter 주입)
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
*/

    // 생성자 주입: 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입 된다. -> Lombok 사용 시 생성자도 생략 가능하다.
    // 조회 빈이 2개 이상인 경우 필드 명과 파라미터 명, @Qualifier, @Primary로 해결 가능하다. -> @Qualifier가 우선순위가 더 높다.
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
