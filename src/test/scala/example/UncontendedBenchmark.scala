package example



import org.scalameter.api._



class UncontendedBenchmark extends PerformanceTest.Regression with Serializable {
  def persistor = Persistor.None

  val repetitions = Gen.range("size")(1000000, 5000000, 1000000)

  var cell: AnyRef = null

  performance of "LazyVals" config (
    exec.minWarmupRuns -> 50,
    exec.maxWarmupRuns -> 150,
    exec.benchRuns -> 25,
    exec.independentSamples -> 1,
    exec.jvmflags -> ""
  ) in {
    using(repetitions) curve("non-lazy") in { n =>
      var i = 0
      while (i < n) {
        val c = new Cell(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-current") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazyCell(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-boolean-bitmap") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCell(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-byte-bitmap") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellByteBitmap(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-v2-without-notify") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellVersion2WithoutNotify(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-v2-with-notify") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellVersion2(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-v3") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellVersion3(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-v4") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellVersion4(i)
        cell = c
        c.value
        i += 1
      }
    }

    using(repetitions) curve("lazy-simulation-v4-general") in { n =>
      var i = 0
      while (i < n) {
        val c = new LazySimCellVersion4General(i)
        cell = c
        c.value
        i += 1
      }
    }

  }

}


