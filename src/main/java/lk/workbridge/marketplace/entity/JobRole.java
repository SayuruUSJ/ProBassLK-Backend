package lk.workbridge.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lk.workbridge.marketplace.enums.WorkerJobRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JobRole {
    @Id

    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )

    private Long id;
    @Enumerated(
            EnumType.STRING
    )
    @Column(name = "role-name", nullable = false)
    private WorkerJobRole roleName;
}
