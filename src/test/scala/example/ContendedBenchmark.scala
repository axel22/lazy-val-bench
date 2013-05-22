package example



import scala.reflect.ClassTag
import org.scalameter.api._



class ContendedBenchmark extends PerformanceTest.Regression {
  def persistor = Persistor.None

  val repetitions = Gen.range("size")(1000000, 5000000, 1000000)
  def objects[T <: AnyRef: ClassTag](newCell: Int => T) = for (sz <- repetitions) yield {
    val array = new Array[T](sz)
    for (i <- 0 until array.length) array(i) = newCell(i)
    array
  }

  performance of "Contended" config (
    exec.minWarmupRuns -> 10,
    exec.maxWarmupRuns -> 30,
    exec.benchRuns -> 30,
    exec.independentSamples -> 1,
    exec.jvmflags -> ""
  ) in {
    using(objects(i => new Cell(i))) curve("non-lazy") setUp {
      arr => for (i <- 0 until arr.length) arr(i) = new Cell(i)
    } tearDown {
      arr => for (i <- 0 until arr.length) arr(i) = null
    } in { array =>
      val threads = for (_ <- 0 until 4) yield new Thread {
        override def run() {
          var i = 0
          while (i < array.length) {
            array(i).value
            i += 1
          }
        }
      }
      threads.foreach(_.start())
      threads.foreach(_.join())
    }

    using(objects(i => new LazyCell(i))) curve("lazy-current") setUp {
      arr => for (i <- 0 until arr.length) arr(i) = new LazyCell(i)
    } tearDown {
      arr => for (i <- 0 until arr.length) arr(i) = null
    } in { array =>
      val threads = for (_ <- 0 until 4) yield new Thread {
        override def run() {
          var i = 0
          while (i < array.length) {
            array(i).value
            i += 1
          }
        }
      }
      threads.foreach(_.start())
      threads.foreach(_.join())
    }

    using(objects(i => new LazySimCellVersion3(i))) curve("lazy-simulation-v3") setUp {
      arr => for (i <- 0 until arr.length) arr(i) = new LazySimCellVersion3(i)
    } tearDown {
      arr => for (i <- 0 until arr.length) arr(i) = null
    } in { array =>
      val threads = for (_ <- 0 until 4) yield new Thread {
        override def run() {
          var i = 0
          while (i < array.length) {
            array(i).value
            i += 1
          }
        }
      }
      threads.foreach(_.start())
      threads.foreach(_.join())
    }

    using(objects(i => new LazySimCellVersion4General(i))) curve("lazy-simulation-v4-general") setUp {
      arr => for (i <- 0 until arr.length) arr(i) = new LazySimCellVersion4General(i)
    } tearDown {
      arr => for (i <- 0 until arr.length) arr(i) = null
    } in { array =>
      val threads = for (_ <- 0 until 4) yield new Thread {
        override def run() {
          var i = 0
          while (i < array.length) {
            array(i).value
            i += 1
          }
        }
      }
      threads.foreach(_.start())
      threads.foreach(_.join())
    }

  }

}







