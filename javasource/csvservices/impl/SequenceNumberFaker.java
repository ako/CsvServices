package csvservices.impl;

import net.datafaker.Faker;

public class SequenceNumberFaker extends Faker {
        public SequenceNumber sequenceNumber() {
            return getProvider(SequenceNumber.class, SequenceNumber::new, this);
        }
    }

