package com.leessy.liuc.commonlibs;

import io.reactivex.Observable;

public class TestOBS {
    public static Observable<Integer> getobs() {
        return Observable.just(123);
    }
}
