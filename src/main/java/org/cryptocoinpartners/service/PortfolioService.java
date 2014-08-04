package org.cryptocoinpartners.service;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.cryptocoinpartners.enumeration.TransactionType;
import org.cryptocoinpartners.schema.Amount;
import org.cryptocoinpartners.schema.Asset;
import org.cryptocoinpartners.schema.Exchange;
import org.cryptocoinpartners.schema.Listing;
import org.cryptocoinpartners.schema.Portfolio;
import org.cryptocoinpartners.schema.Position;


/**
 * PortfolioService reports
 *
 * @author Tim Olson
 */
@Service
public interface PortfolioService    {


    /** returns all Positions in all Exchanges.  NOTE: if you have open orders, you will not be able to trade
     * all the Positions returned by this method.  Use getTradeablePositions() instead. */
	@Nullable
	public ArrayList<Position> getPositions(Portfolio portfolio);
	
	public void CreateTransaction(Portfolio portfolio,Exchange exchange, Asset asset, TransactionType type, Amount amount, Amount price );
		

    /** returns all Postions for the given Exchange.  NOTE: if you have open orders, you will not be able to trade
     * all the Positions returned by this method.  Use getTradeablePositions() instead. */
	@Nullable
	public ArrayList<Position> getPositions(Portfolio portfolio, Exchange exchange );
}
