package goorm.webide.user.entity;

import goorm.webide.chat.entity.Chat;
import goorm.webide.chat.entity.ChatUser;
import goorm.webide.user.util.enums.DeleteStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no", nullable = false, unique = true)
    private Long userNo;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteStatus deleteStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private Set<ChatUser> chatUsers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY)
    private List<Chat> chats;

    @PrePersist
    public void prePersist(){
        this.deleteStatus = DeleteStatus.ACTIVE;
    }
}
