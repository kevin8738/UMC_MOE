package erd.exmaple.erd.example.domain;

import erd.exmaple.erd.example.domain.common.BaseEntity;
import erd.exmaple.erd.example.domain.enums.Ad;
import erd.exmaple.erd.example.domain.enums.Marketing;
import erd.exmaple.erd.example.domain.enums.Provider;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 255)
    private String password;

    @Column(name = "phone_number", nullable = false,length = 11)
    private String phoneNumber;

    @Column(nullable = false,length = 100) //일단 널값허용
    private String nickname;

    @Column(length = 255) //소셜로그인시 필요할 것 같아서 추가
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)")
    private Provider provider;

    @Column
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'ACTIVE'")
    private LoginStatus status;

    @Column
    private LocalDate inactive_date;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'INACTIVE'")
    private Ad ad;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'INACTIVE'")
    private Marketing marketing;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Record_PageEntity> recordPageEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<FollowEntity> FollowEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SearchEntity> searchEntities = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.password == null) {
            this.password = "defaultPassword"; // 더미 비밀번호 설정
        }
    }
}
