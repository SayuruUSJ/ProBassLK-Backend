package lk.workbridge.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderProfileUpdate extends BaseProfileUpdate {

    private Boolean available;

//    private Set<WorkerSkillRequest> skills;

    private int overallExperience;

    private String about;


    private Set<DayOfWeek> workingDays;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean emergencyAvailable;

    private String title;

    private String NIC;
}
