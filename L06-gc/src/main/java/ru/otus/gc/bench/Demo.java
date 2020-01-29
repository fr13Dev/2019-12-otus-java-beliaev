package ru.otus.gc.bench;


import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.*;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    private final static String MINOR_GC = "end of minor GC";
    private final static int MS_PER_MINUTE = 60_000;
    private long timeOfPreviousStatistic;
    private final List<Long> minorTime = new ArrayList<>();
    private final List<Long> majorTime = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        start();
    }

    public static void start() throws Exception {
        final Demo demo = new Demo();
        demo.switchOnMonitoring();
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        final ObjectName name = new ObjectName("ru.otus:type=Benchmark");
        final Benchmark benchmark = new Benchmark();
        mBeanServer.registerMBean(benchmark, name);
        demo.setTimeOfPreviousStatistic(System.currentTimeMillis());
        benchmark.run();
    }

    public void setTimeOfPreviousStatistic(long time) {
        this.timeOfPreviousStatistic = time;
    }

    public void switchOnMonitoring() {
        final List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            final NotificationEmitter emitter = (NotificationEmitter) gcbean;
            final NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    final GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    final String gcAction = info.getGcAction();
                    final long duration = info.getGcInfo().getDuration();
                    if (needToPrintStatistic()) {
                        int spentMinorTime = 0;
                        for (Long value : minorTime) {
                            spentMinorTime += value;
                        }
                        System.out.println(String.format("Minor GC was invoked %d times", minorTime.size()));
                        System.out.println(String.format("Spent time: %dms", spentMinorTime));
                        System.out.println();
                        minorTime.clear();

                        int spentMajorTime = 0;
                        for (Long value : majorTime) {
                            spentMajorTime += value;
                        }
                        System.out.println(String.format("Major GC was invoked %d times", majorTime.size()));
                        System.out.println(String.format("Spent time: %dms", spentMajorTime));
                        System.out.println();
                        majorTime.clear();

                        final int total = spentMinorTime + spentMajorTime;
                        System.out.println(String.format("Total time spent by GC: %dms", total));
                        System.out.println(String.format("Efficiency is %f", ((double) (MS_PER_MINUTE - total) / MS_PER_MINUTE)*100) + "%");
                        System.out.println();

                        timeOfPreviousStatistic = System.currentTimeMillis();
                    }

                    if (gcAction.equals(MINOR_GC)) {
                        minorTime.add(duration);
                    } else {
                        majorTime.add(duration);
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private boolean needToPrintStatistic() {
        return System.currentTimeMillis() - timeOfPreviousStatistic > MS_PER_MINUTE;
    }
}
