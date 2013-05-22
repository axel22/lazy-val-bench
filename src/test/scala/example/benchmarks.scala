package example



import org.scalameter.api._



class benchmarks extends PerformanceTest.Regression {
  def persistor = Persistor.None

  include[UncontendedBenchmark]
  include[ContendedBenchmark]
  include[MemoryFootprint]

}