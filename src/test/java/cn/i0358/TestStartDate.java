package cn.i0358;

import cn.i0358.model.QQTextParse;
import junit.framework.TestCase;
import org.joda.time.DateTime;

import java.time.LocalDate;

/**
 * Created by fanyk on 2017/4/27.
 */
public class TestStartDate extends TestCase{

    public void testStartDate()
    {
//        QQTextParse.SDate date=QQTextParse.SDate.create("的6点");
//        assertEquals(6,date.getTime());
//
//        QQTextParse.SDate date1=QQTextParse.SDate.create("今天下午六点出发，（4月27号），太原回临县，车找人，预定18734911186\n");
//        assertEquals(18,date1.getTime());
//
//        QQTextParse.SDate date2=QQTextParse.SDate.create("（4月29号），太原回临县，车找人，预定18734911186\n");
//        assertEquals(29,date2.getDate().getDate());

        QQTextParse.SDate date3=QQTextParse.SDate.create("车找人，五点多六点，太原回临县，回的提前，联系电话18735837277");
        assertEquals(17,date3.getTime());
    }

    public void testStartTime()
    {
        QQTextParse.SDate date=QQTextParse.SDate.create("明天八点");
        assertEquals(8,date.getTime());

        QQTextParse.SDate date1=QQTextParse.SDate.create("明天下午六点出发，（4月27号），太原回临县，车找人，预定18734911186\n");
        assertEquals(18,date1.getTime());
        assertEquals(LocalDate.now().plusDays(1).getDayOfMonth(),date1.getDate().getDate());

        QQTextParse.SDate date2=QQTextParse.SDate.create("今天午饭后出发，（4月27号），太原回临县，车找人，预定18734911186\n");
        assertEquals(13,date2.getTime());
        assertEquals(LocalDate.now().plusDays(1).getDayOfMonth(),date2.getDate().getDate());
    }

    public void testStartDate2()
    {
        QQTextParse.SDate date2=QQTextParse.SDate.create("今天午饭后出发，太原回临县，车找人，预定18734911186\n");
        assertEquals(13,date2.getTime());
        System.out.println(DateTime.now().withSecondOfMinute(0).withMinuteOfHour(0).toDate());
        System.out.println(date2.getDate());
        assertEquals(DateTime.now().withHourOfDay(13).withSecondOfMinute(0).withMinuteOfHour(0).toDate(),date2.getDate());
    }
}
