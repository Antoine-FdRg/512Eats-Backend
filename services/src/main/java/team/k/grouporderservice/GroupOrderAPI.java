package team.k.grouporderservice;

import commonlibrary.repository.DishJPARepository;
import commonlibrary.repository.GroupOrderJPARepository;
import commonlibrary.repository.LocationJPARepository;
import commonlibrary.repository.RegisteredUserJPARepository;
import commonlibrary.repository.RestaurantJPARepository;
import commonlibrary.repository.TimeSlotJPARepository;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import ssdbrestframework.SSDBHttpServer;

@Service
public class GroupOrderAPI {

    public static LocationJPARepository locationJPARepository;
    public static GroupOrderJPARepository groupOrderJPARepository;
    public static RegisteredUserJPARepository registeredUserJPARepository;
    public static DishJPARepository dishJPARepository;
    public static RestaurantJPARepository restaurantJPARepository;
    public static TimeSlotJPARepository timeSlotJPARepository;
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(GroupOrderConfig.class);
        locationJPARepository = context.getBean(LocationJPARepository.class);
        groupOrderJPARepository = context.getBean(GroupOrderJPARepository.class);
        registeredUserJPARepository = context.getBean(RegisteredUserJPARepository.class);
        dishJPARepository = context.getBean(DishJPARepository.class);
        restaurantJPARepository = context.getBean(RestaurantJPARepository.class);
        timeSlotJPARepository = context.getBean(TimeSlotJPARepository.class);
        SSDBHttpServer serv = new SSDBHttpServer(8085, "team.k.grouporderservice", "grouporderservice/", context);
        serv.start();
    }

}
