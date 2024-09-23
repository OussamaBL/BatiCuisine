package service;

import domain.entities.Quote;
import repository.QuoteRepository;

import java.util.List;
import java.util.Optional;

public class QuoteService {
    private QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public Quote save(Quote Quote) {
        return this.quoteRepository.save(Quote);
    }

    public Optional<Quote> findById(Quote quote) {
        return this.quoteRepository.findById(quote);
    }

    public Quote update(Quote Quote) {
        return this.quoteRepository.update(Quote);
    }

    public boolean delete(Quote quote) {
        return this.quoteRepository.delete(quote);
    }

    public List<Quote> findAll() {
        return this.quoteRepository.findAll();
    }

    public Optional<Quote> findQuoteByProject(int id) {
        return this.quoteRepository.findDevisByProject(id);
    }
    public boolean updateDevisStatus(int id){
       return quoteRepository.updateDevisStatus(id);
    }
}
