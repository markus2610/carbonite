package info.evelio.carbonite;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static info.evelio.carbonite.Carbonite.CacheType.MEMORY;
import static info.evelio.carbonite.Carbonite.CacheType.STORAGE;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ReadmeUsageTest {
  public static final String SOMETHING = "something";
  public static final String KEY = "data";
  private final Context context = Robolectric.application;

  @Test public void buildTest() {
    Carbonite carbonite = readmeBuild();

    assertThat(carbonite).isNotNull();
  }

  @Test public void memorySetAndGetTest() {
    Carbonite carbonite = readmeBuild();

    carbonite.memory(KEY, data());

    YourPojo stored = carbonite.memory(KEY, YourPojo.class);

    assertData(stored, SOMETHING);
  }

  @Test public void storageSetAndGetTest() {
    Carbonite carbonite = readmeBuild();

    carbonite.storage(KEY, data());

    YourPojo stored = carbonite.storage(KEY, YourPojo.class);

    assertData(stored, SOMETHING);
  }

  @Test public void setAndGetTest() throws ExecutionException, InterruptedException {
    Carbonite carbonite = readmeBuild();

    carbonite.set(KEY, data());

    Future<YourPojo> future = carbonite.get(KEY, YourPojo.class);

    YourPojo stored = future.get();

    assertData(stored, SOMETHING);
  }

  @Test public void setAndGetLoadingTest() throws ExecutionException, InterruptedException, TimeoutException {
    Carbonite carbonite = readmeBuild();

    carbonite.storage(KEY, data());

    Future<YourPojo> future = carbonite.get(KEY, YourPojo.class);
    YourPojo stored = future.get();
    if (stored != null) { // we might get it right away
      assertData(stored, SOMETHING);
    } else { // we will wait up to
      stored = future.get(1, TimeUnit.SECONDS);
      assertData(stored, SOMETHING);
    }
  }

  private Carbonite readmeBuild() {
    return Carbonite.using(context) /* getApplicationContext() is used and not retained */
        .retaining(YourPojo.class)
        .in(MEMORY) /* optional */
        .and(STORAGE) /* optional */
        /* This can be replaced by just build() */
        .iLoveYou() /* Does nothing */
        .iKnow(); // calls build()
  }

  private static YourPojo data() {
    return new YourPojo(SOMETHING);
  }

  private static void assertData(YourPojo data, String expected) {
    assertThat(data).isNotNull();
    assertThat(data.getData()).isEqualTo(expected);
  }

  private static class YourPojo {
    private final String mData;

    public YourPojo() {
      this(null);
    }

    public YourPojo(String data) {
      mData = data;
    }

    private String getData() {
      return mData;
    }

  }
}
