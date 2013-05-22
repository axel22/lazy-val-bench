package example;



import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;



class LazySimCellWithPublicBitmap {
	public static AtomicIntegerFieldUpdater<LazySimCellWithPublicBitmap> arfu_0 = AtomicIntegerFieldUpdater.newUpdater(LazySimCellWithPublicBitmap.class, "bitmap_0");
	public volatile int bitmap_0 = 0;
}

