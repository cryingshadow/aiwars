package de.fhdw.aiwars.logic;

import java.util.function.*;

public class IsEven implements Predicate<Integer> {

    public static boolean isEven(final Integer i) {
        return (i & 1) == 0;
    }

    @Override
    public boolean test(final Integer i) {
        return IsEven.isEven(i);
    }

}
