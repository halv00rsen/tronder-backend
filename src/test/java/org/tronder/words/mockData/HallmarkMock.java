package org.tronder.words.mockData;

import org.tronder.words.model.Hallmark;

import java.util.ArrayList;
import java.util.List;

public final class HallmarkMock {

    public static List<Hallmark> getHallmarks() {
        List<Hallmark> hallmarks = new ArrayList<>();
        hallmarks.add(new Hallmark("some hallmark"));
        hallmarks.add(new Hallmark("another hallmark"));
        return hallmarks;
    }
}
