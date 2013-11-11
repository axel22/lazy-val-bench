package example



import org.scalameter.api._
import java.lang.invoke._


class SwitchPointBenchmark extends PerformanceTest.Regression with Serializable {
  def persistor = Persistor.None

  val repetitions = Gen.range("size")(10000, 50000, 10000)
  val emptyArrays = for(rep <- repetitions) yield new Array[SwitchPoint](rep) 
  val createdArrays = for(rep <- repetitions) yield {
    val arr = new Array[LazyValsHm](rep)
    for(i <- 0 until arr.length) {
      arr(i) = new LazyValsHm(i)
    }
    arr      
  }
  val initializedArrays = for(rep <- repetitions) yield {
    val arr = new Array[LazyValsHm](rep)
    for(i <- 0 until arr.length) {
      arr(i) = new LazyValsHm(i)
      arr(i).value
    }
    arr      
  }


  performance of "SwitchPointBenchmark" config (
    exec.minWarmupRuns -> 50,
    exec.maxWarmupRuns -> 150,
    exec.benchRuns -> 25,
    exec.independentSamples -> 1,
//    exec.jvmcmd -> "java8 -server",
    exec.jvmflags -> "-Xms3072M -Xmx3072M"

  ) in {
    using(emptyArrays) curve("creation") in { a =>
      var i = 0
      val n = a.length
      while(i < n) {
        a(i) = new SwitchPoint()
        i = i + 1
      }
      a
    }

    using(createdArrays) curve("write") in { a =>
      var i = 0
      val n = a.length
      while (i < n) {
        a(i).value
        i += 1
      }
    }


    using(initializedArrays) curve("read") in { a =>
      var i = 0
      val n = a.length
      while (i < n) {
        a(i).value
        i += 1
      }
    }
  }

}


