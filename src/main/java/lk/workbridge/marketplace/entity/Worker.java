package lk.workbridge.marketplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lk.workbridge.marketplace.enums.WorkerJobRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Getter
@Setter
@ToString(exclude = "skills")
@EqualsAndHashCode(exclude = "skills")
@AllArgsConstructor
@NoArgsConstructor

public class Worker extends User{

@Column(name="available",nullable = true)

    private Boolean available;
@Column(name="title",nullable = false)
private String title;

    @OneToMany(

            mappedBy = "worker",

            cascade = CascadeType.ALL,

            orphanRemoval = true

    )

    private Set<ClientBookingRequestedAdvertisement> bookingRequests = new HashSet<>();

    @OneToMany(
            mappedBy = "worker",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<WorkerSkill> skills
            = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    private List<Rating> ratings = new ArrayList<>();

}
