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

import static com.example.campy.constant.MentoringStatus.WAITING_FOR_MENTOR;
import static com.example.campy.constant.MentoringType.INDIVIDUAL;

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
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final MaterialRepository materialRepository; // MaterialRepository 주입

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
                .role("USER") // 멘토 역할
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
                .status(WAITING_FOR_MENTOR)
                .type(INDIVIDUAL)
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
                .deleted(false)
                .category("IT/개발") // Added category
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

        // 7. 리뷰 더미 데이터 생성 (user1이 user2에게 멘토링 리뷰 작성)
        Review review1 = Review.builder()
                .author(user1) // 리뷰 작성자
                .targetUser(user2) // 리뷰 대상 사용자 (멘토)
                .rating(5) // 평점
                .category("멘토링") // 리뷰 카테고리
                .content("멘토님의 친절하고 상세한 설명 덕분에 JPA 개념을 완벽하게 이해할 수 있었습니다. 정말 감사합니다!")
                .targetId(match1.getMatchId()) // 멘토링 매칭 ID
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review1);

        // 8. 자료 더미 데이터 생성 (by user2)
        Material material1 = Material.builder()
                .seller(user2)
                .title("스프링 부트 핵심 가이드")
                .content("스프링 부트 개발에 필요한 핵심 개념과 실전 예제를 담은 자료입니다.")
                .fileUrl("http://example.com/files/spring_guide.pdf")
                .previewFileUrl("http://example.com/previews/spring_guide_preview.jpg")
                .thumbnailUrl("http://example.com/thumbnails/spring_guide_thumb.jpg")
                .price(25000)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        materialRepository.save(material1);

        // 9. 리뷰 더미 데이터 생성 (user2가 user1에게 작성)
        Review review2 = Review.builder()
                .author(user2) // 리뷰 작성자
                .targetUser(user1) // 리뷰 대상 사용자
                .rating(4) // 평점
                .category("재능") // 리뷰 카테고리
                .content("user1님의 포트폴리오 리뷰가 매우 도움이 되었습니다. 피드백이 구체적이고 실용적이었어요.")
                .targetId(talent1.getTalentId()) // 재능 ID
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review2);


        System.out.println("더미 데이터 생성이 완료되었습니다.");
    }
}

