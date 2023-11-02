package io.fluentqa.basic.supplement;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadsUtils extends ThreadUtil {

  /**
   * sleep等待,单位为毫秒
   *
   * @return
   */
  public static boolean sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      return false;
    }
    return true;
  }

  /**
   * 停止线程池 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务. 如果超时, 则调用shutdownNow,
   * 取消在workQueue中Pending的任务,并中断所有阻塞函数. 如果仍人超時，則強制退出. 另对在shutdown时线程本身被调用中断做了处理.
   */
  public static void shutdownAndAwaitTermination(ExecutorService pool) {
    if (pool != null && !pool.isShutdown()) {
      pool.shutdown();
      try {
        if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
          pool.shutdownNow();
          if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
            log.info("Pool did not terminate");
          }
        }
      } catch (InterruptedException ie) {
        pool.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
  }

  /** 打印线程异常信息 */
  public static void printException(Runnable r, Throwable t) {
    if (t == null && r instanceof Future<?>) {
      try {
        Future<?> future = (Future<?>) r;
        if (future.isDone()) {
          future.get();
        }
      } catch (CancellationException ce) {
        t = ce;
      } catch (ExecutionException ee) {
        t = ee.getCause();
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }
    if (t != null) {
      log.error(t.getMessage(), t);
    }
  }
}
