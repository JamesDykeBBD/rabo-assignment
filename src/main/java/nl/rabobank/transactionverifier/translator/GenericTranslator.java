package nl.rabobank.transactionverifier.translator;

import nl.rabobank.transactionverifier.model.transaction.Transaction;

public interface GenericTranslator<I> {

    Transaction convertForward(I input);
    I convertBackward(Transaction t);
}
