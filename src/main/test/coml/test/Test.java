package coml.test;

import com.xhrmyy.histool.HisToolApplication;
import com.xhrmyy.histool.service.QueueService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HisToolApplication.class)
public class Test {
    @Autowired
    QueueService queueService;

    @org.junit.Test
    public void test() throws Exception {
        queueService.toExport();
    }


}
