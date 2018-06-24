package com.school.lwh.practicerx_java;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                just_테스트();

            }
        });

    }

    public void just_테스트() {
        Log.d("test332", "create observable");
        Observable<String> observable = Observable.just("안뇽!");
        Log.d("test332", "do subscribe");
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("test332", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("test332", "on Error : " + e.getCause());
            }

            @Override
            public void onNext(String s) {
                Log.d("test332", "On Next : " + s);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void from_테스트() {
        Log.d("test332", "create observable");
        Observable<String> observable = Observable.from(new String[]{"뱀", "꽃", "강도", "습격"});
        Log.d("test332", "do subscribe");
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("test332", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("test332", "on Error : " + e.getCause());
            }

            @Override
            public void onNext(String s) {
                Log.d("test332", "On Next : " + s);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void map() {
        Observable.from(new String[]{"안물", "안궁", "안방"})
                .observeOn(Schedulers.computation())
                .map(text -> "** " + text + " **")
                .subscribe(text -> System.out.println(Thread.currentThread().getName() + " onNext : " + text),
                        e -> System.out.println(Thread.currentThread().getName() + " onError"),
                        () -> System.out.println(Thread.currentThread().getName() + " onCompleted"));
    }

    public void flatMap() {
        Observable.from(new String[]{"인간1", "인간2", "인간3"})
                .flatMap(text -> Observable.from(new String[]{text + "안물", text + "안궁"}))
                .observeOn(Schedulers.computation())
                .subscribe(
                        text->System.out.println(Thread.currentThread().getName()+", onNext : "+text),
                        e->System.out.println(e.getMessage()),
                        ()->System.out.println(Thread.currentThread().getName()+", onCompleted")
                );
    }
    private void zip(){
        Observable.zip(
                Observable.just("개미"),
                Observable.just("ant.jpg"),
                (name,image)->"이름 : "+name+"\n"+"사진 : "+image
        ).observeOn(Schedulers.computation())
                .subscribe(
                        text->System.out.println(Thread.currentThread().getName()+", onNext"+text),
                        e->System.out.println(e.getMessage()),
                        ()->System.out.println(Thread.currentThread().getName()+", onComplete")

                );
    }
    public void publishSubject() {
        PublishSubject<String> subject = PublishSubject.create();

        subject.onNext("첫번째 호출");

        subject.subscribe(text -> {
            System.out.println("onNext : " + text);
        });
        subject.onNext("두번째 호출");
        subject.onNext("세번째 호출");
        subject.onNext("네번째 호출");
    }

    public void defer_테스트() {
        Log.d("test332", "create observable");
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Log.d("test331", "defer function call");
                return Observable.just("HelloWorld");
            }
        });
        Log.d("test332", "do subscribe");
        observable.subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d("test331", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("test331", e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.d("test331", "onNext : " + s);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void range_테스트() {
        Log.d("test332", "create observable");
        Subscription observable = Observable.range(10, 89)
                .observeOn(Schedulers.computation())
                .subscribe(count -> System.out.println(Thread.currentThread().getName() + " onNext : " + count),
                        e -> System.out.println(Thread.currentThread().getName() + " onError : " + e.getMessage()),
                        () -> System.out.println(Thread.currentThread().getName() + " onCompleted"));
    }

    private void defer_비동기_테스트() {
        Log.d("test330", Thread.currentThread().getName() + ", create Observable");
        Observable<String> observable = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Log.d("test330", Thread.currentThread().getName() + ", defer function call");
                return Observable.just("Hello Fucking World I love it");
            }
        });
        Log.d("test330", Thread.currentThread().getName() + ", do subscribe");
        observable
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("test330", Thread.currentThread().getName() + ", onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("test330", Thread.currentThread().getName() + ", " + e.getCause());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("test330", Thread.currentThread().getName() + ", onNext : " + s);
                    }
                });
        Log.d("test330", Thread.currentThread().getName() + ", after subscribe");
    }
}
