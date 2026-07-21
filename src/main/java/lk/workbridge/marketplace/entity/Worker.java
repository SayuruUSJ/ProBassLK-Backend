package lk.workbridge.marketplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(exclude = "skills")
@EqualsAndHashCode(exclude = "skills")
@AllArgsConstructor
@NoArgsConstructor

public class Worker extends User {

    @Column(name = "available", nullable = true)
//currently accept or not
    private Boolean available;
    @Column(name = "title", nullable = true)
    private String title;

    @OneToMany(

            mappedBy = "worker",

            cascade = CascadeType.ALL,

            orphanRemoval = true

    )

    private Set<HireRequest> bookingRequests = new HashSet<>();

    @OneToMany(
            mappedBy = "worker",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<WorkerSkill> skills
            = new HashSet<>();

    @OneToMany(mappedBy = "worker")
    private List<Rating> ratings = new ArrayList<>();

    @Column(name="overallExperience",nullable = true)
    private int overallExperience;

    @Column(name="about",nullable = true)
    private String about;

    @Column(name = "emergency_available")
    private boolean emergencyAvailable;

    @Column(name="nic",nullable = true)
    private String NIC;


    @Column(name = "working_days_mask", nullable = true)
    private int workingDaysMask;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;


    public boolean isWorkingOn(DayOfWeek day) {
        int bit = day.getValue() - 1;
        return (workingDaysMask & (1 << bit)) != 0;
    }

    // 2. Get all selected days as a Set
    public Set<DayOfWeek> getWorkingDays() {
        Set<DayOfWeek> days = new HashSet<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            if (isWorkingOn(day)) {
                days.add(day);
            }
        }
        return days;
    }

    // 3. Set days from a Set (for saving from UI)
    public void setWorkingDaysFromSet(Set<DayOfWeek> days) {
        int mask = 0;
        for (DayOfWeek day : days) {
            int bit = day.getValue() - 1;
            mask |= (1 << bit);
        }
        this.workingDaysMask = mask;
    }

    // 4. Toggle a single day (for update operations)
    public void toggleDay(DayOfWeek day) {
        int bit = day.getValue() - 1;
        workingDaysMask ^= (1 << bit);
    }


}
