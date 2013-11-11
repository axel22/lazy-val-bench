package example



import scala.reflect.ClassTag
import org.scalameter.api._



class MemoryFootprint extends PerformanceTest.Regression {
  def persistor = Persistor.None
  override def measurer = new Executor.Measurer.MemoryFootprint

  val repetitions = Gen.range("size")(1000000, 5000000, 1000000)
  def objects[T <: AnyRef: ClassTag] = for (sz <- repetitions) yield {
    new Array[T](sz)
  }

  performance of "Memory" config (
    exec.minWarmupRuns -> 5,
    exec.maxWarmupRuns -> 10,
    exec.benchRuns -> 8,
    exec.independentSamples -> 1,
    exec.jvmflags -> ""
  ) in {
    using(objects[Cell]) curve("non-lazy") in { array =>
      for (i <- 0 until array.length) array(i) = new Cell(i)
      array
    }

    using(objects[LazyCell]) curve("lazy-current") in { array =>
      for (i <- 0 until array.length) array(i) = new LazyCell(i)
      array
    }

    using(objects[LazySimCellVersion3]) curve("lazy-simulation-v3") in { array =>
      for (i <- 0 until array.length) array(i) = new LazySimCellVersion3(i)
      array
    }

    using(objects[LazySimCellVersion4]) curve("lazy-simulation-v4") in { array =>
      for (i <- 0 until array.length) array(i) = new LazySimCellVersion4(i)
      array
    }

    using(objects[LazySimCellVersion4General]) curve("lazy-simulation-v4-general") in { array =>
      for (i <- 0 until array.length) array(i) = new LazySimCellVersion4General(i)
      array
    }

/*    using(objects[LazyValsHm]) curve("lazy-simulation-MH") in { array =>
      for (i <- 0 until array.length) array(i) = new LazyValsHm(i)
      array
    }
 */
  }

}







