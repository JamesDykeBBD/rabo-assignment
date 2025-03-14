package nl.rabobank.transactionverifier.translator;

import nl.rabobank.transactionverifier.model.Transaction;

public class CSVTranslator implements GenericTranslator<String>{
    @Override
    public Transaction convertForward(String input) {
        return null;
    }

    @Override
    public String convertBackward(Transaction t) {
        return null;
    }
}
