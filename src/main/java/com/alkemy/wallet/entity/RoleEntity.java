package com.alkemy.wallet.entity;

import com.alkemy.wallet.enumeration.RoleName;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name ="NAME", nullable = false, unique = true)
    private RoleName name;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;


}
