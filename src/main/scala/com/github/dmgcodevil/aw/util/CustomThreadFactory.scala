package com.github.dmgcodevil.aw.util

import java.util.concurrent.{Executors, ThreadFactory}

/**
  * Custom thread factory to create thread with given suffix.
  *
  * Created by dmgcodevil on 1/28/2018.
  */
class CustomThreadFactory(val suffix: String, val tf: ThreadFactory = Executors.defaultThreadFactory()) extends ThreadFactory {
  override def newThread(r: Runnable): Thread = {
    val thread = tf.newThread(r)
    thread.setName(thread.getName + "-" + suffix)
    thread
  }
}
