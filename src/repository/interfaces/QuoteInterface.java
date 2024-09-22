package repository.interfaces;

import domain.entities.Quote;

import java.util.List;
import java.util.Optional;

public interface QuoteInterface<Quote> extends CrudInterface<Quote>{
    @Override
    Quote save(Quote entity);

    @Override
    Optional<Quote> findById(Quote quote);

    @Override
    List<Quote> findAll();

    @Override
    Quote update(Quote entity);

    @Override
    boolean delete(Quote entity);
}
