package lumaceon.mods.aeonicraft.util;

public class TimeParser
{
    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;
    public static final long YEAR = MONTH * 12;
    public static final long DECADE =  YEAR * 10;
    public static final long CENTURY = DECADE * 10;
    public static final long MILLENNIUM = CENTURY * 10;
    public static final long TERASECOND =  SECOND * 1000000000000L;
    public static final long ETERNITY =  SECOND * 1000000000000000L;

    public static String parseTimeValue(long time, int maxUnitsShown)
    {
        if(time >= ETERNITY)
            return "Eternity";

        String parsedString = "";
        int pass = 0;
        long numberOf;

        if(time >= TERASECOND)
        {
            numberOf = time / TERASECOND;
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Terasecond");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= TERASECOND * numberOf;
        }

        if(time >= MILLENNIUM)
        {
            numberOf = time / MILLENNIUM;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Millenn");
            if(numberOf > 1)
                parsedString = parsedString.concat("ia");
            else
                parsedString = parsedString.concat("ium");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= MILLENNIUM * numberOf;
        }

        if(time >= CENTURY)
        {
            numberOf = time / CENTURY;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Centur");
            if(numberOf > 1)
                parsedString = parsedString.concat("ies");
            else
                parsedString = parsedString.concat("y");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= CENTURY * numberOf;
        }

        if(time >= DECADE)
        {
            numberOf = time / DECADE;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Decade");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= DECADE * numberOf;
        }

        if(time >= YEAR)
        {
            numberOf = time / YEAR;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Year");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= YEAR * numberOf;
        }

        if(time >= MONTH)
        {
            numberOf = time / MONTH;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Month");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= MONTH * numberOf;
        }

        if(time >= DAY)
        {
            numberOf = time / DAY;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Day");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= DAY * numberOf;
        }

        if(time >= HOUR)
        {
            numberOf = time / HOUR;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Hour");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= HOUR * numberOf;
        }

        if(time >= MINUTE)
        {
            numberOf = time / MINUTE;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Minute");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitsShown)
                return parsedString;
            else
                time -= MINUTE * numberOf;
        }

        if(time >= SECOND)
        {
            numberOf = time / SECOND;
            if(pass > 0)
                parsedString = parsedString.concat(", ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Second");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
        }

        if(time < SECOND && parsedString.isEmpty())
        {
            parsedString = time + (time == 1 ? " Millisecond" : "Milliseconds");
        }
        if(parsedString.isEmpty())
            parsedString = "Frozen";
        return parsedString;
    }
}
