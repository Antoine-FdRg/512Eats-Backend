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
import team.k.CommonJPAConfig;

@Service
public class GroupOrderAPI {
    private static AnnotationConfigApplicationContext context;

    public static void main(String[] args) {
        context = new AnnotationConfigApplicationContext(GroupOrderConfig.class, CommonJPAConfig.class);
        SSDBHttpServer serv = new SSDBHttpServer(8085, "team.k.grouporderservice", "grouporderservice/", context);
        serv.start();
    }

}
