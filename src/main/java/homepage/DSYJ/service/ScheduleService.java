package homepage.DSYJ.service;

import homepage.DSYJ.domain.Schedule;
import homepage.DSYJ.repository.SpringDataJpaScheduleRepository;
import jakarta.transaction.Transactional;

@Transactional
public class ScheduleService {

    private final SpringDataJpaScheduleRepository scheduleRepository;

    public ScheduleService(SpringDataJpaScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    public void scheduleSave(Schedule schedule){
        scheduleRepository.save(schedule);
    }
}
