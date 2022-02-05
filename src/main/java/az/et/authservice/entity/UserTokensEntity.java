package az.et.authservice.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles")
public class UserTokensEntity implements Serializable {

    private static final long serialVersionUID = 7916286765054251635L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;
}
