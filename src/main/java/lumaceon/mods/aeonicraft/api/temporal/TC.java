package lumaceon.mods.aeonicraft.api.temporal;

/**
 * A more semantic form of a simple 64-bit integer to make string conversion more convenient.
 */
public class TC
{
    private long value = 0L;

    public TC(long value) {
        this.value = value;
    }

    public long getVal() {
        return this.value;
    }


    // Math functions...
    public TC multiply(long val)    { return new TC(this.value * val); }
    public TC multiply(TC tc)       { return new TC(this.value * tc.value); }
    public TC add(long val)         { return new TC(this.value + val); }
    public TC add(TC tc)            { return new TC(this.value + tc.value); }
    public TC subtract(long val)    { return new TC(this.value - val); }
    public TC subtract(TC tc)       { return new TC(this.value - tc.value); }
    public TC divide(long val)      { return new TC(this.value / val); }
    public TC divide(TC tc)         { return new TC(this.value / tc.value); }


    // Standard units...
    public static final TC NONE =        new TC(0);                                  // 0
    public static final TC MILLISECOND = new TC(1);                                  // 1
    public static final TC TICK =        new TC(MILLISECOND.value * 50);             // 50
    public static final TC SECOND =      new TC(TICK.value * 20);                   // 1000
    public static final TC MINUTE =      new TC(SECOND.value * 60);                  // 60k
    public static final TC HOUR =        new TC(MINUTE.value * 60);                  // 3.6m
    public static final TC DAY =         new TC(HOUR.value * 24);                    // 86.4m
    public static final TC MONTH =       new TC(DAY.value * 30);                     // ~2.6b
    public static final TC YEAR =        new TC(MONTH.value * 12);                   // ~31.1b
    public static final TC DECADE =      new TC(YEAR.value * 10);                    // ~310b
    public static final TC CENTURY =     new TC(YEAR.value * 100);                   // ~3.1t
    public static final TC MILLENNIUM =  new TC(YEAR.value * 1000);                  // ~31t
    public static final TC TERASECOND =  new TC(SECOND.value * 1000000000000L);      // One quadrillion
    public static final TC ETERNITY =    new TC(Long.MAX_VALUE - 1);                 // A lot.


    @Override
    public String toString() {
        return toString(2);
    }

    /**
     * @param maxUnitPrecision The number of units before cutoff.
     *                         Ex: 2 might show hours and minutes, but skip any additional seconds this TC represents.
     */
    public String toString(int maxUnitPrecision) {
        return toString(maxUnitPrecision, ',');
    }

    /**
     * Overloaded to allow custom unit separators.
     */
    public String toString(int maxUnitPrecision, char unitSeparator)
    {
        if(this.value >= ETERNITY.value)
            return "Eternity";

        String parsedString = "";
        int pass = 0;
        long numberOf;

        if(this.value >= TERASECOND.value)
        {
            numberOf = this.value / TERASECOND.value;
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Terasecond");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= TERASECOND.value * numberOf;
        }

        if(this.value >= MILLENNIUM.value)
        {
            numberOf = this.value / MILLENNIUM.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Millenn");
            if(numberOf > 1)
                parsedString = parsedString.concat("ia");
            else
                parsedString = parsedString.concat("ium");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= MILLENNIUM.value * numberOf;
        }

        if(this.value >= CENTURY.value)
        {
            numberOf = this.value / CENTURY.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Centur");
            if(numberOf > 1)
                parsedString = parsedString.concat("ies");
            else
                parsedString = parsedString.concat("y");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= CENTURY.value * numberOf;
        }

        if(this.value >= DECADE.value)
        {
            numberOf = this.value / DECADE.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Decade");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= DECADE.value * numberOf;
        }

        if(this.value >= YEAR.value)
        {
            numberOf = this.value / YEAR.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Year");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= YEAR.value * numberOf;
        }

        if(this.value >= MONTH.value)
        {
            numberOf = this.value / MONTH.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Month");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= MONTH.value * numberOf;
        }

        if(this.value >= DAY.value)
        {
            numberOf = this.value / DAY.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Day");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= DAY.value * numberOf;
        }

        if(this.value >= HOUR.value)
        {
            numberOf = this.value / HOUR.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Hour");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= HOUR.value * numberOf;
        }

        if(this.value >= MINUTE.value)
        {
            numberOf = this.value / MINUTE.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Minute");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
            pass++;
            if(pass == maxUnitPrecision)
                return parsedString;
            else
                this.value -= MINUTE.value * numberOf;
        }

        if(this.value >= SECOND.value)
        {
            numberOf = this.value / SECOND.value;
            if(pass > 0)
                parsedString = parsedString.concat(unitSeparator + " ");
            parsedString = parsedString.concat(Long.toString(numberOf));
            parsedString = parsedString.concat(" Second");
            if(numberOf > 1)
                parsedString = parsedString.concat("s");
        }

        if(this.value < SECOND.value && parsedString.isEmpty())
        {
            parsedString = this.value + " Ms";
        }
        if(parsedString.isEmpty())
            parsedString = "Frozen";
        return parsedString;
    }
}
