package li.urchin.earth.twic.collections;

import com.google.common.collect.testing.ListTestSuiteBuilder;
import com.google.common.collect.testing.TestStringListGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.ListFeature;
import junit.framework.TestSuite;
import org.junit.runner.RunWith;
import org.junit.runners.AllTests;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@RunWith(AllTests.class)
public class BlockListTests {

    public static TestSuite suite() throws Exception {
        return ListTestSuiteBuilder.using(generator(es -> new BlockList<>(Arrays.asList(es))))
                                   .named(BlockList.class.getSimpleName())
                                   .withFeatures(ListFeature.GENERAL_PURPOSE,
                                                 CollectionFeature.ALLOWS_NULL_VALUES,
                                                 CollectionSize.ANY)
                                   .createTestSuite();
    }

    private static TestStringListGenerator generator(Function<String[], List<String>> factory) {
        return new TestStringListGenerator() {
            @Override
            public List<String> create(String[] elements) {
                return factory.apply(elements);
            }
        };
    }

}
