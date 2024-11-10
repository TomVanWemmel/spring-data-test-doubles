package be.tvw.customer.fakes;

import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactory;
import org.springframework.data.map.MapKeyValueAdapter;

public abstract class FakeRepository<T> {

    protected T repository;
    protected KeyValueRepositoryFactory factory;

    public FakeRepository() {
        KeyValueOperations operations = new KeyValueTemplate(new MapKeyValueAdapter());
        KeyValueRepositoryFactory keyValueRepositoryFactory = createKeyValueRepositoryFactory(operations);

        this.repository = getRepository(keyValueRepositoryFactory);
    }

    protected KeyValueRepositoryFactory createKeyValueRepositoryFactory(KeyValueOperations operations) {
        return new KeyValueRepositoryFactory(operations);
    }

    protected abstract T getRepository(KeyValueRepositoryFactory factory);

}
