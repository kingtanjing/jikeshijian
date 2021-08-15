# GC和堆内存总结

前言：

jdk版本：1.8.0_161

默认jvm配置：-XX:InitialHeapSize=266742208 -XX:MaxHeapSize=4267875328 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC



##### 一、使用默认配置 

启动参数：java -XX:+PrintGCDetails -Xmx1g -Xms1g GCLogAnalysis

说明：垃圾收集器：-XX:+UseParallelGC  堆内存：1G

结果分析：

[GC (Allocation Failure) [PSYoungGen: 262144K->43517K(305664K)] 262144K->87181K(1005056K), 0.0223139 secs] [Times: user=0.06 sys=0.05, real=0.02 secs]

GC方式：Minor GC

GC触发的原因：Allocation Failure说明年轻代没有足够的内存分配导致的GC

GC结果：young区的总内存是305664K，从262144K降低到43517K，减少了218,627K。堆内存总大小是1005056K，从262144K减少到87181K，减少了174,963K。说明整个垃圾回收回收了174,963K数据，还有有43,664K的数据从young区转移到了old区。整个GC花的时间为0.0223139 secs。

[Full GC (Ergonomics) [PSYoungGen: 38530K->0K(232960K)] [ParOldGen: 620651K->333319K(699392K)] 659182K->333319K(932352K), [Metaspace: 2810K->2810K(1056768K)], 0.0850183 secs] [Times: user=0.42 sys=0.02, real=0.09 secs]

GC方式：Full GC

GC原因：为了调节GC时间和吞吐量

GC结果：young区的总内存是232960K，从38530K降低到0K，减少了38530K。old区的总内存大小是699392K，从620651K减少到333319K，减少了287,332K。堆内存总大小是932352K，从659182K减少到333319K，减少了325,863K。元空间的数据没有变化。说明整个垃圾回收回收了174,963K数据，没有数据从young区转移到了old区。整个GC花的时间为0.0850183 sec。



疑问：

1：为什么两次GC显示的young区总内存大小不一样

2：Ergonomics不是太理解



#### 二、使用串行GC

启动参数：java -XX:+UseSerialGC -XX:+PrintGCDetails -Xmx1g -Xms1g GCLogAnalysis

说明：垃圾收集器：-XX:+UseSerialGC  堆内存：1G

结果分析：

[GC (Allocation Failure) [DefNew: 279616K->34944K(314560K), 0.0468125 secs] 279616K->86290K(1013632K), 0.0471151 secs] [Times: user=0.02 sys=0.03, real=0.05 secs]

GC方式：Minor GC

GC触发的原因：Allocation Failure说明年轻代没有足够的内存分配导致的GC

GC结果：young区的总内存是314560K，从279616K降低到34944K，减少了244,672K。堆内存总大小是1013632K，从279616K减少到86290K，减少了193,326K。说明整个垃圾回收回收了193,326K数据，还有有51,346K的数据从young区转移到了old区。整个GC花的时间为0.0471151 secs。

[GC (Allocation Failure) [DefNew: 314559K->314559K(314560K), 0.0003608 secs]Tenured: 607187K->372047K(699072K), 0.0608445 secs] 921746K->372047K(1013632K), [Metaspace: 2809K->2809K(1056768K)], 0.0625509 secs] [Times: user=0.06 sys=0.00, real=0.06 secs]

GC方式：Full GC

GC触发的原因：Allocation Failure说明年轻代没有足够的内存分配导致的GC

GC结果:young区的内存显示上没有变化，但实际可能已经全部被回收了。old区内存从607187k减少到372047K，减少了235,140K数据。整个堆内存从921746K减少到372047K，减少了549,699k。整个GC的时间为0.0625509 secs



疑问：

1、Full GC的时候显示young区的内存没有变化，但总的回收的内存大于old区回收的内存？



#### 三、使用CMS垃圾收集器

启动参数：java -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xmx1g -Xms1g GCLogAnalysis

说明：垃圾收集器：-XX:+UseConcMarkSweepGC  堆内存：1G

结果分析：

[GC (Allocation Failure) [ParNew: 279616K->34943K(314560K), 0.0180549 secs] 279616K->81687K(1013632K), 0.0184850 secs] [Times: user=0.05 sys=0.08, real=0.02 secs]

GC方式：Minor GC

GC触发的原因：Allocation Failure说明年轻代没有足够的内存分配导致的GC

GC结果：young区的总内存是314560K，从279616K降低到34943K，减少了244,673K，使用的时间 0.0180549 secs。堆内存总大小是1013632K，从279616K减少到81687K，减少了197,929K。说明整个垃圾回收回收了174,963K数据，还有有46,744K的数据从young区转移到了old区。整个GC花的时间为0.0184850 secs。

[GC (CMS Initial Mark) [1 CMS-initial-mark: 361932K(699072K)] 403050K(1013632K), 0.0006368 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-mark-start]
[CMS-concurrent-mark: 0.004/0.004 secs] [Times: user=0.03 sys=0.02, real=0.00 secs]
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

[GC (CMS Final Remark) YG occupancy: 40870 K (314560 K)weak refs processing, 0.0000376 secsscrub symbol table, 0.0004276 secs[1 CMS-remark: 670216K(699072K)] 711087K(1013632K), 0.0027572 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

[CMS-concurrent-sweep-start]
[CMS-concurrent-sweep: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.002/0.002 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]

GC方式：Full GC

GC触发的原因：？

GC过程：

1. CMS Initial Mark初始化标记，标记存活的对象，old区使用内存361932K，总共内存有403050K。标记时间0.0006368 secs，会造成STW。
2. concurrent-mark并发收集阶段遍历整个老年代，并标记存活的对象。
3. CMS-concurrent-preclean前一阶段标记的对象又发生了改变的对象进行标记
4. [GC (CMS Final Remark) 最终标记阶段，会造成STW。 YG occupancy: 40870 K (314560 K)young区大小和使用情况，
5. concurrent-sweep 同步清理，清理那些没有被标记的对象
6. concurrent-reset 重新设置CMS算法内部的数据结构，准备下一个CMS生命周期的使用



问题：

整个CMS FullGC过程中还发生了几次Minor GC?

怎么查看CMS FullGC的原因？



#### 四、使用G1垃圾收集器

启动参数：java -XX:+UseG1GC -XX:+PrintGCDetails -Xmx1g -Xms1g GCLogAnalysis

说明：垃圾收集器：-XX:+UseG1GC 堆内存：1G

结果分析：

[GC pause (G1 Evacuation Pause) (young), 0.0063886 secs]
   [Parallel Time: 4.4 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 155.0, Avg: 155.4, Max: 158.3, Diff: 3.4]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 1.0]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.9, Avg: 3.6, Max: 4.1, Diff: 3.3, Sum: 28.7]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 1.1]
         [Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.3]
      [GC Worker Total (ms): Min: 1.0, Avg: 3.9, Max: 4.4, Diff: 3.4, Sum: 31.1]
      [GC Worker End (ms): Min: 159.3, Avg: 159.3, Max: 159.3, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.6 ms]
   [Other: 1.3 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.3 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.6 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 51.0M(51.0M)->0.0B(44.0M) Survivors: 0.0B->7168.0K Heap: 68.5M(1024.0M)->25.1M(1024.0M)]
 [Times: user=0.00 sys=0.00, real=0.01 secs]

GC方式：(young)纯年轻代模式

GC触发的原因：？

[GC pause (G1 Evacuation Pause) (young), 0.0063886 secs]：整个GC花费的时间为0.0063886 secs

 [Parallel Time: 4.4 ms, GC Workers: 8]并行GC的时间为4.4ms，GC 线程数8.

[Eden: 51.0M(51.0M)->0.0B(44.0M) Survivors: 0.0B->7168.0K Heap: 68.5M(1024.0M)->25.1M(1024.0M)] Eden使用内存从51.0M减少到0，Survivors区内存从0增加到7168.0K，整个堆内存使用从68.5M减少到25.1M。



[GC pause (G1 Evacuation Pause) (mixed), 0.0119191 secs]
   [Parallel Time: 10.1 ms, GC Workers: 8]
      [GC Worker Start (ms): Min: 952.1, Avg: 952.2, Max: 952.3, Diff: 0.2]
      [Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.8]
      [Update RS (ms): Min: 0.2, Avg: 0.2, Max: 0.5, Diff: 0.3, Sum: 1.9]
         [Processed Buffers: Min: 0, Avg: 3.0, Max: 4, Diff: 4, Sum: 24]
      [Scan RS (ms): Min: 0.0, Avg: 0.1, Max: 0.1, Diff: 0.1, Sum: 0.4]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 9.1, Avg: 9.3, Max: 9.5, Diff: 0.3, Sum: 74.6]
      [Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.9]
         [Termination Attempts: Min: 1, Avg: 1.1, Max: 2, Diff: 1, Sum: 9]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 9.8, Avg: 9.8, Max: 10.0, Diff: 0.2, Sum: 78.8]
      [GC Worker End (ms): Min: 962.0, Avg: 962.0, Max: 962.1, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.3 ms]
   [Other: 1.5 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.9 ms]
      [Humongous Register: 0.1 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.1 ms]
   [Eden: 38.0M(38.0M)->0.0B(44.0M) Survivors: 13.0M->7168.0K Heap: 784.2M(1024.0M)->667.6M(1024.0M)]
 [Times: user=0.11 sys=0.00, real=0.02 secs]

GC方式：(mixed)混合模式

GC触发的原因：？

[GC pause (G1 Evacuation Pause) (mixed), 0.0119191 secs] 整个GC暂停的时间为0.0119191 secs

 [Parallel Time: 10.1 ms, GC Workers: 8] 并行GC的时间为10.1 ms，GC的线程数8

[Eden: 38.0M(38.0M)->0.0B(44.0M) Survivors: 13.0M->7168.0K Heap: 784.2M(1024.0M)->667.6M(1024.0M)] Eden区从38.0M减少为0，Survivors区从13.0M减少为7168.0K。整个堆内存使用从784.2M减少为667.6M。



问题：

G1 GC触发的原因怎么看？