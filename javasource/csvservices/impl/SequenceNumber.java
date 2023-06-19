package csvservices.impl;

import net.datafaker.providers.base.AbstractProvider;
import net.datafaker.providers.base.BaseProviders;

import java.util.HashMap;

public class SequenceNumber extends AbstractProvider<BaseProviders> {

    private static final HashMap<String,Long> sequences = new HashMap();

    public SequenceNumber(BaseProviders faker) {
        super(faker);
    }

    public String next(String key) {
        Long seqVal = 0L;
        if(sequences.containsKey(key)) {
            seqVal = sequences.get(key);
        }
        seqVal ++;
        sequences.put(key, seqVal);
        return String.format("%d",seqVal);
    }
}
