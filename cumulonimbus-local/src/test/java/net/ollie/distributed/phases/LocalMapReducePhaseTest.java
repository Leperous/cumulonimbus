package net.ollie.distributed.phases;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Ollie
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class LocalMapReducePhaseTest {

    @Test(timeout = 10000L)
    public void shouldMapReduce() throws Exception {

        final Map<String, Integer> words = new HashMap<>();
        words.put("one", 1);
        words.put("two", 2);
        words.put("three", 3);
        words.put("four", 4);
        words.put("five", 5);
        words.put("six", 6);
        words.put("seven", 7);
        words.put("eight", 8);
        words.put("nine", 9);

        final LocalMapReducePhase<String, Integer, Integer, Integer> doSumLettersPerWord = new LocalMapReducePhase<>(
                true,
                word -> word.length(),
                values -> values.stream().mapToInt(i -> i).sum());
        
        final Map<Integer, Integer> sumLettersPerWord = doSumLettersPerWord.transform(words).get();
        assertThat(sumLettersPerWord.size(), is(3));
        assertThat(sumLettersPerWord.get(3), is(9));
        assertThat(sumLettersPerWord.get(4), is(18));
        assertThat(sumLettersPerWord.get(5), is(18));
        
    }

}
