package example



import org.scalameter.api._



object LazyValBenchmark extends PerformanceTest.Regression {
  def persistor = Persistor.None

  val repetitions = Gen.range("size")(1000000, 5000000, 1000000)

  class Cell(x: Int) {
    val value = 0
  }

  class LazyCell(x: Int) {
    lazy val value = 0
  }

  final class LazySimCell(x: Int) {
    @volatile var bitmap_0: Boolean = false
    var value_0: Int = _
    private def value_lzycompute(): Int = {
      // note that most field accesses translate to virtual calls
      // (the bytecode is otherwise identical to `LazyCell`)
      // this is probably the reason this is 25% slower
      this.synchronized {
        if (bitmap_0) {
          value_0 = 0
          bitmap_0 = true
        }
      }
      value_0
    }
    def value = if (bitmap_0) value_0 else value_lzycompute()
  }

  final class LazySimCellByteBitmap(x: Int) {
    @volatile var bitmap_0: Byte = 0.toByte
    var value_0: Int = _
    private def value_lzycompute(): Int = {
      this.synchronized {
        if (bitmap_0 == 0.toByte) {
          value_0 = 0
          bitmap_0 = 2.toByte
        }
      }
      value_0
    }
    def value = if (bitmap_0 == 2.toByte) value_0 else value_lzycompute()
  }

  final class LazySimCellVersion2WithoutNotify(x: Int) {
    @volatile var bitmap_0: Byte = 0.toByte
    var value_0: Int = _
    private def value_lzycompute(): Int = {
      this.synchronized {
        if (bitmap_0 == 0.toByte) {
          bitmap_0 = 1.toByte
        } else {
          while (bitmap_0 == 1.toByte) {
            this.wait()
          }
          return value_0
        }
      }
      val result = 0
      this.synchronized {
        value_0 = result
        bitmap_0 = 2.toByte
      }
      value_0
    }
    def value = if (bitmap_0 == 2.toByte) value_0 else value_lzycompute()
  }

  final class LazySimCellVersion2(x: Int) {
    @volatile var bitmap_0: Byte = 0.toByte
    var value_0: Int = _
    private def value_lzycompute(): Int = {
      this.synchronized {
        if (bitmap_0 == 0.toByte) {
          bitmap_0 = 1.toByte
        } else {
          while (bitmap_0 == 1.toByte) {
            this.wait()
          }
          return value_0
        }
      }
      val result = 0
      this.synchronized {
        value_0 = result
        bitmap_0 = 2.toByte
        this.notifyAll()
      }
      value_0
    }
    def value = if (bitmap_0 == 2.toByte) value_0 else value_lzycompute()
  }

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

  }

}


