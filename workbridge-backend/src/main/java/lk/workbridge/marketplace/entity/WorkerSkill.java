package lk.workbridge.marketplace.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "worker")
@EqualsAndHashCode(exclude = "worker")
@Table(name="worker-skill")
public class WorkerSkill {
    @Id

    @GeneratedValue(
            strategy=
                    GenerationType.IDENTITY
    )

    private Long id;

    @ManyToOne

    private Worker worker;

    @ManyToOne

    private JobRole jobRole;

    private Double dailyRate;
}
