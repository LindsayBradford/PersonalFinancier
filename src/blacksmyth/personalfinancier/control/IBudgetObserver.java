package blacksmyth.personalfinancier.control;

import java.util.Observer;

/**
 * An interface that all MVC view classes of the
 * <tt>BudgetModel</tt> must implement if they wish be
 * notified of BudgetModel updates.
 * @author linds
 */
public interface IBudgetObserver extends Observer {}
