import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Setmeal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:spring-dao.xml"})
public class demo {
    @Autowired
    private SetmealDao setmealDao;
    @Test
    public void f1(){
        //System.out.println(setmealDao);
        Setmeal setmeal = setmealDao.testSetmeal(12);
        System.out.println(setmeal);
    }
}
