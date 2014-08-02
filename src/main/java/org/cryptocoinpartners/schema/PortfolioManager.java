package org.cryptocoinpartners.schema;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;

import org.cryptocoinpartners.module.When;
import org.cryptocoinpartners.util.PersistUtil;
import org.slf4j.Logger;


/**
 * PortfolioManagers are allowed to control the Positions within a Portfolio
 *
 * @author Tim Olson
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class PortfolioManager extends EntityBase {

    // todo we need to get the tradeable portfolio separately from the "reserved" portfolio (assets needed for open orders)
    @OneToOne
    public Portfolio getPortfolio() { return portfolio; }
    
  
    //TODO Limit handle FIll to only handle fills for orders it has places. should only recieve fills for orders that this portfolios belonging to this manager has placed.
    @When("select * from Transaction")
    public void handleFill( Transaction transaction ) {
    	//Exchange exchange=fill.getMarket().getExchange();
    	Asset asset=transaction.getAsset();
    	Amount amount = transaction.getAmount();
    	Amount price = transaction.getPrice();
    	Exchange exchange =transaction.getMarket().getExchange();
        Position position = new Position(exchange, asset, amount, price);
       // Balance balance=new Balance( exchange,asset, amount, Balance.BalanceType.ACTUAL );
        //Update the position and balance. The position might be higher than the balance when trading on margin.
        portfolio.modifyPosition( position, new Authorization("Fill for " + transaction.toString() ));
       // portfolio.modifyBalance( balance, new Authorization("Fill for " + fill.toString() ));
        portfolio.addTransactions(transaction);
        log.info("Positions:"+portfolio.getPositions().toString());
      
    }

    /** for subclasses */
    protected PortfolioManager(String portfolioName) { this.portfolio = new Portfolio(portfolioName,this); }


    // JPA
    protected PortfolioManager() { }
    protected void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }

    @Inject private Logger log;
    private Portfolio portfolio;
}