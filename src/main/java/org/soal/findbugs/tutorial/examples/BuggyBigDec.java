package org.soal.findbugs.tutorial.examples;

import java.math.BigDecimal;
import java.util.Map;

public class BuggyBigDec
{

    public BigDecimal loopOfDeath()
    {
        BigDecimal bd = BigDecimal.valueOf(50);

        while (bd.compareTo(BigDecimal.ZERO) > 0)
        {
            bd.subtract(BigDecimal.ONE);
        }

        return bd;
    }

    public String slowLoop(Map<String, String> map)
    {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet())
        {
            String value = map.get(key);
            builder.append(key + "=" + value + ", ");
        }

        return builder.toString();
    }

}
