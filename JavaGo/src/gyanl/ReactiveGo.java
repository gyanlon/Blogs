package gyanl;

import rx.Observable;

public class Main
{

    public static  void main(String[] args) {

        Observable.just("abc").subscribe(System.out::println);

    }
}