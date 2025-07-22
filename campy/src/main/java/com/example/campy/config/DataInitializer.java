package com.example.campy.config;

import com.example.campy.entity.*;
import com.example.campy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Profile("dev") // 'dev' 프로필에서만 실행
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final MentoringOfferRepository mentoringOfferRepository;
    private final MentoringMatchRepository mentoringMatchRepository;
    private final TalentRepository talentRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 데이터가 이미 있으면 실행하지 않음
        if (userRepository.count() > 0) {
            System.out.println("더미 데이터가 이미 존재하여 생성하지 않습니다.");
            return;
        }

        System.out.println("개발용 더미 데이터를 생성합니다...");

        // 1. 사용자 더미 데이터 생성
        User user1 = User.builder()
                .username("user1")
                .email("user1@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("김일반")
                .nickname("캠핑왕초보")
                .school("캠피대학교")
                .major("컴퓨터공학과")
                .entranceYear(2021)
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .username("user2")
                .email("user2@test.com")
                .password(passwordEncoder.encode("1234"))
                .name("이멘토")
                .nickname("리액트고수")
                .school("캠피대학교")
                .major("소프트웨어공학과")
                .entranceYear(2020)
                .role("MENTOR") // 멘토 역할
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.saveAll(Arrays.asList(user1, user2));

        // 2. 게시판 더미 데이터 생성
        Board board1 = Board.builder()
                .user(user1)
                .title("안녕하세요! 질문 있습니다.")
                .content("스프링 부트 JPA가 너무 어려워요. 다들 어떻게 공부하시나요?")
                .category("질문")
                .createdAt(LocalDateTime.now())
                .build();

        Board board2 = Board.builder()
                .user(user2)
                .title("리액트 state 관리 노하우 공유합니다.")
                .content("프로젝트가 커질 때 state 관리가 복잡해지는 분들을 위해, 저만의 노하우를 공유합니다.")
                .category("정보공유")
                .createdAt(LocalDateTime.now())
                .build();

        boardRepository.saveAll(Arrays.asList(board1, board2));

        // 3. 멘토링 제안 더미 데이터 생성 (by user2)
        MentoringOffer offer1 = MentoringOffer.builder()
                .user(user2)
                .title("JPA 기초부터 알려드립니다.")
                .description("객체와 테이블 매핑의 어려움을 겪는 분들께, ORM의 기본 개념부터 차근차근 알려드립니다.")
                .price(50000)
                .status(com.example.campy.constant.MentoringStatus.REQUESTED)
                .createdAt(LocalDateTime.now())
                .build();

        mentoringOfferRepository.save(offer1);

        // 4. 멘토링 성사 더미 데이터 생성 (user1이 user2의 제안을 수락)
        MentoringMatch match1 = MentoringMatch.builder()
                .mentoringOffer(offer1)
                .status("ACCEPTED")
                .type("개인")
                .createdAt(LocalDateTime.now())
                .build();

        mentoringMatchRepository.save(match1);
        
        // 5. 재능 판매 더미 데이터 생성 (by user2)
        Talent talent1 = Talent.builder()
                .user(user2)
                .title("포트폴리오 리뷰해드립니다.")
                .description("신입 개발자, 학생분들의 포트폴리오를 현직자의 시선으로 꼼꼼하게 리뷰해드립니다.")
                .price(30000)
                .status("판매중")
                .createdAt(LocalDateTime.now())
                .build();
                
        talentRepository.save(talent1);

        // 6. 결제 더미 데이터 생성 (user1이 user2의 멘토링을 결제)
        Payment payment1 = Payment.builder()
                .buyer(user1)
                .targetId(match1.getMatchId()) // 성사된 멘토링에 대한 결제
                .type("멘토링")
                .status("결제완료")
                .method("카카오페이")
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment1);


        System.out.println("더미 데이터 생성이 완료되었습니다.");
    }
}
